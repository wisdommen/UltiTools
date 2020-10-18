package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstracts.AbstractTabExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.enums.Sounds;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

import static com.minecraft.ultikits.utils.Utils.getHomeList;

public class HomeCommands extends AbstractTabExecutor implements Listener {

    public final static Map<UUID, Boolean> teleportingPlayers = new HashMap<>();
    private static final File homeFile = new File(ConfigsEnum.HOME.toString());
    private static final YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            try {
                if (args.length == 0 || (args.length == 1 && args[0].equals(UltiTools.languageUtils.getWords("default")))) {
                    World world = Bukkit.getServer().getWorld(Objects.requireNonNull(config.getString(player.getName() + ".Def.world")));
                    int x = config.getInt(player.getName() + ".Def.x");
                    int y = config.getInt(player.getName() + ".Def.y");
                    int z = config.getInt(player.getName() + ".Def.z");
                    Location location = new Location(world, x, y, z);
                    teleportingPlayers.put(player.getUniqueId(), true);
                    teleportPlayer(player, location);
                } else if (args.length == 1) {
                    World world = Bukkit.getServer().getWorld(Objects.requireNonNull(config.getString(player.getName() + "." + args[0] + ".world")));
                    int x = config.getInt(player.getName() + "." + args[0] + ".x");
                    int y = config.getInt(player.getName() + "." + args[0] + ".y");
                    int z = config.getInt(player.getName() + "." + args[0] + ".z");
                    Location location = new Location(world, x, y, z);
                    teleportingPlayers.put(player.getUniqueId(), true);
                    teleportPlayer(player, location);
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("home_usage"));
                    return false;
                }
            } catch (NullPointerException e) {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("home_dont_have"));
            }
        } else {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("home_have_no_home"));
        }
        return true;
    }

    @Override
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        return getHomeList(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (teleportingPlayers.get(player.getUniqueId()) == null || !teleportingPlayers.get(player.getUniqueId()))
            return;
        if (teleportingPlayers.get(player.getUniqueId())) {
            teleportingPlayers.put(player.getUniqueId(), false);
        }
    }

    private static void teleportPlayer(Player player, Location location) {
        boolean isChunkLoaded = location.getChunk().isLoaded();

        if (!isChunkLoaded) {
            location.getChunk().load();
        }

        new BukkitRunnable() {
            float time = homeConfig.getInt("home_tpwait");

            @Override
            public void run() {
                if (!teleportingPlayers.get(player.getUniqueId())) {
                    player.sendTitle(ChatColor.RED + UltiTools.languageUtils.getWords("home_teleport_failed"), UltiTools.languageUtils.getWords("do_not_move"), 10, 50, 20);
                    this.cancel();
                    return;
                }
                if (time == 0) {
                    player.teleport(location);
                    player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.ENTITY_ENDERMAN_TELEPORT), 1, 0);
                    player.sendTitle(ChatColor.GREEN + UltiTools.languageUtils.getWords("home_teleport_success"), "", 10, 50, 20);
                    teleportingPlayers.put(player.getUniqueId(), false);
                    this.cancel();
                    return;
                }
                if ((time / 0.5 % 2) == 0) {
                    player.sendTitle(ChatColor.GREEN + UltiTools.languageUtils.getWords("home_teleporting"), String.format(UltiTools.languageUtils.getWords("world_teleporting_countdown"),(int) time), 10, 70, 20);
                }
                time -= 0.5;
            }
        }.runTaskTimer(UltiTools.getInstance(), 0, 10L);
    }
}

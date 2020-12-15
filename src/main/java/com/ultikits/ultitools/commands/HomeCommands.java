package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.enums.Sounds;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class HomeCommands extends AbstractTabExecutor {

    public final static Map<UUID, Boolean> teleportingPlayers = new HashMap<>();
    private static final File homeFile = new File(ConfigsEnum.HOME.toString());
    private static final YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

    static {
        new Task().runTaskTimerAsynchronously(UltiTools.getInstance(), 0, 10L);
    }

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            try {
                if (args.length == 0 || (args.length == 1 && args[0].equals(UltiTools.languageUtils.getString("default")))) {
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
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("home_usage"));
                    return false;
                }
            } catch (NullPointerException e) {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("home_dont_have"));
            }
        } else {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("home_have_no_home"));
        }
        return true;
    }

    @Override
    protected @Nullable
    List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        return Utils.getHomeList(player);
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
                    player.sendTitle(ChatColor.RED + UltiTools.languageUtils.getString("home_teleport_failed"), UltiTools.languageUtils.getString("do_not_move"), 10, 50, 20);
                    Task.playerLocationMap.put(player.getUniqueId(), null);
                    this.cancel();
                    return;
                }
                if (time == 0) {
                    player.teleport(location);
                    player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.ENTITY_ENDERMAN_TELEPORT), 1, 0);
                    player.sendTitle(ChatColor.GREEN + UltiTools.languageUtils.getString("home_teleport_success"), "", 10, 50, 20);
                    teleportingPlayers.put(player.getUniqueId(), false);
                    Task.playerLocationMap.put(player.getUniqueId(), null);
                    this.cancel();
                    return;
                }
                if ((time / 0.5 % 2) == 0) {
                    player.sendTitle(ChatColor.GREEN + UltiTools.languageUtils.getString("home_teleporting"), String.format(UltiTools.languageUtils.getString("world_teleporting_countdown"), (int) time), 10, 70, 20);
                }
                time -= 0.5;
            }
        }.runTaskTimer(UltiTools.getInstance(), 0, 10L);
    }
}

class Task extends BukkitRunnable {
    public static Map<UUID, String> playerLocationMap = new HashMap<>();

    @Override
    public void run() {
        for (UUID playerUUID : HomeCommands.teleportingPlayers.keySet()) {
            if (!HomeCommands.teleportingPlayers.get(playerUUID)){
                playerLocationMap.put(playerUUID, null);
                continue;
            }
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) {
                continue;
            }
            Location location = player.getLocation();
            String currentLocation = location.getX() + "" + location.getY() + "" + location.getZ();
            if (playerLocationMap.get(playerUUID) == null) {
                playerLocationMap.put(playerUUID, currentLocation);
            } else {
                String lastLocation = playerLocationMap.get(playerUUID);
                if (!currentLocation.equals(lastLocation)) {
                    HomeCommands.teleportingPlayers.put(playerUUID, false);
                }
            }
        }
    }
}

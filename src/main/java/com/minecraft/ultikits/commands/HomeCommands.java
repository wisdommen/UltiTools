package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstractExecutors.AbstractTabExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.minecraft.ultikits.utils.Utils.getHomeList;

public class HomeCommands extends AbstractTabExecutor implements Listener {

    private final static Map<Player, Boolean> teleportingPlayers = new HashMap<>();

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            try {
                if (args.length == 0 || (args.length == 1 && args[0].equals("默认"))) {
                    World world = Bukkit.getServer().getWorld(Objects.requireNonNull(config.getString(player.getName() + ".Def.world")));
                    int x = config.getInt(player.getName() + ".Def.x");
                    int y = config.getInt(player.getName() + ".Def.y");
                    int z = config.getInt(player.getName() + ".Def.z");
                    Location location = new Location(world, x, y, z);
                    teleportingPlayers.put(player, true);
                    teleportPlayer(player, location);
                } else if (args.length == 1) {
                    World world = Bukkit.getServer().getWorld(Objects.requireNonNull(config.getString(player.getName() + "." + args[0] + ".world")));
                    int x = config.getInt(player.getName() + "." + args[0] + ".x");
                    int y = config.getInt(player.getName() + "." + args[0] + ".y");
                    int z = config.getInt(player.getName() + "." + args[0] + ".z");
                    Location location = new Location(world, x, y, z);
                    teleportingPlayers.put(player, true);
                    teleportPlayer(player, location);
                } else {
                    player.sendMessage(ChatColor.RED + "[家插件]用法：/home [家的名字（不设置则为默认）]");
                    return false;
                }
            } catch (NullPointerException e) {
                player.sendMessage(ChatColor.RED + "[家插件]你没有这个家！");
            }
        } else {
            player.sendMessage(ChatColor.RED + "[家插件]你还没有设置家！");
        }
        return true;
    }

    @Override
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        return getHomeList(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if (teleportingPlayers.get(player)==null) return;
        if (teleportingPlayers.get(player)){
            teleportingPlayers.put(player, false);
        }
    }

    private static void teleportPlayer(Player player, Location location) {

        new BukkitRunnable() {
            float time = UltiTools.getInstance().getConfig().getInt("home_tpwait");

            @Override
            public void run() {
                if (!teleportingPlayers.get(player)){
                    player.sendTitle(ChatColor.RED + "[家插件]传送失败", "请勿移动！", 10, 50, 20);
                    this.cancel();
                    return;
                }
                if (time == 0) {
                    player.teleport(location);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
                    player.sendTitle(ChatColor.GREEN + "[家插件]欢迎回家！", "", 10, 50, 20);
                    this.cancel();
                    return;
                }
                if ((time / 0.5 % 2) == 0) {
                    player.sendTitle(ChatColor.GREEN + "[家插件]正在传送...", "离传送还有" + (int) time + "秒", 10, 70, 20);
                }
                time -= 0.5;
            }
        }.runTaskTimer(UltiTools.getInstance(), 0, 10L);
    }
}

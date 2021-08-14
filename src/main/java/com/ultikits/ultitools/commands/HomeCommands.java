package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.enums.Sounds;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.DelayTeleportUtils;
import com.ultikits.ultitools.utils.HomeUtils;
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
import java.time.LocalDate;
import java.util.*;

public class HomeCommands extends AbstractTabExecutor {

    private static final File homeFile = new File(ConfigsEnum.HOME.toString());
    private static final YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

//    static {
//        new Task().runTaskTimerAsynchronously(UltiTools.getInstance(), 0, 10L);
//    }

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        if (file.exists()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    try {
                        if (args.length == 0 || (args.length == 1 && args[0].equals(UltiTools.languageUtils.getString("default")))) {
                            Location location = config.getObject(player.getName() + ".Def", Location.class);
                            if (location == null) {
                                location = getLegacyLocation(config, player.getName() + ".Def");
                                HomeUtils.setHome(player, "Def", location);
                            }
                            DelayTeleportUtils.delayTeleport(player, location, homeConfig.getInt("home_tpwait"));
                        } else if (args.length == 1) {
                            Location location = config.getObject(player.getName() + "." + args[0], Location.class);
                            if (location == null) {
                                location = getLegacyLocation(config, player.getName() + "." + args[0]);
                                HomeUtils.setHome(player, args[0], location);
                            }
                            DelayTeleportUtils.delayTeleport(player, location, homeConfig.getInt("home_tpwait"));
                        } else {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("home_usage"));
                        }
                    } catch (NullPointerException e) {
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("home_dont_have"));
                    }
                }
            }.runTaskAsynchronously(UltiTools.getInstance());
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

    private Location getLegacyLocation(YamlConfiguration config, String path){
        World world = Bukkit.getServer().getWorld(Objects.requireNonNull(config.getString(path + ".world")));
        int x = config.getInt(path + ".x");
        int y = config.getInt(path + ".y");
        int z = config.getInt(path + ".z");
        return new Location(world, x, y, z);
    }
}

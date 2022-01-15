package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.DelayTeleportUtils;
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

    private static final File homeFile = new File(ConfigsEnum.HOME.toString());
    private static final YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        if (file.exists()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    Location location;
                    try {
                        String path = player.getName() + ".Def";
                        if (args.length == 1 && !args[0].equals(UltiTools.languageUtils.getString("default"))) {
                            path = player.getName() + "." + args[0];
                        } else if (args.length > 1){
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("home_usage"));
                            return;
                        }
                        location = getLocation(config, path);
                        DelayTeleportUtils.delayTeleport(player, location, homeConfig.getInt("home_tpwait"));
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

    public static Location getLocation(YamlConfiguration config, String path) {
        try {
            return (Location) config.get(path);
        }catch (Exception e){
            World world = Bukkit.getServer().getWorld(Objects.requireNonNull(config.getString(path + ".world")));
            double x = config.getDouble(path + ".x");
            double y = config.getDouble(path + ".y");
            double z = config.getDouble(path + ".z");
            if (config.get(path + ".yam") != null && config.get(path + ".pitch") != null) {
                float yam = (float) config.getDouble(path + ".yam");
                float pitch = (float) config.getDouble(path + ".pitch");
                return new Location(world, x, y, z, yam, pitch);
            }
            return new Location(world, x, y, z);
        }
    }
}

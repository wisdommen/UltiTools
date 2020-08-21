package com.minecraft.ultikits.home;

import com.minecraft.ultikits.abstractClass.AbstractTabExecutor;
import com.minecraft.ultikits.config.ConfigsEnum;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static com.minecraft.ultikits.utils.Utils.getHomeList;

public class Home extends AbstractTabExecutor {

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
                    player.teleport(new Location(world, x, y, z));
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
                    player.sendMessage(ChatColor.GREEN + "[家插件]欢迎回家！");
                } else if (args.length == 1) {
                    World world = Bukkit.getServer().getWorld(Objects.requireNonNull(config.getString(player.getName() + "." + args[0] + ".world")));
                    int x = config.getInt(player.getName() + "." + args[0] + ".x");
                    int y = config.getInt(player.getName() + "." + args[0] + ".y");
                    int z = config.getInt(player.getName() + "." + args[0] + ".z");
                    player.teleport(new Location(world, x, y, z));
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
                    player.sendMessage(ChatColor.GREEN + "[家插件]欢迎回家！");
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
}

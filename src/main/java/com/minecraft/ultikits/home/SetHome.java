package com.minecraft.ultikits.home;

import com.minecraft.ultikits.abstractClass.AbstractCommandExecutor;
import com.minecraft.ultikits.abstractClass.AbstractPlayerCommandExecutor;
import com.minecraft.ultikits.config.ConfigsEnum;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SetHome extends AbstractPlayerCommandExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (args.length == 0) {
            String homelist = config.getString(player.getName() + ".homelist");
            config.set(player.getName() + ".Def.world", player.getWorld().getName());
            config.set(player.getName() + ".Def.x", player.getLocation().getBlockX());
            config.set(player.getName() + ".Def.y", player.getLocation().getBlockY());
            config.set(player.getName() + ".Def.z", player.getLocation().getBlockZ());
            try {
                if (!config.getString(player.getName() + ".homelist").contains("默认")) {
                    config.set(player.getName() + ".homelist", homelist + " 默认");
                }
            } catch (NullPointerException e) {
                config.set(player.getName() + ".homelist", "默认");
            }
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.sendMessage(ChatColor.YELLOW + "[家插件]设置家成功！");
            return true;
        } else if (args.length == 1) {
            String homelist = config.getString(player.getName() + ".homelist");
            config.set(player.getName() + "." + args[0] + ".world", player.getWorld().getName());
            config.set(player.getName() + "." + args[0] + ".x", player.getLocation().getBlockX());
            config.set(player.getName() + "." + args[0] + ".y", player.getLocation().getBlockY());
            config.set(player.getName() + "." + args[0] + ".z", player.getLocation().getBlockZ());
            if (!Objects.equals(config.getString(player.getName() + ".homelist"), null)) {
                if (!config.getString(player.getName() + ".homelist").contains(args[0])) {
                    config.set(player.getName() + ".homelist", homelist + " " + args[0]);
                }
            } else {
                config.set(player.getName() + ".homelist", args[0]);
            }
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.sendMessage(ChatColor.YELLOW + "[家插件]设置家成功！");
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "[家插件]用法：/sethome [家的名字（不设置则为默认）]");
            return false;
        }
    }
}

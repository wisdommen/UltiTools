package com.ultikits.ultitools.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!((Player) sender).getAllowFlight()){
            if(sender.hasPermission("ultitools.fly")) {
                ((Player)sender).setAllowFlight(true);
                sender.sendMessage(ChatColor.YELLOW + "已打开飞行");
            } else {
                sender.sendMessage(ChatColor.RED + "你没有使用该指令的权限");
            }
        } else {
            if(sender.hasPermission("ultitools.fly")) {
                ((Player)sender).setAllowFlight(false);
                sender.sendMessage(ChatColor.YELLOW + "已关闭飞行");
            } else {
                sender.sendMessage(ChatColor.RED + "你没有使用该指令的权限");
            }
        }

        return false;
    }
}

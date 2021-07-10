package com.ultikits.ultitools.commands;

import com.ultikits.ultitools.ultitools.UltiTools;
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
            if(sender.hasPermission("ultikits.tools.command.fly")) {
                ((Player)sender).setAllowFlight(true);
                sender.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("fly_enabled"));
            } else {
                sender.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("no_permission"));
            }
        } else {
            if(sender.hasPermission("ultikits.tools.command.fly")) {
                ((Player)sender).setAllowFlight(false);
                sender.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("fly_disabled"));
            } else {
                sender.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("no_permission"));
            }
        }

        return false;
    }
}

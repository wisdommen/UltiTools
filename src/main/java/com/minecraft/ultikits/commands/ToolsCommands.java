package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ToolsCommands implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.isOp() || player.hasPermission("ultitools.tools.commands")) {
                if (command.getName().equalsIgnoreCase("ultitools")) {
                    if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                        UltiTools.getInstance().reloadConfig();
                        player.sendMessage(ChatColor.RED+"配置文件已重载！");
                        return true;
                    }
                }
            }
        }else {
            if (command.getName().equalsIgnoreCase("ultitools")) {
                if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    UltiTools.getInstance().reloadConfig();
                    sender.sendMessage(ChatColor.RED+"配置文件已重载！");
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length == 1){
                List<String> tabCommands = new ArrayList<>();
                if (player.isOp()|| player.hasPermission("ultikits.tools.commands")){
                    tabCommands.add("reload");
                }
                return tabCommands;
            }
        }
        return null;
    }
}

package com.minecraft.ultikits.whiteList;

import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.DatabasePlayerTools;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class whitelist_commands implements TabExecutor {
    static File file = new File(UltiTools.getInstance().getDataFolder(), "whitelist.yml");
    static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp() ||player.hasPermission("ultikits.tools.whitelist")) {
                return whiteListCommands(sender, command, args);
            } else {
                player.sendMessage(ChatColor.RED + "你没有权限！");
                return true;
            }
        } else {
            return whiteListCommands(sender, command, args);
        }
    }

    private void addPlayerToWhitelist(File file, String name) {
        List<String> whitelist = config.getStringList("whitelist");
        if (UltiTools.isDatabaseEnabled){
            if (DatabasePlayerTools.isPlayerExist(name) && DatabasePlayerTools.getPlayerData(name, "whitelisted").equals("false")){
                DatabasePlayerTools.updatePlayerData(name, "whitelisted", "true");
            }
        }else {
            whitelist.add(name);
            config.set("whitelist", whitelist);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void removePlayerFromWhitelist(File file, String name) {
        List<String> whitelist = config.getStringList("whitelist");
        if (UltiTools.isDatabaseEnabled){
            if (DatabasePlayerTools.isPlayerExist(name) && DatabasePlayerTools.getPlayerData(name, "whitelisted").equals("true")){
                DatabasePlayerTools.updatePlayerData(name, "whitelisted", "false");
            }
        }else {
            if (whitelist.contains(name)) {
                whitelist.remove(name);
                config.set("whitelist", whitelist);
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendHelpMessage(CommandSender sender){
        sender.sendMessage(ChatColor.YELLOW + "白名单系统帮助：");
        sender.sendMessage(ChatColor.AQUA + "/wl help 帮助");
        sender.sendMessage(ChatColor.AQUA + "/wl list 白名单列表");
        sender.sendMessage(ChatColor.AQUA + "/wl add [玩家名] 添加玩家到白名单");
        sender.sendMessage(ChatColor.AQUA + "/wl remove [玩家名] 将玩家移出白名单");
    }

    private boolean whiteListCommands(CommandSender sender, Command command, String[] args){
        if ("wl".equalsIgnoreCase(command.getName())) {
            if (args.length == 1) {
                switch (args[0]) {
                    case "help":
                        sendHelpMessage(sender);
                        return true;
                    case "list":
                        List<String> whitelist = config.getStringList("whitelist");
                        sender.sendMessage(String.format("%s白名单列表有：", ChatColor.YELLOW));
                        for (String each : whitelist) {
                            sender.sendMessage(String.format("-%s", each));
                        }
                        return true;
                    default:
                        return false;
                }
            } else if (args.length == 2) {
                if ("add".equalsIgnoreCase(args[0])) {
                    addPlayerToWhitelist(file, args[1]);
                    sender.sendMessage(String.format("%s已将%s加入白名单！", ChatColor.RED, args[1]));
                    return true;
                } else if ("remove".equalsIgnoreCase(args[0])) {
                    removePlayerFromWhitelist(file, args[1]);
                    sender.sendMessage(String.format("%s已将%s移出白名单！", ChatColor.RED, args[1]));
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
            if (player.isOp() || player.hasPermission("ultikits.tools.whitelist")) {
                if (args.length == 1) {
                    List<String> tabCommands = new ArrayList<>();
                    tabCommands.add("help");
                    tabCommands.add("list");
                    tabCommands.add("add");
                    tabCommands.add("remove");
                    return tabCommands;
                }else if(args.length == 2){
                    List<String> tabCommands = new ArrayList<>();
                    tabCommands.add("[玩家名]");
                    return tabCommands;
                }
            }
        }
        return null;
    }
}

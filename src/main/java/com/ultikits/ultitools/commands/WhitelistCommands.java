package com.ultikits.ultitools.commands;

import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.services.DatabasePlayerService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@CmdExecutor(function = "white-list", permission = "ultikits.tools.whitelist", description = "whitelist_function", alias = "wl")
public class WhitelistCommands implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ultikits.tools.admin") || player.hasPermission("ultikits.tools.whitelist")) {
                return whiteListCommands(sender, command, args);
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("no_permission"));
                return true;
            }
        } else {
            return whiteListCommands(sender, command, args);
        }
    }

    private void addPlayerToWhitelist(String name) {
        YamlConfiguration config = ConfigController.getConfig("whitelist");
        List<String> whitelist = config.getStringList("whitelist");
        if (UltiTools.isDatabaseEnabled) {
            if (DatabasePlayerService.isPlayerExist(name, "userinfo") && DatabasePlayerService.getPlayerData(name, "userinfo", "whitelisted").equals("false")) {
                DatabasePlayerService.updatePlayerData(name, "userinfo", "whitelisted", "true");
            }
        } else {
            whitelist.add(name);
            config.set("whitelist", whitelist);
            ConfigController.saveConfig("whitelist");
        }

    }

    private void removePlayerFromWhitelist(String name) {
        YamlConfiguration config = ConfigController.getConfig("whitelist");
        List<String> whitelist = config.getStringList("whitelist");
        if (UltiTools.isDatabaseEnabled) {
            if (DatabasePlayerService.isPlayerExist(name, "userinfo") && DatabasePlayerService.getPlayerData(name, "userinfo", "whitelisted").equals("true")) {
                DatabasePlayerService.updatePlayerData(name, "userinfo", "whitelisted", "false");
            }
        } else {
            if (whitelist.contains(name)) {
                whitelist.remove(name);
                config.set("whitelist", whitelist);
                ConfigController.saveConfig("whitelist");
            }
        }
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("whitelist_help_header"));
        sender.sendMessage(ChatColor.AQUA + "/wl help " + UltiTools.languageUtils.getString("whitelist_help_help"));
        sender.sendMessage(ChatColor.AQUA + "/wl list " + UltiTools.languageUtils.getString("whitelist_help_list"));
        sender.sendMessage(ChatColor.AQUA + "/wl add [" + UltiTools.languageUtils.getString("player_name") + "] " + UltiTools.languageUtils.getString("whitelist_help_add"));
        sender.sendMessage(ChatColor.AQUA + "/wl remove [" + UltiTools.languageUtils.getString("player_name") + "] " + UltiTools.languageUtils.getString("whitelist_help_remove"));
    }

    private boolean whiteListCommands(CommandSender sender, Command command, String[] args) {
        YamlConfiguration config = ConfigController.getConfig("whitelist");
        if ("wl".equalsIgnoreCase(command.getName())) {
            if (args.length == 1) {
                switch (args[0]) {
                    case "help":
                        sendHelpMessage(sender);
                        return true;
                    case "list":
                        List<String> whitelist = config.getStringList("whitelist");
                        sender.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("whitelist_contains"));
                        for (String each : whitelist) {
                            sender.sendMessage(String.format("-%s", each));
                        }
                        return true;
                    default:
                        return false;
                }
            } else if (args.length == 2) {
                if ("add".equalsIgnoreCase(args[0])) {
                    addPlayerToWhitelist(args[1]);
                    sender.sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getString("whitelist_added"), args[1]));
                    return true;
                } else if ("remove".equalsIgnoreCase(args[0])) {
                    removePlayerFromWhitelist(args[1]);
                    sender.sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getString("whitelist_removed"), args[1]));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp() || player.hasPermission("ultikits.tools.whitelist")) {
                if (args.length == 1) {
                    List<String> tabCommands = new ArrayList<>();
                    tabCommands.add("help");
                    tabCommands.add("list");
                    tabCommands.add("add");
                    tabCommands.add("remove");
                    return tabCommands;
                } else if (args.length == 2) {
                    List<String> tabCommands = new ArrayList<>();
                    tabCommands.add("[" + UltiTools.languageUtils.getString("player_name") + "]");
                    return tabCommands;
                }
            }
        }
        return null;
    }
}

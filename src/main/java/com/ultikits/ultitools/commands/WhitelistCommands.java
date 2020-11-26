package com.ultikits.ultitools.commands;

import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.DatabasePlayerTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WhitelistCommands implements TabExecutor {
    static File file = new File(UltiTools.getInstance().getDataFolder(), "whitelist.yml");
    static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ultikits.tools.admin") || player.hasPermission("ultikits.tools.whitelist")) {
                return whiteListCommands(sender, command, args);
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("no_permission"));
                return true;
            }
        } else {
            return whiteListCommands(sender, command, args);
        }
    }

    private void addPlayerToWhitelist(File file, String name) {
        List<String> whitelist = config.getStringList("whitelist");
        if (UltiTools.isDatabaseEnabled) {
            if (DatabasePlayerTools.isPlayerExist(name) && DatabasePlayerTools.getPlayerData(name, "whitelisted").equals("false")) {
                DatabasePlayerTools.updatePlayerData(name, "whitelisted", "true");
            }
        } else {
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
        if (UltiTools.isDatabaseEnabled) {
            if (DatabasePlayerTools.isPlayerExist(name) && DatabasePlayerTools.getPlayerData(name, "whitelisted").equals("true")) {
                DatabasePlayerTools.updatePlayerData(name, "whitelisted", "false");
            }
        } else {
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

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getWords("whitelist_help_header"));
        sender.sendMessage(ChatColor.AQUA + "/wl help " + UltiTools.languageUtils.getWords("whitelist_help_help"));
        sender.sendMessage(ChatColor.AQUA + "/wl list " + UltiTools.languageUtils.getWords("whitelist_help_list"));
        sender.sendMessage(ChatColor.AQUA + "/wl add [" + UltiTools.languageUtils.getWords("player_name") + "] " + UltiTools.languageUtils.getWords("whitelist_help_add"));
        sender.sendMessage(ChatColor.AQUA + "/wl remove [" + UltiTools.languageUtils.getWords("player_name") + "] " + UltiTools.languageUtils.getWords("whitelist_help_remove"));
    }

    private boolean whiteListCommands(CommandSender sender, Command command, String[] args) {
        if ("wl".equalsIgnoreCase(command.getName())) {
            if (args.length == 1) {
                switch (args[0]) {
                    case "help":
                        sendHelpMessage(sender);
                        return true;
                    case "list":
                        List<String> whitelist = config.getStringList("whitelist");
                        sender.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getWords("whitelist_contains"));
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
                    sender.sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getWords("whitelist_added"), args[1]));
                    return true;
                } else if ("remove".equalsIgnoreCase(args[0])) {
                    removePlayerFromWhitelist(file, args[1]);
                    sender.sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getWords("whitelist_removed"), args[1]));
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
                    tabCommands.add("[" + UltiTools.languageUtils.getWords("player_name") + "]");
                    return tabCommands;
                }
            }
        }
        return null;
    }
}

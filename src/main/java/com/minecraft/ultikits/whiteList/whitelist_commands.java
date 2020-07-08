package com.minecraft.ultikits.whiteList;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class whitelist_commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                File file = new File(UltiTools.getInstance().getDataFolder(), "whitelist.yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


                if ("wl".equalsIgnoreCase(command.getName())) {
                    if (args.length == 1) {
                        switch (args[0]) {
                            case "help":
                                player.sendMessage(ChatColor.YELLOW + "白名单系统帮助：");
                                player.sendMessage(ChatColor.AQUA + "/wl help 帮助");
                                player.sendMessage(ChatColor.AQUA + "/wl list 白名单列表");
                                player.sendMessage(ChatColor.AQUA + "/wl add [玩家名] 添加玩家到白名单");
                                player.sendMessage(ChatColor.AQUA + "/wl remove [玩家名] 将玩家移出白名单");
                                return true;
                            case "list":
                                List<String> whitelist = config.getStringList("whitelist");
                                player.sendMessage(String.format("%s白名单列表有：", ChatColor.YELLOW));
                                for (String each : whitelist) {
                                    player.sendMessage(String.format("-%s", each));
                                }
                                return true;
                            default:
                                return false;
                        }
                    } else if (args.length == 2) {
                        if ("add".equalsIgnoreCase(args[0])) {
                            addPlayerToWhitelist(file, config, args[1]);
                            player.sendMessage(String.format("%s已将%s加入白名单！", ChatColor.RED, args[1]));
                            return true;
                        } else if ("remove".equalsIgnoreCase(args[0])) {
                            removePlayerFromWhitelist(file, config, args[1]);
                            player.sendMessage(String.format("%s已将%s移出白名单！", ChatColor.RED, args[1]));
                            return true;
                        }
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "你没有权限！");
                return true;
            }
        } else {
            File file = new File(UltiTools.getInstance().getDataFolder(), "whitelist.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            if ("wl".equalsIgnoreCase(command.getName())) {
                if (args.length == 1) {
                    switch (args[0]) {
                        case "help":
                            sender.sendMessage(ChatColor.YELLOW + "白名单系统帮助：");
                            sender.sendMessage(ChatColor.AQUA + "/wl help 帮助");
                            sender.sendMessage(ChatColor.AQUA + "/wl list 白名单列表");
                            sender.sendMessage(ChatColor.AQUA + "/wl add [玩家名] 添加玩家到白名单");
                            sender.sendMessage(ChatColor.AQUA + "/wl remove [玩家名] 将玩家移出白名单");
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
                        addPlayerToWhitelist(file, config, args[1]);
                        sender.sendMessage(String.format("%s已将%s加入白名单！", ChatColor.RED, args[1]));
                        return true;
                    } else if ("remove".equalsIgnoreCase(args[0])) {
                        removePlayerFromWhitelist(file, config, args[1]);
                        sender.sendMessage(String.format("%s已将%s移出白名单！", ChatColor.RED, args[1]));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void addPlayerToWhitelist(File file, YamlConfiguration config, String name) {
        List<String> whitelist = config.getStringList("whitelist");

        whitelist.add(name);
        config.set("whitelist", whitelist);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void removePlayerFromWhitelist(File file, YamlConfiguration config, String name) {
        List<String> whitelist = config.getStringList("whitelist");
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

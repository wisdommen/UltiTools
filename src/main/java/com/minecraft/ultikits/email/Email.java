package com.minecraft.ultikits.email;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Email implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            File folder = new File(UltiTools.getInstance().getDataFolder() + "/playerData");
            File file = new File(folder, player.getName() + ".yml");
            YamlConfiguration config;
            if (!file.exists()) {
                folder.mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                config = new YamlConfiguration();
            } else {
                config = YamlConfiguration.loadConfiguration(file);
            }
            if (command.getName().equalsIgnoreCase("email")) {
                if (strings.length == 1) {
                    if (strings[0].equalsIgnoreCase("read")) {
                        if (config.getString("email.1") != null) {
                            player.sendMessage(ChatColor.AQUA + "以下是你的收到的邮件！共有" + ChatColor.RED + config.getInt("count") + ChatColor.AQUA + "封邮件！");

                            for (int i = 1; i <= config.getConfigurationSection("email").getKeys(false).size(); i++) {
                                try {
                                    String mail = Objects.requireNonNull(config.getString("email." + i));
                                    player.sendMessage(ChatColor.YELLOW + "第" + i + "封邮件：");
                                    player.sendMessage(mail);
                                } catch (NullPointerException e) {
                                    player.sendMessage(ChatColor.RED + "没有更多未读邮件了！");
                                    break;
                                }
                            }
                            for (int a = 1; a <= config.getConfigurationSection("email").getKeys(false).size(); a++) {
                                try {
                                    String Hmail = Objects.requireNonNull(config.getString("email." + a));
                                    if (config.getString("historyEmail") != null) {
                                        config.set("historyEmail", config.getString("historyEmail") + Hmail + "\n");
                                    }
                                    if (config.getString("historyEmail") == null) {
                                        config.set("historyEmail", config.getString("email." + a) + "\n");
                                    }
                                } catch (NullPointerException e) {
                                    break;
                                }
                            }
                            config.set("email", null);
                            config.set("count", 0);
                            try {
                                config.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        } else {
                            player.sendMessage(ChatColor.AQUA + "你没有新邮件可读！");
                            return true;
                        }
                    }
                    if (strings[0].equalsIgnoreCase("history")) {
                        if (config.getString("historyEmail") != null) {
                            player.sendMessage(Objects.requireNonNull(config.getString("historyEmail")));
                            return true;
                        } else {
                            player.sendMessage(ChatColor.RED + "你还没有收到过任何邮件！");
                            return true;
                        }
                    }
                    if (strings[0].equalsIgnoreCase("delhistory")) {
                        if (config.getString("historyEmail") != null) {
                            config.set("historyEmail", null);
                            try {
                                config.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(ChatColor.RED + "所有历史邮件都已删除！");
                            return true;
                        } else {
                            player.sendMessage(ChatColor.RED + "你还没有收到过任何邮件！");
                            return true;
                        }
                    }
                }
                if (strings.length == 3) {
                    if (strings[0].equalsIgnoreCase("send")) {
                        File f = new File(UltiTools.getInstance().getDataFolder() + "/playerData", strings[1] + ".yml");
                        YamlConfiguration config2;
                        if (!f.exists()) {
                            player.sendMessage(ChatColor.RED + "未找到指定的收件人！");
                        } else {
                            config2 = YamlConfiguration.loadConfiguration(f);
                            if (config2.getString("email.1") == null) {
                                config2.set("email.1", "来自" + player.getName() + ":" + strings[2]);
                                config2.set("count", 1);
                                try {
                                    config2.save(f);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(ChatColor.GOLD + "正在发送邮件...");
                                player.sendMessage(ChatColor.GOLD + "发送成功！");
                                return true;
                            } else {
                                int x = config.getConfigurationSection("email").getKeys(false).size();
                                config2.set("email." + (x + 1), "来自" + player.getName() + ":" + strings[2]);
                                config2.set("count", config2.getInt("count") + 1);
                                try {
                                    config2.save(f);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(ChatColor.GOLD + "正在发送邮件...");
                                player.sendMessage(ChatColor.GOLD + "发送成功！");
                            }
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
                if (strings.length != 3 && strings.length != 1) {
                    player.sendMessage("格式错误！");
                }
            }
        }
        return false;
    }
}

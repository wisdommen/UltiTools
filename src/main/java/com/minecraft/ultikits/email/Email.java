package com.minecraft.ultikits.email;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Email implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            EmailManager emailManager = new EmailManager(player);

            if (command.getName().equalsIgnoreCase("email")) {
                if (strings.length == 1) {
                    if (strings[0].equalsIgnoreCase("read")) {
                        if (emailManager.getEmails().length() != 0) {
                            player.sendMessage(ChatColor.AQUA + "以下是你的收到的邮件！共有" + ChatColor.RED + emailManager.getEmailNum() + ChatColor.AQUA + "封邮件！");

                            player.sendMessage(emailManager.getEmails().toString());
                            emailManager.setHistoryEmail();
                        } else {
                            player.sendMessage(ChatColor.AQUA + "你没有新邮件可读！");
                        }
                        return true;
                    } else if (strings[0].equalsIgnoreCase("history")) {
                        player.sendMessage(emailManager.getHistoryEmails());
                        return true;
                    } else if (strings[0].equalsIgnoreCase("delhistory")) {
                        if (emailManager.deleteHistoryEmails()) {
                            player.sendMessage(ChatColor.RED + "所有历史邮件都已删除！");
                        } else {
                            player.sendMessage(ChatColor.RED + "你还没有收到过任何邮件！");
                        }
                        return true;
                    }
                } else if (strings.length == 3) {
                    if (strings[0].equalsIgnoreCase("send")) {
                        if (Bukkit.getPlayer(strings[1]) != null) {
                            if (emailManager.sendEmail(Bukkit.getPlayer(strings[1]), strings[2])) {
                                player.sendMessage(ChatColor.GOLD + "正在发送邮件...");
                                player.sendMessage(ChatColor.GOLD + "发送成功！");
                            } else {
                                player.sendMessage(ChatColor.RED + "未找到指定的收件人！");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "未找到指定的收件人！");
                        }
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "格式错误！");
                }
            }
        }
        return false;
    }
}

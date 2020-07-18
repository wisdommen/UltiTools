package com.minecraft.ultikits.email;

import com.minecraft.ultikits.GUIs.GUISetup;
import com.minecraft.ultikits.GUIs.ItemStackManager;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Email implements TabExecutor {

    public static Map<String, EmailContentManager> emailContentManagerMap;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            EmailManager emailManager = new EmailManager(player);

            if ("email".equalsIgnoreCase(command.getName())) {
                if (strings.length == 1) {
                    if ("read".equalsIgnoreCase(strings[0])) {
                        emailContentManagerMap = GUISetup.setUpEmailInBox(player);
                        player.openInventory(GUISetup.inventoryMap.get(player.getName()+".inbox").getInventory());
                        return true;
                    }
                    else if ("delhistory".equalsIgnoreCase(strings[0])) {
                        if (emailManager.deleteHistoryEmails()) {
                            player.sendMessage(ChatColor.RED + "所有历史邮件都已删除！");
                        } else {
                            player.sendMessage(ChatColor.RED + "你还没有收到过任何邮件！");
                        }
                        return true;
                    }else if ("help".equalsIgnoreCase(strings[0])){
                        player.sendMessage(ChatColor.YELLOW+"-------------邮件系统帮助------------");
                        player.sendMessage(ChatColor.GREEN+"/email read " + ChatColor.GRAY+"打开收件箱");
                        player.sendMessage(ChatColor.GREEN+"/email delhistory " + ChatColor.GRAY+"删除所有邮件");
                        player.sendMessage(ChatColor.GREEN+"/email send [玩家名] [文本内容] " + ChatColor.GRAY+"给某人发送只包含文本的邮件");
                        player.sendMessage(ChatColor.GREEN+"/email senditem [玩家名] [文本内容] " + ChatColor.GRAY+"手持需要发送的物品，发送一个带有附件的文本邮件");
                        if (player.isOp()){
                            player.sendMessage(ChatColor.GREEN+"/email sendall [文本内容] " + ChatColor.GRAY+"给所有人发邮件，如果空手则不包含附件，手持物品发送带有物品的邮件，不会扣除你的物品！");
                        }
                    }
                }else if (strings.length == 2){
                    if ("sendall".equalsIgnoreCase(strings[0])){
                        if (player.isOp()){
                            if (player.getInventory().getItemInMainHand().getType()!= Material.AIR) {
                                ItemStack itemStack = player.getInventory().getItemInMainHand();
                                ItemStackManager itemStackManager = new ItemStackManager(itemStack);
                                itemStackManager.setUpItem();
                                for (OfflinePlayer player1 : UltiTools.getInstance().getServer().getOfflinePlayers()){
                                    emailManager.sendTo(player1, strings[1], itemStackManager);
                                }
                            }else {
                                for (OfflinePlayer player2 : UltiTools.getInstance().getServer().getOfflinePlayers()){
                                    emailManager.sendTo(player2, strings[1]);
                                }
                            }
                            player.sendMessage(ChatColor.GOLD + "正在发送全体邮件...");
                            player.sendMessage(ChatColor.GOLD + "发送成功！");
                        }
                        return true;
                    }
                }else if (strings.length == 3) {
                    if ("send".equalsIgnoreCase(strings[0])) {
                        if (Bukkit.getOfflinePlayer(strings[1]) != null) {
                            if (emailManager.sendTo(Bukkit.getOfflinePlayer(strings[1]), strings[2])) {
                                player.sendMessage(ChatColor.GOLD + "正在发送邮件...");
                                player.sendMessage(ChatColor.GOLD + "发送成功！");
                            } else {
                                player.sendMessage(ChatColor.RED + "未找到指定的收件人！");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "未找到指定的收件人！");
                        }
                        return true;
                    }else if ("senditem".equalsIgnoreCase(strings[0])){
                        if (Bukkit.getOfflinePlayer(strings[1]) != null) {
                            if (player.getInventory().getItemInMainHand().getType()!= Material.AIR){
                                ItemStack itemStack = player.getInventory().getItemInMainHand();
                                ItemStackManager itemStackManager = new ItemStackManager(itemStack);
                                itemStackManager.setUpItem();
                                if (emailManager.sendTo(Bukkit.getOfflinePlayer(strings[1]), strings[2], itemStackManager)) {
                                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                    player.sendMessage(ChatColor.RED + "发送成功！");
                                } else {
                                    player.sendMessage(ChatColor.RED + "发送失败！");
                                }
                            }else {
                                player.sendMessage(ChatColor.RED+"请手持需要发送的物品！");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "未找到指定的收件人！");
                        }
                        return true;
                    }else {
                        return false;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "格式错误！");
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
                tabCommands.add("read");
                tabCommands.add("delhistory");
                tabCommands.add("send");
                tabCommands.add("senditem");
                if (player.isOp()){
                    tabCommands.add("sendall");
                }
                return tabCommands;
            }else if (args.length==2){
                List<String> tabCommands = new ArrayList<>();
                for (OfflinePlayer offlinePlayer : UltiTools.getInstance().getServer().getOfflinePlayers()){
                    tabCommands.add(offlinePlayer.getName());
                }
                return tabCommands;
            } else if (args.length==3){
                List<String> tabCommands = new ArrayList<>();
                tabCommands.add("[邮件内容]");
                return tabCommands;
            }
        }
        return null;
    }
}

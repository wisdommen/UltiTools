package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.utils.GUIUtils;
import com.minecraft.ultikits.commands.abstractExecutors.AbstractTabExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.beans.EmailContentBean;
import com.minecraft.ultikits.manager.EmailManager;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.minecraft.ultikits.utils.MessagesUtils.info;
import static com.minecraft.ultikits.utils.MessagesUtils.warning;


public class EmailCommands extends AbstractTabExecutor {

    public static Map<String, EmailContentBean> emailContentManagerMap;

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        File senderFile = new File(ConfigsEnum.PLAYER_EMAIL.toString(), player.getName() + ".yml");
        EmailManager emailManager = new EmailManager(senderFile);

        if ("email".equalsIgnoreCase(command.getName())) {
            switch (strings.length) {
                case 1:
                    switch (strings[0].toLowerCase()) {
                        case "read":
                            readEmails(player);
                            return true;
                        case "delhistory":
                            deleteHistoryEmail(emailManager, player);
                            return true;
                        case "help":
                            sendHelpMessage(player);
                            return true;
                        default:
                            player.sendMessage(ChatColor.RED + "格式错误！");
                            return false;
                    }
                case 2:
                    File file = new File(ConfigsEnum.PLAYER_EMAIL.toString(), strings[1] + ".yml");

                    switch (strings[0].toLowerCase()) {
                        case "sendall":
                            if (player.isOp()) {
                                sendAllMessage(player, emailManager, strings[1]);
                                return true;
                            }
                            return false;
                        case "senditem":
                            sendMessage(file, emailManager, player, strings[1]);
                            return true;
                        default:
                            player.sendMessage(ChatColor.RED + "格式错误！");
                            return false;
                    }
                case 3:
                    File file2 = new File(ConfigsEnum.PLAYER_EMAIL.toString(), strings[1] + ".yml");
                    boolean hasContent;

                    switch (strings[0].toLowerCase()) {
                        case "send":
                            hasContent = false;
                            break;
                        case "senditem":
                            hasContent = true;
                            break;
                        default:
                            player.sendMessage(ChatColor.RED + "格式错误！");
                            return false;
                    }
                    sendMessage(file2, emailManager, player, strings[1], strings[2], hasContent);
                    return true;
                default:
                    player.sendMessage(ChatColor.RED + "格式错误！");
                    return false;
            }
        }
        return false;
    }

    @Override
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        List<String> tabCommands = new ArrayList<>();

        switch (args.length) {
            case 1:
                tabCommands.add("help");
                tabCommands.add("read");
                tabCommands.add("delhistory");
                tabCommands.add("send");
                tabCommands.add("senditem");
                if (player.isOp()) {
                    tabCommands.add("sendall");
                }
                return tabCommands;
            case 2:
                for (OfflinePlayer offlinePlayer : UltiTools.getInstance().getServer().getOfflinePlayers()) {
                    tabCommands.add(offlinePlayer.getName());
                }
                return tabCommands;
            case 3:
                tabCommands.add("[邮件内容]");
                return tabCommands;
        }
        return null;
    }

    public void readEmails(Player player) {
        emailContentManagerMap = GUIUtils.setUpEmailInBox(player);
        player.openInventory(GUIUtils.inventoryMap.get(player.getName() + ".inbox").getInventory());
        player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 10, 1);
    }

    private void sendHelpMessage(@NotNull Player player) {
        player.sendMessage(ChatColor.YELLOW + "-------------邮件系统帮助------------");
        player.sendMessage(ChatColor.GREEN + "/email read " + ChatColor.GRAY + "打开收件箱");
        player.sendMessage(ChatColor.GREEN + "/email delhistory " + ChatColor.GRAY + "删除所有邮件");
        player.sendMessage(ChatColor.GREEN + "/email send [玩家名] [文本内容] " + ChatColor.GRAY + "给某人发送只包含文本的邮件");
        player.sendMessage(ChatColor.GREEN + "/email senditem [玩家名] " + ChatColor.GRAY + "手持需要发送的物品，发送一个带有附件的邮件");
        player.sendMessage(ChatColor.GREEN + "/email senditem [玩家名] [文本内容] " + ChatColor.GRAY + "手持需要发送的物品，发送一个带有附件的文本邮件");
        if (!player.isOp()) {
            return;
        }
        player.sendMessage(ChatColor.GREEN + "/email sendall [文本内容] " + ChatColor.GRAY + "给所有人发邮件，如果空手则不包含附件，手持物品发送带有物品的邮件，不会扣除你的物品！");
    }

    private void pushToReceiver(String receiver) {
        if (Bukkit.getPlayer(receiver) == null) {
            return;
        }
        Player receiverPlayer = Bukkit.getPlayer(receiver);
        receiverPlayer.sendMessage(info("你收到一条新的邮件！\n使用/email read 来查看"));
        receiverPlayer.playSound(receiverPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10, 1);
    }

    public void sendAllMessage(@NotNull Player player, EmailManager emailManager, String receiver) {
        if (!player.isOp()) {
            return;
        }
        player.sendMessage(ChatColor.GOLD + "正在发送全体邮件...");
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            for (OfflinePlayer player1 : UltiTools.getInstance().getServer().getOfflinePlayers()) {
                File file = new File(ConfigsEnum.PLAYER_EMAIL.toString(), player1.getName() + ".yml");
                emailManager.sendTo(file, receiver, itemStack);
                pushToReceiver(player1.getName());
            }
            return;
        }
        for (OfflinePlayer player2 : UltiTools.getInstance().getServer().getOfflinePlayers()) {
            File file = new File(ConfigsEnum.PLAYER_EMAIL.toString(), player2.getName() + ".yml");
            emailManager.sendTo(file, receiver);
            pushToReceiver(player2.getName());
        }
        player.sendMessage(ChatColor.GOLD + "发送成功！");
        player.playSound(player.getLocation(), Sound.UI_TOAST_OUT, 15, 1);
    }

    public void deleteHistoryEmail(@NotNull EmailManager emailManager, Player player) {
        if (!emailManager.deleteHistoryEmails()) {
            player.sendMessage(warning("你还没有收到过任何邮件！"));
            return;
        }
        player.sendMessage(warning("所有历史邮件都已删除！"));
        player.playSound(player.getLocation(), Sound.BLOCK_WET_GRASS_BREAK, 10, 1);
    }

    public void sendMessage(@NotNull File file, EmailManager emailManager, Player player, String receiver) {
        if (!file.exists()) {
            player.sendMessage(warning("未找到指定的收件人！"));
        }
        sendItem(file, emailManager, player, receiver);
    }

    public void sendMessage(@NotNull File file, EmailManager emailManager, Player player, String receiver, String message, boolean hasContent) {
        if (!file.exists()) {
            player.sendMessage(warning("未找到指定的收件人！"));
        }
        if (hasContent) {
            sendItem(file, emailManager, player, receiver, message);
        } else {
            sendText(file, emailManager, player, receiver, message);
        }
    }

    private void sendText(File file, @NotNull EmailManager emailManager, @NotNull Player player, String receiver, String message) {
        player.sendMessage(ChatColor.GOLD + "正在发送邮件...");
        if (emailManager.sendTo(file, message)) {
            player.sendMessage(ChatColor.GOLD + "发送成功！");
            player.playSound(player.getLocation(), Sound.UI_TOAST_OUT, 15, 1);
            pushToReceiver(receiver);
        } else {
            player.sendMessage(warning("未找到指定的收件人！"));
        }
    }

    private void sendItem(File file, EmailManager emailManager, @NotNull Player player, String receiver, String message) {
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            player.sendMessage(ChatColor.GOLD + "正在发送邮件...");
            if (emailManager.sendTo(file, message, itemStack)) {
                player.getInventory().setItemInMainHand(null);
                player.sendMessage(ChatColor.GOLD + "发送成功！");
                player.playSound(player.getLocation(), Sound.UI_TOAST_OUT, 15, 1);
                pushToReceiver(receiver);
            } else {
                player.sendMessage(warning("发送失败！"));
            }
        } else {
            player.sendMessage(warning("请手持需要发送的物品！"));
        }
    }

    private void sendItem(File file, EmailManager emailManager, @NotNull Player player, String receiver) {
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            player.sendMessage(ChatColor.GOLD + "正在发送邮件...");
            if (emailManager.sendTo(file, "对方没有留言哦!", itemStack)) {
                player.getInventory().setItemInMainHand(null);
                player.sendMessage(ChatColor.GOLD + "发送成功！");
                player.playSound(player.getLocation(), Sound.UI_TOAST_OUT, 15, 1);
                pushToReceiver(receiver);
            } else {
                player.sendMessage(warning("发送失败！"));
            }
        } else {
            player.sendMessage(warning("请手持需要发送的物品！"));
        }
    }
}

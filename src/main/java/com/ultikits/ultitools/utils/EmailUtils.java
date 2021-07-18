package com.ultikits.ultitools.utils;

import com.ultikits.beans.EmailContentBean;
import com.ultikits.enums.Sounds;
import com.ultikits.ultitools.enums.EmailResponse;
import com.ultikits.ultitools.manager.EmailManager;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.views.EmailView;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.ultikits.utils.MessagesUtils.info;
import static com.ultikits.utils.MessagesUtils.warning;

public class EmailUtils {

    /**
     * 为一个玩家打开他的收件箱界面
     *
     * @param player 需要打开的玩家
     */
    public static void readEmails(Player player) {
        Inventory inventory = EmailView.setUp(player);
        player.openInventory(inventory);
        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.ITEM_BOOK_PAGE_TURN), 10, 1);
    }

    /**
     * @param strings 命令
     * @return 去除开头两个参数剩下的命令部分形成的字符串
     */
    public static String stripSpaceInCommand(String[] strings, int commandIndex) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = commandIndex; i < strings.length; i++) {
            String s = " " + strings[i] + " ";
            if (i == commandIndex) {
                s = strings[i] + " ";
            } else if (i == strings.length - 1) {
                s = " " + strings[i];
            }
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    /**
     * 给收件人发送收到新邮件提醒
     *
     * @param receiver 收件人
     */
    public static void pushToReceiver(Player receiver) {
        receiver.sendMessage(info(UltiTools.languageUtils.getString("email_received_new_email")));
        TextComponent textComponent = new TextComponent(ChatColor.RED + UltiTools.languageUtils.getString("email_message_clickable"));
        TextComponent textComponent_suffix = new TextComponent(ChatColor.AQUA + UltiTools.languageUtils.getString("email_message_clickable_suffix"));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/email read"));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(UltiTools.languageUtils.getString("email_message_clickable") + UltiTools.languageUtils.getString("email_message_clickable_suffix"))));
        receiver.spigot().sendMessage(textComponent, textComponent_suffix);
        receiver.playSound(receiver.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_CHIME), 10, 1);
    }

    /**
     * 给全服所有玩家发送一封邮件，包含离线玩家
     *
     * @param message  信息内容
     * @param itemSend 物品内容
     */
    public static void sendAllMessage(String message, ItemStack itemSend) {
        itemSend = itemSend.getType() == Material.AIR ? null : itemSend;
        for (OfflinePlayer player : UltiTools.getInstance().getServer().getOfflinePlayers()) {
            EmailManager emailManager = new EmailManager(player);
            emailManager.sendNotification(message, itemSend, null);
            if (player.isOnline()) {
                pushToReceiver((Player) player);
            }
        }
    }

    /**
     * 删除所有已读邮件
     *
     * @param player 需要删除邮件的玩家
     */
    public static void deleteReadEmails(Player player) {
        EmailManager emailManager = new EmailManager(player);
        for (EmailContentBean emailContentBean : emailManager.getEmails().values()) {
            if (emailContentBean.getRead()) {
                emailManager.deleteEmail(emailContentBean.getUuid());
            }
        }
        player.sendMessage(warning(UltiTools.languageUtils.getString("email_all_read_email_deleted")));
    }

    /**
     * 删除所有邮件
     *
     * @param player 玩家
     */
    public static void deleteHistoryEmail(Player player) {
        EmailManager emailManager = new EmailManager(player);
        if (!emailManager.deleteHistoryEmails()) {
            player.sendMessage(warning(UltiTools.languageUtils.getString("email_not_received_any_email")));
            return;
        }
        player.sendMessage(warning(UltiTools.languageUtils.getString("email_all_email_deleted")));
        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_WET_GRASS_BREAK), 10, 1);
    }

    /**
     * 发送一封邮件
     *
     * @param player     发件人
     * @param receiver   收件人
     * @param message    文本消息
     * @param hasContent 是否发送含有附件的邮件
     */
    public static void sendMessage(Player player, OfflinePlayer receiver, String message, boolean hasContent) {
        if (hasContent) {
            sendItem(player, receiver, message, player.getInventory().getItemInMainHand());
        } else {
            sendText(player, receiver, message);
        }
    }

    /**
     * 发送一个文本邮件，不包含附件
     *
     * @param player   发件人
     * @param receiver 收件人
     * @param message  文本内容
     */
    private static void sendText(@NotNull Player player, OfflinePlayer receiver, String message) {
        player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_sending"));
        EmailManager emailManager = new EmailManager(player);
        EmailResponse emailResponse = emailManager.sendTo(receiver, message);
        if (emailResponse == EmailResponse.SEND_SUCCESS) {
            player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_send_successfully"));
            player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.UI_TOAST_OUT), 15, 1);
            if (receiver.isOnline()) {
                pushToReceiver((Player) receiver);
            }
        } else if (emailResponse == EmailResponse.PLAYER_NOTFOUND) {
            player.sendMessage(warning(UltiTools.languageUtils.getString("email_receiver_not_found")));
        } else {
            player.sendMessage(warning(UltiTools.languageUtils.getString("email_send_failed")));
        }
    }

    /**
     * 发送一个包含附件的邮件
     *
     * @param player    发件人
     * @param receiver  收件人
     * @param message   文本消息
     * @param itemStack 附件内容
     */
    private static void sendItem(@NotNull Player player, OfflinePlayer receiver, String message, ItemStack itemStack) {
        if (itemStack.getType() != Material.AIR) {
            player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_sending"));
            message = message == null ? UltiTools.languageUtils.getString("email_sender_no_message") : message;
            EmailManager emailManager = new EmailManager(player);
            EmailResponse emailResponse = emailManager.sendTo(receiver, message, itemStack);
            if (emailResponse == EmailResponse.SEND_SUCCESS) {
                player.getInventory().setItemInMainHand(null);
                player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_send_successfully"));
                player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.UI_TOAST_OUT), 15, 1);
                if (receiver.isOnline()) {
                    pushToReceiver((Player) receiver);
                }
            } else if (emailResponse == EmailResponse.SEND_FAILED) {
                player.sendMessage(warning(UltiTools.languageUtils.getString("email_send_failed")));
            } else {
                player.sendMessage(warning(UltiTools.languageUtils.getString("email_receiver_not_found")));
            }
        } else {
            player.sendMessage(warning(UltiTools.languageUtils.getString("email_hand_item")));
        }
    }
}

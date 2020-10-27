package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.enums.Sounds;
import com.minecraft.ultikits.utils.GUIUtils;
import com.minecraft.ultikits.commands.abstracts.AbstractTabExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.beans.EmailContentBean;
import com.minecraft.ultikits.manager.EmailManager;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.views.EmailView;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
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
            if (strings.length == 1) {
                switch (strings[0].toLowerCase()) {
                    case "read":
                        readEmails(player);
                        return true;
                    case "delhistory":
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                deleteHistoryEmail(emailManager, player);
                            }
                        }.runTaskAsynchronously(UltiTools.getInstance());
                        return true;
                    case "help":
                        sendHelpMessage(player);
                        return true;
                    default:
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("wrong_format"));
                        return false;
                }
            } else if (strings.length == 2) {
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
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("wrong_format"));
                        return false;
                }
            } else if (strings.length >= 3) {
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
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("wrong_format"));
                        return false;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 2; i < strings.length; i++) {
                    String s = " " + strings[i] + " ";
                    if (i == 2) {
                        s = strings[i] + " ";
                    } else if (i == strings.length - 1) {
                        s = " " + strings[i];
                    }
                    stringBuilder.append(s);
                }
                sendMessage(file2, emailManager, player, strings[1], stringBuilder.toString(), hasContent);
                return true;
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("wrong_format"));
                sendHelpMessage(player);
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
        Inventory inventory = EmailView.setUp(player);
        player.openInventory(inventory);
        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.ITEM_BOOK_PAGE_TURN), 10, 1);
    }

    private void sendHelpMessage(@NotNull Player player) {
        player.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getWords("email_help_header"));
        player.sendMessage(ChatColor.GREEN + "/email read " + ChatColor.GRAY + UltiTools.languageUtils.getWords("email_help_read"));
        player.sendMessage(ChatColor.GREEN + "/email delhistory " + ChatColor.GRAY + UltiTools.languageUtils.getWords("email_help_delhistory"));
        player.sendMessage(ChatColor.GREEN + "/email send [" + UltiTools.languageUtils.getWords("player_name") + "] [" + UltiTools.languageUtils.getWords("text_content") + "] " + ChatColor.GRAY + UltiTools.languageUtils.getWords("email_help_send"));
        player.sendMessage(ChatColor.GREEN + "/email senditem [" + UltiTools.languageUtils.getWords("player_name") + "] " + ChatColor.GRAY + UltiTools.languageUtils.getWords("email_help_senditem"));
        player.sendMessage(ChatColor.GREEN + "/email senditem [" + UltiTools.languageUtils.getWords("player_name") + "] [" + UltiTools.languageUtils.getWords("text_content") + "] " + ChatColor.GRAY + UltiTools.languageUtils.getWords("email_help_senditem_with_text"));
        if (!player.isOp()) {
            return;
        }
        player.sendMessage(ChatColor.GREEN + "/email sendall [" + UltiTools.languageUtils.getWords("text_content") + "] " + ChatColor.GRAY + UltiTools.languageUtils.getWords("email_help_sendall"));
    }

    private void pushToReceiver(String receiver) {
        if (Bukkit.getPlayer(receiver) == null) {
            return;
        }
        Player receiverPlayer = Bukkit.getPlayer(receiver);
        receiverPlayer.sendMessage(info(UltiTools.languageUtils.getWords("email_received_new_email")));
        receiverPlayer.playSound(receiverPlayer.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_CHIME), 10, 1);
    }

    public void sendAllMessage(@NotNull Player player, EmailManager emailManager, String receiver) {
        if (!player.isOp()) {
            return;
        }
        player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getWords("email_sending_all_email"));
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
        player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getWords("email_send_successfully"));
        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.UI_TOAST_OUT), 15, 1);
    }

    public void deleteHistoryEmail(@NotNull EmailManager emailManager, Player player) {
        if (!emailManager.deleteHistoryEmails()) {
            player.sendMessage(warning(UltiTools.languageUtils.getWords("email_not_received_any_email")));
            return;
        }
        player.sendMessage(warning(UltiTools.languageUtils.getWords("email_all_email_deleted")));
        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_WET_GRASS_BREAK), 10, 1);
    }

    public void sendMessage(@NotNull File file, EmailManager emailManager, Player player, String receiver) {
        if (!file.exists()) {
            player.sendMessage(warning(UltiTools.languageUtils.getWords("email_receiver_not_found")));
        }
        sendItem(file, emailManager, player, receiver);
    }

    public void sendMessage(@NotNull File file, EmailManager emailManager, Player player, String receiver, String message, boolean hasContent) {
        if (!file.exists()) {
            player.sendMessage(warning(UltiTools.languageUtils.getWords("email_receiver_not_found")));
        }
        if (hasContent) {
            sendItem(file, emailManager, player, receiver, message);
        } else {
            sendText(file, emailManager, player, receiver, message);
        }
    }

    private void sendText(File file, @NotNull EmailManager emailManager, @NotNull Player player, String receiver, String message) {
        player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getWords("email_sending"));
        if (emailManager.sendTo(file, message)) {
            player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getWords("email_send_successfully"));
            player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.UI_TOAST_OUT), 15, 1);
            pushToReceiver(receiver);
        } else {
            player.sendMessage(warning(UltiTools.languageUtils.getWords("email_receiver_not_found")));
        }
    }

    private void sendItem(File file, EmailManager emailManager, @NotNull Player player, String receiver, String message) {
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getWords("email_sending"));
            if (emailManager.sendTo(file, message, itemStack)) {
                player.getInventory().setItemInMainHand(null);
                player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getWords("email_send_successfully"));
                player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.UI_TOAST_OUT), 15, 1);
                pushToReceiver(receiver);
            } else {
                player.sendMessage(warning(UltiTools.languageUtils.getWords("email_send_failed")));
            }
        } else {
            player.sendMessage(warning(UltiTools.languageUtils.getWords("email_hand_item")));
        }
    }

    private void sendItem(File file, EmailManager emailManager, @NotNull Player player, String receiver) {
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getWords("email_sending"));
            if (emailManager.sendTo(file, UltiTools.languageUtils.getWords("email_sender_no_message"), itemStack)) {
                player.getInventory().setItemInMainHand(null);
                player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getWords("email_send_successfully"));
                player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.UI_TOAST_OUT), 15, 1);
                pushToReceiver(receiver);
            } else {
                player.sendMessage(warning(UltiTools.languageUtils.getWords("email_send_failed")));
            }
        } else {
            player.sendMessage(warning(UltiTools.languageUtils.getWords("email_hand_item")));
        }
    }
}

package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.beans.EmailContentBean;
import com.ultikits.enums.Sounds;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.manager.EmailManager;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.views.EmailView;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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

import static com.ultikits.utils.MessagesUtils.info;
import static com.ultikits.utils.MessagesUtils.warning;


public class EmailCommands extends AbstractTabExecutor {

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
                    case "delall":
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                deleteHistoryEmail(emailManager, player);
                            }
                        }.runTaskAsynchronously(UltiTools.getInstance());
                        return true;
                    case "delread":
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                deleteReadEmails(emailManager, player);
                            }
                        }.runTaskAsynchronously(UltiTools.getInstance());
                        return true;
                    case "help":
                        sendHelpMessage(player);
                        return true;
                    default:
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("wrong_format"));
                        return false;
                }
            } else if (strings.length == 2) {
                File file = new File(ConfigsEnum.PLAYER_EMAIL.toString(), strings[1] + ".yml");

                switch (strings[0].toLowerCase()) {
                    case "sendall":
                        if (player.isOp()) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    sendAllMessage(player, emailManager, strings[1]);
                                }
                            }.runTaskAsynchronously(UltiTools.getInstance());
                            return true;
                        }
                        return false;
                    case "senditem":
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                sendMessage(file, emailManager, player, strings[1]);
                            }
                        }.runTaskAsynchronously(UltiTools.getInstance());
                        return true;
                    default:
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("wrong_format"));
                        return false;
                }
            } else if (strings.length >= 3) {
                File file2 = new File(ConfigsEnum.PLAYER_EMAIL.toString(), strings[1] + ".yml");
                boolean hasContent;

                switch (strings[0].toLowerCase()) {
                    case "send":
                        hasContent = false;
                        break;
                    case "sendall":
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                sendAllMessage(player, emailManager, stripSpaceInCommand(strings, 1));
                            }
                        }.runTaskAsynchronously(UltiTools.getInstance());
                        return true;
                    case "senditem":
                        hasContent = true;
                        break;
                    default:
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("wrong_format"));
                        return false;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        sendMessage(file2, emailManager, player, strings[1], stripSpaceInCommand(strings, 2), hasContent);
                    }
                }.runTaskAsynchronously(UltiTools.getInstance());
                return true;
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("wrong_format"));
                sendHelpMessage(player);
                return false;
            }
        }
        return false;
    }

    @Override
    protected @Nullable
    List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
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
                tabCommands.add("[Email Content]");
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
        player.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("email_help_header"));
        player.sendMessage(ChatColor.GREEN + "/email read " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_read"));
        player.sendMessage(ChatColor.GREEN + "/email delhistory " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_delhistory"));
        player.sendMessage(ChatColor.GREEN + "/email delread " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_delread"));
        player.sendMessage(ChatColor.GREEN + "/email send [" + UltiTools.languageUtils.getString("player_name") + "] [" + UltiTools.languageUtils.getString("text_content") + "] " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_send"));
        player.sendMessage(ChatColor.GREEN + "/email senditem [" + UltiTools.languageUtils.getString("player_name") + "] " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_senditem"));
        player.sendMessage(ChatColor.GREEN + "/email senditem [" + UltiTools.languageUtils.getString("player_name") + "] [" + UltiTools.languageUtils.getString("text_content") + "] " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_senditem_with_text"));
        if (!player.isOp()) {
            return;
        }
        player.sendMessage(ChatColor.GREEN + "/email sendall [" + UltiTools.languageUtils.getString("text_content") + "] " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_sendall"));
    }


    /**
     * @param strings 命令
     * @return 去除开头两个参数剩下的命令部分形成的字符串
     */
    private String stripSpaceInCommand(String[] strings, int commandIndex){
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

    private void pushToReceiver(String receiver) {
        if (Bukkit.getPlayer(receiver) == null) {
            return;
        }
        Player receiverPlayer = Bukkit.getPlayer(receiver);
        receiverPlayer.sendMessage(info(UltiTools.languageUtils.getString("email_received_new_email").replace("\\n", "\n")));
        receiverPlayer.playSound(receiverPlayer.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_NOTE_BLOCK_CHIME), 10, 1);
    }

    public void sendAllMessage(@NotNull Player player, EmailManager emailManager, String receiver) {
        if (!player.isOp()) {
            return;
        }
        player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_sending_all_email"));
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            for (OfflinePlayer player1 : UltiTools.getInstance().getServer().getOfflinePlayers()) {
                File file = new File(ConfigsEnum.PLAYER_EMAIL.toString(), player1.getName() + ".yml");
                emailManager.sendTo(file, receiver, itemStack);
                if (player1.isOnline()) {
                    pushToReceiver(player1.getName());
                }
            }
        } else {
            for (OfflinePlayer player2 : UltiTools.getInstance().getServer().getOfflinePlayers()) {
                File file = new File(ConfigsEnum.PLAYER_EMAIL.toString(), player2.getName() + ".yml");
                emailManager.sendTo(file, receiver);
                if (player2.isOnline()) {
                    pushToReceiver(player2.getName());
                }
            }
        }
        player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_send_successfully"));
        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.UI_TOAST_OUT), 15, 1);
    }

    public void deleteReadEmails(@NotNull EmailManager emailManager, Player player) {
        for (EmailContentBean emailContentBean : emailManager.getEmails().values()) {
            if (emailContentBean.getRead()) {
                emailManager.deleteEmail(emailContentBean.getUuid());
            }
        }
        player.sendMessage(warning(UltiTools.languageUtils.getString("email_all_read_email_deleted")));
    }

    public void deleteHistoryEmail(@NotNull EmailManager emailManager, Player player) {
        if (!emailManager.deleteHistoryEmails()) {
            player.sendMessage(warning(UltiTools.languageUtils.getString("email_not_received_any_email")));
            return;
        }
        player.sendMessage(warning(UltiTools.languageUtils.getString("email_all_email_deleted")));
        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_WET_GRASS_BREAK), 10, 1);
    }

    public void sendMessage(@NotNull File file, EmailManager emailManager, Player player, String receiver) {
        if (!file.exists()) {
            player.sendMessage(warning(UltiTools.languageUtils.getString("email_receiver_not_found")));
        }
        sendItem(file, emailManager, player, receiver);
    }

    public void sendMessage(@NotNull File file, EmailManager emailManager, Player player, String receiver, String message, boolean hasContent) {
        if (!file.exists()) {
            player.sendMessage(warning(UltiTools.languageUtils.getString("email_receiver_not_found")));
        }
        if (hasContent) {
            sendItem(file, emailManager, player, receiver, message);
        } else {
            sendText(file, emailManager, player, receiver, message);
        }
    }

    private void sendText(File file, @NotNull EmailManager emailManager, @NotNull Player player, String receiver, String message) {
        player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_sending"));
        if (emailManager.sendTo(file, message)) {
            player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_send_successfully"));
            player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.UI_TOAST_OUT), 15, 1);
            pushToReceiver(receiver);
        } else {
            player.sendMessage(warning(UltiTools.languageUtils.getString("email_receiver_not_found")));
        }
    }

    private void sendItem(File file, EmailManager emailManager, @NotNull Player player, String receiver, String message) {
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_sending"));
            if (emailManager.sendTo(file, message, itemStack)) {
                player.getInventory().setItemInMainHand(null);
                player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_send_successfully"));
                player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.UI_TOAST_OUT), 15, 1);
                pushToReceiver(receiver);
            } else {
                player.sendMessage(warning(UltiTools.languageUtils.getString("email_send_failed")));
            }
        } else {
            player.sendMessage(warning(UltiTools.languageUtils.getString("email_hand_item")));
        }
    }

    private void sendItem(File file, EmailManager emailManager, @NotNull Player player, String receiver) {
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_sending"));
            if (emailManager.sendTo(file, UltiTools.languageUtils.getString("email_sender_no_message"), itemStack)) {
                player.getInventory().setItemInMainHand(null);
                player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_send_successfully"));
                player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.UI_TOAST_OUT), 15, 1);
                pushToReceiver(receiver);
            } else {
                player.sendMessage(warning(UltiTools.languageUtils.getString("email_send_failed")));
            }
        } else {
            player.sendMessage(warning(UltiTools.languageUtils.getString("email_hand_item")));
        }
    }
}

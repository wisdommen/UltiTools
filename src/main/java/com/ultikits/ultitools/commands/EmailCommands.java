package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.enums.Sounds;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.ultikits.ultitools.utils.EmailUtils.*;


public class EmailCommands extends AbstractTabExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {

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
                                deleteHistoryEmail(player);
                            }
                        }.runTaskAsynchronously(UltiTools.getInstance());
                        return true;
                    case "delread":
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                deleteReadEmails(player);
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
                switch (strings[0].toLowerCase()) {
                    case "sendall":
                        if (player.isOp()) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_sending_all_email"));
                                    sendAllMessage(strings[1], player.getInventory().getItemInMainHand());
                                    player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_send_successfully"));
                                    player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.UI_TOAST_OUT), 15, 1);
                                }
                            }.runTaskAsynchronously(UltiTools.getInstance());
                            return true;
                        }
                        return false;
                    case "senditem":
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                sendMessage(player, Bukkit.getOfflinePlayer(strings[1]), null, true);
                            }
                        }.runTaskAsynchronously(UltiTools.getInstance());
                        return true;
                    default:
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("wrong_format"));
                        return false;
                }
            } else if (strings.length >= 3) {
                boolean hasContent;

                switch (strings[0].toLowerCase()) {
                    case "send":
                        hasContent = false;
                        break;
                    case "sendall":
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_sending_all_email"));
                                sendAllMessage(stripSpaceInCommand(strings, 1), player.getInventory().getItemInMainHand());
                                player.sendMessage(ChatColor.GOLD + UltiTools.languageUtils.getString("email_send_successfully"));
                                player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.UI_TOAST_OUT), 15, 1);
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
                        sendMessage(player, Bukkit.getOfflinePlayer(strings[1]), stripSpaceInCommand(strings, 2), hasContent);
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
                tabCommands.add("delall");
                tabCommands.add("delread");
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

    private static void sendHelpMessage(@NotNull Player player) {
        player.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("email_help_header"));
        player.sendMessage(ChatColor.GREEN + "/email read " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_read"));
        player.sendMessage(ChatColor.GREEN + "/email delall " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_delhistory"));
        player.sendMessage(ChatColor.GREEN + "/email delread " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_delread"));
        player.sendMessage(ChatColor.GREEN + "/email send [" + UltiTools.languageUtils.getString("player_name") + "] [" + UltiTools.languageUtils.getString("text_content") + "] " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_send"));
        player.sendMessage(ChatColor.GREEN + "/email senditem [" + UltiTools.languageUtils.getString("player_name") + "] " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_senditem"));
        player.sendMessage(ChatColor.GREEN + "/email senditem [" + UltiTools.languageUtils.getString("player_name") + "] [" + UltiTools.languageUtils.getString("text_content") + "] " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_senditem_with_text"));
        if (!player.isOp()) {
            return;
        }
        player.sendMessage(ChatColor.GREEN + "/email sendall [" + UltiTools.languageUtils.getString("text_content") + "] " + ChatColor.GRAY + UltiTools.languageUtils.getString("email_help_sendall"));
    }

}

package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.manager.EmailManager;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.DatabasePlayerTools;
import com.ultikits.ultitools.views.ApplyView;
import com.ultikits.ultitools.views.FriendsView;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SocialSystemCommands extends AbstractTabExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!UltiTools.isProVersion) {
            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("warning_pro_fuction")));
            return true;
        }
        switch (strings.length) {
            case 0:
                Inventory friendsGUI = FriendsView.setUp(player);
                player.openInventory(friendsGUI);
                return true;
            case 1:
                switch (strings[0]) {
                    case "list":
                        player.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("friend_list"));
                        for (String friend : DatabasePlayerTools.getFriendList(player)) {
                            player.sendMessage(friend);
                        }
                        return true;
                }
                return true;
            case 2:
                File receiverFile = new File(ConfigsEnum.PLAYER_EMAIL.toString(), strings[1] + ".yml");
                OfflinePlayer requestPlayer = Bukkit.getOfflinePlayer(strings[1]);
                switch (strings[0]) {
                    case "add":
                        if (DatabasePlayerTools.getFriendList(player).contains(requestPlayer.getName())) {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("friend_had"));
                            return true;
                        }
                        EmailManager.sendNotification(receiverFile, player.getName() + UltiTools.languageUtils.getString("friend_apply_lore"), null, Collections.singletonList("friends apply " + player.getName()));
                        player.sendMessage(ChatColor.AQUA + UltiTools.languageUtils.getString("friend_apply_sent"));
                        return true;
                    case "remove":
                        if (!DatabasePlayerTools.getFriendList(player).contains(requestPlayer.getName())) {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("friend_not_friend"));
                            return true;
                        }
                        DatabasePlayerTools.removePlayerFriends(player, requestPlayer);
                        player.sendMessage(ChatColor.AQUA + UltiTools.languageUtils.getString("friend_deleted") + requestPlayer.getName());
                        return true;
                    case "accept":
                        if (DatabasePlayerTools.getFriendList(player).contains(requestPlayer.getName())) {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("friend_had"));
                            return true;
                        }
                        DatabasePlayerTools.addPlayerFriends(player, requestPlayer);
                        DatabasePlayerTools.addPlayerFriends(requestPlayer, player);
                        player.sendMessage(ChatColor.AQUA + String.format(UltiTools.languageUtils.getString("friend_apply_accept"), ChatColor.YELLOW + strings[1] + ChatColor.AQUA));
                        return true;
                    case "reject":
                        if (!DatabasePlayerTools.getFriendList(player).contains(requestPlayer.getName())) {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("friend_not_friend"));
                            return true;
                        }
                        EmailManager.sendNotification(receiverFile, player.getName() + UltiTools.languageUtils.getString("friend_apply_lore"), null, Collections.singletonList("friends apply " + player.getName()));
                        player.sendMessage(ChatColor.AQUA + String.format(UltiTools.languageUtils.getString("friend_apply_reject"), ChatColor.YELLOW + strings[1] + ChatColor.AQUA));
                        return true;
                    case "apply":
                        Inventory inventory = ApplyView.setUp(strings[1] + UltiTools.languageUtils.getString("friend_apply"));
                        player.openInventory(inventory);
                        return true;
                }
                return true;
            case 3:
                switch (strings[0]) {

                }
                return true;
        }
        return false;
    }

    @Nullable
    @Override
    protected List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        List<String> tabCommands = new ArrayList<>();
        switch (strings.length) {
            case 0:
                tabCommands.add("list");
                return tabCommands;
            case 1:
                tabCommands.add("add");
                tabCommands.add("remove");
                tabCommands.add("accept");
                tabCommands.add("reject");
                tabCommands.add("apply");
                return tabCommands;
        }
        return tabCommands;
    }
}

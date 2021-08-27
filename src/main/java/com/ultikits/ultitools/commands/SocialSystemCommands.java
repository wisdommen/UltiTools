package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.manager.EmailManager;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.DatabasePlayerTools;
import com.ultikits.ultitools.utils.EmailUtils;
import com.ultikits.ultitools.views.ApplyView;
import com.ultikits.ultitools.views.FriendsView;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SocialSystemCommands extends AbstractTabExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!UltiTools.getInstance().getProChecker().getProStatus()) {
            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("warning_pro_function")));
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
                OfflinePlayer requestPlayer = Bukkit.getOfflinePlayer(strings[1]);
                EmailManager emailManager = new EmailManager(player);
                switch (strings[0]) {
                    case "add":
                        if (player.getName().equals(strings[1])) {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("friend_not_self"));
                            return true;
                        }
                        if (DatabasePlayerTools.getFriendList(player).contains(requestPlayer.getName())) {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("friend_had"));
                            return true;
                        }
                        setApplyList(player.getName(), strings[1], true);
                        emailManager.sendNotification(player.getName() + UltiTools.languageUtils.getString("friend_apply_lore"), null, Collections.singletonList("friends apply " + player.getName()));
                        Player player2 = Bukkit.getPlayer(strings[1]);
                        if (player2 != null) {
                            EmailUtils.pushToReceiver(player2);
                        }
                        player.sendMessage(ChatColor.AQUA + UltiTools.languageUtils.getString("friend_apply_sent"));
                        return true;
                    case "remove":
                        if (!DatabasePlayerTools.getFriendList(player).contains(requestPlayer.getName())) {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("friend_not_friend"));
                            return true;
                        }
                        DatabasePlayerTools.removePlayerFriends(player, requestPlayer);
                        DatabasePlayerTools.removePlayerFriends(requestPlayer, player);
                        emailManager.sendNotification(String.format(UltiTools.languageUtils.getString("friend_no_more_friend"), player.getName(), player.getName()), null, null);
                        player.sendMessage(ChatColor.AQUA + String.format(UltiTools.languageUtils.getString("friend_deleted"), ChatColor.YELLOW + requestPlayer.getName() + ChatColor.AQUA));
                        return true;
                    case "accept":
                        if (DatabasePlayerTools.getFriendList(player).contains(requestPlayer.getName())) {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("friend_had"));
                            return true;
                        }
                        setApplyList(strings[1], player.getName(), false);
                        DatabasePlayerTools.addPlayerFriends(player, requestPlayer);
                        DatabasePlayerTools.addPlayerFriends(requestPlayer, player);
                        if (requestPlayer.isOnline()) {
                            Player player1 = (Player) requestPlayer;
                            player1.sendMessage(ChatColor.AQUA + String.format(UltiTools.languageUtils.getString("friend_apply_accept"), ChatColor.YELLOW + player.getName() + ChatColor.AQUA));
                        }
                        player.sendMessage(ChatColor.AQUA + String.format(UltiTools.languageUtils.getString("friend_apply_accept"), ChatColor.YELLOW + strings[1] + ChatColor.AQUA));
                        return true;
                    case "reject":
                        if (DatabasePlayerTools.getFriendList(player).contains(requestPlayer.getName())) {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("friend_had"));
                            return true;
                        }
                        setApplyList(strings[1], player.getName(), false);
                        if (requestPlayer.isOnline()) {
                            Player player1 = (Player) requestPlayer;
                            player1.sendMessage(ChatColor.AQUA + String.format(UltiTools.languageUtils.getString("friend_apply_reject_re"), ChatColor.YELLOW + player.getName() + ChatColor.AQUA));
                        }
                        player.sendMessage(ChatColor.AQUA + String.format(UltiTools.languageUtils.getString("friend_apply_reject"), ChatColor.YELLOW + strings[1] + ChatColor.AQUA));
                        return true;
                    case "apply":
                        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
                        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                        List<String> friendsApply = config.getStringList("friends_apply");
                        if (!friendsApply.contains(strings[1])) {
                            player.sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getString("friend_not_applied"), ChatColor.YELLOW + strings[1] + ChatColor.RED));
                            return true;
                        }
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
            case 1:
                tabCommands.add("list");
                tabCommands.add("add");
                tabCommands.add("remove");
                tabCommands.add("accept");
                tabCommands.add("reject");
                tabCommands.add("apply");
                return tabCommands;
            case 2:
                if (strings[0].equals("remove")) {
                    return DatabasePlayerTools.getFriendList(player);
                }
                if (strings[0].equals("accept") || strings[0].equals("reject") || strings[0].equals("apply")) {
                    File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    return config.getStringList("friends_apply");
                }
                for (OfflinePlayer player1 : Bukkit.getOfflinePlayers()) {
                    tabCommands.add(player1.getName());
                }
                return tabCommands;
        }
        return tabCommands;
    }

    private static void setApplyList(String applier, String name, boolean operate) {
        File file = new File(ConfigsEnum.PLAYER.toString(), name + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> friendsApply = config.getStringList("friends_apply");
        if (operate) {
            if (!friendsApply.contains(applier)) {
                friendsApply.add(applier);
            }
        } else {
            friendsApply.remove(applier);
        }
        config.set("friends_apply", friendsApply);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

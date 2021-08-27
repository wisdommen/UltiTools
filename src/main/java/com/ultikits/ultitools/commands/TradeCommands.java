package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.TradeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class TradeCommands extends AbstractTabExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!UltiTools.isProVersion) {
            player.sendMessage(UltiTools.languageUtils.getString("warning_pro_fuction"));
            return true;
        }

        if (strings.length == 0) return false;
        if (strings.length > 3) return false;

        switch (strings[0]) {
            case "accept":
                if (TradeUtils.isPlayerInRequestMode(player)) {
                    TradeUtils.acceptTrade(player);
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_you_have_no_request"));
                }
                return true;
            case "reject":
                if (TradeUtils.isPlayerInRequestMode(player)) {
                    TradeUtils.rejectTrade(player);
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_you_have_no_request"));
                }
                return true;
            case "toggle":
                TradeUtils.toggleTrade(player);
                return true;
            case "ban":
                if (strings.length != 3) return false;
                if (strings[1].equalsIgnoreCase("add")) {
                    TradeUtils.addBannedPlayer(player, strings[2]);
                    player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_you_banned"));
                    return true;
                }
                if (strings[1].equalsIgnoreCase("remove")) {
                    TradeUtils.removeBannedPlayer(player, strings[2]);
                    player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_you_un_banned"));
                    return true;
                }
                return false;
        }
        if (strings[0].equals(player.getName())) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_you_cannot_trade_with_yourself"));
            return true;
        }
        Player To = Bukkit.getPlayerExact(strings[0]);
        if (To == null) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_player_not_found"));
            return true;
        }
        if (!To.isOnline()) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_player_not_found"));
            return true;
        }
        if (!TradeUtils.isCrossWorldTradeAllowed() && !player.getWorld().getName().equals(To.getWorld().getName())) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_cannot_in_different_worlds"));
            return true;
        }
        if ((TradeUtils.isBannedPlayer(To, player))) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_you_cannot_do"));
            return true;
        }
        TradeUtils.requestTrade(player, To);
        return true;
    }

    @Nullable
    @Override
    protected List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        switch (strings.length) {
            case 1:
                return Arrays.asList("accept", "reject", "toggle", "ban");
            case 2:
                if (strings[0].equalsIgnoreCase("ban")) {
                    return Arrays.asList("add", "remove");
                } else {
                    return null;
                }
        }
        return null;
    }
}

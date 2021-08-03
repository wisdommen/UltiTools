package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.TradeUtils;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TradeCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!UltiTools.isProVersion) {
            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("warning_pro_fuction")));
            return true;
        }
        if (strings.length == 0) return false;
        if ("accept".equals(strings[0])) {
            if (TradeUtils.isPlayerInRequestMode(player)) {
                TradeUtils.acceptTrade(player);
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_you_have_no_request"));
            }
            return true;
        } else if ("reject".equals(strings[0])) {
            if (TradeUtils.isPlayerInRequestMode(player)) {
                TradeUtils.getInRequestMode().inverse().remove(player.getName());
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_you_have_no_request"));
            }
            return true;
        }else if ("toggle".equals(strings[0])) {
            TradeUtils.toggleTrade(player);
            return true;
        }else if ("ban".equals(strings[0])) {
            if ("add".equals(strings[1])) {
                TradeUtils.addBannedPlayer(player, strings[2]);
            } else if ("remove".equals(strings[1])) {
                TradeUtils.removeBannedPlayer(player, strings[2]);
            }
            return true;
        } else {
            Player To = UltiTools.getInstance().getServer().getPlayerExact(strings[0]);
            if (To != null) {
                if (To.isOnline()) {
                    if (player.getWorld().getName().equals(To.getWorld().getName())) {
                        TradeUtils.requestTrade(player, To);
                    } else if (TradeUtils.isCrossWorldTradeAllowed()) {
                        if (TradeUtils.isBannedPlayer(To, player)) {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_you_cannot_do"));
                        } else {
                            TradeUtils.requestTrade(player, To);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_cannot_in_different_worlds"));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_player_not_found"));
                }
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_player_not_found"));
            }
            return true;
        }
    }
}

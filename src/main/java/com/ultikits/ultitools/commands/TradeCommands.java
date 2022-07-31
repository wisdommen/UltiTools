package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.services.TradeService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@CmdExecutor(function = "trade", permission = "ultikits.tools.trade", description = "trade_function", alias = "t,trade")
public class TradeCommands extends AbstractTabExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!UltiTools.getInstance().getConfig().getBoolean("enable_pro") || !UltiTools.getInstance().getProChecker().getProStatus()) {
            player.sendMessage(UltiTools.languageUtils.getString("warning_pro_function"));
            return true;
        }

        if (strings.length == 0) return false;
        if (strings.length > 3) return false;

        switch (strings[0]) {
            case "accept":
                if (TradeService.isPlayerInRequestMode(player)) {
                    TradeService.acceptTrade(player);
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_you_have_no_request"));
                }
                return true;
            case "reject":
                if (TradeService.isPlayerInRequestMode(player)) {
                    TradeService.rejectTrade(player);
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_you_have_no_request"));
                }
                return true;
            case "toggle":
                TradeService.toggleTrade(player);
                return true;
            case "ban":
                if (strings.length != 3) return false;
                if (strings[1].equalsIgnoreCase("add")) {
                    TradeService.addBannedPlayer(player, strings[2]);
                    player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_you_banned"));
                    return true;
                }
                if (strings[1].equalsIgnoreCase("remove")) {
                    TradeService.removeBannedPlayer(player, strings[2]);
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
        if (!TradeService.isCrossWorldTradeAllowed() && !player.getWorld().getName().equals(To.getWorld().getName())) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_cannot_in_different_worlds"));
            return true;
        }
        if ((TradeService.isBannedPlayer(To, player))) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("trade_you_cannot_do"));
            return true;
        }
        TradeService.requestTrade(player, To);
        return true;
    }

    @Nullable
    @Override
    protected List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        List<String> tabCommands = new ArrayList<>();
        switch (strings.length) {
            case 1:
                tabCommands.add("accept");
                tabCommands.add("reject");
                tabCommands.add("toggle");
                tabCommands.add("ban");
                return tabCommands;
            case 2:
                if (strings[0].equals("ban")) {
                    tabCommands.add("add");
                    tabCommands.add("remove");
                    return tabCommands;
                }
                for (Player player1 : Bukkit.getOnlinePlayers()) {
                    tabCommands.add(player1.getName());
                }
                return tabCommands;
            case 3:
                for (Player player1 : Bukkit.getOnlinePlayers()) {
                    tabCommands.add(player1.getName());
                }
                return tabCommands;
            default:
                return null;
        }
    }
}

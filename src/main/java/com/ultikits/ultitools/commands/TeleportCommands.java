package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.tasks.TpTimerTask;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.ultikits.ultitools.commands.TpaHereCommands.getTpTabList;

public class TeleportCommands extends AbstractTabExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        switch (strings.length) {
            case 1:
                switch (strings[0]) {
                    case "accept":
                        Player teleporter = TpTimerTask.tpTemp.get(player);
                        if (teleporter == null) {
                            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_no_request")));
                            return true;
                        }
                        TpTimerTask.tpTemp.put(player, null);
                        TpTimerTask.tpTimer.put(player, 0);
                        teleporter.teleport(player.getLocation());
                        teleporter.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpa_teleport_success")));
                        return true;
                    case "reject":
                        Player teleporter2 = TpTimerTask.tpTemp.get(player);
                        if (teleporter2 == null) {
                            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_no_request")));
                            return true;
                        }
                        teleporter2.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_teleport_rejected")));
                        player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpa_rejected")));
                        TpTimerTask.tpTemp.put(player, null);
                        TpTimerTask.tpTimer.put(player, 0);
                        return true;
                    default:
                        Player target = Bukkit.getPlayerExact(strings[0]);
                        if (target == null) {
                            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_player_not_found")));
                            return true;
                        }
                        TpTimerTask.tpTemp.put(target, player);
                        TpTimerTask.tpTimer.put(target, 20);
                        player.sendMessage(MessagesUtils.info(String.format(UltiTools.languageUtils.getString("tpa_tp_send_successfully"), target.getName())));
                        target.sendMessage(MessagesUtils.info(String.format(UltiTools.languageUtils.getString("tpa_tp_enquire"), player.getName())));
                        target.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpa_accept_tip")));
                        target.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpa_reject_tip")));
                        return true;
                }
            default:
                return false;
        }
    }

    @Override
    protected @Nullable
    List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        return getTpTabList(strings);
    }

}


package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.beans.TpBean;
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
                        Player teleporter = TpBean.tpTemp.get(player);
                        if (teleporter == null) {
                            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_no_request")));
                            return true;
                        }
                        TpBean.tpTemp.put(player, null);
                        TpBean.tpTimer.put(player, 0);
                        teleporter.teleport(player.getLocation());
                        teleporter.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpa_teleport_success")));
                        return true;
                    case "reject":
                        Player teleporter2 = TpBean.tpTemp.get(player);
                        teleporter2.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_teleport_rejected")));
                        player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpa_rejected")));
                        TpBean.tpTemp.put(player, null);
                        TpBean.tpTimer.put(player, 0);
                        return true;
                    default:
                        Player target = Bukkit.getPlayerExact(strings[0]);
                        if (target == null) {
                            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_player_not_found")));
                            return true;
                        }
                        TpBean.tpTemp.put(target, player);
                        TpBean.tpTimer.put(target, 20);
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


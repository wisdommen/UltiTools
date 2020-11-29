package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.FunctionUtils;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeleportCommands extends AbstractTabExecutor {
    public static Map<Player, Player> temp = new HashMap<>();
    protected static Map<Player, Integer> timer = new HashMap<>();

    static {
        new Timer().runTaskTimer(UltiTools.getInstance(), 0, 20L);
    }

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        switch (strings.length) {
            case 1:
                switch (strings[0]) {
                    case "accept":
                        Player teleporter = temp.get(player);
                        if (teleporter == null) {
                            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_no_request")));
                            return true;
                        }
                        temp.put(player, null);
                        timer.put(player, 0);
                        teleporter.teleport(player.getLocation());
                        teleporter.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpa_teleport_success")));
                        return true;
                    case "reject":
                        Player teleporter2 = temp.get(player);
                        teleporter2.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_teleport_rejected")));
                        temp.put(player, null);
                        timer.put(player, 0);
                        return true;
                    default:
                        Player target = Bukkit.getPlayerExact(strings[0]);
                        if (target == null) {
                            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_player_not_found")));
                            return true;
                        }
                        temp.put(target, player);
                        timer.put(target, 20);
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
        List<String> tabCommands = new ArrayList<>();
        if (strings.length == 1) {
            tabCommands.add("accept");
            tabCommands.add("reject");
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                tabCommands.add(player1.getName());
            }
            return tabCommands;
        }
        return null;
    }

}

class Timer extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : TeleportCommands.temp.keySet()) {
            int time = TeleportCommands.timer.get(player);
            if (time > 0) {
                time -= 1;
            } else {
                if (TeleportCommands.temp.get(player) != null) {
                    TeleportCommands.temp.get(player).sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_request_timeout")));
                    TeleportCommands.temp.put(player, null);
                }
            }
            TeleportCommands.timer.put(player, time);
        }
    }
}

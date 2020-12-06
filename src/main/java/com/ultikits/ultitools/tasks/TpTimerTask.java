package com.ultikits.ultitools.tasks;


import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TpTimerTask {
    public static Map<Player, Player> tpTemp = new HashMap<>();
    public static Map<Player, Integer> tpTimer = new HashMap<>();
    public static Map<Player, Player> tphereTemp = new HashMap<>();
    public static Map<Player, Integer> tphereTimer = new HashMap<>();

    static {
        new tpTimer().runTaskTimerAsynchronously(UltiTools.getInstance(), 0, 20L);
        new tphereTimer().runTaskTimerAsynchronously(UltiTools.getInstance(), 0, 20L);
    }
}

class tpTimer extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : TpTimerTask.tpTemp.keySet()) {
            int time = TpTimerTask.tpTimer.get(player);
            if (time > 0) {
                time -= 1;
            } else {
                if (TpTimerTask.tpTemp.get(player) != null) {
                    TpTimerTask.tpTemp.get(player).sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_request_timeout")));
                    TpTimerTask.tpTemp.put(player, null);
                }
            }
            TpTimerTask.tpTimer.put(player, time);
        }
    }
}

class tphereTimer extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : TpTimerTask.tphereTemp.keySet()) {
            int time = TpTimerTask.tphereTimer.get(player);
            if (time > 0) {
                time -= 1;
            } else {
                if (TpTimerTask.tphereTemp.get(player) != null) {
                    TpTimerTask.tphereTemp.get(player).sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_request_timeout")));
                    TpTimerTask.tphereTemp.put(player, null);
                }
            }
            TpTimerTask.tphereTimer.put(player, time);
        }
    }
}

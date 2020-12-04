package com.ultikits.ultitools.beans;

import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TpBean {
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
        for (Player player : TpBean.tpTemp.keySet()) {
            int time = TpBean.tpTimer.get(player);
            if (time > 0) {
                time -= 1;
            } else {
                if (TpBean.tpTemp.get(player) != null) {
                    TpBean.tpTemp.get(player).sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_request_timeout")));
                    TpBean.tpTemp.put(player, null);
                }
            }
            TpBean.tpTimer.put(player, time);
        }
    }
}

class tphereTimer extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : TpBean.tphereTemp.keySet()) {
            int time = TpBean.tphereTimer.get(player);
            if (time > 0) {
                time -= 1;
            } else {
                if (TpBean.tphereTemp.get(player) != null) {
                    TpBean.tphereTemp.get(player).sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_request_timeout")));
                    TpBean.tphereTemp.put(player, null);
                }
            }
            TpBean.tphereTimer.put(player, time);
        }
    }
}

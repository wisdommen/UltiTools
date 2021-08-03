package com.ultikits.ultitools.tasks;

import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.TradeUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TradeTask extends BukkitRunnable {

    private final BossBar bossBar;
    private final Player to;

    public TradeTask (Player From, Player To) {
        to = To;
        bossBar = Bukkit.createBossBar(
                UltiTools.languageUtils.getString("trade_request").replace("%s", From.getName()),
                BarColor.PURPLE,
                BarStyle.SOLID,
                BarFlag.CREATE_FOG
        );
        bossBar.setProgress(1.00);
        bossBar.addPlayer(To);
    }

    @Override
    public void run() {
        if (bossBar.getProgress() > 0.01 && TradeUtils.isPlayerInRequestMode(to)) {
            bossBar.setProgress(bossBar.getProgress() - 0.01);
        } else if (TradeUtils.isPlayerInTradeMode(to)){
            bossBar.removeAll();
            this.cancel();
        } else {
            TradeUtils.rejectTrade(to);
            bossBar.removeAll();
            this.cancel();
        }
    }
}

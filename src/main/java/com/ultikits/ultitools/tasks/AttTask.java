package com.ultikits.ultitools.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AttTask extends BukkitRunnable {

    private final BossBar bossBar;

    public AttTask (Player To, String Message) {
        bossBar = Bukkit.createBossBar(ChatColor.BOLD + Message, BarColor.YELLOW, BarStyle.SOLID, BarFlag.CREATE_FOG);
        bossBar.setProgress(1.00);
        bossBar.addPlayer(To);
    }

    @Override
    public void run() {
        if (bossBar.getProgress() > 0.01) {
            bossBar.setProgress(bossBar.getProgress() - 0.01);
        } else {
            bossBar.removeAll();
            this.cancel();
        }
    }
}

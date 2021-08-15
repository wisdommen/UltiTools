package com.ultikits.ultitools.tasks;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BroadcastTask {
    static File announcementFile = new File(ConfigsEnum.ANNOUNCEMENT.toString());
    public static YamlConfiguration config = YamlConfiguration.loadConfiguration(announcementFile);

    public static void run() {
        int messagePeriod = config.getInt("announcement.message.period");
        int bossbarPeriod = config.getInt("announcement.bossbar.period");
        int titlePeriod = config.getInt("announcement.title.period");
        if (config.getBoolean("announcement.message.enable")) {
            new BukkitRunnable() {
                int i = 0;
                final int count = config.getStringList("announcement.message.broadcast").size();

                @Override
                public void run() {
                    Bukkit.broadcastMessage(config.getStringList("announcement.message.broadcast").get(i));
                    i++;
                    if (i == count) i = 0;
                }
            }.runTaskTimerAsynchronously(UltiTools.getInstance(), 0, (messagePeriod > 0 ? messagePeriod : 1200) * 20L);
        }
        if (config.getBoolean("announcement.bossbar.enable")) {
            new BukkitRunnable() {
                private final int count = config.getStringList("announcement.bossbar.broadcast").size();
                int i = 0;

                @Override
                public void run() {
                    BossBar bossbar = Bukkit.createBossBar(config.getStringList("announcement.bossbar.broadcast").get(i), BarColor.PINK, BarStyle.SOLID, BarFlag.CREATE_FOG);
                    bossbar.setProgress(1.00);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        bossbar.addPlayer(player);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            bossbar.removeAll();
                        }
                    }.runTaskLaterAsynchronously(UltiTools.getInstance(), config.getInt("announcement.bossbar.stay") * 20L);
                    i++;
                    if (i == count) i = 0;
                }
            }.runTaskTimerAsynchronously(UltiTools.getInstance(), 0, (bossbarPeriod > 0 ? bossbarPeriod : 1800) * 20L);
        }
        if (config.getBoolean("announcement.title.enable")) {
            new BukkitRunnable() {
                int i = 0;
                final int count = config.getStringList("announcement.title.broadcast.title").size();
                final List<String> titlesList = new ArrayList<>(config.getConfigurationSection("announcement.title.broadcast").getKeys(false));

                @Override
                public void run() {
                    String title = titlesList.get(i);
                    String subtitle = config.getString("announcement.title.broadcast." + title);

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(title, subtitle, config.getInt("announcement.title.fade-in"), config.getInt("announcement.title.stay"), config.getInt("announcement.title.fade-out"));
                    }
                    i++;
                    if (i == count) i = 0;
                }
            }.runTaskTimerAsynchronously(UltiTools.getInstance(), 0, (titlePeriod > 0 ? titlePeriod : 12000) * 20L);
        }
    }
}

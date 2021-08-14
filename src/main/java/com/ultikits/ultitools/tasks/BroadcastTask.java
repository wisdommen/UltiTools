package com.ultikits.ultitools.tasks;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BroadcastTask {
    public static YamlConfiguration config = BossbarBroadcastTask.announcementConfig;
    public void run() {
        if(config.getBoolean("announcement.message.enable")) {
            new MessageBroadcastTask().runTaskTimer(UltiTools.getInstance(),0,config.getInt("announcement.message.period") * 20L);
        }
        if(config.getBoolean("announcement.bossbar.enable")) {
            new BossbarBroadcastTask().runTaskTimer(UltiTools.getInstance(),0,config.getInt("announcement.bossbar.period") * 20L);
        }
        if(config.getBoolean("announcement.title.enable")) {
            new TitleBroadcastTask().runTaskTimer(UltiTools.getInstance(),0,config.getInt("announcement.title.period") * 20L);
        }
    }
 }


class BossbarBroadcastTask extends BukkitRunnable {
    public static BossBar bossbar;
    int i = 0;
    static File announcementFile = new File(ConfigsEnum.ANNOUNCEMENT.toString());
    public static YamlConfiguration announcementConfig = YamlConfiguration.loadConfiguration(announcementFile);

    public int getLines() {
        int lines = announcementConfig.getStringList("announcement.bossbar.broadcast").size();
        return lines;
    }

    @Override
    public void run() {
        int count = getLines();
        List<Object> onlinePlayerList = (List) Bukkit.getOnlinePlayers();
        bossbar = Bukkit.createBossBar(announcementConfig.getStringList("announcement.bossbar.broadcast").get(i), BarColor.PINK, BarStyle.SOLID, BarFlag.CREATE_FOG);
        bossbar.setProgress(1.00);
        for(Object player : onlinePlayerList) {
            Player onlinePlayer = (Player) player;
            bossbar.addPlayer(onlinePlayer);
        }
        new BossbarBroadcastTask.BossbarBroadcastRemoveTask().runTaskTimer(UltiTools.getInstance(),announcementConfig.getInt("announcement.bossbar.stay") *20L,0);
        i++;
        if(i == count) {
            i = 0;
        }
    }

    public static class BossbarBroadcastRemoveTask extends BukkitRunnable{
        @Override
        public void run() {
            bossbar.removeAll();
            this.cancel();
        }
    }
}

class MessageBroadcastTask extends BukkitRunnable {
    int i = 0;
    private int getLines() {
        int lines = BossbarBroadcastTask.announcementConfig.getStringList("announcement.message.broadcast").size();
        return lines;
    }

    @Override
    public void run() {
        int count = getLines();
        Bukkit.broadcastMessage(BossbarBroadcastTask.announcementConfig.getStringList("announcement.message.broadcast").get(i));
        i++;
        if(i == count) {
            i = 0;
        }
    }
}



class TitleBroadcastTask extends BukkitRunnable {
    int i = 0;
    List<String> titlesList = new ArrayList<>();
    YamlConfiguration config = BossbarBroadcastTask.announcementConfig;
    ConfigurationSection section = config.getConfigurationSection("announcement.title.broadcast");

    private int getLines() {
        int lines = BossbarBroadcastTask.announcementConfig.getStringList("announcement.title.broadcast.title").size();
        return lines;
    }
    List<Object> onlinePlayerList = (List) Bukkit.getOnlinePlayers();
    Set<String> titlesSet = section.getKeys(false);

    @Override
    public void run() {
        titlesList.addAll(titlesSet);
        int count = getLines();
        String title = titlesList.get(i);
        String subtitle = config.getString("announcement.title.broadcast." + title);

        for(Object player : onlinePlayerList) {
            Player onlinePlayer = (Player) player;
            onlinePlayer.sendTitle(title,subtitle,config.getInt("announcement.title.fade-in"),config.getInt("announcement.title.stay"),config.getInt("announcement.title.fade-out"));
        }
        i++;
        if(i == count) {
            i = 0;
        }
    }
}

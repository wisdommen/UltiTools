package com.ultikits.ultitools.tasks;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.TimeUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BanTimeCheckerTask {
    public void startBanTimeCheckerTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                YamlConfiguration banListConfig = YamlConfiguration.loadConfiguration(new File(ConfigsEnum.BANLIST.toString()));
                ConfigurationSection banListOfPlayerSection = banListConfig.getConfigurationSection("banlist.banned-players");
                if(banListOfPlayerSection == null) {
                    return;
                }
                List<String> uuids = new ArrayList();
                uuids.addAll(banListOfPlayerSection.getKeys(false));
                for(String uuid : uuids) {
                    if(Objects.equals(banListConfig.getString("banlist.banned-players." + uuid + ".to"), "FOREVER")) {
                        continue;
                    }
                    if(new TimeUtils().isTimeAfter(new TimeUtils().getTimeWithDate(),banListConfig.getString("banlist.banned-players." + uuid + ".to"))) {
                        banListConfig.set("banlist.banned-players." + uuid,null);
                        try {
                            banListConfig.save(new File(ConfigsEnum.BANLIST.toString()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(UltiTools.getInstance(),0,20 * 60L);
    }
}

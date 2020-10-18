package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Collections;

public class DeathPunishConfig extends AbstractConfigReviewable{

    private static final DeathPunishConfig config = new DeathPunishConfig("death", ConfigsEnum.DEATH.toString());

    public DeathPunishConfig() {
        config.init();
    }

    public DeathPunishConfig(String name, String filePath) {
        super(name, filePath);
        map.put("config_version", 1.0);
        map.put("enable_item_drop", false);
        map.put("enable_money_drop", true);
        map.put("enable_punish_commands", true);
        map.put("money_dropped_ondeath", 100);
        map.put("item_dropped_ondeath", 3);
        map.put("punish_command", Collections.emptyList());
        map.put("worlds_enabled_item_drop", Collections.emptyList());
        map.put("worlds_enabled_money_drop", Collections.emptyList());
        map.put("worlds_enabled_punish_commands", Collections.emptyList());
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, config);
    }

    @Override
    void doInit(YamlConfiguration config) {
        for (String key : map.keySet()) {
            if (!key.equals("config_version") && config.getKeys(false).contains(key)){
                continue;
            }
            config.set(key, map.get(key));
        }
    }
}

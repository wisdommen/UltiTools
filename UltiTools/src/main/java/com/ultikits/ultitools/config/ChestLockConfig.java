package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;


public class ChestLockConfig extends AbstractConfigReviewable{

    private static final ChestLockConfig chestLockConfig = new ChestLockConfig("chestConfig", ConfigsEnum.CHEST_LOCK.toString());

    public ChestLockConfig(){
        chestLockConfig.init();
    }

    private ChestLockConfig(String name, String filePath) {
        super(name, filePath);
        map.put("config_version", 1.0);
        map.put("op_unlock", true);
        map.put("op_break_locked", true);
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, chestLockConfig);
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

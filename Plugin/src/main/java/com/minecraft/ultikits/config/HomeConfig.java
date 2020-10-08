package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;


public class HomeConfig extends AbstractConfigReviewable{

    private static final HomeConfig homeConfig = new HomeConfig("homeConfig", ConfigsEnum.HOME.toString());

    public HomeConfig() {
        homeConfig.init();
    }

    private HomeConfig(String name, String filePath) {
        super(name, filePath);
        map.put("config_version", 1.0);
        map.put("home_normal", 3);
        map.put("home_pro", 5);
        map.put("home_ultimate", 10);
        map.put("home_tpwait", 3);
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, homeConfig);
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

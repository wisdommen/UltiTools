package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;

public class BagConfig extends AbstractConfigReviewable{

    private static final BagConfig config = new BagConfig("bag", ConfigsEnum.BAG.toString());

    public BagConfig() {
        config.init();
    }

    private BagConfig(String name, String filePath) {
        super(name, filePath);
        map.put("config_version", 1.0);
        map.put("price_of_create_a_remote_chest", 10000);
        map.put("enable_price_increase", true);
        map.put("price_increase_rate", 0.1);
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

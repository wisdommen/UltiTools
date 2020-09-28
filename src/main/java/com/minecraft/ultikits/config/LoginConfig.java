package com.minecraft.ultikits.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoginConfig extends AbstractConfigReviewable{

    static Map<String, Object> map = new LinkedHashMap<>();

    static {
        map.put("configVersion", 1.0);
        map.put("playerLimitForOneIP", 1);
    }

    @Override
    void doInit(File file, YamlConfiguration config) {
        for (String key : map.keySet()) {
            if (!key.equals("configVersion") && config.getKeys(false).contains(key)){
                continue;
            }
            config.set(key, map.get(key));
        }
    }

    @Override
    void doReview(File file, YamlConfiguration config) {
        if (config.getDouble("configVersion") < (double) map.get("configVersion")) {
            doInit(file, config);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

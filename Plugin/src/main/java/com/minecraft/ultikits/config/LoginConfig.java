package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class LoginConfig extends AbstractConfigReviewable{

    private static final LoginConfig loginConfig = new LoginConfig("login", ConfigsEnum.LOGIN.toString());

    public LoginConfig() {
        loginConfig.init();
    }

    private LoginConfig(String name, String filePath) {
        super(name, filePath);
        map.put("configVersion", 1.0);
        map.put("playerLimitForOneIP", 1);
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, loginConfig);
    }

    @Override
    void doInit(YamlConfiguration config) {
        for (String key : map.keySet()) {
            if (!key.equals("configVersion") && config.getKeys(false).contains(key)){
                continue;
            }
            config.set(key, map.get(key));
        }
    }

    @Override
    public void review() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.getDouble("configVersion") < (double) map.get("configVersion")) {
            doInit(config);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

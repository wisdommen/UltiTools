package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Collections;

public class WhiteListConfig extends AbstractConfig{

    private static final WhiteListConfig config = new WhiteListConfig("whiteList", ConfigsEnum.WHITELIST.toString());

    public WhiteListConfig() {
        config.init();
    }

    public WhiteListConfig(String name, String filePath) {
        super(name, filePath);
        map.put("whitelist", Collections.emptyList());
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, config);
    }

    @Override
    void doInit(YamlConfiguration config) {
    }
}

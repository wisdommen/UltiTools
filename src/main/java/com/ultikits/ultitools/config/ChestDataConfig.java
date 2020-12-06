package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class ChestDataConfig extends AbstractConfig{

    private static final ChestDataConfig chest = new ChestDataConfig("chestData", ConfigsEnum.CHEST.toString());

    public ChestDataConfig() {
        chest.init();
    }

    private ChestDataConfig(String name, String filePath) {
        super(name, filePath);
        map.put("locked", new ArrayList<>());
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, chest);
    }

    @Override
    void doInit(YamlConfiguration config) {
    }
}

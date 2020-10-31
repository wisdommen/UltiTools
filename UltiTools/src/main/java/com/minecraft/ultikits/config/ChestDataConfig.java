package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class ChestDataConfig extends AbstractConfig{

    private static final ChestDataConfig chest = new ChestDataConfig("chestData", ConfigsEnum.CHEST.toString());

    public ChestDataConfig() {
        chest.init();
    }

    public ChestDataConfig(String name, String filePath) {
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

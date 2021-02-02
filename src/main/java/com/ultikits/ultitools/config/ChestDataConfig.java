package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;

public class ChestDataConfig extends AbstractConfig{

    private static final ChestDataConfig chest = new ChestDataConfig("chestData", UltiTools.getInstance().getDataFolder().toString(), ConfigsEnum.CHEST.toString(), "chestData.yml");

    public ChestDataConfig() {
        chest.init();
    }

    private ChestDataConfig(String name, String folder, String filePath, String resourcePath) {
        super(name,folder, filePath, resourcePath);
    }
}

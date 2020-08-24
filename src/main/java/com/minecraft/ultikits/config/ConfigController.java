package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;

public class ConfigController {

    public static void initFiles() {
        new KitsConfig().init(ConfigsEnum.KIT.toString());
        new CleanerConfig().init(ConfigsEnum.CLEANER.toString());
    }
}

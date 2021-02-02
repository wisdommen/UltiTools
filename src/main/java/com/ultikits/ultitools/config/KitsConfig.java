package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class KitsConfig extends AbstractConfig{

    private static final KitsConfig kitsConfig = new KitsConfig("kits", ConfigsEnum.KIT.toString());

    public KitsConfig() {
        kitsConfig.init();
    }

    private KitsConfig(String name, String filePath) {
        super(name, filePath);
    }
}

package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class MainConfig extends AbstractConfigReviewable{

    private static final MainConfig config = new MainConfig("config", ConfigsEnum.MAIN.toString());

    public MainConfig() {
        config.init();
    }

    private MainConfig(String name, String filePath) {
        super(name, filePath);
        version = 3.28;
    }
}

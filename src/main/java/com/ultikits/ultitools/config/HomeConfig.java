package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;


public class HomeConfig extends AbstractConfigReviewable{

    private static final HomeConfig homeConfig = new HomeConfig("home", ConfigsEnum.HOME.toString());

    public HomeConfig() {
        homeConfig.init();
    }

    private HomeConfig(String name, String filePath) {
        super(name, filePath);
        version = 1.0;
    }
}

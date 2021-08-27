package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class MOTDConfig extends AbstractConfigReviewable {
    private static final MOTDConfig config = new MOTDConfig("motd", ConfigsEnum.MOTD.toString());

    public MOTDConfig() {
        config.init();
    }

    private MOTDConfig(String name, String filePath) {
        super(name, filePath);
        version = 1.0;
    }
}

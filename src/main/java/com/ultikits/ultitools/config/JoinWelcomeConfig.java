package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class JoinWelcomeConfig extends AbstractConfigReviewable{

    private static final JoinWelcomeConfig joinWelcomeConfig = new JoinWelcomeConfig("joinwelcome", ConfigsEnum.JOIN_WELCOME.toString());

    public JoinWelcomeConfig() {
        joinWelcomeConfig.init();
    }

    private JoinWelcomeConfig(String name, String filePath) {
        super(name, filePath);
        version = 1.0;
    }
}

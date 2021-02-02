package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class DeathPunishConfig extends AbstractConfigReviewable{

    private static final DeathPunishConfig config = new DeathPunishConfig("death", ConfigsEnum.DEATH.toString());

    public DeathPunishConfig() {
        config.init();
    }

    private DeathPunishConfig(String name, String filePath) {
        super(name, filePath);
        version = 1.0;
    }
}

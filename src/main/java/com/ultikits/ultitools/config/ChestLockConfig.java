package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;


public class ChestLockConfig extends AbstractConfigReviewable{

    private static final ChestLockConfig chestLockConfig = new ChestLockConfig("chestlock", ConfigsEnum.CHEST_LOCK.toString());

    public ChestLockConfig(){
        chestLockConfig.init();
    }

    private ChestLockConfig(String name, String filePath) {
        super(name, filePath);
        version = 1.0;
    }
}

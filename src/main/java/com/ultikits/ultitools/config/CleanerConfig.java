package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class CleanerConfig extends AbstractConfigReviewable {

    private static final CleanerConfig cleanerConfig = new CleanerConfig("cleaner", ConfigsEnum.CLEANER.toString());

    public CleanerConfig(){
        cleanerConfig.init();
    }

    private CleanerConfig(String name, String filePath) {
        super(name, filePath);
        version = 1.2;
    }

}

package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class BagConfig extends AbstractConfigReviewable {
    private static final BagConfig config = new BagConfig("bag", ConfigsEnum.BAG.toString());

    public BagConfig() {
        config.init();
    }

    private BagConfig(String name, String filePath) {
        super(name, filePath);
        version = 1.0;
    }
}

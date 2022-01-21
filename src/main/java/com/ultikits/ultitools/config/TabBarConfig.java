package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class TabBarConfig extends AbstractConfigReviewable {
    private static final TabBarConfig config = new TabBarConfig("tab-bar", ConfigsEnum.TAB_BAR.toString());

    public TabBarConfig() {
        config.init();
    }

    private TabBarConfig(String name, String filePath) {
        super(name, filePath);
        version = 1.0;
    }
}

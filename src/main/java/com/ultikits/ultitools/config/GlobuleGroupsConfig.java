package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;


public class GlobuleGroupsConfig extends AbstractConfig{

    private static final GlobuleGroupsConfig globuleGroupsConfig = new GlobuleGroupsConfig("globuleGroupsConfig", ConfigsEnum.PERMISSION_INHERITED.toString());

    public GlobuleGroupsConfig() {
        globuleGroupsConfig.init();
    }

    private GlobuleGroupsConfig(String name, String filePath) {
        super(name, filePath);
    }
}

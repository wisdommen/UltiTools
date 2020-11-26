package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Collections;

public class SideBarDataConfig extends AbstractConfig{

    private static final SideBarDataConfig sideBarDataConfig = new SideBarDataConfig("sideBarData", ConfigsEnum.SIDEBAR_DATA.toString());

    public SideBarDataConfig() {
        sideBarDataConfig.init();
    }

    private SideBarDataConfig(String name, String filePath) {
        super(name, filePath);
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, sideBarDataConfig);
    }

    @Override
    void doInit(YamlConfiguration config) {
        config.set("player_closed_sb", Collections.emptyList());
    }
}

package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;


public class GlobuleGroupsConfig extends AbstractConfig{

    private static final GlobuleGroupsConfig globuleGroupsConfig = new GlobuleGroupsConfig("globuleGroupsConfig", ConfigsEnum.PERMISSION_INHERITED.toString());

    public GlobuleGroupsConfig() {
        globuleGroupsConfig.init();
    }

    private GlobuleGroupsConfig(String name, String filePath) {
        super(name, filePath);
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig("globuleGroupsConfig", globuleGroupsConfig);
    }

    @Override
    void doInit(YamlConfiguration config) {
        config.set("groups", null);
    }

}

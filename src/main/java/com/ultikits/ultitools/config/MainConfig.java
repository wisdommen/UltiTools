package com.ultikits.ultitools.config;

import org.bukkit.configuration.file.YamlConfiguration;

public class MainConfig extends AbstractConfigReviewable{

    public MainConfig() {
        init();
    }

    public MainConfig(String name, String filePath) {
        super(name, filePath);
    }

    @Override
    public void load() {

    }

    @Override
    void doInit(YamlConfiguration config) {

    }
}

package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Arrays;
import java.util.Collections;

public class MultiworldsConfig extends AbstractConfig{

    private static final MultiworldsConfig config = new MultiworldsConfig("multiworlds", ConfigsEnum.WORLDS.toString());

    public MultiworldsConfig(){
        config.init();
    }

    private MultiworldsConfig(String name, String path){
        super(name, path);
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, config);
    }

    @Override
    void doInit(YamlConfiguration config) {
        config.set("worlds", Collections.emptyList());
        config.set("blocked_worlds", Collections.emptyList());
        config.set("world.World.type", "GRASS_BLOCK");
        config.set("world.World.describe", "无");
        config.set("world.Nether.type", "GRASS_BLOCK");
        config.set("world.Nether.describe", "无");
        config.set("world.End.type", "GRASS_BLOCK");
        config.set("world.End.describe", "无");
    }
}

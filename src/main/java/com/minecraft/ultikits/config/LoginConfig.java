package com.minecraft.ultikits.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LoginConfig extends AbstractConfig{
    @Override
    void doInit(File file, YamlConfiguration config) {
        config.set("configVersion", 1.0);
        config.set("playerLimitForOneIP", 1);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

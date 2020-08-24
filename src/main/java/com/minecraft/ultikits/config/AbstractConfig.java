package com.minecraft.ultikits.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

abstract class AbstractConfig {
    public void init(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        doInit(file, config);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    abstract void doInit(File file, YamlConfiguration config);
}

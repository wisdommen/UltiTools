package com.minecraft.ultikits.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class AbstractConfigReviewable extends AbstractConfig{

    @Override
    public void init(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            review(filePath);
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

    public void review(String filePath){
        File file = new File(filePath);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        doReview(file, config);
    }
    abstract void doReview(File file, YamlConfiguration config);
}

package com.ultikits.ultitools.config;

import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class AbstractConfig {
    String name;
    String filePath;
    String resourcePath;
    File file;
    String folder;
    YamlConfiguration config;

    AbstractConfig() {
    }

    public AbstractConfig(String name, String filePath) {
        this.name = name;
        this.folder = String.valueOf(UltiTools.getInstance().getDataFolder());
        this.filePath = filePath;
        this.resourcePath = UltiTools.language + "_" + name + ".yml";
        this.file = new File(filePath);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public AbstractConfig(String name, String folder, String filePath, String resourcePath) {
        this.name = name;
        this.folder = folder;
        this.filePath = filePath;
        this.resourcePath = resourcePath;
        this.file = new File(filePath);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void init() {
        if (!file.exists()) {
            UltiTools.yaml.saveYamlFile(folder, name + ".yml", resourcePath);
        }
        config = YamlConfiguration.loadConfiguration(file);
        if (!ConfigController.getConfigMap().containsKey(name)){
            ConfigController.registerConfig(name, this);
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        reload();
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}

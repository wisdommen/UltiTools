package com.ultikits.ultitools.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

abstract class AbstractConfig {

    String name;
    String filePath;
    Map<String, Object> map = new LinkedHashMap<>();
    File file;

    public AbstractConfig() {
    }

    public AbstractConfig(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
        this.file = new File(filePath);
    }

    public void init() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            doInit(config);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        load();
    }

    public void reload() {
        this.file = new File(filePath);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(true)) {
            map.put(key, config.get(key));
        }
    }

    public void save() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : map.keySet()) {
            if (config.getKeys(true).contains(key)) {
                config.set(key, map.get(key));
            }
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    abstract public void load();

    abstract void doInit(YamlConfiguration config);
}

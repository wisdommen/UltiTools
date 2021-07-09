package com.ultikits.ultitools.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConfigController {

    private static final Map<String, AbstractConfig> configMap = new HashMap<>();

    private ConfigController() {
    }

    public static void registerConfig(String name, AbstractConfig config) {
        configMap.put(name, config);
    }

    public static Map<String, AbstractConfig> getConfigMap() {
        return configMap;
    }

    public static YamlConfiguration getConfig(String name) {
        return configMap.get(name).getConfig();
    }

    public static void saveConfig(String configName) {
        AbstractConfig config = configMap.get(configName);
        config.save();
        reloadConfig(config);
    }

    public static void reloadConfig(AbstractConfig config) {
        config.reload();
    }

    public static void saveConfigs() {
        for (AbstractConfig configs : configMap.values()) {
            configs.save();
        }
    }

    public static void reloadAll(){
        for (AbstractConfig config : configMap.values()){
            reloadConfig(config);
        }
    }
}

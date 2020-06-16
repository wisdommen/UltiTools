package com.minecraft.ultikits.UpdateChecker;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigFileChecker {

    private static Map<String, Object> getAll() {
        Map<String, Object> config = new HashMap<>();

        for (String key : UltiTools.getInstance().getConfig().getKeys(false)) {
            Object value = UltiTools.getInstance().getConfig().get(key);
            config.put(key, value);
        }

        return config;
    }

    public static void reviewConfigFile() {
        Map<String, Object> config = getAll();
        File file = new File(UltiTools.getInstance().getDataFolder(), "config.yml");

        if (file.delete()) {
            UltiTools.getInstance().saveDefaultConfig();
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            for (String key : configuration.getKeys(false)) {
                if (config.containsKey(key)) {
                    configuration.set(key, config.get(key));
                }
            }
            try {
                configuration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

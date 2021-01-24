package com.ultikits.ultitools.utils;

import com.ultikits.ultitools.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CustomGuiUtils {

    private static Map<String, String> customGuiCommands = new HashMap<>();

    static {
        File file = new File(ConfigsEnum.CUSTOMERGUI.toString());
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getConfigurationSection("guis").getKeys(false)) {
            String cmd = config.getString("guis." + key + ".command");
            customGuiCommands.put(cmd, key);
        }
    }

    private CustomGuiUtils() {
    }

    public static String getSignature(String command) {
        return customGuiCommands.get(command);
    }
}

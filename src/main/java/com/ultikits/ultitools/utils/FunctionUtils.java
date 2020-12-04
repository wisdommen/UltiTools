package com.ultikits.ultitools.utils;

import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FunctionUtils {

    private static Map<String, String> commandMap = new LinkedHashMap<>();

    static {
        commandMap.put("email", "enable_email");
        commandMap.put("login", "enable_login");
        commandMap.put("home", "enable_home");
        commandMap.put("whitelist", "enable_white_list");
        commandMap.put("sidebar", "enable_scoreboard");
        commandMap.put("chestlock", "enable_lock");
        commandMap.put("remote-bag", "enable_remote_chest");
        commandMap.put("multiworlds", "enable_multiworlds");
        commandMap.put("kits", "enable_kits");
        commandMap.put("cleaner", "enable_cleaner");
        commandMap.put("permission", "enable_permission");
        commandMap.put("join-welcome", "enable_onjoin");
        commandMap.put("armor-monitor", "enable_armor_check");
        commandMap.put("chat-prefix", "enable_chat");
        commandMap.put("name-prefix", "enable_name_prefix");
        commandMap.put("death-punishment", "enable_death_punishment");
        commandMap.put("tpa", "enable_tpa");
        commandMap.put("warp", "enable_wrap");
        commandMap.put("papi", "enable_PAPI");
        commandMap.put("social-system", "enable_social_system");
        commandMap.put("auto-version-check", "enable_version_check");
        commandMap.put("auto-update", "enable_auto_update");
    }

    private FunctionUtils() {
    }

    public static void functionSwitch(String function, boolean isEnable){
        if (UltiTools.getInstance().getConfig().get(commandMap.get(function))==null){
            System.out.println(function+" not found");
            return;
        }
        File file = new File(UltiTools.getInstance().getDataFolder(),"config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(commandMap.get(function), isEnable);
        try {
            config.save(file);
            System.out.println("saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllFunctions(){
        return new ArrayList<>(commandMap.keySet());
    }

}

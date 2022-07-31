package com.ultikits.ultitools.utils;

import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FunctionUtils {

    private static final Map<String, String>       functionsMap = new LinkedHashMap<>();

    static {
        functionsMap.put("email", "enable_email");
        functionsMap.put("login", "enable_login");
        functionsMap.put("home", "enable_home");
        functionsMap.put("white-list", "enable_white_list");
        functionsMap.put("sidebar", "enable_scoreboard");
        functionsMap.put("chest-locker", "enable_lock");
        functionsMap.put("remote-bag", "enable_remote_chest");
        functionsMap.put("multi-worlds", "enable_multiworlds");
        functionsMap.put("kits", "enable_kits");
        functionsMap.put("cleaner", "enable_cleaner");
        functionsMap.put("join-welcome", "enable_onjoin");
        functionsMap.put("armor-monitor", "enable_armor_check");
        functionsMap.put("chat", "enable_chat");
        functionsMap.put("name-prefix", "enable_name_prefix");
        functionsMap.put("death-punish", "enable_death_punishment");
        functionsMap.put("tpa", "enable_tpa");
        functionsMap.put("warp", "enable_warp");
        functionsMap.put("papi", "enable_PAPI");
        functionsMap.put("social-system", "enable_social_system");
        functionsMap.put("random-tp", "enable_random_tp");
        functionsMap.put("auto-version-check", "enable_version_check");
        functionsMap.put("auto-update", "enable_auto_update");
        functionsMap.put("back", "enable_back");
        functionsMap.put("tp-back", "enable_tpback_command");
        functionsMap.put("trade", "enable_trade");
        functionsMap.put("motd", "enable_motd_function");
        functionsMap.put("inv-backup", "enable_inv_backup_function");
        functionsMap.put("ban", "enable_ban_function");
        functionsMap.put("command-alias", "enable_command-alias_function");
        functionsMap.put("spawn", "enable_spawn");
        functionsMap.put("fly", "enable_fly_command");
        functionsMap.put("lobby", "enable_lobby_command");
        functionsMap.put("inv-see", "enable_invsee_command");
        functionsMap.put("armor-see", "enable_armor_see_command");
        functionsMap.put("ender-chest-see", "enable_enderChest_see_command");
        functionsMap.put("recipe", "enable_custom_recipe");
        functionsMap.put("hide", "enable_hide_function");
        functionsMap.put("recall", "enable_recall_command");
    }

    private FunctionUtils() {
    }

    public static void functionSwitch(String function, boolean isEnable){
        if (UltiTools.getInstance().getConfig().get(functionsMap.get(function))==null){
            System.out.println(function+" not found");
            return;
        }
        File file = new File(UltiTools.getInstance().getDataFolder(),"config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(functionsMap.get(function), isEnable);
        try {
            config.save(file);
            System.out.println("saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllFunctions(){
        return new ArrayList<>(functionsMap.keySet());
    }

    public static String getFunctionCode(String name){
        return functionsMap.get(name);
    }
}

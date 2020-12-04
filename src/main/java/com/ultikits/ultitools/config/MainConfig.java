package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;

public class MainConfig extends AbstractConfigReviewable{

    private static final MainConfig config = new MainConfig("main", ConfigsEnum.MAIN.toString());

    public MainConfig() {
        config.init();
    }

    public MainConfig(String name, String filePath) {
        super(name, filePath);
        map.put("config_version", 3.2);
        map.put("language", "zh_CN");
        map.put("enable_pro", false);
        map.put("pro_name", "xxxxxxxxxxxxxx");
        map.put("pro_key", "xxxxxxxxxxxxxx");
        map.put("enableDataBase", false);
        map.put("host", "localhost");
        map.put("port", 3306);
        map.put("username", "root");
        map.put("password", "root");
        map.put("database", "minecraft");
        map.put("enable_scoreboard", true);
        map.put("enable_version_check", true);
        map.put("enable_auto_update", false);
        map.put("enable_home", true);
        map.put("enable_lock", true);
        map.put("enable_email", true);
        map.put("enable_onjoin", true);
        map.put("enable_remote_chest", true);
        map.put("enable_armor_check", true);
        map.put("enable_multiworlds", true);
        map.put("enable_chat", true);
        map.put("enable_white_list", false);
        map.put("enable_name_prefix", true);
        map.put("enable_login", true);
        map.put("enable_kits", true);
        map.put("enable_cleaner", true);
        map.put("enable_permission", true);
        map.put("enable_PAPI", false);
        map.put("enable_death_punishment", true);
        map.put("enable_social_system", true);
        map.put("enable_tpa", true);
        map.put("enable_warp", true);
        map.put("price_of_create_a_remote_chest", 10000);
        map.put("chat_prefix", "§e[§a%player_world%§e][§c%ul_job%§dLv.%ul_level%§e][§b%player_name%§e]");
        map.put("name_prefix", "§e[§c%ul_job%§dLv.%ul_level%§e]");
        map.put("name_suffix", "§e[§c%ul_health%§e/§c%ul_max_health%§e]");
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, config);
    }

    @Override
    void doInit(YamlConfiguration config) {
        for (String key : map.keySet()) {
            if (!key.equals("config_version") && config.getKeys(false).contains(key)){
                continue;
            }
            config.set(key, map.get(key));
        }
    }
}

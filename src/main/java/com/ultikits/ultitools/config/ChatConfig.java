package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;

public class ChatConfig extends AbstractConfigReviewable{

    private static final ChatConfig config = new ChatConfig("chat", ConfigsEnum.CHAT.toString());

    public ChatConfig() {
        config.init();
    }

    private ChatConfig(String name, String filePath) {
        super(name, filePath);
        map.put("config_version", 1.0);
        map.put("chat_prefix", "§e[§a%player_world%§e][§c%ul_job%§dLv.%ul_level%§e][§b%player_name%§e]");
        map.put("auto-reply.test", "You did it! This is a great test!");
        map.put("auto-reply.more_test", "No more test plz!");
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

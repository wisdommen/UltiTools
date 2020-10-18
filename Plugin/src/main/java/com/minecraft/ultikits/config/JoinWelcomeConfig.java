package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Arrays;

public class JoinWelcomeConfig extends AbstractConfigReviewable{

    private static final JoinWelcomeConfig joinWelcomeConfig = new JoinWelcomeConfig("welcome", ConfigsEnum.JOIN_WELCOME.toString());

    public JoinWelcomeConfig() {
        joinWelcomeConfig.init();
    }

    private JoinWelcomeConfig(String name, String filePath) {
        super(name, filePath);
        map.put("config_version", 1.0);
        map.put("sendMessageDelay", 4);
        map.put("welcome_message", Arrays.asList(UltiTools.languageUtils.getWords("join_welcome_message_1"),
                UltiTools.languageUtils.getWords("join_welcome_message_2"),
                UltiTools.languageUtils.getWords("join_welcome_message_3")));
        map.put("op_join", UltiTools.languageUtils.getWords("join_op_join"));
        map.put("op_quit", UltiTools.languageUtils.getWords("join_op_quit"));
        map.put("player_join", UltiTools.languageUtils.getWords("join_player_join"));
        map.put("player_quit", UltiTools.languageUtils.getWords("join_player_quit"));
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, joinWelcomeConfig);
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

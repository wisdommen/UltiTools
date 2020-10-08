package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;
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
        map.put("welcome_message", Arrays.asList("§b欢迎加入服务器，§e%player_name%§b!", "§b请友善对待他人，游戏愉快！", "     ---可以在配置文件在修改此提醒"));
        map.put("op_join", "§c[管理员] §e%player_name% §c已上线");
        map.put("op_quit", "§c[管理员] §e%player_name% §c已下线");
        map.put("player_join", "§c[玩家] §e%player_name% §c已上线");
        map.put("player_quit", "§c[玩家] §e%player_name% §c已下线");
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

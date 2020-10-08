package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class SideBarConfig extends AbstractConfigReviewable{

    private static final SideBarConfig sideBarConfig = new SideBarConfig("sidebar", ConfigsEnum.SIDEBAR.toString());

    public SideBarConfig() {
        sideBarConfig.init();
    }

    private SideBarConfig(String name, String filePath) {
        super(name, filePath);
        map.put("config_version", 1.0);
        map.put("scoreBoardTitle", "欢迎加入服务器");
        map.put("name", "%player_name%");
        map.put("online_player", "");
        map.put("CDq", "");
        map.put("CDw", "");
        map.put("CDe", "");
        map.put("CDr", "");
        map.put("money", "");
        map.put("deposit", "");
        map.put("level", "");
        map.put("exp", "");
        map.put("max_exp", "");
        map.put("mp", "");
        map.put("max_mp", "");
        map.put("hp", "");
        map.put("max_hp", "");
        map.put("occupation", "");
        map.put("customerline", Arrays.asList("这是自定义的内容", "可以在配置文件里改", "支持PAPI变量"));
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, sideBarConfig);
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

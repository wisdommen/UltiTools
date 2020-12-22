package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitsConfig extends AbstractConfig{

    private static final KitsConfig kitsConfig = new KitsConfig("kits", ConfigsEnum.KIT.toString());

    public KitsConfig() {
        kitsConfig.init();
    }

    private KitsConfig(String name, String filePath) {
        super(name, filePath);
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, kitsConfig);
    }

    @Override
    void doInit(YamlConfiguration config) {
        config.set("xinshou.item", "LOG");
        config.set("xinshou.reBuyable", false);
        config.set("xinshou.name", UltiTools.languageUtils.getString("kits_config_name"));
        config.set("xinshou.level", 1);
        config.set("xinshou.job", UltiTools.languageUtils.getString("kits_config_job"));
        config.set("xinshou.description", UltiTools.languageUtils.getString("kits_config_description"));
        config.set("xinshou.price", 0);
        config.set("xinshou.contain", new ArrayList<>());
        List<String> playerCommands = new ArrayList<>();
        config.set("xinshou.playerCommands",playerCommands);
        List<String> console = Arrays.asList(UltiTools.languageUtils.getString("kits_config_commands"), "givemoney {PLAYER} 100");
        config.set("xinshou.consoleCommands", console);
    }

}

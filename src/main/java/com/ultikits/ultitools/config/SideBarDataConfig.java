package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;

public class SideBarDataConfig extends AbstractConfig {

    private static final SideBarDataConfig sideBarDataConfig = new SideBarDataConfig("sideBarData", UltiTools.getInstance().getDataFolder() + "/sidebar", ConfigsEnum.SIDEBAR_DATA.toString(), "sideBarData.yml");

    public SideBarDataConfig() {
        sideBarDataConfig.init();
    }

    private SideBarDataConfig(String name, String folder, String filePath, String resourcePath) {
        super(name, folder, filePath, resourcePath);
    }

    @Override
    public void init() {
        if (!file.exists()) {
            UltiTools.yaml.saveYamlFile(String.valueOf(folder), "data.yml", resourcePath);
        }
        config = YamlConfiguration.loadConfiguration(file);
        if (!ConfigController.getConfigMap().containsKey(name)){
            ConfigController.registerConfig(name, this);
        }
    }
}

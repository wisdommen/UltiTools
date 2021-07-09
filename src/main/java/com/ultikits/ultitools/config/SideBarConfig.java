package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;

public class SideBarConfig extends AbstractConfigReviewable{

    private static final SideBarConfig sideBarConfig = new SideBarConfig("sidebar",UltiTools.getInstance().getDataFolder()+"/sidebar", ConfigsEnum.SIDEBAR.toString(), UltiTools.language+"_sidebar.yml");

    public SideBarConfig() {
        sideBarConfig.init();
    }

    private SideBarConfig(String name,String folder, String filePath, String resourcePath) {
        super(name, folder, filePath, resourcePath);
        version = 1.0;
    }

    @Override
    public void init() {
        if (file.exists()) {
            review();
        } else {
            UltiTools.yaml.saveYamlFile(String.valueOf(folder), "config.yml", resourcePath);
        }
        config = YamlConfiguration.loadConfiguration(file);
        if (!ConfigController.getConfigMap().containsKey(name)){
            ConfigController.registerConfig(name, this);
        }
    }
}

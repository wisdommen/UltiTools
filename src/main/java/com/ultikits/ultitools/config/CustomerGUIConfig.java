package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class CustomerGUIConfig extends AbstractConfig{
    private static final CustomerGUIConfig gui = new CustomerGUIConfig("customergui", ConfigsEnum.CUSTOMERGUI.toString());

    public CustomerGUIConfig(){
        gui.init();
    }

    private CustomerGUIConfig(String name, String filePath){
        super(name, filePath);
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, gui);
    }

    @Override
    void doInit(YamlConfiguration config) {
    }

    @Override
    public void init() {
        if (!file.exists()) {
            UltiTools.yaml.saveYamlFile(UltiTools.getInstance().getDataFolder().getPath(), "customgui.yml", UltiTools.language+"_customgui.yml", false);
            config = YamlConfiguration.loadConfiguration(file);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        load();
    }
}

package com.minecraft.ultikits.config;


import java.util.HashMap;
import java.util.Map;

public class ConfigController {

    private static final Map<String, AbstractConfig> configMap = new HashMap<>();
    private static final Map<String, String> config = new HashMap<>();

    private ConfigController(){}

    public static void initFiles() {
        new KitsConfig();
        new CleanerConfig();
        new GroupPermissionConfig();
        new UserPermissionConfig();
        new GlobuleGroupsConfig();
        new LoginConfig();
        new JoinWelcomeConfig();
        new SideBarConfig();
        new SideBarDataConfig();
        new ChestLockConfig();
        new HomeConfig();
        new ChestDataConfig();
        new MultiworldsConfig();
        for (AbstractConfig abstractConfig : configMap.values()){
            for (String key : abstractConfig.map.keySet()){
                config.put(key, abstractConfig.name);
            }
        }
    }

    public static Map<String, AbstractConfig> getConfigMap() {
        return configMap;
    }

    public static void registerConfig(String name, AbstractConfig config){
        configMap.put(name, config);
    }

    public static AbstractConfig getConfig(String name){
        return configMap.get(name);
    }

    public static Object getValue(String key){
        return configMap.get(config.get(key)).map.get(key);
    }

    public static void setValue(String key, Object value){
        configMap.get(config.get(key)).map.put(key, value);
    }

    public static void saveConfig(String configName){
        configMap.get(config.get(configName)).save();
    }

    public static void saveConfigs(){
        for (AbstractConfig configs : configMap.values()){
            configs.save();
        }
    }
}

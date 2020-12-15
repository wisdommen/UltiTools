package com.ultikits.ultitools.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigController {

    private static final Map<String, AbstractConfig> configMap = new HashMap<>();
    private static final Map<String, String> config = new HashMap<>();

    private ConfigController() {
    }

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
        new DeathPunishConfig();
        new MainConfig();
        new WhiteListConfig();
        new BagConfig();
        new ChatConfig();
        for (AbstractConfig abstractConfig : configMap.values()) {
            for (String key : abstractConfig.map.keySet()) {
                config.put(key, abstractConfig.name);
            }
        }
    }

    public static Map<String, AbstractConfig> getConfigMap() {
        return configMap;
    }

    public static void registerConfig(String name, AbstractConfig config) {
        configMap.put(name, config);
    }

    public static AbstractConfig getConfig(String name) {
        return configMap.get(name);
    }

    public static Object getValue(String key) {
        return configMap.get(config.get(key)).map.get(key);
    }

    public static void setValue(String key, Object value) {
        configMap.get(config.get(key)).map.put(key, value);
    }

    public static void saveConfig(String configName) {
        AbstractConfig config = configMap.get(configName);
        config.save();
        reloadConfig(config);
    }

    public static void reloadConfig(AbstractConfig config) {
        config.init();
    }

    public static void saveConfigs() {
        for (AbstractConfig configs : configMap.values()) {
            configs.save();
        }
    }

    public static void reloadAll(){
        for (AbstractConfig config : configMap.values()){
            reloadConfig(config);
        }
    }

    public static String getString(String key){
        return (String) getValue(key);
    }

    public static Integer getInt(String key){
        return (Integer) getValue(key);
    }

    public static List<String> getStringList(String key){
        return castList(getValue(key), String.class);
    }

    public static Boolean getBoolean(String key){
        return (Boolean) getValue(key);
    }

    public static Short getShort(String key){
        return (Short) getValue(key);
    }

    public static Long getLong(String key){
        return (Long) getValue(key);
    }

    public static Double getDouble(String key){
        return (Double) getValue(key);
    }

    private static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
}

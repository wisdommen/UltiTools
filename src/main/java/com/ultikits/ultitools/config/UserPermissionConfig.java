package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import org.bukkit.configuration.file.YamlConfiguration;


public class UserPermissionConfig extends AbstractConfig{

    private static final UserPermissionConfig usersConfig = new UserPermissionConfig("usersPermission", ConfigsEnum.PERMISSION_USER.toString());

    public UserPermissionConfig() {
        usersConfig.init();
    }

    private UserPermissionConfig(String name, String filePath) {
        super(name, filePath);
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, usersConfig);
    }

    @Override
    void doInit(YamlConfiguration config) {
        config.set("users", null);
    }
}

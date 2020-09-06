package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;

public class ConfigController {

    private ConfigController(){}

    public static void initFiles() {
        new KitsConfig().init(ConfigsEnum.KIT.toString());
        new CleanerConfig().init(ConfigsEnum.CLEANER.toString());
        new GroupPermissionConfig().init(ConfigsEnum.PERMISSION_GROUP.toString());
        new UserPermissionConfig().init(ConfigsEnum.PERMISSION_USER.toString());
        new GlobuleGroupsConfig().init(ConfigsEnum.PERMISSION_INHERITED.toString());
    }
}

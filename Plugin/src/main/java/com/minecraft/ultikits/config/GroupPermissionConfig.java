package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class GroupPermissionConfig extends AbstractConfig {

    private static final GroupPermissionConfig groupPermissionConfig = new GroupPermissionConfig("groupPermissionConfig", ConfigsEnum.PERMISSION_GROUP.toString());

    public GroupPermissionConfig() {
        groupPermissionConfig.init();
    }

    private GroupPermissionConfig(String name, String filePath) {
        super(name, filePath);
    }

    @Override
    public void load() {
        reload();
        ConfigController.registerConfig(name, groupPermissionConfig);
    }

    @Override
    void doInit(YamlConfiguration config) {
        String defaultPath = "groups.default";
        config.set(defaultPath + ".name", UltiTools.languageUtils.getWords("default"));
        config.set(defaultPath + ".type", "PAPER");
        config.set(defaultPath + ".isDefault", true);
        config.set(defaultPath + ".permissions", new ArrayList<>());
        config.set(defaultPath + ".inherited", new ArrayList<>());
    }
}

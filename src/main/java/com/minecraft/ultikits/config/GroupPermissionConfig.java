package com.minecraft.ultikits.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

public class GroupPermissionConfig extends AbstractConfig {
    @Override
    void doInit(File file, YamlConfiguration config) {
        String defaultPath = "groups.default";
        config.set(defaultPath + ".name", "默认");
        config.set(defaultPath + ".type", "PAPER");
        config.set(defaultPath + ".isDefault", true);
        config.set(defaultPath + ".permissions", new ArrayList<>());
        config.set(defaultPath + ".inherited", new ArrayList<>());
    }
}

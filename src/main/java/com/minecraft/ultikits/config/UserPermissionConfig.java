package com.minecraft.ultikits.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class UserPermissionConfig extends AbstractConfig{
    @Override
    void doInit(File file, YamlConfiguration config) {
        config.set("users", null);
    }
}

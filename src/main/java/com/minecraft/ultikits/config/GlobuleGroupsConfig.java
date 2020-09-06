package com.minecraft.ultikits.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class GlobuleGroupsConfig extends AbstractConfig{
    @Override
    void doInit(File file, YamlConfiguration config) {
        config.set("groups", null);
    }
}

package com.minecraft.ultikits.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collections;

public class CleanerConfig extends AbstractConfig{
    @Override
    void doInit(File file, YamlConfiguration config) {
        config.set("config_version", 1.0);
        config.set("cleaner_name", "服务器清理");
        config.set("clean_entity_task_enable", false);
        config.set("unload_chunk_task_enable", true);
        config.set("enable_smart_clean", true);
        config.set("clean_period", 3000);
        config.set("clean_type", Collections.singletonList("all"));
        config.set("clean_worlds", Collections.singletonList("all"));
        config.set("item_max", 2000);
        config.set("mob_max", 1000);
        config.set("total_entity_max", 2500);
    }
}

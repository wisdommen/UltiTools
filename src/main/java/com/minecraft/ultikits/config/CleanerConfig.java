package com.minecraft.ultikits.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CleanerConfig extends AbstractConfigReviewable {

    static Map<String, Object> map = new LinkedHashMap<>();

    static {
        map.put("config_version", 1.1);
        map.put("cleaner_name", "服务器清理");
        map.put("clean_entity_task_enable", false);
        map.put("enable_smart_clean", true);
        map.put("unload_chunk_task_enable", false);
        map.put("clean_period", 3000);
        map.put("clean_type", Collections.singletonList("all"));
        map.put("clean_worlds", Collections.singletonList("all"));
        map.put("item_max", 2000);
        map.put("mob_max", 1000);
        map.put("total_entity_max", 2500);
        map.put("max_chunk_distance", 320);
        map.put("max_unused_chunks", 100);
        map.put("unload_chunks_per_minute", 10);
    }

    @Override
    void doInit(File file, YamlConfiguration config) {
        for (String key : map.keySet()) {
            if (!key.equals("config_version") && config.getKeys(false).contains(key)){
                continue;
            }
            config.set(key, map.get(key));
        }
    }

    @Override
    void doReview(File file, YamlConfiguration config) {
        if (config.getDouble("config_version") < (double) map.get("config_version")) {
            doInit(file, config);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

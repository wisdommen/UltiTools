package com.minecraft.ultikits.config;

import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Collections;

public class CleanerConfig extends AbstractConfigReviewable {

    private static final CleanerConfig cleanerConfig = new CleanerConfig("cleaner", ConfigsEnum.CLEANER.toString());

    public CleanerConfig(){
        cleanerConfig.init();
    }

    private CleanerConfig(String name, String filePath) {
        super(name, filePath);
        map.put("config_version", 1.1);
        map.put("cleaner_name", UltiTools.languageUtils.getWords("clean_name"));
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
    public void load() {
        reload();
        ConfigController.registerConfig(name, cleanerConfig);
    }

    @Override
    void doInit(YamlConfiguration config) {
        for (String key : map.keySet()) {
            if (!key.equals("config_version") && config.getKeys(false).contains(key)){
                continue;
            }
            config.set(key, map.get(key));
        }
    }

}

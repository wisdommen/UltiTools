package com.ultikits.ultitools.utils;

import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageUtils {
    private YamlConfiguration configuration;

    public LanguageUtils(YamlConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getString(String path){
        return configuration.getString(path)+"";
    }
}

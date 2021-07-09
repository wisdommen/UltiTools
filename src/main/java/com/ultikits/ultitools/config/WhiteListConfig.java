package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;


public class WhiteListConfig extends AbstractConfig{

    private static final WhiteListConfig config = new WhiteListConfig("whitelist", UltiTools.getInstance().getDataFolder().toString(), ConfigsEnum.WHITELIST.toString(), "whitelist.yml");

    public WhiteListConfig() {
        config.init();
    }

    private WhiteListConfig(String name, String folder, String filePath,String resourcePath) {
        super(name, folder, filePath, resourcePath);
    }
}

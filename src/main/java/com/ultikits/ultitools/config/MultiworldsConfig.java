package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class MultiworldsConfig extends AbstractConfig{

    private static final MultiworldsConfig config = new MultiworldsConfig("worlds", ConfigsEnum.WORLDS.toString());

    public MultiworldsConfig(){
        config.init();
    }

    private MultiworldsConfig(String name, String path){
        super(name, path);
    }
}

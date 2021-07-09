package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class CustomerGUIConfig extends AbstractConfig{
    private static final CustomerGUIConfig gui = new CustomerGUIConfig("customgui", ConfigsEnum.CUSTOMERGUI.toString());

    public CustomerGUIConfig(){
        gui.init();
    }

    private CustomerGUIConfig(String name, String filePath){
        super(name, filePath);
    }
}

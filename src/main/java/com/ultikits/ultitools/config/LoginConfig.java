package com.ultikits.ultitools.config;

import com.ultikits.ultitools.enums.ConfigsEnum;

public class LoginConfig extends AbstractConfigReviewable{

    private static final LoginConfig loginConfig = new LoginConfig("login", ConfigsEnum.LOGIN.toString());

    public LoginConfig() {
        loginConfig.init();
    }

    private LoginConfig(String name, String filePath) {
        super(name, filePath);
        version = 1.2;
    }
}

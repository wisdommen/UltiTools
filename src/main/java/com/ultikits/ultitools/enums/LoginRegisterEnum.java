package com.ultikits.ultitools.enums;

import com.ultikits.ultitools.ultitools.UltiTools;

public enum LoginRegisterEnum {
    LOGIN(UltiTools.languageUtils.getString("login_login_page_title")),
    REGISTER(UltiTools.languageUtils.getString("login_register_page_tile")),
    VALIDATION(UltiTools.languageUtils.getString("login_validation_page_title"));

    private final String type;

    LoginRegisterEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

}

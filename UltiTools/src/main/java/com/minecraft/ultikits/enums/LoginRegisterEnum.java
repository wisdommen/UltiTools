package com.minecraft.ultikits.enums;

import com.minecraft.ultikits.ultitools.UltiTools;

public enum LoginRegisterEnum {
    LOGIN(UltiTools.languageUtils.getWords("login_login_page_title")),
    REGISTER(UltiTools.languageUtils.getWords("login_register_page_tile")),
    VALIDATION(UltiTools.languageUtils.getWords("login_validation_page_title"));

    private final String type;

    LoginRegisterEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

}

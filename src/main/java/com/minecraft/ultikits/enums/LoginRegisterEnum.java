package com.minecraft.ultikits.enums;

public enum LoginRegisterEnum {
    LOGIN("登录界面-请点击输入你的密码来登录"),
    REGISTER("注册界面-请点击输入你的密码来注册"),
    VALIDATION("验证界面-请输入你收到的验证码");

    private final String type;

    LoginRegisterEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

}

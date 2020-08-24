package com.minecraft.ultikits.enums;

public enum LoginRegisterEnum {
    LOGIN("登录界面-请点击输入你的密码来登录"), REGISTER("注册界面-请点击输入你的密码来注册");

    private String type;

    LoginRegisterEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

}

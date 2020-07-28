package com.minecraft.ultikits.GUIs;

public enum LoginRegisterEnum {
    LOGIN("登陆界面-请点击输入你的密码来登录"),REGISTER("注册界面-请点击输入你的密码来注册");

    private String type;

    private LoginRegisterEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }

}

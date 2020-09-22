package com.minecraft.ultikits.enums;

public enum PermissionsEnum {

    ADMIN("ultikits.tools.admin"),
    LEVEL1("ultikits.tools.level1"),
    LEVEL2("ultikits.tools.level2");

    String permission;

    PermissionsEnum(String permission){
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }
}

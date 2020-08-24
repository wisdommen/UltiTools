package com.minecraft.ultikits.enums;

public enum ErrorType {
    VALID("激活成功！", 200),
    NAME_EXISTS("用户名已存在", 401),
    KEY_EXISTS("激活码已被使用", 402),
    IP_EXISTS("此服务器IP已被绑定", 403),
    INVALID_KEY("无效的激活码", 404),
    NULL("服务器无法处理", 500),
    EMPTY_INPUT("用户名或激活码不可为空", 400);

    String name;
    int code;

    ErrorType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String toString() {
        return this.name;
    }

    public int getCode() {
        return this.code;
    }

    public static String getNameByCode(int code) {
        for (ErrorType type : ErrorType.values()) {
            if (code == type.getCode()) return type.toString();
        }
        return null;
    }
}

package com.minecraft.ultikits.enums;

public enum CleanTypeEnum {
    MOBS("生物", "mobs"), ITEMS("掉落物", "items"), ENTITIES("实体", "all"), CHECK("实体", "check");

    String name;
    String alis;

    CleanTypeEnum(String name, String alis) {
        this.name = name;
        this.alis = alis;
    }

    public String toString() {
        return this.name;
    }

    public String getAlis() {
        return this.alis;
    }

    public static String getNameByAlis(String alis) {
        for (CleanTypeEnum type : CleanTypeEnum.values()) {
            if (type.getAlis().equals(alis)) {
                return type.getAlis();
            }
        }
        return null;
    }

    public static CleanTypeEnum getTypeByAlis(String alis) {
        for (CleanTypeEnum type : CleanTypeEnum.values()) {
            if (type.getAlis().equals(alis)) {
                return type;
            }
        }
        return null;
    }
}

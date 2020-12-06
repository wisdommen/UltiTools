package com.ultikits.ultitools.enums;


import com.ultikits.ultitools.ultitools.UltiTools;

public enum CleanTypeEnum {
    MOBS(UltiTools.languageUtils.getString("mobs"), "mobs"),
    ITEMS(UltiTools.languageUtils.getString("dropped_item"), "items"),
    ENTITIES(UltiTools.languageUtils.getString("entity"), "all"),
    CHECK(UltiTools.languageUtils.getString("entity"), "check");

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

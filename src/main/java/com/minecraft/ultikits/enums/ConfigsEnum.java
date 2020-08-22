package com.minecraft.ultikits.enums;

import com.minecraft.ultikits.ultitools.UltiTools;

public enum ConfigsEnum {
    PLAYER_EMAIL( UltiTools.getInstance().getDataFolder()+"/emailData"),
    PLAYER_CHEST(UltiTools.getInstance().getDataFolder()+"/chestData"),
    CHEST(UltiTools.getInstance().getDataFolder()+"/chestData.yml"),
    PLAYER_LOGIN(UltiTools.getInstance().getDataFolder()+"/loginData"),
    WHITELIST(UltiTools.getInstance().getDataFolder()+"/whitelist.yml"),
    KIT(UltiTools.getInstance().getDataFolder()+"/kits.yml"),
    DATA_KIT(UltiTools.getInstance().getDataFolder()+"/kitData/kit.yml"),
    PLAYER(UltiTools.getInstance().getDataFolder()+"/playerData");

    private final String path;

    ConfigsEnum(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}

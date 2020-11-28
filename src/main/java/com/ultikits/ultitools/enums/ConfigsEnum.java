package com.ultikits.ultitools.enums;

import com.ultikits.ultitools.ultitools.UltiTools;

public enum ConfigsEnum {
    PLAYER_EMAIL(UltiTools.getInstance().getDataFolder() + "/emailData"),
    PLAYER_CHEST(UltiTools.getInstance().getDataFolder() + "/chestData"),
    CHEST(UltiTools.getInstance().getDataFolder() + "/chestData.yml"),
    PLAYER_LOGIN(UltiTools.getInstance().getDataFolder() + "/loginData"),
    LOGIN(UltiTools.getInstance().getDataFolder() + "/login.yml"),
    WHITELIST(UltiTools.getInstance().getDataFolder() + "/whitelist.yml"),
    KIT(UltiTools.getInstance().getDataFolder() + "/kits.yml"),
    DATA_KIT(UltiTools.getInstance().getDataFolder() + "/kitData/kit.yml"),
    PLAYER(UltiTools.getInstance().getDataFolder() + "/playerData"),
    CLEANER(UltiTools.getInstance().getDataFolder() + "/cleaner.yml"),
    PERMISSION_GROUP(UltiTools.getInstance().getDataFolder()+"/permission/groups.yml"),
    PERMISSION_USER(UltiTools.getInstance().getDataFolder()+"/permission/users.yml"),
    PERMISSION_INHERITED(UltiTools.getInstance().getDataFolder()+"/permission/globalgroups.yml"),
    WORLDS(UltiTools.getInstance().getDataFolder() + "/worlds.yml"),
    SIDEBAR(UltiTools.getInstance().getDataFolder() + "/sidebar/config.yml"),
    SIDEBAR_DATA(UltiTools.getInstance().getDataFolder() + "/sidebar/data.yml"),
    CHEST_LOCK(UltiTools.getInstance().getDataFolder() + "/chestlock.yml"),
    HOME(UltiTools.getInstance().getDataFolder() + "/home.yml"),
    JOIN_WELCOME(UltiTools.getInstance().getDataFolder() + "/joinwelcome.yml"),
    MAIN(UltiTools.getInstance().getDataFolder() + "/config.yml"),
    DEATH(UltiTools.getInstance().getDataFolder()+"/death.yml");

    private final String path;

    ConfigsEnum(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}

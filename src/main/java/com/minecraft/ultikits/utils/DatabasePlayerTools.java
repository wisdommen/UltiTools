package com.minecraft.ultikits.utils;

import java.util.Map;

public class DatabasePlayerTools {

    private static final String table = "userinfo";
    private static final String primaryID = "username";

    private DatabasePlayerTools(){}

    public static boolean isPlayerExist(String playerName){
        return DatabaseUtils.isRecordExists(table, primaryID, playerName);
    }

    public static String getPlayerData(String playerName, String field){
        return DatabaseUtils.getData(primaryID, playerName, table, field);
    }

    public static boolean updatePlayerData(String playerName, String field, String value){
        return DatabaseUtils.updateData(table, field, primaryID, playerName, value);
    }

    public static boolean insertPlayerData(Map<String, String> dataMap){
        return DatabaseUtils.insertData(table, dataMap);
    }
}

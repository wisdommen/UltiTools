package com.minecraft.ultikits.utils.database;

import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.database.DatabaseUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.minecraft.ultikits.listener.LoginListener.playerLoginStatus;
import static com.minecraft.ultikits.ultitools.UltiTools.isDatabaseEnabled;

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

    public static String getPlayerPassword(Player player){
        if (isDatabaseEnabled){
            return getPlayerData(player.getName(), "password");
        }else {
            File file = new File(UltiTools.getInstance().getDataFolder() + "/loginData", player.getName() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            return config.getString("password");
        }
    }

    public static String getPlayerPassword(String playerName){
        if (isDatabaseEnabled){
            return getPlayerData(playerName, "password");
        }else {
            File file = new File(UltiTools.getInstance().getDataFolder() + "/loginData", playerName + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            return config.getString("password");
        }
    }

    public static void setPlayerPassword(Player player, String password){
        if (isDatabaseEnabled){
            updatePlayerData(player.getName(), "password", password);
        }else {
            File file = new File(UltiTools.getInstance().getDataFolder() + "/loginData", player.getName() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("password", password);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setPlayerPassword(String playerName, String password){
        if (isDatabaseEnabled){
            updatePlayerData(playerName, "password", password);
        }else {
            File file = new File(UltiTools.getInstance().getDataFolder() + "/loginData", playerName + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("password", password);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isPlayerAccountExist(Player player){
        if (isDatabaseEnabled){
            return !DatabaseUtils.getData(primaryID, player.getName(), table, "password").equals("");
        }else {
            File file = new File(UltiTools.getInstance().getDataFolder() + "/loginData", player.getName() + ".yml");
            return file.exists();
        }
    }

    public static boolean isPlayerAccountExist(String playerName){
        if (isDatabaseEnabled){
            return !DatabaseUtils.getData(primaryID, playerName, table, "password").equals("");
        }else {
            File file = new File(UltiTools.getInstance().getDataFolder() + "/loginData", playerName + ".yml");
            return file.exists();
        }
    }

    public static boolean getIsLogin(Player player){
        return playerLoginStatus.get(player.getName()) != null ? playerLoginStatus.get(player.getName()) : false;
    }

    public static boolean getIsLogin(String playerName){
        return playerLoginStatus.get(playerName) != null ? playerLoginStatus.get(playerName) : false;
    }


    public static void setIsLogin(Player player, boolean isLogin){
        playerLoginStatus.put(player.getName(), isLogin);
    }

    public static void setIsLogin(String playerName, boolean isLogin){
        playerLoginStatus.put(playerName, isLogin);
    }
}

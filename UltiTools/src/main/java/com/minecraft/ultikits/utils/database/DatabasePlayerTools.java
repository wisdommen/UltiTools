package com.minecraft.ultikits.utils.database;

import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.MD5Utils;
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

    private DatabasePlayerTools() {
    }

    public static boolean isPlayerExist(String playerName) {
        return DatabaseUtils.isRecordExists(table, primaryID, playerName);
    }

    public static String getPlayerData(String playerName, String field) {
        return DatabaseUtils.getData(primaryID, playerName, table, field);
    }

    public static boolean updatePlayerData(String playerName, String field, String value) {
        return DatabaseUtils.updateData(table, field, primaryID, playerName, value);
    }

    public static boolean insertPlayerData(Map<String, String> dataMap) {
        return DatabaseUtils.insertData(table, dataMap);
    }

    public static String getPlayerPassword(Player player) {
        return getPlayerPassword(player.getName());
    }

    public static String getPlayerPassword(String playerName) {
        if (isDatabaseEnabled) {
            return getPlayerData(playerName, "password");
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), playerName + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            return config.getString("password");
        }
    }

    public static void setPlayerPassword(Player player, String password) {
        setPlayerPassword(player.getName(), password);
    }

    public static void setPlayerPassword(String playerName, String password) {
        password = MD5Utils.encrypt(password, playerName);
        if (isDatabaseEnabled) {
            updatePlayerData(playerName, "password", password);
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), playerName + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("password", password);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearPlayerPassword(Player player) {
        clearPlayerPassword(player.getName());
    }

    public static void clearPlayerPassword(String playerName) {
        if (isPlayerAccountExist(playerName)) {
            return;
        }
        if (isDatabaseEnabled) {
            updatePlayerData(playerName, "password", null);
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), playerName + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("password", null);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getPlayerEmail(Player player) {
        return getPlayerEmail(player.getName());
    }

    public static String getPlayerEmail(String playerName) {
        if (!isPlayerAccountExist(playerName)) {
            return null;
        }
        if (isDatabaseEnabled) {
            if (!DatabaseUtils.isColumnExists(table, "email")) {
                DatabaseUtils.addColumn(table, "email");
            }
            return getPlayerData(playerName, "email");
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), playerName + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            return config.getString("register_email");
        }
    }

    public static void setPlayerEmail(Player player, String playerEmail) {
        setPlayerEmail(player.getName(), playerEmail);
    }

    public static void setPlayerEmail(String playerName, String playerEmail) {
        if (!isPlayerAccountExist(playerName)) {
            return;
        }
        if (isDatabaseEnabled) {
            if (!DatabaseUtils.isColumnExists(table, "email")) {
                DatabaseUtils.addColumn(table, "email");
            }
            updatePlayerData(playerName, "email", playerEmail);
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), playerName + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("register_email", playerEmail);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isPlayerAccountExist(Player player) {
        return isPlayerAccountExist(player.getName());
    }

    public static boolean isPlayerAccountExist(String playerName) {
        if (isDatabaseEnabled) {
            return !DatabaseUtils.getData(primaryID, playerName, table, "password").equals("");
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), playerName + ".yml");
            return file.exists();
        }
    }

    public static boolean getIsLogin(Player player) {
        return getIsLogin(player.getName());
    }

    public static boolean getIsLogin(String playerName) {
        return playerLoginStatus.get(playerName) != null ? playerLoginStatus.get(playerName) : false;
    }


    public static void setIsLogin(Player player, boolean isLogin) {
        setIsLogin(player.getName(), isLogin);
    }

    public static void setIsLogin(String playerName, boolean isLogin) {
        playerLoginStatus.put(playerName, isLogin);
    }
}

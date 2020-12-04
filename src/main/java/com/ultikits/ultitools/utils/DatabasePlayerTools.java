package com.ultikits.ultitools.utils;

import com.alibaba.fastjson.JSON;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.listener.LoginListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MD5Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ultikits.ultitools.ultitools.UltiTools.isDatabaseEnabled;

public class DatabasePlayerTools {

    private static final String userTable = "userinfo";
    private static final String friendTable = "social_system";
    private static final String primaryID = "username";

    private DatabasePlayerTools() {
    }

    public static boolean isPlayerExist(String playerName, String table) {
        return UltiTools.databaseUtils.isRecordExists(table, primaryID, playerName);
    }

    public static String getPlayerData(String playerName, String table, String field) {
        return UltiTools.databaseUtils.getData(primaryID, playerName, table, field);
    }

    public static boolean updatePlayerData(String playerName, String table, String field, String value) {
        return UltiTools.databaseUtils.updateData(table, field, primaryID, playerName, value);
    }

    public static boolean insertPlayerData(Map<String, String> dataMap, String table) {
        return UltiTools.databaseUtils.insertData(table, dataMap);
    }

    public static String getPlayerPassword(Player player) {
        return getPlayerPassword(player.getName());
    }

    public static String getPlayerPassword(String playerName) {
        if (isDatabaseEnabled) {
            return getPlayerData(playerName, userTable, "password");
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
            if (isPlayerAccountExist(playerName)) {
                updatePlayerData(playerName, userTable, "password", password);
            } else {
                Map<String, String> temp = new HashMap<>();
                temp.put("username", playerName);
                temp.put("password", password);
                insertPlayerData(temp, userTable);
            }
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
            updatePlayerData(playerName, userTable, "password", null);
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
            if (!UltiTools.databaseUtils.isColumnExists(userTable, "email")) {
                UltiTools.databaseUtils.addColumn(userTable, "email");
            }
            return getPlayerData(playerName, userTable, "email");
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
            if (!UltiTools.databaseUtils.isColumnExists(userTable, "email")) {
                UltiTools.databaseUtils.addColumn(userTable, "email");
            }
            updatePlayerData(playerName, userTable, "email", playerEmail);
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
            return UltiTools.databaseUtils.getData(primaryID, playerName, userTable, "password") != null && !UltiTools.databaseUtils.getData(primaryID, playerName, userTable, "password").equals("");
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), playerName + ".yml");
            return file.exists();
        }
    }

    public static boolean getIsLogin(Player player) {
        return getIsLogin(player.getName());
    }

    public static boolean getIsLogin(String playerName) {
        return LoginListener.playerLoginStatus.get(playerName) != null ? LoginListener.playerLoginStatus.get(playerName) : false;
    }


    public static void setIsLogin(Player player, boolean isLogin) {
        setIsLogin(player.getName(), isLogin);
    }

    public static void setIsLogin(String playerName, boolean isLogin) {
        LoginListener.playerLoginStatus.put(playerName, isLogin);
    }

    public static void addPlayerFriends(OfflinePlayer player, OfflinePlayer target) {
        playerFriendOperator(player, target, true);
    }

    public static void removePlayerFriends(OfflinePlayer player, OfflinePlayer target) {
        playerFriendOperator(player, target, false);
    }

    private static void playerFriendOperator(OfflinePlayer player, OfflinePlayer target, boolean operation) {
        if (isDatabaseEnabled) {
            if (!isPlayerExist(player.getName(), friendTable)) {
                String friends = JSON.toJSONString(new ArrayList<>());
                String blackList = JSON.toJSONString(new ArrayList<>());
                Map<String, String> map = new HashMap<>();
                map.put("username", player.getName());
                map.put("black_list", blackList);
                map.put("friends", friends);
                insertPlayerData(map, friendTable);
            }
            String friendsJsonString = getPlayerData(player.getName(), friendTable, "username");
            List<String> friends = JSON.parseArray(friendsJsonString, String.class);
            friendsListOperator(friends, target.getName(), operation);
            String friendsJsonStringReady = JSON.toJSONString(friends);
            updatePlayerData(player.getName(), friendTable, "friends", friendsJsonStringReady);
            return;
        }
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> list;
        if (config.get("friends") == null) {
            list = new ArrayList<>();
        } else {
            list = config.getStringList("friends");
        }
        friendsListOperator(list, target.getName(), operation);
        config.set("friends", list);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> friendsListOperator(List<String> friends, String name, boolean operation) {
        if (operation) {
            friends.add(name);
        } else {
            if (friends.contains(name)) {
                friends.remove(name);
            }
        }
        return friends;
    }
}

package com.ultikits.ultitools.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ultikits.ultitools.dao.UserInfo;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.listener.LoginListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MD5Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.ultikits.ultitools.ultitools.UltiTools.isDatabaseEnabled;

public class DatabasePlayerService {

    private static final String userTable = "userinfo";
    private static final String friendTable = "social_system";
    private static final String primaryID = "username";

    private DatabasePlayerService() {
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
            Session currentSession = UltiTools.getSessionFactory().getCurrentSession();
            Transaction transaction = currentSession.getTransaction();
            Query<UserInfo> query = currentSession.createQuery("from UserInfo pd where pd.playerName = :name", UserInfo.class);
            query.setParameter("name", playerName);
            List<UserInfo> resultList = query.getResultList();
            transaction.commit();
            return !resultList.isEmpty();
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

    public static List<String> getFriendList(OfflinePlayer player){
        if (isDatabaseEnabled){
            if (!isPlayerExist(player.getName(), friendTable)) {
                String friendListJSON = DatabasePlayerService.getPlayerData(player.getName(), friendTable, "friends");
                return JSON.parseObject(friendListJSON, new TypeReference<ArrayList<String>>(){});
            }
            return Collections.emptyList();
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
        return config.getStringList("friends");
    }

    private static void friendsListOperator(List<String> friends, String name, boolean operation) {
        if (operation) {
            if (friends.contains(name)){
                return;
            }
            friends.add(name);
        } else {
            friends.remove(name);
        }
    }
}

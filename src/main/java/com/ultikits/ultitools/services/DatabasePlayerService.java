package com.ultikits.ultitools.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ultikits.annotations.ioc.Autowired;
import com.ultikits.annotations.ioc.Service;
import com.ultikits.ultitools.dao.UserInfoDAO;
import com.ultikits.ultitools.entity.UserInfoEntity;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.listener.LoginListener;
import com.ultikits.utils.MD5Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.ultikits.ultitools.ultitools.UltiTools.isDatabaseEnabled;

@Service
public class DatabasePlayerService {
    @Autowired
    public UserInfoDAO userInfoDAO;

    public String getPlayerPassword(Player player) {
        return getPlayerPassword(player.getUniqueId().toString());
    }

    public String getPlayerPassword(String uuid) {
        if (isDatabaseEnabled) {
            UserInfoEntity userInfoEntity = userInfoDAO.getUserInfoById(uuid);
            return userInfoEntity.getPassword();
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), uuid + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            return config.getString("password");
        }
    }


    public void setPlayerPassword(OfflinePlayer player, String password) {
        password = MD5Utils.encrypt(password, player.getUniqueId().toString());
        String uuid = player.getUniqueId().toString();
        if (isDatabaseEnabled) {
            if (isPlayerAccountExist(uuid)) {
                UserInfoEntity userInfoEntityById = userInfoDAO.getUserInfoById(uuid);
                userInfoEntityById.setPassword(password);
            } else {
                UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                        .uuid(uuid)
                        .username(player.getName())
                        .password(password)
                        .build();
                userInfoDAO.addUserInfo(userInfoEntity);
            }
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), uuid + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("password", password);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearPlayerPassword(OfflinePlayer player) {
        if (isPlayerAccountExist(player.getUniqueId().toString())) {
            return;
        }
        if (isDatabaseEnabled) {
            UserInfoEntity userInfoEntityById = userInfoDAO.getUserInfoById(player.getUniqueId().toString());
            userInfoEntityById.setPassword(null);
            userInfoDAO.updateUserInfo(userInfoEntityById);
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), player.getUniqueId() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("password", null);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPlayerEmail(OfflinePlayer player) {
        if (!isPlayerAccountExist(player.getUniqueId().toString())) {
            return null;
        }
        if (isDatabaseEnabled) {
            UserInfoEntity userInfoEntityById = userInfoDAO.getUserInfoById(player.getUniqueId().toString());
            return userInfoEntityById.getEmail();
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), player.getUniqueId() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            return config.getString("register_email");
        }
    }

    public void setPlayerEmail(OfflinePlayer player, String playerEmail) {
        if (!isPlayerAccountExist(player.getUniqueId().toString())) {
            return;
        }
        if (isDatabaseEnabled) {
            UserInfoEntity userInfoEntityById = userInfoDAO.getUserInfoById(player.getUniqueId().toString());
            userInfoEntityById.setEmail(playerEmail);
            userInfoDAO.updateUserInfo(userInfoEntityById);
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), player.getUniqueId() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("register_email", playerEmail);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isPlayerAccountExist(String uuid) {
        if (isDatabaseEnabled) {
            UserInfoEntity userInfoEntityById = userInfoDAO.getUserInfoById(uuid);
            return userInfoEntityById != null;
        } else {
            File file = new File(ConfigsEnum.PLAYER_LOGIN.toString(), uuid + ".yml");
            return file.exists();
        }
    }

    public boolean getIsLogin(Player player) {
        return getIsLogin(player.getName());
    }

    public boolean getIsLogin(String playerName) {
        return LoginListener.playerLoginStatus.get(playerName) != null ? LoginListener.playerLoginStatus.get(playerName) : false;
    }


    public void setIsLogin(Player player, boolean isLogin) {
        setIsLogin(player.getName(), isLogin);
    }

    public void setIsLogin(String playerName, boolean isLogin) {
        LoginListener.playerLoginStatus.put(playerName, isLogin);
    }

    public void addPlayerFriends(OfflinePlayer player, OfflinePlayer target) {
        playerFriendOperator(player, target, true);
    }

    public void removePlayerFriends(OfflinePlayer player, OfflinePlayer target) {
        playerFriendOperator(player, target, false);
    }

    private void playerFriendOperator(OfflinePlayer player, OfflinePlayer target, boolean operation) {
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

    public List<String> getFriendList(OfflinePlayer player){
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

    private void friendsListOperator(List<String> friends, String name, boolean operation) {
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

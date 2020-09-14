package com.minecraft.ultikits.utils;

import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GroupManagerUtils {
    private static final YamlConfiguration usersConfig;
    private static final YamlConfiguration groupsConfig;
    private static final YamlConfiguration inheritedConfig;
    private static final File usersFile;
    private static final File groupsFile;
    private static final File inheritedFile;

    static {
        if (UltiTools.isGroupManagerEnabled) {
            usersFile = new File(UltiTools.getInstance().getDataFolder().getPath().replace(java.io.File.separator+"UltiTools", "%SEP%GroupManager%SEP%worlds%SEP%world%SEP%users.yml".replaceAll("%sep%", java.io.File.separator)));
            groupsFile = new File(UltiTools.getInstance().getDataFolder().getPath().replace(java.io.File.separator+"UltiTools", "%SEP%GroupManager%SEP%worlds%SEP%world%SEP%groups.yml".replaceAll("%sep%", java.io.File.separator)));
            inheritedFile = new File(UltiTools.getInstance().getDataFolder().getPath().replace(java.io.File.separator+"UltiTools", "%SEP%GroupManager%SEP%globalgroups.yml".replaceAll("%sep%", java.io.File.separator)));
        } else {
            usersFile = new File(ConfigsEnum.PERMISSION_USER.toString());
            groupsFile = new File(ConfigsEnum.PERMISSION_GROUP.toString());
            inheritedFile = new File(ConfigsEnum.PERMISSION_INHERITED.toString());
        }
        usersConfig = YamlConfiguration.loadConfiguration(usersFile);
        groupsConfig = YamlConfiguration.loadConfiguration(groupsFile);
        inheritedConfig = YamlConfiguration.loadConfiguration(inheritedFile);
    }

    private GroupManagerUtils() {
    }

    public static String getGroup(UUID uuid) {
        return usersConfig.getString("users." + uuid.toString() + ".group");
    }

    public static List<String> getSubGroups(UUID uuid) {
        return usersConfig.getStringList("users." + uuid.toString() + ".subgroups");
    }

    public static List<String> getAllPlayerPermissions(UUID uuid) {
        return usersConfig.getStringList("users." + uuid.toString() + ".permissions");
    }

    public static List<String> getAllPermissions(UUID uuid){
        List<String> permission = getAllPlayerPermissions(uuid);
        permission.addAll(getGroupPermissions(getGroup(uuid)));
        for (String subGroup : getSubGroups(uuid)){
            permission.addAll(getGroupPermissions(subGroup));
        }
        return permission;
    }

    public static List<String> getGroups() {
        return new ArrayList<>(groupsConfig.getConfigurationSection("groups").getKeys(false));
    }

    private static List<String> getInheritedGroups(String group) {
        return groupsConfig.getStringList("groups." + group + ".inheritance");
    }

    private static List<String> getInheritedPermissions(String inheritedGroup) {
        return inheritedConfig.getStringList("groups." + inheritedGroup + ".permissions");
    }

    private static List<String> getGroupPermissions(String group) {
        return groupsConfig.getStringList("groups." + group + ".permissions");
    }

    public static void addGroupPermission(String group, String permission) {
        List<String> permissions = groupsConfig.getStringList("groups." + group + ".permissions");
        if (permissions.contains(permission)) {
            return;
        }
        permissions.add(permission);
        groupsConfig.set("groups." + group + ".permissions", permission);
        try {
            groupsConfig.save(groupsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addPlayerPermission(Player player, String permission) {
        addPlayerPermission(player, permission, false);
    }

    public static void addPlayerPermission(Player player, String permission, boolean fromInheritance) {
        UUID playerID = player.getUniqueId();
        if (!fromInheritance) {
            List<String> permissions = getAllPlayerPermissions(playerID);
            if (permissions.contains(permission)) {
                return;
            }
            permissions.add(permission);

            usersConfig.set("users." + player.getUniqueId().toString() + ".permissions", permissions);
            try {
                usersConfig.save(usersFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PermissionAttachment attachment = player.addAttachment(UltiTools.getInstance());
        attachment.setPermission(permission, true);
    }

    public static void takeGroupPermission(String group, String permission) {
        List<String> permissions = groupsConfig.getStringList("groups." + group + ".permissions");
        if (!permissions.contains(permission)) {
            return;
        }
        permissions.remove(permission);
        groupsConfig.set("groups." + group + ".permissions", permission);
        try {
            groupsConfig.save(groupsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void takePlayerPermission(Player player, String permission) {
        UUID playerID = player.getUniqueId();
        List<String> permissions = getAllPlayerPermissions(playerID);
        if (!permissions.contains(permission)) {
            return;
        }
        permissions.remove(permission);
        usersConfig.set("users." + player.getUniqueId().toString() + ".permissions", permissions);
        try {
            usersConfig.save(usersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PermissionAttachment attachment = player.addAttachment(UltiTools.getInstance());
        attachment.unsetPermission(permission);
    }

    public static void addPlayerToGroup(Player player, String group) {
        if (!getGroups().contains(group)) {
            return;
        }
        UUID playerId = player.getUniqueId();
        if (getGroup(playerId) != null) {
            List<String> subGroups = getSubGroups(playerId);
            if (subGroups.contains(group)) {
                return;
            }
            subGroups.add(group);
            usersConfig.set("users." + playerId.toString() + ".subgroups", subGroups);
        } else {
            usersConfig.set("users." + playerId.toString() + ".group", group);
        }
        for (String each : groupsConfig.getStringList("groups." + group + ".permissions")) {
            addPlayerPermission(player, each);
        }
        for (String eachGroup : getInheritedGroups(group)) {
            for (String eachPermission : getInheritedPermissions(eachGroup)) {
                addPlayerPermission(player, eachPermission, true);
            }
        }
        try {
            usersConfig.save(usersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removePlayerFromGroup(Player player, String group) {
        if (!getGroups().contains(group)) {
            return;
        }
        UUID playerID = player.getUniqueId();
        if (getGroup(playerID) == null) {
            removeFromSubGroups(playerID, group);
        } else {
            usersConfig.set("users." + playerID.toString() + ".group", null);
            if (getSubGroups(playerID).size() == 0) {
                return;
            }
            String subGroup = getSubGroups(playerID).get(0);
            addPlayerToGroup(player, subGroup);
            removeFromSubGroups(playerID, subGroup);
        }
        for (String permission : getGroupPermissions(group)) {
            takePlayerPermission(player, permission);
        }
        for (String eachGroup : getInheritedGroups(group)) {
            if (getSubGroups(playerID).contains(eachGroup)) {
                continue;
            }
            for (String eachPermission : getInheritedPermissions(eachGroup)) {
                takePlayerPermission(player, eachPermission);
            }
        }
    }

    private static void removeFromSubGroups(UUID playerID, String group) {
        List<String> subGroups = getSubGroups(playerID);
        if (!subGroups.contains(group)) {
            return;
        }
        subGroups.remove(group);
        usersConfig.set("users." + playerID.toString() + ".subgroups", subGroups);
        try {
            usersConfig.save(usersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createGroup(String group) {
        createGroup(group, null);
    }

    public static void createGroup(String group, String inherited) {
        if (getGroups().contains(group)) {
            return;
        }
        String path = "groups." + group;
        groupsConfig.set(path + ".default", false);
        groupsConfig.set(path + ".permissions", new ArrayList<>());
        groupsConfig.set(path + ".info.build", true);
        groupsConfig.set(path + ".info.prefix", "&5");
        groupsConfig.set(path + ".info.suffix", "");
        if (inherited != null) {
            groupsConfig.set(path + ".inheritance", Collections.singletonList(inherited));
        } else {
            groupsConfig.set(path + ".inheritance", new ArrayList<>());
        }
        try {
            groupsConfig.save(groupsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteGroup(String group) {
        for (String player : usersConfig.getConfigurationSection("users").getKeys(false)) {
            UUID playerID = UUID.fromString(player);
            Player bukkitPlayer = Bukkit.getPlayer(playerID);
            if (getGroup(playerID).equals(group)) {
                removePlayerFromGroup(bukkitPlayer, group);
            }
            if (getSubGroups(playerID).contains(group)) {
                removeFromSubGroups(playerID, group);
            }
        }
        groupsConfig.set("groups." + group, null);
        try {
            groupsConfig.save(groupsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initPlayerData(UUID uuid){
        String path = "users."+uuid.toString();
        usersConfig.set(path+".subgroup", new ArrayList<>());
        usersConfig.set(path+".group", "Default");
        usersConfig.set(path+".permissions", new ArrayList<>());
        try {
            usersConfig.save(usersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateLastName(Player player){
        usersConfig.set("users."+player.getUniqueId().toString()+".lastname", player.getName());
        try {
            usersConfig.save(usersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

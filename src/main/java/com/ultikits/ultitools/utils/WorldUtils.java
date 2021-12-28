package com.ultikits.ultitools.utils;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class WorldUtils {
    public static String getWorldAlisName(UUID uuid){
        Player player = Bukkit.getPlayer(uuid);
        if (player == null){
            return null;
        }
        World world = player.getWorld();
        if (world.getName().equalsIgnoreCase("world_nether")) {
            return getWorldAlisName("Nether");
        } else if (world.getName().equalsIgnoreCase("world_the_end")) {
            return getWorldAlisName("End");
        } else if (world.getName().equalsIgnoreCase("world")) {
            return getWorldAlisName("World");
        } else {
            return getWorldAlisName(world.getName());
        }
    }

    public static String getWorldAlisName(String worldName){
        YamlConfiguration config = ConfigController.getConfig("worlds");
        String aliasName = config.getString("world." + worldName + ".alias");
        if (aliasName == null){
            config.set("world." + worldName + ".alias", worldName);
            try {
                config.save(ConfigsEnum.WORLDS.toString());
            } catch (IOException ignored) {
            }
        }
        aliasName = (aliasName == null ? worldName : aliasName.replaceAll("&", "§"));
        return aliasName;
    }
}

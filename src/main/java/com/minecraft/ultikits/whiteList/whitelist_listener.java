package com.minecraft.ultikits.whiteList;

import com.minecraft.economy.database.DataBase;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.File;
import java.util.List;

public class whitelist_listener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(UltiTools.getInstance().getDataFolder(), "whitelist.yml"));
        Player player = event.getPlayer();
        List<String> whitelist = config.getStringList("whitelist");

        if (!UltiTools.getInstance().getConfig().getBoolean("enableDataBase")) {
            if (!whitelist.contains(player.getName())) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.AQUA + "你不在白名单上哦！");
            }
        } else {
            DataBase dataBase = UltiTools.dataBase;
            dataBase.connect();
            if (dataBase.isExist(player.getName())) {
                if (dataBase.isExist(player.getName()) && Integer.parseInt((String) dataBase.getData(player.getName(), "active")) != 1) {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.AQUA + "你不在白名单上哦！");
                }
            }
        }
    }
}

package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.DatabasePlayerTools;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhitelistListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(ConfigsEnum.WHITELIST.toString()));
        Player player = event.getPlayer();
        List<String> whitelist = config.getStringList("whitelist");

        if (!UltiTools.isDatabaseEnabled) {
            if (!whitelist.contains(player.getName())) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.AQUA + UltiTools.languageUtils.getString("whitelist_not_on"));
            }
        } else {
            if (DatabasePlayerTools.isPlayerExist(player.getName())) {
                if (DatabasePlayerTools.isPlayerExist(player.getName()) && DatabasePlayerTools.getPlayerData(player.getName(), "whitelisted").equals("false")) {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.AQUA + UltiTools.languageUtils.getString("whitelist_not_on"));
                }
            }else {
                Map<String, String> playerData = new HashMap<>();
                playerData.put("username", player.getName());
                playerData.put("password", "");
                playerData.put("whitelisted", "false");
                playerData.put("banned", "false");
                DatabasePlayerTools.insertPlayerData(playerData);
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.AQUA + UltiTools.languageUtils.getString("whitelist_not_on"));
            }
        }
    }
}

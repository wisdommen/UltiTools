package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.config.ConfigController;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MOTDListener implements Listener {
    @EventHandler
    public static void onServerListPing(ServerListPingEvent event) {
        String motd = ConfigController.getConfig("motd").getString("motd");
        if (motd == null) return;
        event.setMotd(ChatColor.translateAlternateColorCodes('&', motd));
    }
}

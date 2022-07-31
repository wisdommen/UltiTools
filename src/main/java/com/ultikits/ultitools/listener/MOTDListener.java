package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.config.ConfigController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

@EventListener(function = "motd")
public class MOTDListener implements Listener {
    @EventHandler
    public static void onServerListPing(ServerListPingEvent event) {
        String motd = ConfigController.getConfig("motd").getString("motd");
        if (motd == null) return;
        event.setMotd(motd.replace("&","§"));
    }
}

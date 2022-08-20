package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.commands.HideCommands;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class HideListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        HideCommands.hidePlayers.remove(event.getPlayer().getName());
    }
}

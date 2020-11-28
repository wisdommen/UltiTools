package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.commands.TeleportCommands;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class TpaAcceptListener implements Listener {

    @EventHandler
    public void onPlayerPressQ(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if (player.isSneaking() && TeleportCommands.temp.get(player)!=null){
            event.setCancelled(true);
            player.performCommand("tpa accept");
        }
    }
}

package com.ultikits.ultitools.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;

public class BackListener implements Listener {

    private static final Map<Player, Location> playerDeathLocation = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();
        playerDeathLocation.put(player, deathLocation);
    }

    public static Location getPlayerLastDeathLocation(Player player){
        return playerDeathLocation.get(player);
    }
}

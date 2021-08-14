package com.ultikits.ultitools.listener;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import java.util.HashMap;

public class TeleportListener implements Listener {

    private static final HashMap<Player, Location> playerTeleportLocation = new HashMap<>();

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location teleportLocation = player.getLocation();
        playerTeleportLocation.put(player,teleportLocation);
    }

    public static Location getPlayerFinalTeleportLocation(Player player) {
        return playerTeleportLocation.get(player);
    }
}

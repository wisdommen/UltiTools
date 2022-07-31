package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

@EventListener(function = "tp-back")
public class TeleportListener implements Listener {

    private static final HashMap<Player, Location> playerTeleportLocation = new HashMap<>();

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                Location teleportLocation = event.getFrom();
                playerTeleportLocation.put(player,teleportLocation);
            }
        }.runTaskLaterAsynchronously(UltiTools.getInstance(),0);
    }

    public static Location getPlayerFinalTeleportLocation(Player player) {
        return playerTeleportLocation.get(player);
    }
}

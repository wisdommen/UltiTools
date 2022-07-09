package com.ultikits.ultitools.services;

import com.ultikits.enums.Sounds;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DelayTeleportService {

    private final static Map<UUID, Boolean> teleportingPlayers = new HashMap<>();
    private final static Map<UUID, String> locationMap = new HashMap<>();

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID playerUUID : teleportingPlayers.keySet()) {
                    if (!teleportingPlayers.get(playerUUID)) {
                        locationMap.put(playerUUID, null);
                        continue;
                    }
                    Player player = Bukkit.getPlayer(playerUUID);
                    if (player == null) {
                        continue;
                    }
                    Location location = player.getLocation();
                    String currentLocation = location.getX() + "" + location.getY() + "" + location.getZ();
                    if (locationMap.get(playerUUID) == null) {
                        locationMap.put(playerUUID, currentLocation);
                    } else {
                        String lastLocation = locationMap.get(playerUUID);
                        if (!currentLocation.equals(lastLocation)) {
                            DelayTeleportService.teleportingPlayers.put(playerUUID, false);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(UltiTools.getInstance(), 0, 10L);
    }

    public static void delayTeleport(Player player, Location location, int delay) {
        teleportingPlayers.put(player.getUniqueId(), true);
        Chunk chunk = location.getChunk();
        if (!chunk.isLoaded()) {
            chunk.load();
        }
        new BukkitRunnable() {
            float time = delay;

            @Override
            public void run() {
                if (!teleportingPlayers.get(player.getUniqueId())) {
                    player.sendTitle(ChatColor.RED + UltiTools.languageUtils.getString("teleport_failed"), UltiTools.languageUtils.getString("do_not_move"), 10, 50, 20);
                    this.cancel();
                    return;
                }
                if (time == 0) {
                    player.teleport(location);
                    player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.ENTITY_ENDERMAN_TELEPORT), 1, 0);
                    player.sendTitle(ChatColor.GREEN + UltiTools.languageUtils.getString("teleport_succeed"), "", 10, 50, 20);
                    teleportingPlayers.put(player.getUniqueId(), false);
                    this.cancel();
                    return;
                }
                if ((time / 0.5 % 2) == 0) {
                    player.sendTitle(ChatColor.GREEN + UltiTools.languageUtils.getString("teleporting"), String.format(UltiTools.languageUtils.getString("teleporting_countdown"), (int) time), 10, 70, 20);
                }
                time -= 0.5;
            }
        }.runTaskTimer(UltiTools.getInstance(), 0, 10L);
    }
}
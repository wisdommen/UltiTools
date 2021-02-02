package com.ultikits.ultitools.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.ultikits.ultitools.utils.DatabasePlayerTools.getIsLogin;

public class LoginLimitTask extends BukkitRunnable {
    static Map<UUID, String> notLoggedInMap = new HashMap<>();
    static Map<String, Location> stringLocationMap = new HashMap<>();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!getIsLogin(player)) {
                String location = notLoggedInMap.get(player.getUniqueId());
                Location location1 = player.getLocation();
                String location1Str = location1.getYaw()+location1.getX()+location1.getY()+location1.getZ()+location1.getWorld().getName();
                if (location != null) {
                    if (!location.equals(location1Str)) {
                        player.teleport(stringLocationMap.get(location));
                    }
                } else {
                    notLoggedInMap.put(player.getUniqueId(), location1Str);
                    stringLocationMap.put(location1Str, player.getLocation());
                }
            }
        }
    }
}

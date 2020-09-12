package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.GroupManagerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PermissionAddOnJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        new BukkitRunnable(){

            @Override
            public void run() {
                Player player = event.getPlayer();
                for (String permission : GroupManagerUtils.getAllPermissions(player.getUniqueId())){
                    GroupManagerUtils.addPlayerPermission(player, permission);
                }
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
    }
}

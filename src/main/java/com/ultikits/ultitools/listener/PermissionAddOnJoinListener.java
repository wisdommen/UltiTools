package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.services.GroupManagerService;
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
                if (GroupManagerService.getGroup(player.getUniqueId())==null){
                    GroupManagerService.initPlayerData(player.getUniqueId());
                }
                for (String permission : GroupManagerService.getAllPermissions(player.getUniqueId())){
                    GroupManagerService.addPlayerPermission(player, permission);
                }
                GroupManagerService.updateLastName(player);
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
    }
}

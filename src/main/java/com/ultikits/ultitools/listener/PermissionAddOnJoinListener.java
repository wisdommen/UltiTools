package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.GroupManagerUtils;
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
                if (GroupManagerUtils.getGroup(player.getUniqueId())==null){
                    GroupManagerUtils.initPlayerData(player.getUniqueId());
                }
                for (String permission : GroupManagerUtils.getAllPermissions(player.getUniqueId())){
                    GroupManagerUtils.addPlayerPermission(player, permission);
                }
                GroupManagerUtils.updateLastName(player);
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
    }
}

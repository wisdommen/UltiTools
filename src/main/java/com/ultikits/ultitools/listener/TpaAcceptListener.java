package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.tasks.TpTimerTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@EventListener(function = "tpa")
public class TpaAcceptListener implements Listener {

    @EventHandler
    public void onPlayerPressKey(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking()) {
            if (TpTimerTask.tpTemp.get(player) != null) {
                tpOperation(event, player, "tpa");
            } else if (TpTimerTask.tphereTemp.get(player) != null) {
                tpOperation(event, player, "tpahere");
            }
        }
    }

    private void tpOperation(PlayerInteractEvent event, Player player, String command) {
        event.setCancelled(true);
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            player.performCommand(command + " accept");
        } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            player.performCommand(command + " reject");
        }
    }
}

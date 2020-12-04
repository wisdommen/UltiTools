package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.beans.TpBean;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TpaAcceptListener implements Listener {

    @EventHandler
    public void onPlayerPressKey(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking()) {
            if (TpBean.tpTemp.get(player) != null) {
                tpOperation(event, player, "tpa");
            } else if (TpBean.tphereTemp.get(player) != null) {
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

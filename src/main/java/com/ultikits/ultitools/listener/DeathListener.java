package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.tasks.DeathPunishmentTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qianmo, wisdomme
 */
public class DeathListener implements Listener {
    private static final List<Player> list = new ArrayList<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (list.contains(player)){
            list.remove(player);
            DeathPunishmentTask.addPlayerToQueue(player);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();

        if (player.getHealth() > event.getFinalDamage()) {
            return;
        }
        list.add(player);
    }
}

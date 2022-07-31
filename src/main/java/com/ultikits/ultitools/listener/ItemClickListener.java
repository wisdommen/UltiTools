package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.commands.InventoryBackupCommands;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


/**
 * @author Shpries
 */

@EventListener(function = "inv-backup")
public class ItemClickListener implements Listener {
    @EventHandler
    public void onItemClick(InventoryClickEvent e) {
        if(InventoryBackupCommands.isWorking) {
            e.setCancelled(true);
        }
    }
}

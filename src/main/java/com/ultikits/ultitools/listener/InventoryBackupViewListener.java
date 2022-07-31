package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Shpries
 */

@EventListener(function = "inv-backup")
public class InventoryBackupViewListener extends PagesListener {
    @Override
    public CancelResult onItemClick(InventoryClickEvent inventoryClickEvent, Player player, InventoryManager inventoryManager, ItemStack itemStack) {
        if(inventoryClickEvent.getView().getTitle().contains(UltiTools.languageUtils.getString("inv_backup_view_title"))) {
            return CancelResult.TRUE;
        }
        return CancelResult.NONE;
    }
}

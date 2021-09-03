package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class EmailToolsViewListener extends PagesListener {
    @Override
    public CancelResult onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {
        if (!event.getView().getTitle().contains(UltiTools.languageUtils.getString("email_tools") + " - " + player.getName())) {
            return CancelResult.NONE;
        }
        switch (clickedItem.getType()) {
            case WRITABLE_BOOK:
                player.performCommand("email read");
                return CancelResult.TRUE;

            case GLOWSTONE_DUST:
                player.performCommand("email delread");
                return CancelResult.TRUE;

            case REDSTONE:
                player.performCommand("email delall");
                return CancelResult.TRUE;

            default:
                return CancelResult.TRUE;
        }
    }
}

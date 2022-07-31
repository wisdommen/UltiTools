package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.annotations.EventListener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@EventListener(function = "ban")
public class BanlistViewListener extends PagesListener {
    @Override
    public CancelResult onItemClick(InventoryClickEvent inventoryClickEvent, Player player, InventoryManager inventoryManager, ItemStack itemStack) {
        return CancelResult.NONE;
    }
}

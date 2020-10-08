package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.inventoryapi.InventoryManager;
import com.minecraft.ultikits.inventoryapi.PagesListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WorldsListListener extends PagesListener {
    @Override
    public void onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {
        if (inventoryManager.getTitle().contains("世界列表")) {
            String worldName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            player.performCommand("mw " + worldName);
        }
    }
}

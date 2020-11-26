package com.ultikits.ultitools.listener;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WorldsListListener extends PagesListener {
    @Override
    public void onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {
        if (inventoryManager.getTitle().contains(UltiTools.languageUtils.getWords("world_page_title"))) {
            String worldName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            player.performCommand("mw " + worldName);
        }
    }
}

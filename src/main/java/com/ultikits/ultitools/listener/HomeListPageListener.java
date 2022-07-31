package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@EventListener(function = "home")
public class HomeListPageListener extends PagesListener {
    @Override
    public CancelResult onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {
        if (inventoryManager.getTitle().contains(player.getName()+ UltiTools.languageUtils.getString("home_'s_home_list"))) {
            String homeName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            player.performCommand("home " + homeName);
            player.closeInventory();
            return CancelResult.TRUE;
        }
        return CancelResult.NONE;
    }
}

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

@EventListener(function = "warp")
public class WarpListener extends PagesListener {
    @Override
    public CancelResult onItemClick(InventoryClickEvent inventoryClickEvent, Player player, InventoryManager inventoryManager, ItemStack itemStack) {
        if (!inventoryManager.getTitle().contains(UltiTools.languageUtils.getString("warp"))){
            return CancelResult.NONE;
        }
        String name = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).replace(UltiTools.languageUtils.getString("sidebar_name"), "").trim();
        player.performCommand("warp " + name);
        player.closeInventory();
        return CancelResult.TRUE;
    }
}

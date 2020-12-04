package com.ultikits.ultitools.listener;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WarpListener extends PagesListener {
    @Override
    public void onItemClick(InventoryClickEvent inventoryClickEvent, Player player, InventoryManager inventoryManager, ItemStack itemStack) {
        String name = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).replace(UltiTools.languageUtils.getString("sidebar_name"), "").trim();
        player.performCommand("warp " + name);
    }
}

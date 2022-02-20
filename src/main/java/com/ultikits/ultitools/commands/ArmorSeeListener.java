package com.ultikits.ultitools.commands;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorSeeListener extends PagesListener {
    @Override
    public CancelResult onItemClick(InventoryClickEvent inventoryClickEvent, Player player, InventoryManager inventoryManager, ItemStack itemStack) {
        if (!inventoryManager.getTitle().contains(UltiTools.languageUtils.getString("armor_title"))) return CancelResult.NONE;
        Player p = Bukkit.getPlayer(inventoryManager.getTitle().split("-")[1]);
        try {
            p.getInventory().setItemInOffHand(inventoryManager.getInventory().getItem(8));
            p.getInventory().setHelmet(inventoryManager.getInventory().getItem(0));
            p.getInventory().setChestplate(inventoryManager.getInventory().getItem(2));
            p.getInventory().setLeggings(inventoryManager.getInventory().getItem(4));
            p.getInventory().setBoots(inventoryManager.getInventory().getItem(6));
        } catch (Exception ignored) {
            p.getInventory().setHelmet(inventoryManager.getInventory().getItem(1));
            p.getInventory().setChestplate(inventoryManager.getInventory().getItem(3));
            p.getInventory().setLeggings(inventoryManager.getInventory().getItem(5));
            p.getInventory().setBoots(inventoryManager.getInventory().getItem(7));
        }
        return CancelResult.NONE;
    }
}

package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.inventoryapi.InventoryManager;
import com.minecraft.ultikits.inventoryapi.PagesListener;
import com.minecraft.ultikits.inventoryapi.ViewManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static com.minecraft.ultikits.inventoryapi.ViewManager.linkViews;

public class GivePermissionListener extends PagesListener {
    @Override
    public void onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {

    }
}

package com.ultikits.ultitools.views;

import com.ultikits.enums.Colors;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class ArmorView {
    private ArmorView() {
    }

    public static Inventory setUp(Player player) {
        String title = UltiTools.languageUtils.getString("armor_title") + "-" + player.getName();
        InventoryManager inventoryManager = new InventoryManager(null, 9, title, true);
        inventoryManager.create();
        inventoryManager.setBackgroundColor(Colors.GRAY);
        ViewManager.registerView(inventoryManager);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    inventoryManager.setItem(0, player.getInventory().getHelmet());
                    inventoryManager.setItem(2, player.getInventory().getChestplate());
                    inventoryManager.setItem(4, player.getInventory().getLeggings());
                    inventoryManager.setItem(6, player.getInventory().getBoots());
                    inventoryManager.setItem(8, player.getInventory().getItemInOffHand());
                } catch(Exception ignored) {
                    inventoryManager.setItem(1, player.getInventory().getHelmet());
                    inventoryManager.setItem(3, player.getInventory().getChestplate());
                    inventoryManager.setItem(5, player.getInventory().getLeggings());
                    inventoryManager.setItem(7, player.getInventory().getBoots());
                }
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
        return inventoryManager.getInventory();
    }
}
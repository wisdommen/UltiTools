package com.minecraft.ultikits.inventoryapi;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.minecraft.ultikits.inventoryapi.ViewManager.getLastView;

public abstract class PagesListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory currentInventory = event.getClickedInventory();
        ItemStack clicked = event.getCurrentItem();
        InventoryManager inventoryManager = ViewManager.getViewByInventory(currentInventory);
        if (inventoryManager == null) {
            return;
        }
        if (clicked != null) {
            event.setCancelled(true);
            if (event.getSlot() < inventoryManager.getSize() && !inventoryManager.isBackGround(clicked)) {
                onItemClick(event, player, inventoryManager, clicked);
                return;
            }
            if (clicked.getItemMeta() != null) {
                if (inventoryManager.isBackGround(clicked) || !inventoryManager.isLastLineDisabled()) {
                    return;
                }
                String itemName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
                switch (itemName) {
                    case "上一页":
                        if (inventoryManager.getTitle().contains(" 第1页")) {
                            return;
                        }
                        InventoryManager previousInventory = ViewManager.getViewByName(inventoryManager.getGroupTitle() + " 第" + (inventoryManager.getPageNumber() - 1) + "页");
                        if (previousInventory != null) {
                            player.openInventory(previousInventory.getInventory());
                        }
                        return;
                    case "下一页":
                        InventoryManager nextInventory = ViewManager.getViewByName(inventoryManager.getGroupTitle() + " 第" + (inventoryManager.getPageNumber() + 1) + "页");
                        if (nextInventory != null) {
                            player.openInventory(nextInventory.getInventory());
                        }
                        return;
                    case "退出":
                        player.closeInventory();
                        return;
                    case "返回":
                    case "确认":
                    case "取消":
                        InventoryManager lastInventory = getLastView(inventoryManager);
                        if (lastInventory == null) {
                            player.closeInventory();
                        } else {
                            ViewManager.openInventoryForPlayer(player, inventoryManager, lastInventory);
                        }
                }
            }
        }
    }

    public abstract void onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem);

}

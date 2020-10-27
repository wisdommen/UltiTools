package com.minecraft.ultikits.inventoryapi;

import com.minecraft.ultikits.ultitools.UltiTools;
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
                if (itemName.equals(UltiTools.languageUtils.getWords("button_previous"))) {
                    if (inventoryManager.getTitle().contains(String.format(" " + UltiTools.languageUtils.getWords("inventory_manager_title_page_number"), 1))) {
                        return;
                    }
                    InventoryManager previousInventory = ViewManager.getViewByName(inventoryManager.getGroupTitle() + " " + String.format(UltiTools.languageUtils.getWords("inventory_manager_title_page_number"), (inventoryManager.getPageNumber() - 1)));
                    if (previousInventory != null) {
                        player.openInventory(previousInventory.getInventory());
                    }
                } else if (itemName.equals(UltiTools.languageUtils.getWords("button_next"))) {
                    InventoryManager nextInventory = ViewManager.getViewByName(inventoryManager.getGroupTitle() + " " + String.format(UltiTools.languageUtils.getWords("inventory_manager_title_page_number"), (inventoryManager.getPageNumber() + 1)));
                    if (nextInventory != null) {
                        player.openInventory(nextInventory.getInventory());
                    }
                } else if (itemName.equals(UltiTools.languageUtils.getWords("button_quit"))) {
                    player.closeInventory();
                } else if (itemName.equals(UltiTools.languageUtils.getWords("button_back")) || itemName.equals(UltiTools.languageUtils.getWords("button_ok")) || itemName.equals(UltiTools.languageUtils.getWords("button_cancel"))) {
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

    /**
     * You need to complete the event when item clicked.
     * You don't need to worry about the last line if you create the page with preset page.
     * 你需要实现当物品被点击后的方法。
     * 你不必处理最后一行的点击事件如果此界面是你使用预设界面创建的。
     *
     * @param event            InventoryClickEvent
     * @param player           Player who clicked the item
     * @param inventoryManager The inventoryManager that response to this inventory
     * @param clickedItem      the item that been clicked
     */
    public abstract void onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem);

}

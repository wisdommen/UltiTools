package com.ultikits.ultitools.listener;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PermissionListener extends PagesListener {

    @Override
    public void onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {
//        String playerName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
//        Player playerToGive = Bukkit.getPlayerExact(playerName);
//        if (ViewManager.getViewByName("请分配权限组")==null){
//            GiveGroupPermissionView.setUp();
//        }
//        ViewManager.openInventoryForPlayer(player, inventoryManager, ViewManager.getViewByName("请分配权限组"));
    }


}

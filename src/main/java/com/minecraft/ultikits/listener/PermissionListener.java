package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.inventoryapi.*;
import com.minecraft.ultikits.views.GiveGroupPermissionView;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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

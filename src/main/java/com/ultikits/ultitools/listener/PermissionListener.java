package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PermissionListener extends PagesListener {

    @Override
    public CancelResult onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {
//        String playerName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
//        Player playerToGive = Bukkit.getPlayerExact(playerName);
//        if (ViewManager.getViewByName("请分配权限组")==null){
//            GiveGroupPermissionView.setUp();
//        }
//        ViewManager.openInventoryForPlayer(player, inventoryManager, ViewManager.getViewByName("请分配权限组"));
        if (!inventoryManager.getTitle().contains(UltiTools.languageUtils.getString("permission_check_page_title"))){
            return CancelResult.NONE;
        }
        return CancelResult.TRUE;
    }


}

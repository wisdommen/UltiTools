package com.ultikits.ultitools.views;

import com.ultikits.enums.Colors;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.manager.ItemStackManager;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.inventory.Inventory;

public class ApplyView {

    private ApplyView() {
    }

    public static Inventory setUp(String title){
        InventoryManager inventoryManager = new InventoryManager(null, 27, title, true);
        inventoryManager.create();
        ItemStackManager agreeItem = new ItemStackManager(UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.GREEN), UltiTools.languageUtils.getString("button_yes"));
        ItemStackManager rejectItem = new ItemStackManager(UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED), UltiTools.languageUtils.getString("button_no"));
        inventoryManager.setItem(11, agreeItem.getItem());
        inventoryManager.setItem(15, rejectItem.getItem());
        inventoryManager.setBackgroundColor(Colors.BLACK);
        ViewManager.registerView(inventoryManager);
        return inventoryManager.getInventory();
    }
}

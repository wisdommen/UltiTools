package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.ultitools.listener.CreateKitsViewListener;
import com.ultikits.utils.SerializationUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateKitsView {

    private static final Map<String, Inventory> inventoryMap = new HashMap<>();

    private CreateKitsView() {
    }

//    public static Inventory setUp(String name) {
//        if (inventoryMap.get(name) != null) {
//            setUpItems(name, inventoryMap.get(name));
//            return inventoryMap.get(name);
//        }
//        InventoryManager inventoryManager = new InventoryManager(null, 36, name, true);
//        inventoryManager.presetPage(ViewType.OK_CANCEL);
//        inventoryManager.create();
//        inventoryManager.clearBackGround();
//        setUpItems(name, inventoryManager.getInventory());
//        ViewManager.registerView(inventoryManager, new CreateKitsViewListener());
//
//        Inventory inventory = inventoryManager.getInventory();
//        inventoryMap.put(name, inventory);
//        return inventory;
//    }
//
//    private static void setUpItems(String name, Inventory inventory) {
//        List<ItemStack> itemStackList = setUpItem(name);
//        for (int i = 0; i < itemStackList.size();i++) {
//            inventory.setItem(i, itemStackList.get(i));
//        }
//    }
//
//    private static List<ItemStack> setUpItem(String name) {
//        List<ItemStack> itemStackList = new ArrayList<>();
//
//            ItemStack itemStack = SerializationUtils.encodeToItem(each);
//            itemStackList.add(itemStack);
//
//        return itemStackList;
//    }
}

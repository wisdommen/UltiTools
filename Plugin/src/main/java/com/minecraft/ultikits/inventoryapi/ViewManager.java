package com.minecraft.ultikits.inventoryapi;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class ViewManager {
    private static final Map<String, InventoryManager> nameViewsMap = new HashMap<>();
    private static final Map<Inventory, InventoryManager> inventoryViewsMap = new HashMap<>();
    private static final Map<String, List<InventoryManager>> groupNameViewsListMap = new HashMap<>();
    private static final Map<InventoryManager, InventoryManager> linkedViewsMap = new HashMap<>();

    private ViewManager() {
    }

    public static void registerView(InventoryManager inventoryManager) {
        registerView(inventoryManager, PageRegister.getPagesListenerByGroupName(inventoryManager.getGroupTitle()));
    }

    public static void registerView(InventoryManager inventoryManager, PagesListener listener) {
        if (listener == null) {
            return;
        }
        UltiTools.getPageRegister().register(inventoryManager, listener);
        nameViewsMap.put(inventoryManager.getTitle(), inventoryManager);
        inventoryViewsMap.put(inventoryManager.getInventory(), inventoryManager);
        String groupName = inventoryManager.getGroupTitle();
        if (groupName == null) {
            return;
        }
        List<InventoryManager> list = groupNameViewsListMap.get(groupName);
        if (list != null) {
            list.remove(inventoryManager);
            list.add(inventoryManager);
            groupNameViewsListMap.put(inventoryManager.getGroupTitle(), list);
        } else {
            List<InventoryManager> newList = new ArrayList<>();
            newList.add(inventoryManager);
            groupNameViewsListMap.put(inventoryManager.getGroupTitle(), newList);
        }
    }

    public static InventoryManager getViewByName(String title) {
        return nameViewsMap.get(title);
    }

    public static InventoryManager getViewByInventory(Inventory inventory) {
        return inventoryViewsMap.get(inventory);
    }

    public static List<InventoryManager> getGroupViewsByGroupName(String groupName) {
        return groupNameViewsListMap.get(groupName);
    }

    public static InventoryManager getLastView(InventoryManager inventoryManager) {
        return linkedViewsMap.get(inventoryManager);
    }

    public static void linkViews(InventoryManager last, InventoryManager current) {
        linkedViewsMap.put(current, last);
    }

    public static void openInventoryForPlayer(Player player, InventoryManager current, InventoryManager next) {
        ViewManager.linkViews(current, next);
        player.openInventory(next.getInventory());
    }
}

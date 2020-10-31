package com.minecraft.ultikits.inventoryapi;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

/**
 * ViewManager Utils
 * ViewManager 工具类
 */
public class ViewManager {
    private static final Map<String, InventoryManager> nameViewsMap = new HashMap<>();
    private static final Map<Inventory, InventoryManager> inventoryViewsMap = new HashMap<>();
    private static final Map<String, List<InventoryManager>> groupNameViewsListMap = new HashMap<>();
    private static final Map<InventoryManager, InventoryManager> linkedViewsMap = new HashMap<>();

    private ViewManager() {
    }

    /**
     * Register view.
     *
     * @param inventoryManager the inventory manager
     */
    public static void registerView(InventoryManager inventoryManager) {
        registerView(inventoryManager, PageRegister.getPagesListenerByGroupName(inventoryManager.getGroupTitle()));
    }

    /**
     * Register view.
     *
     * @param inventoryManager the inventory manager
     * @param listener         the listener
     */
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

    /**
     * Gets view by name.
     *
     * @param title the title
     * @return the view by name
     */
    public static InventoryManager getViewByName(String title) {
        return nameViewsMap.get(title);
    }

    /**
     * Gets view by inventory.
     *
     * @param inventory the inventory
     * @return the view by inventory
     */
    public static InventoryManager getViewByInventory(Inventory inventory) {
        return inventoryViewsMap.get(inventory);
    }

    /**
     * Gets group views by group name.
     *
     * @param groupName the group name
     * @return the group views by group name
     */
    public static List<InventoryManager> getGroupViewsByGroupName(String groupName) {
        return groupNameViewsListMap.get(groupName);
    }

    /**
     * Gets last view.
     *
     * @param inventoryManager the inventory manager
     * @return the last view
     */
    public static InventoryManager getLastView(InventoryManager inventoryManager) {
        return linkedViewsMap.get(inventoryManager);
    }

    /**
     * Link views.
     *
     * @param last    the last
     * @param current the current
     */
    public static void linkViews(InventoryManager last, InventoryManager current) {
        linkedViewsMap.put(current, last);
    }

    /**
     * Open inventory for player.
     *
     * @param player  the player
     * @param current the current
     * @param next    the next
     */
    public static void openInventoryForPlayer(Player player, InventoryManager current, InventoryManager next) {
        ViewManager.linkViews(current, next);
        player.openInventory(next.getInventory());
    }
}

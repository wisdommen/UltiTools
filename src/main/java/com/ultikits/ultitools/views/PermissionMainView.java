package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.manager.ItemStackManager;
import com.ultikits.ultitools.listener.PermissionListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.GroupManagerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PermissionMainView {

    private PermissionMainView() {
    }

    public static Inventory setUp() {
        InventoryManager inventoryManager = new InventoryManager(null, 54, UltiTools.languageUtils.getString("permission_check_page_title"), true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager, new PermissionListener());
        for (ItemStackManager each : setUpItems()) {
            inventoryManager.addItem(each.getItem());
        }
        return inventoryManager.getInventory();
    }

    private static List<ItemStackManager> setUpItems() {
        List<ItemStackManager> list = new ArrayList<>();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            ItemStackManager itemStackManager;
            if (player.isOp()) {
                itemStackManager = new ItemStackManager(UltiTools.versionAdaptor.getHead(player), setUpLore(player), player.getName());
            } else {
                itemStackManager = new ItemStackManager(UltiTools.versionAdaptor.getHead(player), setUpLore(player), player.getName());
            }
            list.add(itemStackManager);
        }
        return list;
    }

    private static ArrayList<String> setUpLore(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        List<String> permissions = GroupManagerUtils.getAllPlayerPermissions(uuid);
        String group = GroupManagerUtils.getGroup(uuid);
        List<String> subGroups = GroupManagerUtils.getSubGroups(uuid);
        ArrayList<String> lore = new ArrayList<>();
        if (group == null) group = UltiTools.languageUtils.getString("none");
        lore.add(ChatColor.LIGHT_PURPLE + UltiTools.languageUtils.getString("permission_check_page_main_group")+" " + group);
        if (subGroups.size() == 0) {
            lore.add(ChatColor.LIGHT_PURPLE + UltiTools.languageUtils.getString("permission_check_page_sub_group")+" "+UltiTools.languageUtils.getString("none"));
        } else {
            lore.add(ChatColor.LIGHT_PURPLE + UltiTools.languageUtils.getString("permission_check_page_sub_group")+" ");
            lore.addAll(subGroups);
        }
        lore.add(ChatColor.YELLOW + UltiTools.languageUtils.getString("permission_check_page_permission_header"));
        if (permissions.size() == 0) {
            lore.add(UltiTools.languageUtils.getString("none"));
        } else {
            lore.addAll(permissions);
        }
        return lore;
    }
}

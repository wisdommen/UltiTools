package com.minecraft.ultikits.views;

import com.minecraft.ultikits.inventoryapi.InventoryManager;
import com.minecraft.ultikits.inventoryapi.ViewManager;
import com.minecraft.ultikits.inventoryapi.ViewType;
import com.minecraft.ultikits.listener.PermissionListener;
import com.minecraft.ultikits.manager.ItemStackManager;
import com.minecraft.ultikits.utils.GroupManagerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PermissionMainView {

    private PermissionMainView() {
    }

    public static Inventory setUp() {
        InventoryManager inventoryManager = new InventoryManager(null, 54, "服务器权限管理", true);
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
                itemStackManager = new ItemStackManager(new ItemStack(Material.DRAGON_HEAD), setUpLore(player), player.getName());
            } else {
                itemStackManager = new ItemStackManager(new ItemStack(Material.PLAYER_HEAD), setUpLore(player), player.getName());
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
        if (group == null) group = "无";
        lore.add(ChatColor.LIGHT_PURPLE + "主权限组: " + group);
        if (subGroups.size() == 0) {
            lore.add(ChatColor.LIGHT_PURPLE + "副权限组: 无");
        } else {
            lore.add(ChatColor.LIGHT_PURPLE + "副权限组: ");
            lore.addAll(subGroups);
        }
        lore.add(ChatColor.YELLOW + "------拥有的权限------");
        if (permissions.size() == 0) {
            lore.add("无");
        } else {
            lore.addAll(permissions);
        }
        return lore;
    }
}

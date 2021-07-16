package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.main.UltiCore;
import com.ultikits.manager.ItemStackManager;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomGUIView {

    private CustomGUIView() {
    }

    public static Inventory setUp(String signature, Player player) {
        YamlConfiguration config = ConfigController.getConfig("customgui");
        String title = config.getString("guis." + signature + ".title") + " - " + player.getName();
        int slot = config.getInt("guis." + signature + ".size");
        InventoryManager inventoryManager = new InventoryManager(null, slot, title, false);
        setUpItems(player, signature, config, inventoryManager);
        ViewManager.registerView(inventoryManager);
        return inventoryManager.getInventory();
    }

    private static void setUpItems(Player player, String signature, YamlConfiguration config, InventoryManager inventoryManager) {
        Map<String, ItemStackManager> itemStackManagers = setUpItems(player, signature);
        for (String btn : itemStackManagers.keySet()) {
            ItemStackManager itemStackManager = itemStackManagers.get(btn);
            ItemStack itemStack = itemStackManager.getItem();
            itemStack.getItemMeta().addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.getItemMeta().addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            inventoryManager.forceSetItem(config.getInt(signature + "." + btn + ".position"), itemStack);
        }
    }

    private static Map<String, ItemStackManager> setUpItems(Player player, String signature) {
        Map<String, ItemStackManager> itemStacks = new HashMap<>();
        YamlConfiguration config = ConfigController.getConfig("customgui");
        for (String btn : config.getConfigurationSection(signature).getKeys(false)) {
            String path = signature + "." + btn + ".";
            String item = config.getString(path + "item");
            String name = config.getString(path + "name").replace("&", "§");
            String price = config.getString(path + "price");
            ArrayList<String> lore = new ArrayList<>();
            for (String each : config.getStringList(path + "lore")) {
                each = PlaceholderAPI.setPlaceholders(player, each);
                lore.add(each);
            }
            if (price != null && !price.equals("0")) {
                lore.add(ChatColor.YELLOW + UltiTools.languageUtils.getString("price") + price);
            }
            ItemStack itemStack = UltiCore.versionAdaptor.getGrassBlock();
            try {
                Material type = Material.valueOf(item);
                itemStack = new ItemStack(type);
            } catch (IllegalArgumentException e) {
                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(MessagesUtils.warning("[WARNING] [Custom GUI] Item type under " + path + "item '" + item + "' is not acceptable! Used grass block instead!"));
            }
            ItemStackManager itemStackManager = new ItemStackManager(itemStack, lore, name);
            itemStacks.put(btn, itemStackManager);
        }
        return itemStacks;
    }
}

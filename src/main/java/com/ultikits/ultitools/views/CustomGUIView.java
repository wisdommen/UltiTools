package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.main.UltiCore;
import com.ultikits.manager.ItemStackManager;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.listener.CustomGUIListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomGUIView {

    private static final Map<UUID, InventoryManager> inventoryMap = new HashMap<>();

    private CustomGUIView() {
    }

    public static Inventory setUp(String signature, Player player) {
        YamlConfiguration config = ConfigController.getConfig("customergui");
        if (inventoryMap.get(player.getUniqueId()) != null) {
            setUpItems(player, config, inventoryMap.get(player.getUniqueId()));
            return inventoryMap.get(player.getUniqueId()).getInventory();
        }
        String title = config.getString("guis." + signature + ".title") + " - " + player.getName();
        int slot = config.getInt("guis." + signature + ".size");
        InventoryManager inventoryManager = new InventoryManager(null, slot, title, false);
        setUpItems(player, config, inventoryManager);
        ViewManager.registerView(inventoryManager, new CustomGUIListener(signature));
        Inventory inventory = inventoryManager.getInventory();
        inventoryMap.put(player.getUniqueId(), inventoryManager);
        return inventory;
    }

    private static void setUpItems(Player player, YamlConfiguration config, InventoryManager inventoryManager) {
        Map<String, ItemStackManager> itemStackManagers = setUpItems(player);
        for (String btn : itemStackManagers.keySet()) {
            ItemStackManager itemStackManager = itemStackManagers.get(btn);
            ItemStack itemStack = itemStackManager.getItem();
            itemStack.getItemMeta().addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.getItemMeta().addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            inventoryManager.forceSetItem(config.getInt("main." + btn + ".position"), itemStack);
        }
    }

    private static Map<String, ItemStackManager> setUpItems(Player player) {
        Map<String, ItemStackManager> itemStacks = new HashMap<>();
        YamlConfiguration config = ConfigController.getConfig("customergui");
        for (String btn : config.getConfigurationSection("main").getKeys(false)) {
            String path = "main." + btn + ".";
            String item = config.getString(path + "item");
            String name = config.getString(path + "name").replace("&", "§");
            ArrayList<String> lore = new ArrayList<>();
            for (String each : config.getStringList(path + "lore")) {
                each = PlaceholderAPI.setPlaceholders(player, each);
                lore.add(each);
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

package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.manager.ItemStackManager;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.listener.WarpListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WarpsView {

    private WarpsView() {
    }

    public static Inventory setUp() {
        InventoryManager inventoryManager = new InventoryManager(null, 54, UltiTools.languageUtils.getString("warp_page_title"), true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager, new WarpListener());
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemStack itemStack : setUpItems()) {
                    inventoryManager.addItem(itemStack);
                }
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
        return inventoryManager.getInventory();
    }

    private static List<ItemStack> setUpItems() {
        List<ItemStack> itemStackList = new ArrayList<>();
        List<File> files = Utils.getFiles(ConfigsEnum.WARPS.toString());
        if (files == null) {
            return itemStackList;
        }
        for (File file : files) {
            ArrayList<String> lore = new ArrayList<>();
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String world = config.getString("world");
            int x = (int) config.getDouble("x");
            int y = (int) config.getDouble("y");
            int z = (int) config.getDouble("z");
            String name = config.getString("name");
            lore.add(ChatColor.GRAY + String.format("%s x: %d y: %d z: %d", world, x, y, z));
            ItemStackManager itemStackManager = new ItemStackManager(new ItemStack(Material.PAPER), lore, ChatColor.AQUA + UltiTools.languageUtils.getString("sidebar_name") + " " + name);
            itemStackList.add(itemStackManager.getItem());
        }
        return itemStackList;
    }
}

package com.minecraft.ultikits.views;

import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.inventoryapi.InventoryManager;
import com.minecraft.ultikits.inventoryapi.ViewManager;
import com.minecraft.ultikits.inventoryapi.ViewType;
import com.minecraft.ultikits.listener.HomeListPageListener;
import com.minecraft.ultikits.manager.ItemStackManager;
import com.minecraft.ultikits.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HomeListView {

    private static final List<Material> beds = Arrays.asList(Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED, Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED, Material.LIME_BED, Material.MAGENTA_BED);

    private HomeListView() {
    }

    public static Inventory setUp(Player player) {
        InventoryManager inventoryManager = new InventoryManager(null, 54, player.getName() + "的家列表", true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager, new HomeListPageListener());
        for (ItemStackManager home : setUpItems(player)){
            inventoryManager.addItem(home);
        }
        return inventoryManager.getInventory();
    }

    private static List<ItemStackManager> setUpItems(Player player) {
        List<ItemStackManager> itemStackManagers = new ArrayList<>();
        List<String> homeNames = Utils.getHomeList(player);
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String each : homeNames) {
            ArrayList<String> lore = new ArrayList<>();
            String homeName = each;
            if (each.equals("默认")) {
                each = "Def";
            }
            String path = player.getName() + "." + each;
            String world = String.format(ChatColor.YELLOW + "所在世界: %s", config.getString(path + ".world"));
            String xyz = String.format(ChatColor.GRAY + "X: %d Y: %d Z: %d", config.getInt(path + ".x"), config.getInt(path + ".y"), config.getInt(path + ".z"));
            lore.add(world);
            lore.add(xyz);
            Random random = new Random();
            ItemStackManager itemStackManager = new ItemStackManager(new ItemStack(beds.get(random.nextInt(10))), lore, homeName);
            itemStackManagers.add(itemStackManager);
        }
        return itemStackManagers;
    }
}

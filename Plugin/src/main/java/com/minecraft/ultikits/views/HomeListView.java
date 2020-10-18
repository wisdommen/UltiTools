package com.minecraft.ultikits.views;

import com.minecraft.ultikits.enums.Colors;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.inventoryapi.InventoryManager;
import com.minecraft.ultikits.inventoryapi.ViewManager;
import com.minecraft.ultikits.inventoryapi.ViewType;
import com.minecraft.ultikits.listener.HomeListPageListener;
import com.minecraft.ultikits.manager.ItemStackManager;
import com.minecraft.ultikits.ultitools.UltiTools;
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

    private static final List<Colors> beds = Arrays.asList(Colors.values());

    private HomeListView() {
    }

    public static Inventory setUp(Player player) {
        InventoryManager inventoryManager = new InventoryManager(null, 36, player.getName() + UltiTools.languageUtils.getWords("home_'s_home_list"), true);
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
            if (each.equals(UltiTools.languageUtils.getWords("default"))) {
                each = "Def";
            }
            String path = player.getName() + "." + each;
            String world = String.format(ChatColor.YELLOW + UltiTools.languageUtils.getWords("home_page_home_description_in_world")+" %s", config.getString(path + ".world"));
            String xyz = String.format(ChatColor.GRAY + "X: %d Y: %d Z: %d", config.getInt(path + ".x"), config.getInt(path + ".y"), config.getInt(path + ".z"));
            lore.add(world);
            lore.add(xyz);
            Random random = new Random();
            ItemStackManager itemStackManager = new ItemStackManager(UltiTools.versionAdaptor.getBed(beds.get(random.nextInt(beds.size()))), lore, homeName);
            itemStackManagers.add(itemStackManager);
        }
        return itemStackManagers;
    }
}

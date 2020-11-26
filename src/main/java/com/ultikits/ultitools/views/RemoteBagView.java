package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.manager.ItemStackManager;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.listener.ChestPageListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.Utils;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ultikits.utils.MessagesUtils.unimportant;

public class RemoteBagView {

    private final static YamlConfiguration config = Utils.getConfig(Utils.getConfigFile());


    private RemoteBagView(){}

    public static Inventory setUp(String playerName) {
        InventoryManager inventoryManager = new InventoryManager(null, 54, String.format(UltiTools.languageUtils.getWords("bag_main_page_title"),playerName), true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager, new ChestPageListener());
        ArrayList<String> lore = new ArrayList<>();
        lore.add(unimportant(UltiTools.languageUtils.getWords("kits_page_description_price") + config.getInt("price_of_create_a_remote_chest")));
        ItemStackManager newBag = new ItemStackManager(new ItemStack(Material.MINECART), lore, UltiTools.languageUtils.getWords("bag_button_create_bag"));
        inventoryManager.setItem(44, newBag.getItem());
        for (ItemStackManager each : setUpItems(playerName)) {
            inventoryManager.addItem(each.getItem());
        }
        return inventoryManager.getInventory();
    }

    private static List<ItemStackManager> setUpItems(String playerName) {
        List<ItemStackManager> itemStackManagers = new ArrayList<>();
        File chestFile = new File(ConfigsEnum.PLAYER_CHEST.toString(), playerName + ".yml");
        YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestFile);

        if (!chestConfig.getKeys(false).isEmpty()) {
            for (int i = 1; i <= chestConfig.getKeys(false).size(); i++) {
                ItemStackManager itemStackManager = new ItemStackManager(new ItemStack(Material.CHEST), new ArrayList<>(), MessagesUtils.info(i + UltiTools.languageUtils.getWords("bag_number")));
                itemStackManagers.add(itemStackManager);
            }
        }
        return itemStackManagers;
    }
}

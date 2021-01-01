package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.listener.CreateKitsViewListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.SerializationUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateKitsView {

    private static final Map<String, Inventory> inventoryMap = new HashMap<>();

    private CreateKitsView() {
    }

    public static Inventory setUp(String name) {
        if (inventoryMap.get(name) != null) {
            setUpItems(name, inventoryMap.get(name));
            return inventoryMap.get(name);
        }
        InventoryManager inventoryManager = new InventoryManager(null, 36, name + UltiTools.languageUtils.getString("kits_title_edit"), true);
        inventoryManager.presetPage(ViewType.OK_CANCEL);
        inventoryManager.create();
        inventoryManager.clearBackGround();
        setUpItems(name, inventoryManager.getInventory());
        ViewManager.registerView(inventoryManager, new CreateKitsViewListener());

        Inventory inventory = inventoryManager.getInventory();
        inventoryMap.put(name, inventory);
        return inventory;
    }

    private static void setUpItems(String name, Inventory inventory) {
        List<ItemStack> itemStackList = setUpItem(name);
        for (int i = 0; i < itemStackList.size(); i++) {
            inventory.setItem(i, itemStackList.get(i));
        }
    }

    private static List<ItemStack> setUpItem(String name) {
        List<ItemStack> itemStackList = new ArrayList<>();
        File file = new File(ConfigsEnum.KIT.toString());
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.isList(name + ".contain")) {
            for (String each : config.getStringList(name + ".contain")) {
                ItemStack itemStack = SerializationUtils.encodeToItem(each);
                itemStackList.add(itemStack);
            }
        } else {
            for (String each : config.getConfigurationSection(name + ".contain").getKeys(false)) {
                try {
                    Material material = Material.valueOf(each);
                    try {
                        int amount = config.getInt(name + ".contain." + each + ".quantity");
                        ItemStack itemStack = new ItemStack(material, amount);
                        itemStackList.add(itemStack);
                    } catch (Exception ignored) {
                    }
                }catch (IllegalArgumentException ignored){
                }
            }
        }
        return itemStackList;
    }
}

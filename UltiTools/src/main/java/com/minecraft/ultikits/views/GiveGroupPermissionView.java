package com.minecraft.ultikits.views;

import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.inventoryapi.InventoryManager;
import com.minecraft.ultikits.inventoryapi.ViewManager;
import com.minecraft.ultikits.inventoryapi.ViewType;
import com.minecraft.ultikits.listener.GivePermissionListener;
import com.minecraft.ultikits.manager.ItemStackManager;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.GroupManagerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GiveGroupPermissionView {

    private static final File file = new File(ConfigsEnum.PERMISSION_GROUP.toString());
    private static final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    private GiveGroupPermissionView() {
    }

    public static Inventory setUp() {
        InventoryManager setupGroupPage = new InventoryManager(null, 36, UltiTools.languageUtils.getWords("permission_give_page_title"), true);
        setupGroupPage.presetPage(ViewType.OK_CANCEL);
        setupGroupPage.create();
        ViewManager.registerView(setupGroupPage, new GivePermissionListener());
        for (ItemStackManager each : setUpItems()) {
            setupGroupPage.addItem(each.getItem());
        }
        return setupGroupPage.getInventory();
    }

    private static List<ItemStackManager> setUpItems() {
        List<ItemStackManager> list = new ArrayList<>();
        for (String group : GroupManagerUtils.getGroups()) {
            String type = config.getString("groups." + group + ".type");
            String name = config.getString("groups." + group + ".name");
            if (name == null) name = UltiTools.languageUtils.getWords("none");
            if (type == null) type = "PAPER";
            Material material = Material.getMaterial(type);
            if (material == null) material = Material.PAPER;
            ItemStackManager itemStackManager = new ItemStackManager(new ItemStack(material), setUpLore(group), config.getString(name));
            list.add(itemStackManager);
        }
        return list;
    }

    private static ArrayList<String> setUpLore(String group) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.LIGHT_PURPLE + UltiTools.languageUtils.getWords("permission_page_is_default_group") + config.getBoolean("groups." + group + ".isDefault"));
        lore.add(ChatColor.YELLOW + UltiTools.languageUtils.getWords("permission_page_description_permission_header"));
        List<String> permissions = config.getStringList("groups." + group + ".permissions");
        for (String each : permissions) {
            lore.add(ChatColor.GRAY + each);
        }
        return lore;
    }
}

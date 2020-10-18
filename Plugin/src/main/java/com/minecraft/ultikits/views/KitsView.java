package com.minecraft.ultikits.views;

import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.inventoryapi.InventoryManager;
import com.minecraft.ultikits.inventoryapi.ViewManager;
import com.minecraft.ultikits.inventoryapi.ViewType;
import com.minecraft.ultikits.listener.KitsPageListener;
import com.minecraft.ultikits.manager.ItemStackManager;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.minecraft.ultikits.utils.MessagesUtils.unimportant;
import static com.minecraft.ultikits.utils.MessagesUtils.warning;

public class KitsView {

    private KitsView(){}

    public static Inventory setUp(Player player){
        InventoryManager inventoryManager = new InventoryManager(null, 54, UltiTools.languageUtils.getWords("kits_page_title"), true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager, new KitsPageListener());
        for (ItemStack itemStack : setUpItems(player)){
            inventoryManager.addItem(itemStack);
        }
        return inventoryManager.getInventory();
    }

    private static List<ItemStack> setUpItems(Player player){
        List<ItemStack> itemStacks = new ArrayList<>();
        File kitFile = new File(ConfigsEnum.DATA_KIT.toString());
        File kits = new File(ConfigsEnum.KIT.toString());
        YamlConfiguration claimState = YamlConfiguration.loadConfiguration(kitFile);
        YamlConfiguration kitsConfig = YamlConfiguration.loadConfiguration(kits);

        int s = 0;
        for (String kitItem : kitsConfig.getKeys(false)) {
            ArrayList<String> lore = new ArrayList<>();
            ItemStack item = new ItemStack(Material.valueOf(kitsConfig.getString(kitItem + ".item")));
            ItemMeta itemMeta = item.getItemMeta();
            lore.add(unimportant(kitsConfig.getString(kitItem + ".description")));
            lore.add(ChatColor.AQUA + UltiTools.languageUtils.getWords("kits_page_description_level")+"    " + ChatColor.GOLD + kitsConfig.getInt(kitItem + ".level"));
            lore.add(ChatColor.AQUA + UltiTools.languageUtils.getWords("kits_page_description_job")+" " + ChatColor.GOLD + kitsConfig.getString(kitItem + ".job"));
            lore.add(ChatColor.AQUA + UltiTools.languageUtils.getWords("kits_page_description_price")+"        " + ChatColor.GOLD + kitsConfig.getInt(kitItem + ".price") + ChatColor.AQUA + " "+UltiTools.languageUtils.getWords("money"));
            lore.add(ChatColor.DARK_GRAY + UltiTools.languageUtils.getWords("kits_page_description_content"));
            for (String i : Objects.requireNonNull(kitsConfig.getConfigurationSection(kitItem + ".contain").getKeys(false))) {
                lore.add(unimportant(i) + " x " + unimportant(String.valueOf(kitsConfig.getInt(kitItem + ".contain." + i + ".quantity"))) + UltiTools.languageUtils.getWords("ge"));
            }
            String kitName = kitsConfig.getString(kitItem + ".name");
            if (Objects.requireNonNull(claimState.getStringList(Objects.requireNonNull(kitName))).contains(player.getName())) {
                lore.add(warning(UltiTools.languageUtils.getWords("claimed")));
            }
            Objects.requireNonNull(itemMeta).setLore(lore);
            itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + kitsConfig.getString(kitItem + ".name"));
            item.setItemMeta(itemMeta);
            itemStacks.add(item);
            s = s + 1;
        }
        return itemStacks;
    }
}

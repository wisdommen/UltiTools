package com.ultikits.ultitools.views;

import com.ultikits.enums.Colors;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.listener.KitsPageListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import com.ultikits.utils.SerializationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ultikits.utils.MessagesUtils.unimportant;
import static com.ultikits.utils.MessagesUtils.warning;

public class KitsView {

    private KitsView() {
    }

    public static Inventory setUp(Player player) {
        InventoryManager inventoryManager = new InventoryManager(null, 54, UltiTools.languageUtils.getString("kits_page_title"), true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemStack itemStack : setUpItems(player)) {
                    inventoryManager.addItem(itemStack);
                }
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
        return inventoryManager.getInventory();
    }

    private static List<ItemStack> setUpItems(Player player) {
        List<ItemStack> itemStacks = new ArrayList<>();
        File kitFile = new File(ConfigsEnum.DATA_KIT.toString());
        File kits = new File(ConfigsEnum.KIT.toString());
        YamlConfiguration claimState = YamlConfiguration.loadConfiguration(kitFile);
        YamlConfiguration kitsConfig = YamlConfiguration.loadConfiguration(kits);

        int s = 0;
        for (String kitItem : kitsConfig.getKeys(false)) {
            ArrayList<String> lore = new ArrayList<>();
            ItemStack item = new ItemStack(UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED));
            try {
                item = new ItemStack(Material.valueOf(kitsConfig.getString(kitItem + ".item")));
            } catch (IllegalArgumentException e) {
                if (player.hasPermission("ultikits.tools.admin")) {
                    lore.add(warning(String.format(UltiTools.languageUtils.getString("kits_no_such_item_name"), kitItem + ".item")));
                }
            }
            ItemMeta itemMeta = item.getItemMeta();
            lore.add(unimportant(kitsConfig.getString(kitItem + ".description")));
            lore.add(ChatColor.AQUA + UltiTools.languageUtils.getString("kits_page_description_level") + "    " + ChatColor.GOLD + kitsConfig.getInt(kitItem + ".level"));
            lore.add(ChatColor.AQUA + UltiTools.languageUtils.getString("kits_page_description_job") + " " + ChatColor.GOLD + kitsConfig.getString(kitItem + ".job"));
            lore.add(ChatColor.AQUA + UltiTools.languageUtils.getString("kits_page_description_price") + "        " + ChatColor.GOLD + kitsConfig.getInt(kitItem + ".price") + ChatColor.AQUA + " " + UltiTools.languageUtils.getString("money"));
            lore.add(ChatColor.DARK_GRAY + UltiTools.languageUtils.getString("kits_page_description_content"));
            if (kitsConfig.isList(kitItem + ".contain")) {
                for (String s1 : kitsConfig.getStringList(kitItem + ".contain")) {
                    ItemStack itemStack = SerializationUtils.encodeToItem(s1);
                    if (itemStack == null) {
                        continue;
                    }
                    String itemName = itemStack.getType().name();
                    if (itemStack.getItemMeta() != null && !itemStack.getItemMeta().getDisplayName().equals("")) {
                        itemName = itemStack.getItemMeta().getDisplayName();
                    }
                    lore.add(unimportant(itemName + " x " + itemStack.getAmount() + UltiTools.languageUtils.getString("ge")));
                }
            } else {
                for (String i : Objects.requireNonNull(kitsConfig.getConfigurationSection(kitItem + ".contain").getKeys(false))) {
                    lore.add(unimportant(i) + " x " + unimportant(String.valueOf(kitsConfig.getInt(kitItem + ".contain." + i + ".quantity"))) + UltiTools.languageUtils.getString("ge"));
                }
            }
            String kitName = kitsConfig.getString(kitItem + ".name").replaceAll("_", " ");
            if (Objects.requireNonNull(claimState.getStringList(Objects.requireNonNull(kitName))).contains(player.getName())) {
                lore.add(MessagesUtils.warning(UltiTools.languageUtils.getString("claimed")));
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

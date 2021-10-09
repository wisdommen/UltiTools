package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author Shpries
 */
public class BanlistView {
    public static Inventory setup() {
        InventoryManager inventoryManager = new InventoryManager(null,54, UltiTools.languageUtils.getString("ban_list"),true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        YamlConfiguration banListConfig = YamlConfiguration.loadConfiguration(new File(ConfigsEnum.BANLIST.toString()));
        ConfigurationSection banListSection = banListConfig.getConfigurationSection("banlist.banned-players");

        for(String uuid : banListSection.getKeys(false)) {
            ItemStack is = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(banListConfig.getString("banlist.banned-players." + uuid + ".id"));
            List<String> lore = new ArrayList<>();
            String to = Objects.equals(banListConfig.getString("banlist.banned-players." + uuid + ".to"), "FOREVER") ?
                         UltiTools.languageUtils.getString("ban_forever") : banListConfig.getString("banlist.banned-players." + uuid + ".to");
            lore.add(ChatColor.WHITE + UltiTools.languageUtils.getString("ban_from") + banListConfig.getString("banlist.banned-players." + uuid + ".from"));
            lore.add(ChatColor.WHITE + UltiTools.languageUtils.getString("ban_to") + to);
            im.setLore(lore);
            is.setItemMeta(im);
            inventoryManager.addItem(is);
        }
        ViewManager.registerView(inventoryManager);
        return inventoryManager.getInventory();
    }
}

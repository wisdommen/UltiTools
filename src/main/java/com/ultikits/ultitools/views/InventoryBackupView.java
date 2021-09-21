package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
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


/**
 * @author Shpries
 */
public class InventoryBackupView {
    public static Inventory setUp(Player lookedPlayer) {
        File playerInventoryDataFile = new File(ConfigsEnum.InventoryBackupData + "/" + lookedPlayer.getName() + ".yml");
        YamlConfiguration playerInventoryDataConfig = YamlConfiguration.loadConfiguration(playerInventoryDataFile);
        InventoryManager inventoryManager = new InventoryManager(null, 54,  UltiTools.languageUtils.getString("inv_backup_view_title") + "-" + lookedPlayer.getName() , true);
        inventoryManager.create();
        for(int num = 0; num < 36; num++) {
            inventoryManager.setItem(num,playerInventoryDataConfig.getItemStack("InventoryData." + num));
        }
        for(int num = 36;num < 45;num++) {
            inventoryManager.setItem(num,new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
        }
        ItemStack is = new ItemStack(Material.REDSTONE);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + UltiTools.languageUtils.getString("inv_backup_backup_time"));
        List<String> lore = new ArrayList();
        lore.add(ChatColor.RESET + playerInventoryDataConfig.getString("InventoryInfo.LatestBackupTime"));
        im.setLore(lore);
        is.setItemMeta(im);
        inventoryManager.setItem(45,is);
        ViewManager.registerView(inventoryManager);
        return inventoryManager.getInventory();
    }
}

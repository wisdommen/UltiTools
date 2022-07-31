package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@EventListener(function = "multi-worlds")
public class WorldsListListener extends PagesListener {
    @Override
    public CancelResult onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {
        if (!inventoryManager.getTitle().contains(UltiTools.languageUtils.getString("world_page_title"))) {
            return CancelResult.NONE;
        }
        String aliasName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        YamlConfiguration config = ConfigController.getConfig("worlds");
        for (String each : config.getConfigurationSection("world").getKeys(false)){
            if (aliasName.equals(config.getString("world."+each+".alias"))){
                player.performCommand("mw " + each);
                player.closeInventory();
                return CancelResult.TRUE;
            }
        }
        player.performCommand("mw " + aliasName);
        player.closeInventory();
        return CancelResult.TRUE;
    }
}

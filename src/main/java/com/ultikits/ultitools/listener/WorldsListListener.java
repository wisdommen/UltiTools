package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WorldsListListener extends PagesListener {
    @Override
    public CancelResult onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {
        if (inventoryManager.getTitle().contains(UltiTools.languageUtils.getString("world_page_title"))) {
            String aliasName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            YamlConfiguration config = ConfigController.getConfig("worlds");
            for (String each : config.getConfigurationSection("world").getKeys(false)){
                if (aliasName.equals(config.getString("world."+each+".alias"))){
                    player.performCommand("mw " + each);
                    return CancelResult.TRUE;
                }
            }
            player.performCommand("mw " + aliasName);
            return CancelResult.TRUE;
        }
        return CancelResult.NONE;
    }
}

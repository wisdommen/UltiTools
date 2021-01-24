package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CustomGUIListener extends PagesListener {
    private final String signature;

    public CustomGUIListener(String signature) {
        this.signature = signature;
    }

    @Override
    public CancelResult onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {
        if (!inventoryManager.getTitle().contains("guis." + signature + ".title - " + player.getName())) {
            return CancelResult.NONE;
        }
        if (clickedItem != null) {
            int position = event.getSlot();
            YamlConfiguration config = ConfigController.getConfig("customergui");
            for (String key : config.getConfigurationSection(signature).getKeys(false)) {
                if (position == config.getInt(signature + "." + key + ".position")) {
                    for (String playerCommand : config.getStringList(signature + "." + key + ".player-commands")) {
                        player.performCommand(playerCommand);
                    }
                    for (String consoleCommand : config.getStringList(signature + "." + key + ".console-commands")) {
                        UltiTools.getInstance().getServer().dispatchCommand(player, consoleCommand.replace("{PLAYER}", player.getName()));
                    }
                    return CancelResult.TRUE;
                }
            }
        }
        return CancelResult.TRUE;
    }
}

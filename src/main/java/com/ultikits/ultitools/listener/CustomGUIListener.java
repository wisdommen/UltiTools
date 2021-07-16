package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.EconomyUtils;
import org.bukkit.ChatColor;
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
        String title = ConfigController.getConfig("customgui").getString("guis." + signature + ".title") + " - " + player.getName();
        if (!inventoryManager.getTitle().equals(title)) {
            return CancelResult.NONE;
        }
        if (clickedItem != null) {
            int position = event.getSlot();
            YamlConfiguration config = ConfigController.getConfig("customgui");
            for (String key : config.getConfigurationSection(signature).getKeys(false)) {
                if (position == config.getInt(signature + "." + key + ".position")) {
                    if (EconomyUtils.checkMoney(player) < config.getInt(signature + "." + key + ".price")) {
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("custom_gui_not_enough_money"));
                        return CancelResult.TRUE;
                    }
                    for (String playerCommand : config.getStringList(signature + "." + key + ".player-commands")) {
                        player.performCommand(playerCommand);
                    }
                    for (String consoleCommand : config.getStringList(signature + "." + key + ".console-commands")) {
                        UltiTools.getInstance().getServer().dispatchCommand(UltiTools.getInstance().getServer().getConsoleSender(), consoleCommand.replace("{PLAYER}", player.getName()));
                    }
                    EconomyUtils.withdraw(player, config.getInt(signature + "." + key + ".price"));
                    if (config.getConfigurationSection(signature + "." + key + ".keep-open") != null &&
                            !config.getBoolean(signature + "." + key + ".keep-open")) {
                        player.closeInventory();
                    }
                    return CancelResult.TRUE;
                }
            }
        }
        return CancelResult.TRUE;
    }

    public String getSignature() {
        return signature;
    }
}

package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.SerializationUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class EmailPageListener extends PagesListener {

    @Override
    public CancelResult onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {
        if (!event.getView().getTitle().contains(String.format(UltiTools.languageUtils.getString("email_page_title"), player.getName()))) {
            return CancelResult.NONE;
        }
        ItemStack clicked = event.getCurrentItem();
        File file = new File(ConfigsEnum.PLAYER_EMAIL.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (clicked != null) {
            if (Objects.requireNonNull(clicked.getItemMeta()).getDisplayName().contains(UltiTools.languageUtils.getString("email_item_description_from"))) {
                for (String lore : Objects.requireNonNull(clicked.getItemMeta().getLore())) {
                    if (lore.contains("ID:")) {
                        String uuid = lore.split(":")[1];
                        if (config.getBoolean(uuid + ".isRead")) {
                            return CancelResult.TRUE;
                        }
                        if (config.getString(uuid + ".item") == null) {
                            config.set(uuid + ".isRead", true);
                        } else {
                            String itemStackSerialized = config.getString(uuid + ".item");
                            if (player.getInventory().firstEmpty() != -1) {
                                ItemStack itemStack = SerializationUtils.encodeToItem(itemStackSerialized);
                                player.getInventory().addItem(itemStack);
                                config.set(uuid + ".isRead", true);
                                config.set(uuid + ".isClaimed", true);
                            } else {
                                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("email_inventory_space_not_enough"));
                            }
                        }
                        try {
                            config.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.closeInventory();
                        player.performCommand("email read");
                    }
                }
            }
            return CancelResult.TRUE;
        }
        return CancelResult.TRUE;
    }
}

package com.ultikits.ultitools.listener;

import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.SerializationUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CreateKitsViewListener extends PagesListener {
    @Override
    public CancelResult onItemClick(InventoryClickEvent inventoryClickEvent, Player player, InventoryManager inventoryManager, ItemStack itemStack) {
        return CancelResult.NONE;
    }

    @Override
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().contains(UltiTools.languageUtils.getString("kits_title_edit"))){
            return;
        }
        HumanEntity humanEntity = event.getWhoClicked();
        if (event.getSlot() == 30) {
            String name = event.getView().getTitle().replace(UltiTools.languageUtils.getString("kits_title_edit"), "");
            List<String> itemStackList = new ArrayList<>();
            for (int i = 0; i <= 26; i++) {
                ItemStack itemStack = event.getInventory().getItem(i);
                if (itemStack == null) {
                    continue;
                }
                String serializedItemStuck = SerializationUtils.serialize(itemStack);
                itemStackList.add(serializedItemStuck);
            }
            File file = new File(ConfigsEnum.KIT.toString());
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set(name + ".contain", itemStackList);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            humanEntity.closeInventory();
        } else if (event.getSlot() == 32) {
            String name = event.getView().getTitle().replace(UltiTools.languageUtils.getString("kits_title_edit"), "");
            File file = new File(ConfigsEnum.KIT.toString());
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            List<String> items = config.getStringList(name + ".contain");
            for (int i = 0; i <= 26; i++) {
                ItemStack itemStack = event.getInventory().getItem(i);
                if (itemStack == null || (i < items.size() && items.get(i).equals(SerializationUtils.serialize(itemStack)))) {
                    continue;
                }
                humanEntity.getInventory().addItem(itemStack);
                event.getInventory().setItem(i, null);
            }
            humanEntity.closeInventory();
        } else {
            super.onInventoryClick(event);
        }
    }
}

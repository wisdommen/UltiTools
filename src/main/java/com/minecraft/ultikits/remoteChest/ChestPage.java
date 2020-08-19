package com.minecraft.ultikits.remoteChest;

import com.minecraft.ultikits.config.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.SerializationUtils;
import com.minecraft.ultikits.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static com.minecraft.ultikits.utils.Economy.withdraw;
import static com.minecraft.ultikits.utils.Messages.not_enough_money;

public class ChestPage implements Listener {

    private void loadBag(String chest_name, Player player) {
        Inventory remote_chest = Bukkit.createInventory(player, 36, chest_name);
        File chest_file = new File(ConfigsEnum.PLAYER_CHEST.toString(), player.getName() + ".yml");
        YamlConfiguration chest_config = YamlConfiguration.loadConfiguration(chest_file);

        String name = ChatColor.stripColor(chest_name.split("号")[0]);
        if (chest_config.getString(name) != null && !chest_config.getString(name).equals("")) {
            for (String item : Objects.requireNonNull(chest_config.getConfigurationSection(name)).getKeys(false)) {
                if (item != null) {
                    int item_position = chest_config.getInt(name + "." + item + ".position");
                    ItemStack itemStack = SerializationUtils.encodeToItem(chest_config.getString(name + "." + item + ".item"));
                    remote_chest.setItem(item_position, itemStack);
                }
            }
        }
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 10, 1);
        player.openInventory(remote_chest);
    }


    @EventHandler
    public void onItemClicked(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        YamlConfiguration config = Utils.getConfig(Utils.getConfigFile());
        File chestFile = new File(ConfigsEnum.PLAYER_CHEST.toString(), player.getName() + ".yml");
        YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestFile);

        if ("远程背包".equals(event.getView().getTitle())) {
            if (clicked != null && clicked.getItemMeta() != null) {
                event.setCancelled(true);
                if (clicked.getItemMeta().getDisplayName().contains("号背包")) {
                    String chestName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
                    loadBag(chestName, player);
                } else if ("创建背包".equals(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
                    int price = config.getInt("price_of_create_a_remote_chest");
                    if (UltiTools.getIsVaultInstalled()) {
                        if (UltiTools.getEcon().has(Bukkit.getOfflinePlayer(player.getUniqueId()), price)) {
                            UltiTools.getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), config.getInt("price_of_create_a_remote_chest"));
                            loadBag((chestConfig.getKeys(false).size() + 1) + "号背包", player);
                        } else {
                            player.sendMessage(not_enough_money);
                        }
                    } else if (UltiTools.getIsUltiEconomyInstalled()) {
                        if (withdraw(player, price)) {
                            loadBag((chestConfig.getKeys(false).size() + 1) + "号背包", player);
                        } else {
                            player.sendMessage(not_enough_money);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "未找到经济前置！");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) throws IOException {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        if (event.getView().getTitle().contains("号背包")) {
            File chestFile = new File(ConfigsEnum.PLAYER_CHEST.toString(), player.getName() + ".yml");
            YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestFile);

            chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]), "");

            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);
                if (itemStack != null) {
                    chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + i + ".position", i);
                    chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + i + ".item", SerializationUtils.serialize(itemStack));
                }
            }
            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
            chestConfig.save(chestFile);
        }
    }
}

package com.minecraft.ultikits.remoteChest;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.minecraft.ultikits.utils.Enchants.getEnchantment;
import static com.minecraft.ultikits.utils.Messages.not_enough_money;

public class ChestPage implements Listener {

    private void loadBag(String chest_name, Player player) {
        Inventory remote_chest = Bukkit.createInventory(player, 36, chest_name);
        File chest_file = new File(UltiTools.getInstance().getDataFolder() + "/chestData", player.getName() + ".yml");
        YamlConfiguration chest_config = YamlConfiguration.loadConfiguration(chest_file);

        String name = ChatColor.stripColor(chest_name.split("号")[0]);
        if (chest_config.getString(name) != null && !chest_config.getString(name).equals("")) {
            for (Object item : Objects.requireNonNull(chest_config.getConfigurationSection(name)).getKeys(false)) {
                if (item != null) {
                    int item_quantity = chest_config.getInt(name + "." + item + ".quantity");
                    Material item_material = Material.valueOf(chest_config.getString(name + "." + item + ".type"));
                    int item_position = chest_config.getInt(name + "." + item + ".position");
                    ItemStack contained_item = new ItemStack(item_material, item_quantity);
                    List<String> item_lore = chest_config.getStringList(name + "." + item + ".lore");
                    String item_name = chest_config.getString(name + "." + item + ".name");
                    ItemMeta itemMeta = contained_item.getItemMeta();
                    if (itemMeta != null) {
                        itemMeta.setLore(item_lore);
                        itemMeta.setDisplayName(item_name);
                        if (chest_config.getInt(name + "." + item + ".durability") > 0) {
                            ((Damageable) itemMeta).setDamage(chest_config.getInt(name + "." + item + ".durability"));
                        }
                        contained_item.setItemMeta(itemMeta);
                    }
                    int i = 0;
                    while (chest_config.get(name + "." + item + ".enchant." + i) != null) {
                        if (!Objects.equals(chest_config.getString(name + "." + item + ".enchant." + i + ".name"), "")) {
                            int enchantment_level = chest_config.getInt(name + "." + item + ".enchant." + i + ".level");
                            String enchantment_name = chest_config.getString(name + "." + item + ".enchant." + i + ".name");
                            contained_item.addUnsafeEnchantment(Objects.requireNonNull(getEnchantment(enchantment_name)), enchantment_level);
                            i++;
                        }
                    }
                    remote_chest.setItem(item_position, contained_item);
                }
            }
        }
        player.closeInventory();
        player.openInventory(remote_chest);
    }


    @EventHandler
    public void onItemClicked(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        YamlConfiguration config = Utils.getConfig(Utils.getConfigFile());
        File chestFile = new File(UltiTools.getInstance().getDataFolder() + "/chestData", player.getName() + ".yml");
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
                    } else if (UltiTools.isUltiEconomyInstalled) {
                        UltiEconomy economy = UltiTools.getEconomy();
                        if (economy.checkMoney(player.getName()) >= price) {
                            economy.takeFrom(player.getName(), price);
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
            File chestFile = new File(UltiTools.getInstance().getDataFolder() + "/chestData", player.getName() + ".yml");
            YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestFile);

            chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]), "");
            ItemStack[] chestContents = inventory.getContents();
            int a = 0;
            int l = 0;
            for (ItemStack item : chestContents) {
                if (item != null) {
                    if (item.getItemMeta() != null) {
                        chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".name", item.getItemMeta().getDisplayName());
                    }
                    chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".type", item.getType().name());
                    chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".quantity", item.getAmount());
                    chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".position", l);
                    if (((Damageable) item.getItemMeta()).getDamage() > 0) {
                        chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".durability", ((Damageable) item.getItemMeta()).getDamage());
                    }
                    if (!item.getEnchantments().isEmpty()) {
                        int i = 0;
                        for (Enchantment itemEnchantments : item.getEnchantments().keySet()) {
                            chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".enchant." + i + ".name", itemEnchantments.getKey().toString().split(":")[1]);
                            chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".enchant." + i + ".level", item.getEnchantmentLevel(itemEnchantments));
                            i++;
                        }
                    }
                    if (item.getItemMeta().getLore() != null && item.getItemMeta().getLore().size() > 0) {
                        List<String> lore = item.getItemMeta().getLore();
                        chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".lore", lore);
                    }
                    a++;
                }
                l++;
            }
            if (a == 0) {
                chestConfig.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]), "");
            }
            chestConfig.save(chestFile);
        }
    }
}

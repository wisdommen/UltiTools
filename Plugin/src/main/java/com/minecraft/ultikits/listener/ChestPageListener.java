package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.enums.Sounds;
import com.minecraft.ultikits.inventoryapi.InventoryManager;
import com.minecraft.ultikits.inventoryapi.PageRegister;
import com.minecraft.ultikits.inventoryapi.PagesListener;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.SerializationUtils;
import com.minecraft.ultikits.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.minecraft.ultikits.utils.EconomyUtils.withdraw;
import static com.minecraft.ultikits.utils.MessagesUtils.not_enough_money;
import static com.minecraft.ultikits.utils.MessagesUtils.warning;

public class ChestPageListener extends PagesListener {

    private final static Map<String, UUID> inventoryLock = new HashMap<>();

    public static void loadBag(String chest_name, Player player, OfflinePlayer loader) {
        if (inventoryLock.get(chest_name) != null) {
            player.sendMessage(warning(Bukkit.getPlayer(inventoryLock.get(chest_name)).getName() + UltiTools.languageUtils.getWords("bag_someone_is_using")));
            return;
        }
        Inventory remote_chest = Bukkit.createInventory(null, 36, chest_name);
        File chest_file = new File(ConfigsEnum.PLAYER_CHEST.toString(), loader.getName() + ".yml");
        YamlConfiguration chest_config = YamlConfiguration.loadConfiguration(chest_file);

        String name = ChatColor.stripColor(chest_name.split(UltiTools.languageUtils.getWords("bag_number"))[0].replace(loader.getName() + UltiTools.languageUtils.getWords("bag_s"), ""));
        if (chest_config.getString(name) != null && !chest_config.getString(name).equals("")) {
            for (String item : Objects.requireNonNull(chest_config.getConfigurationSection(name)).getKeys(false)) {
                if (item != null) {
                    int item_position = chest_config.getInt(name + "." + item + ".position");
                    ItemStack itemStack = SerializationUtils.encodeToItem(chest_config.getString(name + "." + item + ".item"));
                    remote_chest.setItem(item_position, itemStack);
                }
            }
        }
        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_CHEST_OPEN), 10, 1);
        player.openInventory(remote_chest);
        inventoryLock.put(chest_name, player.getUniqueId());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) throws IOException {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        if (event.getView().getTitle().contains(UltiTools.languageUtils.getWords("bag_number"))) {
            String[] strings = event.getView().getTitle().split(UltiTools.languageUtils.getWords("bag_s"));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < strings.length - 1; i++) {
                stringBuilder.append(strings[i]);
            }
            String playerName = stringBuilder.toString();
            File chestFile = new File(ConfigsEnum.PLAYER_CHEST.toString(), playerName + ".yml");
            YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestFile);

            String number = ChatColor.stripColor(event.getView().getTitle()).split(UltiTools.languageUtils.getWords("bag_s"))[0].replace(playerName + UltiTools.languageUtils.getWords("bag_s"), "");
            chestConfig.set(number, "");

            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);
                if (itemStack != null) {
                    chestConfig.set(number + "." + i + ".position", i);
                    chestConfig.set(number + "." + i + ".item", SerializationUtils.serialize(itemStack));
                }
            }
            player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_CHEST_CLOSE), 10, 1);
            chestConfig.save(chestFile);
            inventoryLock.put(ChatColor.stripColor(event.getView().getTitle()), null);
        }
    }

    @Override
    public void onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clicked) {
        if (event.getView().getTitle().contains((String.format(UltiTools.languageUtils.getWords("bag_main_page_title"), player.getName())))) {
            YamlConfiguration config = Utils.getConfig(Utils.getConfigFile());
            File chestFile = new File(ConfigsEnum.PLAYER_CHEST.toString(), player.getName() + ".yml");
            YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestFile);

            if (clicked != null && clicked.getItemMeta() != null) {
                event.setCancelled(true);
                if (clicked.getItemMeta().getDisplayName().contains(UltiTools.languageUtils.getWords("bag_number"))) {
                    String chestName = player.getName() + UltiTools.languageUtils.getWords("bag_s") + ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
                    loadBag(chestName, player, player);
                } else if (UltiTools.languageUtils.getWords("bag_button_create_bag").equals(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))) {
                    int price = config.getInt("price_of_create_a_remote_chest");
                    if (UltiTools.getIsVaultInstalled()) {
                        if (UltiTools.getEcon().has(Bukkit.getOfflinePlayer(player.getUniqueId()), price)) {
                            UltiTools.getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), config.getInt("price_of_create_a_remote_chest"));
                            loadBag(player.getName() + UltiTools.languageUtils.getWords("bag_s") + (chestConfig.getKeys(false).size() + 1) + UltiTools.languageUtils.getWords("bag_number"), player, player);
                        } else {
                            player.sendMessage(not_enough_money);
                        }
                    } else if (UltiTools.getIsUltiEconomyInstalled()) {
                        if (withdraw(player, price)) {
                            loadBag(player.getName() + UltiTools.languageUtils.getWords("bag_s") + (chestConfig.getKeys(false).size() + 1) + UltiTools.languageUtils.getWords("bag_number"), player, player);
                        } else {
                            player.sendMessage(not_enough_money);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("economy_dependency_not_found"));
                    }
                }
            }
        }
    }
}

//package com.minecraft.ultikits.remoteChest;
//
//import com.minecraft.economy.apis.UltiEconomy;
//import com.minecraft.ultikits.ultitools.UltiTools;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.configuration.file.YamlConfiguration;
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.event.inventory.InventoryCloseEvent;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.Damageable;
//import org.bukkit.inventory.meta.ItemMeta;
//
//import java.io.File;
//import java.io.IOException;
//
//public class ChestPage implements Listener {
//
//    private void loadBag(String chest_name, Player player) {
//        Inventory remote_chest = Bukkit.createInventory(player, 36, chest_name);
//        File chest_file = new File(UltiTools.getInstance().getDataFolder() + "/chestData", player.getName() + ".yml");
//        YamlConfiguration chest_config = YamlConfiguration.loadConfiguration(chest_file);
//
//        String name = ChatColor.stripColor(chest_name.split("号")[0]);
//        if (chest_config.getString(name) != null) {
//            if (!chest_config.getString(name).equals("")) {
//                if (!chest_config.getConfigurationSection(name).getKeys(false).contains(null)) {
//                    for (Object item : chest_config.getConfigurationSection(name).getKeys(false)) {
//                        if (item != null) {
//                            int item_quantity = chest_config.getInt(name + "." + item + ".quantity");
//                            Material item_material = Material.valueOf(chest_config.getString(name + "." + item + ".type"));
//                            int item_position = chest_config.getInt(name + "." + item + ".position");
//                            ItemStack contained_item = new ItemStack(item_material, item_quantity);
//                            ItemMeta stickmeta = contained_item.getItemMeta();
//                            if (chest_config.getInt(name + "." + item + ".durability") > 0) {
//                                ((Damageable) stickmeta).setDamage(chest_config.getInt(name + "." + item + ".durability"));
//                            }
//                            contained_item.setItemMeta(stickmeta);
//                            int i = 0;
//                            while (chest_config.get(name + "." + item + ".enchant." + i) != null) {
//                                if (!chest_config.getString(name + "." + item + ".enchant." + i + ".name").equals("")) {
//                                    int enchantment_level = chest_config.getInt(name + "." + item + ".enchant." + i + ".level");
//                                    contained_item.addEnchantment(getEnchantment(chest_config.getString(name + "." + item + ".enchant." + i + ".name")), enchantment_level);
//                                    i++;
//                                }
//                            }
//                            remote_chest.setItem(item_position, contained_item);
//                        }
//                    }
//                } else {
//                    player.sendMessage("null");
//                }
//            }
//        }
//        player.openInventory(remote_chest);
//    }
//
//
//    @EventHandler
//    public void onItemClicked(InventoryClickEvent event) throws IOException {
//        Player player = (Player) event.getWhoClicked();
//        ItemStack clicked = event.getCurrentItem();
//        YamlConfiguration config = utils.getConfig(utils.getConfigFile());
//        File chest_file = new File(UltiTools.getInstance().getDataFolder() + "/chestData", player.getName() + ".yml");
//        YamlConfiguration chest_config = YamlConfiguration.loadConfiguration(chest_file);
//
//        UltiEconomy economy = menu_main.getEconomy();
//
//        if (event.getView().getTitle().equals("远程背包")) {
//            if (clicked != null) {
//                if (clicked.getType().equals(Material.CHEST)) {
//                    event.setCancelled(true);
//                    String chest_name = clicked.getItemMeta().getDisplayName();
//                    loadBag(chest_name, player);
//                } else if (clicked.getType().equals(Material.MINECART)) {
//                    event.setCancelled(true);
//                    if (economy.checkMoney(player.getName()) >= config.getInt("price_of_create_a_remote_chest")) {
//                        economy.takeFrom(player.getName(), config.getInt("price_of_create_a_remote_chest"));
//                        loadBag((chest_config.getKeys(false).size() + 1) + "号背包", player);
//                    } else {
//                        player.sendMessage(not_enough_money);
//                    }
//                }
////                else if (clicked.getType().equals(Material.COMPASS)) {
////                    player.openInventory(inventory_main.inventoryMap.get("main"));
////                }
//            }
//        }
//    }
//
//    @EventHandler
//    public void onInventoryClose(InventoryCloseEvent event) throws IOException {
//        Player player = (Player) event.getPlayer();
//        Inventory inventory = event.getInventory();
//        if (event.getView().getTitle().contains("号背包")) {
//            File chest_file = new File(UltiTools.getInstance().getDataFolder() + "/chestData", player.getName() + ".yml");
//            YamlConfiguration chest_config = YamlConfiguration.loadConfiguration(chest_file);
//
//            chest_config.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]), "");
//            ItemStack[] chest_contents = inventory.getContents();
//            int a = 0;
//            int l = 0;
//            for (ItemStack item : chest_contents) {
//                if (item != null) {
//                    chest_config.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".type", item.getType().name());
//                    chest_config.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".quantity", item.getAmount());
//                    chest_config.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".position", l);
//                    if (item.getDurability() > 0) {
//                        chest_config.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".durability", item.getDurability());
//                    }
//                    if (!item.getEnchantments().isEmpty()) {
//                        int i = 0;
//                        for (Enchantment itemEnchantments : item.getEnchantments().keySet()) {
//                            chest_config.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".enchant." + i + ".name", itemEnchantments.getKey().toString().split(":")[1]);
//                            chest_config.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]) + "." + l + ".enchant." + i + ".level", item.getEnchantmentLevel(itemEnchantments));
//                            i++;
//                        }
//                    }
//                    a++;
//                }
//                l++;
//            }
//            if (a == 0) {
//                chest_config.set(ChatColor.stripColor(event.getView().getTitle().split("号")[0]), "");
//            }
//            chest_config.save(chest_file);
//        }
//    }
//}

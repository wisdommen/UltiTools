package com.minecraft.ultikits.email;

import com.minecraft.ultikits.GUIs.GUISetup;
import com.minecraft.ultikits.GUIs.InventoryManager;
import com.minecraft.ultikits.GUIs.ItemStackManager;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.Enchants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.minecraft.ultikits.utils.Enchants.getEnchantment;

public class EmailPage implements Listener {

    @EventHandler
    public void onItemClicked(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        File file = new File(UltiTools.getInstance().getDataFolder() + "/emailData", player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (event.getView().getTitle().equals("收件箱")) {
            if (clicked != null) {
                event.setCancelled(true);
                if (Objects.requireNonNull(clicked.getItemMeta()).getDisplayName().contains("来自：")) {
                    for (String lore : Objects.requireNonNull(clicked.getItemMeta().getLore())) {
                        if (lore.contains("ID")) {
                            String uuid = lore.split(":")[1];
                            if (config.getBoolean(uuid + ".isRead")) {
                                return;
                            }
                            if (config.getString(uuid + ".item.type") == null) {
                                config.set(uuid + ".isRead", true);
                            } else {
                                ItemStackManager itemStackManager = Email.emailContentManagerMap.get(uuid).getItemStackManager();
                                if (player.getInventory().firstEmpty() != -1) {
                                    ItemStack itemStack = itemStackManager.getItem();
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    if (itemMeta != null) {
                                        itemMeta.setDisplayName(itemStackManager.getDisplayName());
                                        itemMeta.setLore(itemStackManager.getLore());
                                        if (config.getDouble(uuid+".item.durability") > 0) {
                                            ((Damageable) itemMeta).setDamage((int) config.getDouble(uuid+".item.durability"));
                                        }
                                        itemStack.setItemMeta(itemMeta);
                                    }
                                    int i = 1;
                                    if (config.getConfigurationSection(uuid + ".item.enchant")!=null) {
                                        while (config.getConfigurationSection(uuid + ".item.enchant").getKeys(false).size() >= i) {
                                            if (config.getString(uuid + ".item.enchant." + i + ".name") != null) {
                                                int enchantment_level = config.getInt(uuid + ".item.enchant." + i + ".level");
                                                String enchantment_name = config.getString(uuid + ".item.enchant." + i + ".name");
                                                itemStack.addUnsafeEnchantment(Objects.requireNonNull(getEnchantment(enchantment_name)), enchantment_level);
                                                i++;
                                            }
                                        }
                                    }
                                    itemStack.setAmount(itemStackManager.getAmount());
                                    player.getInventory().addItem(itemStack);
                                    config.set(uuid+".isRead", true);
                                    config.set(uuid+".isClaimed", true);
                                } else {
                                    player.sendMessage(ChatColor.RED + "背包容量不足，无法接受附件，请保证至少有一个空位！");
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
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        File folder = new File(UltiTools.getInstance().getDataFolder() + "/emailData");
        File file = new File(folder, player.getName() + ".yml");
        if (!folder.exists()){
            folder.mkdirs();
        }
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

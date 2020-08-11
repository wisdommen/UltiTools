package com.minecraft.ultikits.email;

import com.minecraft.ultikits.config.ConfigsEnum;
import com.minecraft.ultikits.utils.SerializationUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class EmailPage implements Listener {

    @EventHandler
    public void onItemClicked(@NotNull InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        File file = new File(ConfigsEnum.PLAYER_EMAIL.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (event.getView().getTitle().equals("收件箱")) {
            if (clicked != null) {
                event.setCancelled(true);
                if (Objects.requireNonNull(clicked.getItemMeta()).getDisplayName().contains("来自：")) {
                    for (String lore : Objects.requireNonNull(clicked.getItemMeta().getLore())) {
                        if (lore.contains("ID:")) {
                            String uuid = lore.split(":")[1];
                            if (config.getBoolean(uuid + ".isRead")) {
                                return;
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
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File folder = new File(ConfigsEnum.PLAYER_EMAIL.toString());
        File file = new File(folder, player.getName() + ".yml");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

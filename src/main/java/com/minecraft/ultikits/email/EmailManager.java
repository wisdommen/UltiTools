package com.minecraft.ultikits.email;

import com.minecraft.ultikits.GUIs.ItemStackManager;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.minecraft.ultikits.utils.Enchants.getEnchantment;

public class EmailManager {

    private final File file;
    private final YamlConfiguration config;
    private final String playerName;

    public EmailManager(File playerFile) {
        file = playerFile;
        playerName = playerFile.getName().replace(".yml", "");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public Map<String, EmailContentManager> getEmails() {
        Map<String, EmailContentManager> emails = new HashMap<>();

        for (String uuid : config.getKeys(false)) {
            if (config.getConfigurationSection(uuid).getKeys(false).contains("item")) {
                int item_quantity = config.getInt(uuid + ".item.amount");
                Material item_material = Material.valueOf(config.getString(uuid + ".item.type"));
                ItemStack contained_item = new ItemStack(item_material, item_quantity);
                List<String> item_lore = config.getStringList(uuid + ".item.lore");
                String item_name = config.getString(uuid + ".item.name");
                int durability = config.getInt(uuid + ".item.durability");

                int i = 0;
                while (config.get(uuid + ".item.enchant." + i) != null) {
                    if (!Objects.equals(config.getString(uuid + ".item.enchant." + i + ".name"), "")) {
                        int enchantment_level = config.getInt(uuid + ".item.enchant.level");
                        String enchantment_name = config.getString(uuid + ".item.enchant.name");
                        contained_item.addUnsafeEnchantment(Objects.requireNonNull(getEnchantment(enchantment_name)), enchantment_level);
                        i++;
                    }
                }
                ItemStackManager itemStackManager = new ItemStackManager(contained_item, (ArrayList<String>) item_lore, item_name);
                itemStackManager.setUpItem();
                itemStackManager.setDurability(durability);
                emails.put(uuid, new EmailContentManager(uuid, config.getString(uuid + ".sender"), config.getString(uuid + ".message"), itemStackManager, config.getBoolean(uuid + ".isRead"), config.getBoolean(uuid + ".isClaimed")));
            } else {
                emails.put(uuid, new EmailContentManager(uuid, config.getString(uuid + ".sender"), config.getString(uuid + ".message"), config.getBoolean(uuid + ".isRead")));
            }
        }
        return emails;
    }


    /**
     * @param receiverFile     发送给某个人
     * @param message          所要发送的消息
     * @param itemStackManager 发送包含的物品
     * @return 是否发送成功
     */
    public Boolean sendTo(File receiverFile, String message, ItemStackManager itemStackManager) {
        if (receiverFile.exists()) {
            EmailManager emailManager = new EmailManager(receiverFile);
            EmailContentManager emailContentManager = new EmailContentManager(generateUUID(), playerName, message, itemStackManager, false, false);
            emailManager.saveEmail(emailContentManager.getUuid(), emailContentManager.getSender(), emailContentManager.getMessage(), emailContentManager.getItemStackManager());
            return true;
        }
        return false;
    }

    public Boolean sendTo(File receiverFile, String message) {
        if (receiverFile.exists()) {
            EmailManager emailManager = new EmailManager(receiverFile);
            EmailContentManager emailContentManager = new EmailContentManager(generateUUID(), playerName, message, false);
            emailManager.saveEmail(emailContentManager.getUuid(), emailContentManager.getSender(), emailContentManager.getMessage());
            return true;
        }
        return false;
    }

    private void saveEmail(String uuid, String sender, String message) {
        config.set(uuid + ".sender", sender);
        config.set(uuid + ".message", message);
        config.set(uuid + ".isRead", false);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveEmail(String uuid, String sender, String message, ItemStackManager itemStackManager) {
        config.set(uuid + ".sender", sender);
        config.set(uuid + ".message", message);
        config.set(uuid + ".isRead", false);
        config.set(uuid + ".isClaimed", false);
        config.set(uuid + ".item.type", itemStackManager.getItem().getType().name());
        config.set(uuid + ".item.amount", itemStackManager.getAmount());
        config.set(uuid + ".item.lore", itemStackManager.getLore());
        if (ChatColor.stripColor(itemStackManager.getItem().getItemMeta().getDisplayName()).equals("")) {
            config.set(uuid + ".item.name", ChatColor.stripColor(itemStackManager.getItem().getItemMeta().getDisplayName()));
        } else {
            config.set(uuid + ".item.name", itemStackManager.getItem().getItemMeta().getDisplayName());
        }
        config.set(uuid + ".item.durability", itemStackManager.getDurability());
        if (itemStackManager.getEnchantment().keySet().size() > 0) {
            int i = 1;
            for (String name : itemStackManager.getEnchantment().keySet()) {
                config.set(uuid + ".item.enchant." + i + ".name", name);
                config.set(uuid + ".item.enchant." + i + ".level", itemStackManager.getEnchantment().get(name));
                i++;
            }
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean deleteHistoryEmails() {
        if (config.getKeys(false).size() != 0) {
            return file.delete();
        }
        return false;
    }

    public void sendTeamInvitation() {

    }

    public static String generateUUID() {
        Date date = new Date();
        return String.valueOf(date.getTime());
    }

}

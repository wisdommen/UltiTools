package com.ultikits.ultitools.listener;

import com.minecraft.Ultilevel.utils.checkLevel;
import com.ultikits.beans.CancelResult;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.PagesListener;
import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.EconomyUtils;
import com.ultikits.utils.MessagesUtils;
import com.ultikits.utils.SerializationUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@EventListener(function = "kits")
public class KitsPageListener extends PagesListener {

    @Override
    public CancelResult onItemClick(InventoryClickEvent event, Player player, InventoryManager inventoryManager, ItemStack clickedItem) {
        YamlConfiguration kitsConfig = ConfigController.getConfig("kits");
        if (!inventoryManager.getTitle().contains(UltiTools.languageUtils.getString("kits_page_title"))){
            return CancelResult.NONE;
        }
        File kit_file = new File(ConfigsEnum.DATA_KIT.toString());
        YamlConfiguration kit_config = YamlConfiguration.loadConfiguration(kit_file);

        String clickedItemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        for (String item : kitsConfig.getKeys(false)) {
            String kitName = kitsConfig.getString(item + ".name");
            if (!clickedItemName.equals(kitName)) continue;
            if (!isPlayerCanBuy(player, item)) return CancelResult.TRUE;
            if (!kitsConfig.getStringList(item + ".contain").isEmpty()) {
                for (String i : kitsConfig.getStringList(item + ".contain")) {
                    player.getInventory().addItem(SerializationUtils.encodeToItem(i));
                }
            }
            if (!kitsConfig.getStringList(item + ".playerCommands").isEmpty()) {
                for (String playerCommand : kitsConfig.getStringList(item + ".playerCommands")) {
                    player.performCommand(playerCommand);
                }
            }
            if (!kitsConfig.getStringList(item + ".consoleCommands").isEmpty()) {
                for (String consoleCommand : kitsConfig.getStringList(item + ".consoleCommands")) {
                    while (consoleCommand.contains("{PLAYER}")) {
                        consoleCommand = consoleCommand.replace("{PLAYER}", player.getName());
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), consoleCommand);
                }
            }
            player.closeInventory();
            player.sendMessage(MessagesUtils.bought + ChatColor.RED + kitName);
            if (!kitsConfig.getBoolean(item + ".reBuyable")) {
                List<String> a = kit_config.getStringList(Objects.requireNonNull(kitName));
                a.add(player.getName());
                kit_config.set(kitName, a);
                try {
                    kit_config.save(kit_file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            int price = kitsConfig.getInt(item + ".price");
            if (price > 0) EconomyUtils.withdraw(player, price);
        }
        return CancelResult.TRUE;
    }

    private boolean isPlayerCanBuy(Player player, String item){
        YamlConfiguration kitsConfig = ConfigController.getConfig("kits");
        File kit_file = new File(UltiTools.getInstance().getDataFolder() + "/kitData", "kit.yml");
        YamlConfiguration kit_config = YamlConfiguration.loadConfiguration(kit_file);

        String kit_name = kitsConfig.getString(item + ".name");
        if (Objects.requireNonNull(kit_config.getStringList(Objects.requireNonNull(kit_name))).contains(player.getName())) {
            player.sendMessage(MessagesUtils.kit_already_claimed);
            return false;
        }
        int empty_slots = 0;
        for (ItemStack i : player.getInventory().getContents()) {
            if (i == null) {
                empty_slots++;
            }
        }
        int contain_size = Objects.requireNonNull(kitsConfig.getStringList(item + ".contain")).size();
        int price = kitsConfig.getInt(item + ".price");
        if (empty_slots < contain_size) {
            player.closeInventory();
            player.sendMessage(MessagesUtils.player_inventory_full);
            return false;
        }
        if (price != 0 && EconomyUtils.checkMoney(player) < price) {
            player.sendMessage(MessagesUtils.not_enough_money);
            return false;
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("UltiLevel") != null) {
            int level = kitsConfig.getInt(item + ".level");
            if (checkLevel.checkLevel(player) < level) return false;
            String job = kitsConfig.getString(item + ".job");
            return job.equals(UltiTools.languageUtils.getString("kits_config_job")) || checkLevel.checkJob(player).equals(job);
        }
        return true;
    }
}

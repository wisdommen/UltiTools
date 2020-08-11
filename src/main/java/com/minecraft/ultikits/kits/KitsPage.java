package com.minecraft.ultikits.kits;

import com.minecraft.ultikits.config.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.Messages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.minecraft.Ultilevel.utils.checkLevel.checkJob;
import static com.minecraft.Ultilevel.utils.checkLevel.checkLevel;

public class KitsPage implements Listener {

    private static final File kits = new File(ConfigsEnum.KIT.toString());
    private static final YamlConfiguration kitsConfig = YamlConfiguration.loadConfiguration(kits);
    private static final Economy economy = UltiTools.getEcon();
    
    @EventHandler
    public void onItemClicked(InventoryClickEvent event) throws IOException {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        File kit_file = new File(ConfigsEnum.DATA_KIT.toString());
        YamlConfiguration kit_config = YamlConfiguration.loadConfiguration(kit_file);

        if (!(event.getView().getTitle().equals("物品包/礼包中心") && clicked != null)) return;
        event.setCancelled(true);
        String clickedItem = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
        for (String item : kitsConfig.getKeys(false)) {
            String kitName = kitsConfig.getString(item + ".name");
            if (!clickedItem.equals(kitName)) continue;
            if (!isPlayerCanBuy(player, item)) return;
            if (!Objects.requireNonNull(kitsConfig.getConfigurationSection(item + ".contain").getKeys(false)).isEmpty()) {
                for (String i : Objects.requireNonNull(kitsConfig.getConfigurationSection(item + ".contain").getKeys(false))) {
                    player.getInventory().addItem(new ItemStack(Material.valueOf(i), kitsConfig.getInt(item + ".contain." + i + ".quantity")));
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
            player.sendMessage(Messages.bought + ChatColor.RED + kitName);
            if (!kitsConfig.getBoolean(item + ".reBuyable")) {
                List<String> a = kit_config.getStringList(Objects.requireNonNull(kitName));
                a.add(player.getName());
                kit_config.set(kitName, a);
                kit_config.save(kit_file);
            }
            int price = kitsConfig.getInt(item + ".price");
            if (price > 0) economy.withdrawPlayer(player, price);
        }
    }

    private boolean isPlayerCanBuy(Player player, String item){
        File kit_file = new File(UltiTools.getInstance().getDataFolder() + "/kitData", "kit.yml");
        YamlConfiguration kit_config = YamlConfiguration.loadConfiguration(kit_file);

        String kit_name = kitsConfig.getString(item + ".name");
        if (Objects.requireNonNull(kit_config.getStringList(Objects.requireNonNull(kit_name))).contains(player.getName())) {
            player.sendMessage(Messages.kit_already_claimed);
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
            player.sendMessage(Messages.player_inventory_full);
            return false;
        }
        if (price != 0 && !economy.has(player, price)) {
            player.sendMessage(Messages.not_enough_money);
            return false;
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("UltiLevel") != null) {
            int level = kitsConfig.getInt(item + ".level");
            if (checkLevel(player) < level) return false;
            String job = kitsConfig.getString(item + ".job");
            return job.equals("全部") || checkJob(player).equals(job);
        }
        return true;
    }
}

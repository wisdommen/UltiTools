package com.minecraft.ultikits.GUIs;

import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.minecraft.ultikits.utils.Messages.info;
import static com.minecraft.ultikits.utils.Messages.unimportant;

public class GUISetup {

    public static Map<String, InventoryManager> inventoryMap = new HashMap<>();

    public static void setUpGUIs(){
        InventoryManager chest = new InventoryManager(null, 36, "远程背包");
        chest.create();
        inventoryMap.put("chest", chest);
    }

    public static void setPlayerRemoteChest(Player player) {
        YamlConfiguration config = Utils.getConfig(Utils.getConfigFile());
        File chest_file = new File(UltiTools.getInstance().getDataFolder() + "/chestData", player.getName() + ".yml");
        YamlConfiguration chest_config = YamlConfiguration.loadConfiguration(chest_file);
        if (!chest_config.getKeys(false).isEmpty()) {
            for (int i = 1; i <= chest_config.getKeys(false).size(); i++) {
                ItemStackManager itemStackManager = new ItemStackManager(new ItemStack(Material.CHEST), new ArrayList<>(), info(i + "号背包"));
                itemStackManager.setUpItem();
                inventoryMap.get("chest").setItem(i - 1, itemStackManager.getItem());
            }
        }
        ArrayList<String> lore = new ArrayList<>();
        ItemStack item2 = new ItemStack(Material.MINECART);
        ItemMeta stickmeta = item2.getItemMeta();
        lore.add(unimportant("价格：" + config.getInt("price_of_create_a_remote_chest")));
        Objects.requireNonNull(stickmeta).setLore(lore);
        stickmeta.setDisplayName(ChatColor.AQUA + "创建背包");
        item2.setItemMeta(stickmeta);
        inventoryMap.get("chest").setItem(35, item2);
    }
}

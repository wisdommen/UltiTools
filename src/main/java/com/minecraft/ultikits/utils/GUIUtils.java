package com.minecraft.ultikits.utils;

import com.minecraft.ultikits.inventoryapi.InventoryManager;
import com.minecraft.ultikits.manager.ItemStackManager;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.enums.LoginRegisterEnum;
import com.minecraft.ultikits.beans.EmailContentBean;
import com.minecraft.ultikits.manager.EmailManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

import static com.minecraft.ultikits.utils.MessagesUtils.*;
import static com.minecraft.ultikits.utils.Utils.convertMillisecondsToRegularTime;

public class GUIUtils {

    public static Map<String, InventoryManager> inventoryMap = new HashMap<>();

    private GUIUtils(){}

    public static void setPlayerRemoteChest(@NotNull Player player) {
        setPlayerRemoteChest(player.getName());
    }

    public static void setPlayerRemoteChest(@NotNull String playerName) {
        YamlConfiguration config = Utils.getConfig(Utils.getConfigFile());
        File chestFile = new File(ConfigsEnum.PLAYER_CHEST.toString(), playerName + ".yml");
        YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestFile);
        InventoryManager chest = new InventoryManager(null, 36, "远程背包");
        inventoryMap.put(playerName + ".chest", chest);

        if (!chestConfig.getKeys(false).isEmpty()) {
            for (int i = 1; i <= chestConfig.getKeys(false).size(); i++) {
                ItemStackManager itemStackManager = new ItemStackManager(new ItemStack(Material.CHEST), new ArrayList<>(), info(i + "号背包"));
                inventoryMap.get(playerName + ".chest").setItem(i - 1, itemStackManager.getItem());
            }
        }
        ArrayList<String> lore = new ArrayList<>();
        ItemStack item2 = new ItemStack(Material.MINECART);
        ItemMeta stickmeta = item2.getItemMeta();
        lore.add(unimportant("价格：" + config.getInt("price_of_create_a_remote_chest")));
        Objects.requireNonNull(stickmeta).setLore(lore);
        stickmeta.setDisplayName(ChatColor.AQUA + "创建背包");
        item2.setItemMeta(stickmeta);
        inventoryMap.get(playerName + ".chest").setItem(35, item2);
    }

    public static void setupLoginRegisterLayout(Player player, @NotNull LoginRegisterEnum title) {
        InventoryManager inventoryManager = new InventoryManager(player, 54, title.toString());
        inventoryMap.put(player.getName() + title.toString(), inventoryManager);

        ItemStackManager itemStackManager = new ItemStackManager(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1), "点按输入数字");
        //数字键盘
        inventoryManager.setItem(21, itemStackManager.getItem(1));
        inventoryManager.setItem(22, itemStackManager.getItem(2));
        inventoryManager.setItem(23, itemStackManager.getItem(3));
        inventoryManager.setItem(30, itemStackManager.getItem(4));
        inventoryManager.setItem(31, itemStackManager.getItem(5));
        inventoryManager.setItem(32, itemStackManager.getItem(6));
        inventoryManager.setItem(39, itemStackManager.getItem(7));
        inventoryManager.setItem(40, itemStackManager.getItem(8));
        inventoryManager.setItem(41, itemStackManager.getItem(9));
        inventoryManager.setItem(49, itemStackManager.getItem(10));

        //其他功能键
        ItemStackManager itemStackManager2 = new ItemStackManager(new ItemStack(Material.RED_STAINED_GLASS_PANE), "清空");
        inventoryManager.setItem(48, itemStackManager2.getItem());
        ItemStackManager itemStackManager3 = new ItemStackManager(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "确认");
        inventoryManager.setItem(50, itemStackManager3.getItem());
        ItemStackManager itemStackManager4 = new ItemStackManager(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), "退出");
        inventoryManager.setItem(53, itemStackManager4.getItem());
        ItemStackManager itemStackManager5 = new ItemStackManager(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "");
        for (int i = 9; i < 54; i++) {
            if (inventoryManager.getInventory().getItem(i) == null) {
                inventoryManager.setItem(i, itemStackManager5.getItem());
            }
        }
    }
}

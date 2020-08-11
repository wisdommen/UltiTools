package com.minecraft.ultikits.GUIs;

import com.minecraft.ultikits.config.ConfigsEnum;
import com.minecraft.ultikits.email.EmailContentManager;
import com.minecraft.ultikits.email.EmailManager;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

import static com.minecraft.ultikits.utils.Messages.*;

public class GUISetup {

    public static Map<String, InventoryManager> inventoryMap = new HashMap<>();

    public static void setPlayerRemoteChest(@NotNull Player player) {
        YamlConfiguration config = Utils.getConfig(Utils.getConfigFile());
        File chestFile = new File(ConfigsEnum.PLAYER_CHEST.toString(), player.getName() + ".yml");
        YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestFile);
        InventoryManager chest = new InventoryManager(null, 36, "远程背包");
        chest.create();
        inventoryMap.put(player.getName() + ".chest", chest);

        if (!chestConfig.getKeys(false).isEmpty()) {
            for (int i = 1; i <= chestConfig.getKeys(false).size(); i++) {
                ItemStackManager itemStackManager = new ItemStackManager(new ItemStack(Material.CHEST), new ArrayList<>(), info(i + "号背包"));
                itemStackManager.setUpItem();
                inventoryMap.get(player.getName() + ".chest").setItem(i - 1, itemStackManager.getItem());
            }
        }
        ArrayList<String> lore = new ArrayList<>();
        ItemStack item2 = new ItemStack(Material.MINECART);
        ItemMeta stickmeta = item2.getItemMeta();
        lore.add(unimportant("价格：" + config.getInt("price_of_create_a_remote_chest")));
        Objects.requireNonNull(stickmeta).setLore(lore);
        stickmeta.setDisplayName(ChatColor.AQUA + "创建背包");
        item2.setItemMeta(stickmeta);
        inventoryMap.get(player.getName() + ".chest").setItem(35, item2);
    }

    public static Map<String, EmailContentManager> setUpEmailInBox(@NotNull Player player) {
        File file = new File(ConfigsEnum.PLAYER_EMAIL.toString(), player.getName() + ".yml");
        EmailManager emailManager = new EmailManager(file);
        Map<String, EmailContentManager> emailContentManagers = emailManager.getEmails();
        InventoryManager inventoryManager = new InventoryManager(player, 36, "收件箱");
        inventoryManager.create();
        inventoryMap.put(player.getName() + ".inbox", inventoryManager);

        int s = 0;
        for (String each : emailContentManagers.keySet()) {
            if (s > 36) {
                s -= 37;
            }
            String sender = emailContentManagers.get(each).getSender();
            String message = emailContentManagers.get(each).getMessage();
            ArrayList<String> lore = (ArrayList<String>) getLoreList(emailContentManagers.get(each), message, 18);
            ItemStackManager mail;
            if (!emailContentManagers.get(each).getRead()) {
                mail = new ItemStackManager(new ItemStack(Material.PAPER, 1), lore, "来自：" + sender);
            } else {
                mail = new ItemStackManager(new ItemStack(Material.FILLED_MAP, 1), lore, "来自：" + sender);
            }
            mail.setUpItem();
            inventoryManager.forceSetItem(s, mail.getItem());
            s++;
        }
        return emailContentManagers;
    }

    public static void setupLoginRegisterLayout(Player player, @NotNull LoginRegisterEnum title) {
        InventoryManager inventoryManager = new InventoryManager(player, 54, title.toString());
        inventoryManager.create();
        inventoryMap.put(player.getName() + title.toString(), inventoryManager);

        ItemStackManager itemStackManager = new ItemStackManager(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1), "点按输入数字");
        itemStackManager.setUpItem();
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
        itemStackManager2.setUpItem();
        inventoryManager.setItem(48, itemStackManager2.getItem());
        ItemStackManager itemStackManager3 = new ItemStackManager(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "确认");
        itemStackManager3.setUpItem();
        inventoryManager.setItem(50, itemStackManager3.getItem());
        ItemStackManager itemStackManager4 = new ItemStackManager(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), "退出");
        itemStackManager4.setUpItem();
        inventoryManager.setItem(53, itemStackManager4.getItem());
        ItemStackManager itemStackManager5 = new ItemStackManager(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "");
        itemStackManager5.setUpItem();
        for (int i = 9; i < 54; i++) {
            if (inventoryManager.getInventory().getItem(i) == null) {
                inventoryManager.setItem(i, itemStackManager5.getItem());
            }
        }
    }

    public static @NotNull List<String> getLoreList(EmailContentManager emailContentManager, @NotNull String inputString, int length) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.GOLD + "------邮件内容------");
        int strLen = inputString.length();
        int start = 0;
        int num = length;
        String temp;
        while (true) {
            try {
                if (num >= strLen) {
                    temp = inputString.substring(start, strLen);
                } else {
                    temp = inputString.substring(start, num);
                }
            } catch (Exception e) {
                break;
            }
            list.add(ChatColor.WHITE + temp);
            start = num;
            num = num + length;
        }
        if (emailContentManager.getItemStack() != null) {
            list.add(ChatColor.GOLD + "------附件内容------");
            if (emailContentManager.getItemStack().getItemMeta().getDisplayName().equals("")) {
                list.add(ChatColor.LIGHT_PURPLE + emailContentManager.getItemStack().getType().name() + " * " + emailContentManager.getItemStack().getAmount() + "个");
            } else {
                list.add(ChatColor.LIGHT_PURPLE + emailContentManager.getItemStack().getItemMeta().getDisplayName() + " * " + emailContentManager.getItemStack().getAmount() + "个");
            }
        }
        if (!emailContentManager.getRead()) {
            if (emailContentManager.getItemStack() != null) {
                list.add(ChatColor.RED + "点击领取附件！");
            } else {
                list.add(ChatColor.RED + "点击来标记为已读！");
            }
        } else {
            list.add(ChatColor.AQUA + "已读！");
        }
        list.add(ChatColor.DARK_GRAY + "ID:" + emailContentManager.getUuid());
        return list;
    }

    public static void setKit(Player player) {
        File kitFile = new File(ConfigsEnum.DATA_KIT.toString());
        File kits = new File(ConfigsEnum.KIT.toString());
        YamlConfiguration claimState = YamlConfiguration.loadConfiguration(kitFile);
        YamlConfiguration kitsConfig = YamlConfiguration.loadConfiguration(kits);
        InventoryManager chest = new InventoryManager(null, 54, "物品包/礼包中心");
        chest.create();
        inventoryMap.put(player.getName() + ".kits", chest);

        int s = 0;
        for (String kitItem : kitsConfig.getKeys(false)) {
            ArrayList<String> lore = new ArrayList<>();
            ItemStack item = new ItemStack(Material.valueOf(kitsConfig.getString(kitItem + ".item")));
            ItemMeta itemMeta = item.getItemMeta();
            lore.add(unimportant(kitsConfig.getString(kitItem + ".description")));
            lore.add(ChatColor.AQUA + "等级要求：    " + ChatColor.GOLD + kitsConfig.getInt(kitItem + ".level"));
            lore.add(ChatColor.AQUA + "可领取职业： " + ChatColor.GOLD + kitsConfig.getString(kitItem + ".job"));
            lore.add(ChatColor.AQUA + "价格：        " + ChatColor.GOLD + kitsConfig.getInt(kitItem + ".price") + ChatColor.AQUA + " 枚金币");
            lore.add(ChatColor.DARK_GRAY + "内含物：");
            for (String i : Objects.requireNonNull(kitsConfig.getConfigurationSection(kitItem + ".contain").getKeys(false))) {
                lore.add(unimportant(i) + " x " + unimportant(String.valueOf(kitsConfig.getInt(kitItem + ".contain." + i + ".quantity"))) + "个");
            }
            String kitName = kitsConfig.getString(kitItem + ".name");
            if (Objects.requireNonNull(claimState.getStringList(Objects.requireNonNull(kitName))).contains(player.getName())) {
                lore.add(warning("已领取！"));
            }
            Objects.requireNonNull(itemMeta).setLore(lore);
            itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + kitsConfig.getString(kitItem + ".name"));
            item.setItemMeta(itemMeta);
            inventoryMap.get(player.getName() + ".kits").setItem(s, item);
            s = s + 1;
        }
    }
}

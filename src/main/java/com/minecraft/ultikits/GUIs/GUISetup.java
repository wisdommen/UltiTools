package com.minecraft.ultikits.GUIs;

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

import java.io.File;
import java.util.*;

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
        File chestFile = new File(UltiTools.getInstance().getDataFolder() + "/chestData", player.getName() + ".yml");
        YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestFile);
        if (!chestConfig.getKeys(false).isEmpty()) {
            for (int i = 1; i <= chestConfig.getKeys(false).size(); i++) {
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

    public static Map<String, EmailContentManager> setUpEmailInBox(Player player) {
        EmailManager emailManager = new EmailManager(player);
        Map<String, EmailContentManager> emailContentManagers = emailManager.getEmails();
        InventoryManager inventoryManager = new InventoryManager(player, 36, "收件箱");
        inventoryManager.create();
        inventoryMap.put(player.getName()+".inbox", inventoryManager);

        int s = 0;
        for (String each : emailContentManagers.keySet()) {
            if (s > 36) {
                s-=37;
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

    public static void setupLoginRegisterLayout(Player player, LoginRegisterEnum title){
        player.sendMessage(title.toString());
        InventoryManager inventoryManager = new InventoryManager(player, 54, title.toString());
        inventoryManager.create();
        inventoryMap.put(player.getName()+title.toString(), inventoryManager);

        ItemStackManager itemStackManager = new ItemStackManager(new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1), "点按输入数字");
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
    }

    public static List<String> getLoreList(EmailContentManager emailContentManager, String inputString, int length) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.GOLD+"------邮件内容------");
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
            list.add(ChatColor.WHITE+temp);
            start = num;
            num = num + length;
        }
        if (emailContentManager.getItemStackManager() != null) {
            list.add(ChatColor.GOLD+"------附件内容------");
            if (emailContentManager.getItemStackManager().getDisplayName().equals("")) {
                list.add(ChatColor.LIGHT_PURPLE + emailContentManager.getItemStackManager().getItem().getType().name() + " * " + emailContentManager.getItemStackManager().getAmount() + "个");
            }else {
                list.add(ChatColor.LIGHT_PURPLE + emailContentManager.getItemStackManager().getDisplayName() + " * " + emailContentManager.getItemStackManager().getAmount() + "个");
            }
        }
        if (!emailContentManager.getRead()) {
            if (emailContentManager.getItemStackManager() != null){
                list.add(ChatColor.RED + "点击领取附件！");
            }else {
                list.add(ChatColor.RED + "点击来标记为已读！");
            }
        }else {
            list.add(ChatColor.AQUA + "已读！");
        }
        list.add(ChatColor.DARK_GRAY+"ID:"+emailContentManager.getUuid());
        return list;
    }
}

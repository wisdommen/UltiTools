package com.minecraft.ultikits.views;

import com.minecraft.ultikits.beans.EmailContentBean;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.inventoryapi.InventoryManager;
import com.minecraft.ultikits.inventoryapi.ViewManager;
import com.minecraft.ultikits.inventoryapi.ViewType;
import com.minecraft.ultikits.listener.EmailPageListener;
import com.minecraft.ultikits.listener.PermissionListener;
import com.minecraft.ultikits.manager.EmailManager;
import com.minecraft.ultikits.manager.ItemStackManager;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

import static com.minecraft.ultikits.utils.Utils.convertMillisecondsToRegularTime;

public class EmailView {

    private EmailView() {
    }

    public static Inventory setUp(Player player) {
        InventoryManager inventoryManager = new InventoryManager(null, 54, String.format(UltiTools.languageUtils.getWords("email_page_title"), player.getName()), true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager, new EmailPageListener());
        File file = new File(ConfigsEnum.PLAYER_EMAIL.toString(), player.getName() + ".yml");
        EmailManager emailManager = new EmailManager(file);
        Map<String, EmailContentBean> emailContentManagers = emailManager.getEmails();
        for (ItemStackManager itemStackManager : setUpItems(emailContentManagers)) {
            inventoryManager.addItem(itemStackManager);
        }
        return inventoryManager.getInventory();
    }

    private static List<ItemStackManager> setUpItems(Map<String, EmailContentBean> emailContentManagers) {
        List<ItemStackManager> res = new ArrayList<>();
        List<String> list = sortSet(emailContentManagers.keySet());
        for (String each : list) {
            String sender = emailContentManagers.get(each).getSender();
            String message = emailContentManagers.get(each).getMessage();
            ArrayList<String> lore = (ArrayList<String>) getLoreList(emailContentManagers.get(each), message, 18);
            ItemStackManager mail;
            if (!emailContentManagers.get(each).getRead()) {
                mail = new ItemStackManager(UltiTools.versionAdaptor.getEmailMaterial(false), lore, UltiTools.languageUtils.getWords("email_item_description_from") + sender);
            } else {
                mail = new ItemStackManager(UltiTools.versionAdaptor.getEmailMaterial(true), lore, UltiTools.languageUtils.getWords("email_item_description_from") + sender);
            }
            res.add(mail);
        }
        return res;
    }

    private static List<String> sortSet(Set<String> set) {
        List<String> stringList = new ArrayList<>(set);
        Collections.sort(stringList);
        Collections.reverse(stringList);
        return stringList;
    }

    public static @NotNull List<String> getLoreList(EmailContentBean emailContentBean, @NotNull String inputString, int length) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.GRAY + convertMillisecondsToRegularTime(Long.valueOf(emailContentBean.getUuid())));
        list.add(ChatColor.GOLD + UltiTools.languageUtils.getWords("email_text_content_header"));
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
        if (emailContentBean.getItemStack() != null) {
            list.add(ChatColor.GOLD + UltiTools.languageUtils.getWords("email_attachment_content_header"));
            if (emailContentBean.getItemStack().getItemMeta().getDisplayName() == null || emailContentBean.getItemStack().getItemMeta().getDisplayName().equals("")) {
                list.add(ChatColor.LIGHT_PURPLE + emailContentBean.getItemStack().getType().name() + " * " + emailContentBean.getItemStack().getAmount() + UltiTools.languageUtils.getWords("ge"));
            } else {
                list.add(ChatColor.LIGHT_PURPLE + emailContentBean.getItemStack().getItemMeta().getDisplayName() + " * " + emailContentBean.getItemStack().getAmount() + UltiTools.languageUtils.getWords("ge"));
            }
        }
        if (!emailContentBean.getRead()) {
            if (emailContentBean.getItemStack() != null) {
                list.add(ChatColor.RED + UltiTools.languageUtils.getWords("email_click_to_claim"));
            } else {
                list.add(ChatColor.RED + UltiTools.languageUtils.getWords("email_click_to_read"));
            }
        } else {
            list.add(ChatColor.AQUA + UltiTools.languageUtils.getWords("email_read"));
        }
        list.add(ChatColor.DARK_GRAY + "ID:" + emailContentBean.getUuid());
        return list;
    }
}

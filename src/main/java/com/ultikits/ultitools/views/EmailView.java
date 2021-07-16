package com.ultikits.ultitools.views;

import com.ultikits.beans.EmailContentBean;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.manager.ItemStackManager;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.listener.EmailPageListener;
import com.ultikits.ultitools.manager.EmailManager;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class EmailView {

    private EmailView() {
    }

    public static Inventory setUp(Player player) {
        InventoryManager inventoryManager = new InventoryManager(null, 54, String.format(UltiTools.languageUtils.getString("email_page_title"), player.getName()), true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager);
        new BukkitRunnable() {
            @Override
            public void run() {
                File file = new File(ConfigsEnum.PLAYER_EMAIL.toString(), player.getName() + ".yml");
                EmailManager emailManager = new EmailManager(file);
                Map<String, EmailContentBean> emailContentManagers = emailManager.getEmails();
                for (ItemStackManager itemStackManager : setUpItems(emailContentManagers)) {
                    inventoryManager.addItem(itemStackManager);
                }
            }
        }.runTaskAsynchronously(UltiTools.getInstance());

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
                mail = new ItemStackManager(UltiTools.versionAdaptor.getEmailMaterial(false), lore, UltiTools.languageUtils.getString("email_item_description_from") + sender);
            } else {
                mail = new ItemStackManager(UltiTools.versionAdaptor.getEmailMaterial(true), lore, UltiTools.languageUtils.getString("email_item_description_from") + sender);
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
        list.add(ChatColor.GRAY + Utils.convertMillisecondsToRegularTime(Long.valueOf(emailContentBean.getUuid())));
        list.add(ChatColor.GOLD + UltiTools.languageUtils.getString("email_text_content_header"));
        if (inputString.contains("\\n")){
            String[] strings = inputString.split("[\\\\n]");
            list.addAll(Arrays.asList(strings));
            list.removeIf(each -> each.equals(""));
        }else {
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
        }
        if (emailContentBean.getItemStack() != null) {
            list.add(ChatColor.GOLD + UltiTools.languageUtils.getString("email_attachment_content_header"));
            if (emailContentBean.getItemStack().getItemMeta().getDisplayName() == null || emailContentBean.getItemStack().getItemMeta().getDisplayName().equals("")) {
                list.add(ChatColor.LIGHT_PURPLE + emailContentBean.getItemStack().getType().name() + " * " + emailContentBean.getItemStack().getAmount() + UltiTools.languageUtils.getString("ge"));
            } else {
                list.add(ChatColor.LIGHT_PURPLE + emailContentBean.getItemStack().getItemMeta().getDisplayName() + " * " + emailContentBean.getItemStack().getAmount() + UltiTools.languageUtils.getString("ge"));
            }
        }
        if (!emailContentBean.getRead()) {
            if (emailContentBean.getItemStack() != null) {
                list.add(ChatColor.RED + UltiTools.languageUtils.getString("email_click_to_claim"));
            } else {
                list.add(ChatColor.RED + UltiTools.languageUtils.getString("email_click_to_read"));
            }
        } else {
            list.add(ChatColor.AQUA + UltiTools.languageUtils.getString("email_read"));
        }
        list.add(ChatColor.DARK_GRAY + "ID:" + emailContentBean.getUuid());
        return list;
    }
}

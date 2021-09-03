package com.ultikits.ultitools.views;

import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.inventoryapi.ViewType;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class EmailToolsView {
    public static Inventory setUp(Player player) {
        InventoryManager inventoryManager = new InventoryManager(null, 36, UltiTools.languageUtils.getString("email_tools") + " - " + player.getName(), true);
        inventoryManager.presetPage(ViewType.PREVIOUS_QUIT_NEXT);
        inventoryManager.create();
        Inventory inv = inventoryManager.getInventory();

        ItemStack mailBoxItem = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta mailBoxIM = mailBoxItem.getItemMeta();
        mailBoxIM.setDisplayName(ChatColor.RESET + UltiTools.languageUtils.getString("email_open_mailbox"));
        mailBoxItem.setItemMeta(mailBoxIM);

        ItemStack delReadItem = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta delReadIM = delReadItem.getItemMeta();
        delReadIM.setDisplayName(ChatColor.RESET + UltiTools.languageUtils.getString("email_help_delread"));
        delReadItem.setItemMeta(delReadIM);

        ItemStack delAllItem = new ItemStack(Material.REDSTONE);
        ItemMeta delAllIM = delAllItem.getItemMeta();
        delAllIM.setDisplayName(ChatColor.RESET + UltiTools.languageUtils.getString("email_help_delhistory"));
        delAllItem.setItemMeta(delAllIM);

        inv.setItem(11,mailBoxItem);
        inv.setItem(13,delReadItem);
        inv.setItem(15,delAllItem);

        ViewManager.registerView(inventoryManager);
        return inv;
    }
}

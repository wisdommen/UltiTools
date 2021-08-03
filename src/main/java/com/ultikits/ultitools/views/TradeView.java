package com.ultikits.ultitools.views;

import com.ultikits.enums.Colors;
import com.ultikits.inventoryapi.InventoryManager;
import com.ultikits.inventoryapi.ViewManager;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.TradeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class TradeView {

    private TradeView(){
    }

    public static Inventory setUp(Player master) {
        String title = UltiTools.languageUtils.getString("trade_title");
        InventoryManager inventoryManager = new InventoryManager(null, 54, title, true);
        inventoryManager.create();
        ViewManager.registerView(inventoryManager);
        new BukkitRunnable() {
            @Override
            public void run() {
                int i = 4;
                while (i < 35) {
                    ItemStack itemStack = new ItemStack(UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.BLACK));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    Objects.requireNonNull(itemMeta).setDisplayName(" ");
                    itemStack.setItemMeta(itemMeta);
                    inventoryManager.setItem(i, itemStack);
                    i = i + 9;
                }
                i = 36;
                while (i < 45) {
                    ItemStack itemStack = new ItemStack(UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.BLACK));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    Objects.requireNonNull(itemMeta).setDisplayName(" ");
                    itemStack.setItemMeta(itemMeta);
                    inventoryManager.setItem(i, new ItemStack(itemStack));
                    i = i + 1;
                }
                for (int index : TradeUtils.getItemPlacementArea(TradeUtils.getOtherParty(master))) {
                    ItemStack itemStack = new ItemStack(UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.BROWN));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    Objects.requireNonNull(itemMeta).setDisplayName(" ");
                    itemStack.setItemMeta(itemMeta);
                    inventoryManager.setItem(index, new ItemStack(itemStack));
                }
                if (TradeUtils.isMoneyTradeAllowed()) {
                    ItemStack i2 = new ItemStack(Material.GOLD_INGOT);
                    ItemMeta im2 = i2.getItemMeta();
                    Objects.requireNonNull(im2).setDisplayName(ChatColor.GOLD + UltiTools.languageUtils.getString("trade_money"));
                    im2.setLore(TradeUtils.getMoneyLore(master));
                    i2.setItemMeta(im2);
                    inventoryManager.setItem(48, i2);
                } else {
                    ItemStack i2 = new ItemStack(UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.WHITE));
                    ItemMeta im2 = i2.getItemMeta();
                    Objects.requireNonNull(im2).setDisplayName(" ");
                    i2.setItemMeta(im2);
                    inventoryManager.setItem(48, i2);
                }
                if (TradeUtils.isExpTradeAllowed()) {
                    ItemStack i3 = new ItemStack(Material.EXPERIENCE_BOTTLE);
                    ItemMeta im3 = i3.getItemMeta();
                    Objects.requireNonNull(im3).setDisplayName(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_exp"));
                    im3.setLore(TradeUtils.getExpLore(master));
                    i3.setItemMeta(im3);
                    inventoryManager.setItem(50, i3);
                } else {
                    ItemStack i2 = new ItemStack(UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.WHITE));
                    ItemMeta im2 = i2.getItemMeta();
                    Objects.requireNonNull(im2).setDisplayName(" ");
                    i2.setItemMeta(im2);
                    inventoryManager.setItem(48, i2);
                }
                ItemStack i1 = new ItemStack(UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED));
                ItemStack i4 = new ItemStack(UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.GREEN));
                ItemStack i5 = new ItemStack(UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.WHITE));
                ItemMeta im1 = i1.getItemMeta();
                ItemMeta im4 = i4.getItemMeta();
                ItemMeta im5 = i5.getItemMeta();
                Objects.requireNonNull(im1).setDisplayName(ChatColor.RED + UltiTools.languageUtils.getString("trade_close"));
                Objects.requireNonNull(im4).setDisplayName(ChatColor.GREEN + UltiTools.languageUtils.getString("trade_confirm"));
                Objects.requireNonNull(im5).setDisplayName(" ");
                im4.setLore(TradeUtils.getConfirmLore(master, false));
                i1.setItemMeta(im1);
                i4.setItemMeta(im4);
                i5.setItemMeta(im5);
                inventoryManager.setItem(45, i1);
                inventoryManager.setItem(46, i5);
                inventoryManager.setItem(47, i5);
                inventoryManager.setItem(49, i5);
                inventoryManager.setItem(51, i5);
                inventoryManager.setItem(52, i5);
                inventoryManager.setItem(53, i4);
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
        return inventoryManager.getInventory();
    }
}

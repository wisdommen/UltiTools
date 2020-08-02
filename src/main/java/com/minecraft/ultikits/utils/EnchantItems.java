package com.minecraft.ultikits.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.minecraft.ultikits.utils.Utils.getRandomNumber;

public class EnchantItems {

    private EnchantItems(){}

    public static Enchantment getRandomEnchant() {
        int i = 0;
        Map<Integer, Enchantment> enchant = new HashMap<>();
        for (Enchantment enchantment : Enchantment.values()) {
            enchant.put(i, enchantment);
            i++;
        }
        return enchant.get(getRandomNumber(enchant.size()));
    }

    public static @NotNull ItemStack getRandomEnchantBook() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            ArrayList<String> lore = new ArrayList<>();
            EnchantmentStorageMeta esm = (EnchantmentStorageMeta) meta;
            Enchantment enchantment = getRandomEnchant();
            esm.addStoredEnchant(enchantment, 1 + (int) (Math.random() * ((enchantment.getMaxLevel() - 1) + 1)), true);
            meta.setLore(lore);
            book.setItemMeta(esm);
        }
        return book;
    }
}

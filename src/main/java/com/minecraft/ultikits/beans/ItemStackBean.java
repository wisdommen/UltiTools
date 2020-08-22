package com.minecraft.ultikits.beans;

import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemStackBean {

    private ArrayList<String> lore = new ArrayList<>();

    private final ItemStack item;
    private String displayName;
    private int position;
    private Inventory page;
    private AttributeModifier modifier;
    private List<String> in;
    private List<String> commands;
    Map<String, Integer> enchants = new HashMap<>();

    public ItemStackBean(ItemStack item) {
        this.item = item;
        this.displayName = item.getItemMeta().getDisplayName();
    }

    public ItemStackBean(ItemStack item, String displayName) {
        this.item = item;
        this.displayName = displayName;
    }

    public ItemStackBean(ItemStack item, ArrayList<String> lore) {
        this.item = item;
        this.lore = lore;
    }

    public ItemStackBean(ItemStack item, ArrayList<String> lore, String displayName) {
        this.item = item;
        this.lore = lore;
        this.displayName = displayName;
    }

    public ItemStackBean(ItemStack item, ArrayList<String> lore, String displayName, int position, List<String> commands) {
        this.item = item;
        this.lore = lore;
        this.displayName = displayName;
        this.position = position;
        this.commands = commands;
    }

    public ItemStackBean(ItemStack item, ArrayList<String> lore, String displayName, int position, Inventory page, List<String> commands) {
        this.item = item;
        this.lore = lore;
        this.displayName = displayName;
        this.position = position;
        this.page = page;
        this.commands = commands;
    }

    public void addLore(String lore) {
        this.lore.add(lore);
    }

    public void setUpItem() {
        ItemMeta itemMeta = this.item.getItemMeta();
        if (lore.size() != 0) {
            Objects.requireNonNull(itemMeta).setLore(lore);
        } else {
            lore = (ArrayList<String>) itemMeta.getLore();
        }
        if (displayName != null) {
            itemMeta.setDisplayName(ChatColor.AQUA + displayName);
        }
        item.setItemMeta(itemMeta);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPosition() {
        return position;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemStack getItem(int amount) {
        ItemStack newItem = item;
        newItem.setAmount(amount);
        return newItem;
    }

    public void setAmount(int number) {
        item.setAmount(number);
    }

    public void setPage(List<String> page) {
        this.in = page;
    }

    public List<String> getPageList() {
        return in;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public ArrayList<String> getLore() {
        return lore;
    }

    public Integer getAmount() {
        return this.item.getAmount();
    }

    public Map<String, Integer> getEnchantment() {
        if (!item.getEnchantments().isEmpty()) {
            for (Enchantment itemEnchantments : item.getEnchantments().keySet()) {
                enchants.put(itemEnchantments.getKey().toString().split(":")[1], item.getEnchantmentLevel(itemEnchantments));
            }
        }
        return enchants;
    }

    public double getDurability() {
        if (item.getItemMeta() != null && !item.getItemMeta().isUnbreakable()) {
            return ((Damageable) item.getItemMeta()).getDamage();
        }
        return -1;
    }

    public void setDurability(int durability) {
        if (item.getItemMeta() != null && !item.getItemMeta().isUnbreakable()) {
            ((Damageable) item.getItemMeta()).setDamage(durability);
        }
    }


    public Multimap<Attribute, AttributeModifier> getModifier() {
        if (item.getItemMeta() != null) {
            ItemMeta itemMeta= item.getItemMeta();
            return itemMeta.getAttributeModifiers();
        }
        return null;
    }

    public void setModifier(AttributeModifier modifier) {
        this.modifier = modifier;
    }
}

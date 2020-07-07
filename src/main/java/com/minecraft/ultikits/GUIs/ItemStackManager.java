package com.minecraft.ultikits.GUIs;

import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemStackManager {

    private ArrayList<String> lore = new ArrayList<>();

    private ItemStack item;
    private String displayName;
    private int position;
    private Inventory page;
    private List<String> in;
    private List<String> commands;

    public ItemStackManager(ItemStack item) {
        this.item = item;
    }

    public ItemStackManager(ItemStack item, String displayName) {
        this.item = item;
        this.displayName = displayName;
    }

    public ItemStackManager(ItemStack item, ArrayList<String> lore) {
        this.item = item;
        this.lore = lore;
    }

    public ItemStackManager(ItemStack item, ArrayList<String> lore, String displayName) {
        this.item = item;
        this.lore = lore;
        this.displayName = displayName;
    }

    public ItemStackManager(ItemStack item, ArrayList<String> lore, String displayName, int position, List<String> commands) {
        this.item = item;
        this.lore = lore;
        this.displayName = displayName;
        this.position = position;
        this.commands = commands;
    }

    public ItemStackManager(ItemStack item, ArrayList<String> lore, String displayName, int position, Inventory page, List<String> commands) {
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
        }
        if (displayName != null) {
            itemMeta.setDisplayName(ChatColor.AQUA + displayName);
        }
        item.setItemMeta(itemMeta);
    }

    public ItemStack setItem() {
        ItemMeta itemMeta = this.item.getItemMeta();
        if (lore.size() != 0) {
            Objects.requireNonNull(itemMeta).setLore(lore);
        }
        if (displayName != null) {
            itemMeta.setDisplayName(ChatColor.AQUA + displayName);
        }
        item.setItemMeta(itemMeta);
        return item;
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
}

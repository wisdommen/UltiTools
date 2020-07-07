package com.minecraft.ultikits.GUIs;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {

    private Inventory inventory;
    private final InventoryHolder owner;
    private final int slots;
    private String title;
    ItemStack[] contains;

    public InventoryManager(InventoryHolder owner, int slots, String title) {
        this.owner = owner;
        this.slots = slots;
        this.title = title;
    }

    public void create() {
        inventory = Bukkit.createInventory(owner, slots, title);
    }

    public void setItem(int position, ItemStack item) {
        if (position < 0) return;
        if (inventory.getItem(position) == null) {
            inventory.setItem(position, item);
        }
    }

    private void forceSetItem(int position, ItemStack item) {
        inventory.setItem(position, item);
        contains = inventory.getContents();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getTitle() {
        return title;
    }

    public void changeTitle(String title) {
        this.title = title;
        create();
        inventory.setContents(contains);
    }
}

package com.minecraft.ultikits.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {

    private Inventory inventory;
    private final InventoryHolder owner;
    private final int slots;
    private String title;
    private final boolean isPageButtonEnabled;
    ItemStack[] contains;

    public InventoryManager(InventoryHolder owner, int slots, String title) {
        this.owner = owner;
        this.slots = slots;
        this.title = title;
        this.isPageButtonEnabled = false;
    }

    public InventoryManager(InventoryHolder owner, int slots, String title, boolean isPageButtonEnabled) {
        this.owner = owner;
        this.slots = slots;
        this.title = title;
        this.isPageButtonEnabled = isPageButtonEnabled;
    }

    public void create() {
        inventory = Bukkit.createInventory(owner, slots, title);
        if (isPageButtonEnabled) {
            setPageButtons();
        }
    }

    public void setItem(int position, ItemStack item) {
        if (position >= inventory.getSize()) {
            return;
        }
        if (position < 0) {
            return;
        }
        if (inventory.getItem(position) == null) {
            inventory.setItem(position, item);
        } else {
            setItem(position + 1, item);
        }
    }

    public void forceSetItem(int position, ItemStack item) throws IndexOutOfBoundsException {
        inventory.setItem(position, item);
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

    public int getSize() {
        if (isPageButtonEnabled) {
            return inventory.getSize() - 9;
        }
        return inventory.getSize();
    }

    private void setPageButtons() {
        for (int i = getSize(); i < inventory.getSize(); i++) {
            this.setItem(i, new ItemStackManager(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "").getItem());
        }
        try {
            this.forceSetItem(getSize() + 3, new ItemStackManager(new ItemStack(Material.RED_STAINED_GLASS_PANE), "上一页").getItem());
            this.forceSetItem(getSize() + 4, new ItemStackManager(new ItemStack(Material.EMERALD), "返回").getItem());
            this.forceSetItem(getSize() + 5, new ItemStackManager(new ItemStack(Material.RED_STAINED_GLASS_PANE), "下一页").getItem());
        } catch (IndexOutOfBoundsException ignored) {
        }
    }
}

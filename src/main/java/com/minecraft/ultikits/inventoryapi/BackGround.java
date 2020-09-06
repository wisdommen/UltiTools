package com.minecraft.ultikits.inventoryapi;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum BackGround {
    RED(new ItemStack(Material.RED_STAINED_GLASS_PANE)),
    YELLOW(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE)),
    BLACK(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
    BLUE(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)),
    GREEN(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)),
    PINK(new ItemStack(Material.PINK_STAINED_GLASS_PANE)),
    WHITE(new ItemStack(Material.WHITE_STAINED_GLASS_PANE)),
    AQUA(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE)),
    GRAY(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

    private final ItemStack itemStack;

    BackGround(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public ItemStack getItem(){
        return itemStack;
    }
}

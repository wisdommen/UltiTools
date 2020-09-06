package com.minecraft.ultikits.inventoryapi;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Buttons {
    PREVIOUS("上一页", new ItemStack(Material.RED_STAINED_GLASS_PANE)),
    NEXT("下一页", new ItemStack(Material.RED_STAINED_GLASS_PANE)),
    BACK("返回", new ItemStack(Material.OAK_SIGN)),
    QUIT("退出", new ItemStack(Material.ENDER_EYE)),
    OK("确认", new ItemStack(Material.GREEN_STAINED_GLASS_PANE)),
    CANCEL("取消", new ItemStack(Material.RED_STAINED_GLASS_PANE));

    String name;
    ItemStack itemStack;

    Buttons(String name, ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack;
    }

    public String getName(){
        return name;
    }

    public ItemStack getItemStack(){
        return itemStack;
    }
}

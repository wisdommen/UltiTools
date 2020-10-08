package com.minecraft.ultikits.inventoryapi;

import com.minecraft.ultikits.enums.Colors;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.inventory.ItemStack;

public enum Buttons {
    PREVIOUS("上一页", UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED)),
    NEXT("下一页", UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED)),
    BACK("返回", UltiTools.versionAdaptor.getSign()),
    QUIT("退出", UltiTools.versionAdaptor.getEndEye()),
    OK("确认", UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.GREEN)),
    CANCEL("取消", UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED));

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

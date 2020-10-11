package com.minecraft.ultikits.inventoryapi;

import com.minecraft.ultikits.enums.Colors;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.inventory.ItemStack;

/**
 * The enum Buttons.
 */
public enum Buttons {
    /**
     * Previous buttons.
     */
    PREVIOUS("上一页", UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED)),
    /**
     * Next buttons.
     */
    NEXT("下一页", UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED)),
    /**
     * Back buttons.
     */
    BACK("返回", UltiTools.versionAdaptor.getSign()),
    /**
     * Quit buttons.
     */
    QUIT("退出", UltiTools.versionAdaptor.getEndEye()),
    /**
     * Ok buttons.
     */
    OK("确认", UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.GREEN)),
    /**
     * Cancel buttons.
     */
    CANCEL("取消", UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED));

    /**
     * The Name.
     */
    String name;
    /**
     * The Item stack.
     */
    ItemStack itemStack;

    Buttons(String name, ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack;
    }

    /**
     * Get name string.
     *
     * @return Button 's name 按钮名称
     */
    public String getName(){
        return name;
    }

    /**
     * Get item stack item stack.
     *
     * @return Button 's material 按钮材质
     */
    public ItemStack getItemStack(){
        return itemStack;
    }
}

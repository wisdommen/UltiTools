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
    PREVIOUS(UltiTools.languageUtils.getWords("button_previous"), UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED)),
    /**
     * Next buttons.
     */
    NEXT(UltiTools.languageUtils.getWords("button_next"), UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED)),
    /**
     * Back buttons.
     */
    BACK(UltiTools.languageUtils.getWords("button_back"), UltiTools.versionAdaptor.getSign()),
    /**
     * Quit buttons.
     */
    QUIT(UltiTools.languageUtils.getWords("button_quit"), UltiTools.versionAdaptor.getEndEye()),
    /**
     * Ok buttons.
     */
    OK(UltiTools.languageUtils.getWords("button_ok"), UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.GREEN)),
    /**
     * Cancel buttons.
     */
    CANCEL(UltiTools.languageUtils.getWords("button_cancel"), UltiTools.versionAdaptor.getColoredPlaneGlass(Colors.RED));

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

package com.minecraft.ultikits.inventoryapi;

import com.minecraft.ultikits.enums.Colors;
import com.minecraft.ultikits.manager.ItemStackManager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * The interface Inventory manager api.
 */
public interface InventoryManagerAPI {
    /**
     * Create the inventory.
     * 创建inventory对象。
     * <p>
     * You should only invoke this method after done the inventory settings. (Such as Setting title, setting size)
     * You should invoke this method before you do any Bukkit inventory operation. (Such as Adding items, clearing content)
     * 你必须在完成所有对GUI的设置的后才可以调用这个方法。(比如设置名字，设置容量)
     * 你必须在对GUI进行操作之前调用这个方法。（比如添加物品，清空物品)
     */
    void create();

    /**
     * Pre-set page.
     * 预设页面。
     * <p>
     * You can use pre-set page to create a view.
     * 你可以使用预设界面来创建一个GUI。
     *
     * @param type the pre-set enum type 预设界面Enum
     */
    void presetPage(ViewType type);

    /**
     * Sets item.
     * 设置物品
     * <p>
     * You can set an item with a position in the inventory. When the position has already had an item,
     * it will set to next slot. If it is the last slot in the inventory and it has already had an item,
     * and the autoAddPage is true, it will generate the next page.
     * 你可以在GUI中的一个位置设置一个物品。当此位置存在物品时，要设置的物品会自动移到下个格子。如果要设置的格子是GUI
     * 的最后一个格子且已经有了物品并且autoAddPage设置为true，则会自动生成下一页。
     *
     * @param position the position  需要生成的位置
     * @param item     the item  需要生成的物品
     */
    void setItem(int position, ItemStack item);

    /**
     * Force set item.
     * 强制设置物品
     * <p>
     * You can force set an item in the inventory. Unlike setItem method, it will not care about whether it
     * has item at that position or not. This method will not generate next page when the position is bigger
     * than inventory size, so it will give an IndexOutOfBoundsException when trying to set the item larger
     * than inventory size.
     * 你可以强制在GUI的某个位置设置物品。但是和addItem不同的是，这个方法不会考虑位置是否有物品。并且当位置大于GUI容量时，
     * 这个方法不会生成下一页，所以当你尝试将一个物品设置到大于GUI容量的位置时会报出IndexOutOfBoundsException的错误。
     *
     * @param position the position
     * @param item     the item
     * @throws IndexOutOfBoundsException the index out of bounds exception
     */
    void forceSetItem(int position, ItemStack item) throws IndexOutOfBoundsException;

    /**
     * Add item.
     *
     * @param item the item
     */
    void addItem(ItemStack item);

    /**
     * Add item.
     *
     * @param itemStackManager the item stack manager
     */
    void addItem(ItemStackManager itemStackManager);

    /**
     * Gets inventory.
     *
     * @return the inventory
     */
    Inventory getInventory();

    /**
     * Gets title.
     *
     * @return the title
     */
    String getTitle();

    /**
     * Gets group title.
     *
     * @return the group title
     */
    String getGroupTitle();

    /**
     * Gets size.
     *
     * @return the size
     */
    int getSize();

    /**
     * Gets page number.
     *
     * @return the page number
     */
    int getPageNumber();

    /**
     * Is page button enabled boolean.
     *
     * @return the boolean
     */
    boolean isPageButtonEnabled();

    /**
     * Is last line disabled boolean.
     *
     * @return the boolean
     */
    boolean isLastLineDisabled();

    /**
     * Clear view.
     */
    void clearView();

    /**
     * Sets background color.
     *
     * @param backgroundColor the background color
     */
    void setBackgroundColor(Colors backgroundColor);

    /**
     * Clear back ground.
     */
    void clearBackGround();

    /**
     * Is back ground boolean.
     *
     * @param item the item
     * @return the boolean
     */
    boolean isBackGround(@Nullable ItemStack item);

    /**
     * Sets middle button.
     *
     * @param middleButton the middle button
     */
    void setMiddleButton(Buttons middleButton);

    /**
     * Sets page button enabled.
     *
     * @param isPageButtonEnabled the is page button enabled
     */
    void setPageButtonEnabled(boolean isPageButtonEnabled);
}

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
     * 向界面中添加一个物品
     * <p>
     * You can add item into a view by using this method. Item can stack automatically if they have a same
     * property.
     * 你可以使用这个方法向一个界面中添加物品。物品会自动堆叠如果物品属性相同。
     *
     * @param item the item
     */
    void addItem(ItemStack item);

    /**
     * Add item.
     * 向界面中添加一个物品
     * <p>
     * See addItem(ItemStack item)
     * 详见 addItem(ItemStack item)
     *
     * @param itemStackManager the item stack manager
     */
    void addItem(ItemStackManager itemStackManager);

    /**
     * Gets inventory.
     * 获取Inventory对象
     * <p>
     * You can get the inventory of this InventoryManager. You must only call this method AFTER call create(),
     * otherwise you will get null.
     * 你可以通过这个方法获取InventoryManager的Inventory对象。你必须先调用create()方法，否则会返回null！
     *
     * @return the inventory
     */
    Inventory getInventory();

    /**
     * Gets title.
     * 获取标题
     * <p>
     * Get the tile of this inventory
     * 获取这个inventory的标题
     *
     * @return the title
     */
    String getTitle();

    /**
     * Gets group title.
     * 获取这个inventoryManager的组名
     * <p>
     * Get the group name of this InventoryManager object if it has a group name.
     * Group name often appears when there are pages hook to the view.
     * 获取这个InventoryManager对象的组名。
     * 组名一般出现在此界面拥有多个页面时。
     *
     * @return the group title
     */
    String getGroupTitle();

    /**
     * Gets size of the Inventory.
     * 获取Inventory的大小
     * <p>
     * You can only invoke this method after call create()
     * 你必须先调用create()方法。
     *
     * @return the size
     */
    int getSize();

    /**
     * Gets page number.
     * 获取此页面的页码
     *
     * @return the page number
     */
    int getPageNumber();

    /**
     * Is page button enabled boolean.
     * 获取是否开启了页面按钮
     *
     * @return the boolean
     */
    boolean isPageButtonEnabled();

    /**
     * Is last line disabled boolean.
     * 获取最后一行是否不允许设置物品
     *
     * @return the boolean
     */
    boolean isLastLineDisabled();

    /**
     * Clear view.
     * 清空页面
     * <p>
     * Clear all the item in the view. However if last line disabled, the last line will not be cleared.
     * 清除页面上的所有物品。但是如果最后一行被关闭了，则不会清除最后一行的物品。
     */
    void clearView();

    /**
     * Sets background color.
     * 设置背景颜色。
     * <p>
     * Set the background color of the Inventory. It will not rewrite the item that is already exists.
     * 设置背景颜色，不会覆盖物品。
     *
     * @param backgroundColor the background color
     */
    void setBackgroundColor(Colors backgroundColor);

    /**
     * Clear back ground.
     * 删除所有背景颜色物品
     */
    void clearBackGround();

    /**
     * Is back ground boolean.
     * 检查一个物品是否为背景颜色物品。
     *
     * @param item the item
     * @return the boolean
     */
    boolean isBackGround(@Nullable ItemStack item);

    /**
     * Sets middle button.
     * 设置中间按钮
     *
     * @param middleButton the middle button
     */
    void setMiddleButton(Buttons middleButton);

    /**
     * Sets page button enabled.
     * 设置是否开启翻页按钮
     *
     * @param isPageButtonEnabled the is page button enabled
     */
    void setPageButtonEnabled(boolean isPageButtonEnabled);
}

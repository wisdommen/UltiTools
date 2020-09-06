package com.minecraft.ultikits.inventoryapi;

import com.minecraft.ultikits.manager.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class InventoryManager {

    private Inventory inventory;
    private final InventoryHolder owner;
    private final int slots;
    private int pageNumber = 0;
    private String title;
    private final String groupTitle;
    private boolean isPageButtonEnabled = false;
    private boolean isOkCancelEnabled = false;
    private boolean isLastLineDisabled = false;
    private boolean autoAddPage = true;
    private BackGround backGroundColor;
    private Buttons middleButton = Buttons.QUIT;

    public InventoryManager(InventoryHolder owner, int slots, String title) {
        this.owner = owner;
        this.slots = slots;
        this.groupTitle = title;
        create();
    }

    private InventoryManager(InventoryHolder owner, int slots, String title, int pageNumber) {
        this.owner = owner;
        this.slots = slots;
        this.pageNumber = pageNumber;
        this.title = title + String.format(" 第%d页", pageNumber);
        this.groupTitle = title;
        this.isPageButtonEnabled = true;
        this.isLastLineDisabled = true;
        create();
    }

    public InventoryManager(InventoryHolder owner, int slots, String title, boolean block) {
        this.owner = owner;
        this.slots = slots;
        this.groupTitle = title;
        if (!block) {
            create();
        }
    }

    public void create() {
        if (title == null){
            title = groupTitle;
        }
        inventory = Bukkit.createInventory(owner, slots, title);
        if (isPageButtonEnabled) {
            setPageButtons();
        }else if (isOkCancelEnabled){
            setOkCancelButtons();
        }
        if (this.backGroundColor!=null) {
            setBackgroundColor(this.backGroundColor);
        }
    }

    public void presetPage(ViewType type){
        this.isLastLineDisabled = type.isLastLineEnabled();
        if (isLastLineDisabled) {
            this.isPageButtonEnabled = type.isPageButtonEnabled();
            this.isOkCancelEnabled = type.isOkCancelEnabled();
            if (isPageButtonEnabled) {
                this.autoAddPage = type.isAutoAddPage();
                this.pageNumber = type.getPageNumber();
                this.middleButton = type.getMiddleButton();
            }
        }
        this.backGroundColor = type.getBackGroundColor();
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
        } else if (isBackGround(inventory.getItem(position))) {
            forceSetItem(position, item);
        } else {
            setItem(position + 1, item);
        }
    }

    public void forceSetItem(int position, ItemStack item) throws IndexOutOfBoundsException {
        inventory.setItem(position, item);
    }

    public void addItem(ItemStack item) {
        clearBackGround();
        Map<Integer, ItemStack> items = inventory.addItem(item);
        if (items.values().size() == 0) {
            setBackgroundColor(this.backGroundColor);
            return;
        }
        if (!isPageButtonEnabled) {
            return;
        }
        InventoryManager nextPage = ViewManager.getViewByName(getGroupTitle() + " 第" + (getPageNumber() + 1) + "页");
        if (nextPage == null && autoAddPage) {
            InventoryManager newPage = new InventoryManager(this.owner, this.slots, getGroupTitle(), getPageNumber() + 1);
            if (this.backGroundColor != null) {
                newPage.setBackgroundColor(this.backGroundColor);
            }
            ViewManager.registerView(newPage);
            newPage.addItem(item);
        } else if (nextPage != null) {
            nextPage.addItem(item);
        }
        setBackgroundColor(this.backGroundColor);
    }

    public void addItem(ItemStackManager itemStackManager) {
        addItem(itemStackManager.getItem());
    }


    public Inventory getInventory() {
        return inventory;
    }

    public String getTitle() {
        return title;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public int getSize() {
        if (isLastLineDisabled) {
            return inventory.getSize() - 9;
        }
        return inventory.getSize();
    }

    private void setPageButtons() {
        this.title = groupTitle + String.format(" 第%d页", pageNumber);
        fillLastLine();
        try {
            ItemStackManager back = new ItemStackManager(Buttons.PREVIOUS.getItemStack(), Buttons.PREVIOUS.getName());
            ItemStackManager quit = new ItemStackManager(middleButton.getItemStack(), middleButton.getName());
            ItemStackManager next = new ItemStackManager(Buttons.NEXT.getItemStack(), Buttons.NEXT.getName());
            this.forceSetItem(getSize() + 3, back.getItem());
            this.forceSetItem(getSize() + 4, quit.getItem());
            this.forceSetItem(getSize() + 5, next.getItem());
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    private void setOkCancelButtons(){
        fillLastLine();
        try {
            ItemStackManager ok = new ItemStackManager(Buttons.OK.getItemStack(), Buttons.OK.getName());
            ItemStackManager cancel = new ItemStackManager(Buttons.CANCEL.getItemStack(), Buttons.CANCEL.getName());
            this.forceSetItem(getSize() + 3, ok.getItem());
            this.forceSetItem(getSize() + 5, cancel.getItem());
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    private void fillLastLine(){
        ItemStackManager blank = new ItemStackManager(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "");
        for (int i = getSize(); i < inventory.getSize(); i++) {
            this.setItem(i, blank.getItem());
        }
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public boolean isPageButtonEnabled() {
        return isPageButtonEnabled;
    }

    public boolean isLastLineDisabled() {
        return isLastLineDisabled;
    }

    public void clearView() {
        for (int i = 0; i < getSize(); i++) {
            forceSetItem(i, null);
        }
    }

    public void setBackgroundColor(BackGround backgroundColor) {
        ItemStackManager itemStackManager = new ItemStackManager(backgroundColor.getItem(), "");
        while (inventory.firstEmpty() > -1) {
            setItem(inventory.firstEmpty(), itemStackManager.getItem());
        }
        this.backGroundColor = backgroundColor;
    }

    public void clearBackGround() {
        for (int i = 0; i < getSize(); i++) {
            if (isBackGround(inventory.getItem(i))) {
                forceSetItem(i, null);
            }
        }
    }

    public boolean isBackGround(@Nullable ItemStack item) {
        if (item == null) return false;
        return item.getType().equals(this.backGroundColor.getItem().getType()) && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("");
    }

    public void setMiddleButton(Buttons middleButton) {
        this.middleButton = middleButton;
    }

    public void setPageButtonEnabled(boolean isPageButtonEnabled){
        this.isPageButtonEnabled = isPageButtonEnabled;
    }
}

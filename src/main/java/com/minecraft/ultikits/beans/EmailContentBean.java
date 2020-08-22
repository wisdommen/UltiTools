package com.minecraft.ultikits.beans;

import org.bukkit.inventory.ItemStack;

public class EmailContentBean {
    private String message;
    private String uuid;
    private String sender;
    private ItemStack itemStack;
    private Boolean isItemClaimed;
    private Boolean isRead;

    public EmailContentBean(String uuid, String sender, String message, ItemStack itemStack, Boolean isRead, Boolean isItemClaimed) {
        this.message = message;
        this.uuid = uuid;
        this.sender = sender;
        this.itemStack = itemStack;
        this.isItemClaimed = isItemClaimed;
        this.isRead = isRead;
    }

    public EmailContentBean(String uuid, String sender, String message, Boolean isRead) {
        this.message = message;
        this.uuid = uuid;
        this.sender = sender;
        this.itemStack = null;
        this.isRead = isRead;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Boolean getItemClaimed() {
        return isItemClaimed;
    }

    public void setItemClaimed(Boolean itemClaimed) {
        isItemClaimed = itemClaimed;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}

package com.minecraft.ultikits.email;

import com.minecraft.ultikits.GUIs.ItemStackManager;

public class EmailContentManager {
    private String message;
    private String uuid;
    private String sender;
    private ItemStackManager itemStackManager;
    private Boolean isItemClaimed;
    private Boolean isRead;

    public EmailContentManager(String uuid, String sender, String message, ItemStackManager itemStackManager, Boolean isRead, Boolean isItemClaimed){
        this.message = message;
        this.uuid = uuid;
        this.sender = sender;
        this.itemStackManager = itemStackManager;
        this.isItemClaimed = isItemClaimed;
        this.isRead = isRead;
    }

    public EmailContentManager(String uuid, String sender, String message, Boolean isRead){
        this.message = message;
        this.uuid = uuid;
        this.sender = sender;
        this.itemStackManager = null;
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

    public ItemStackManager getItemStackManager() {
        return itemStackManager;
    }

    public void setItemStackManager(ItemStackManager itemStackManager) {
        this.itemStackManager = itemStackManager;
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

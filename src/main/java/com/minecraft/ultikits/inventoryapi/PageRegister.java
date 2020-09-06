package com.minecraft.ultikits.inventoryapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class PageRegister {

    private final JavaPlugin plugin;
    private static final Map<String, PagesListener> inventoryListenerMap = new HashMap<>();

    public PageRegister(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(InventoryManager inventoryManager, PagesListener listener) {
        if (inventoryListenerMap.get(inventoryManager.getGroupTitle())!=null){
            return;
        }
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        inventoryListenerMap.put(inventoryManager.getGroupTitle(), listener);
    }

    public static PagesListener getPagesListenerByGroupName(String name){
        return inventoryListenerMap.get(name);

    }
}

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
        if (inventoryListenerMap.get(inventoryManager.getTitle())!=null){
            return;
        }
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        inventoryListenerMap.put(inventoryManager.getTitle(), listener);
    }

    public static PagesListener getPagesListenerByName(String name){
        return inventoryListenerMap.get(name);
    }
}

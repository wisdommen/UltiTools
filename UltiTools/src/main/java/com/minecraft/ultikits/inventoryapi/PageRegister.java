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

    /**
     * Register a view and a listener which combine with the view
     * 注册一个页面以及此页面的监听器
     *
     * @param inventoryManager The inventoryManager
     * @param listener A listener
     */
    public void register(InventoryManager inventoryManager, PagesListener listener) {
        if (inventoryListenerMap.get(inventoryManager.getGroupTitle())!=null){
            return;
        }
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        inventoryListenerMap.put(inventoryManager.getGroupTitle(), listener);
    }

    /**
     * Get a listener by its group name
     * 使用组名获取一个页面的监听器
     *
     * You can get the same listener with group name when the view have multiple page.
     * You don't need to register every page with a same listner
     * 你可以使用组名获取一个相同的监听器。
     * 你不必为多个拥有相同组名的页面多次注册。
     *
     * @param name the group name
     * @return A listener
     */
    public static PagesListener getPagesListenerByGroupName(String name){
        return inventoryListenerMap.get(name);
    }
}

package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @author Shpries
 */
@EventListener
public class CustomGUIProtectListener implements Listener {
    private boolean isGUIOpen = false;
    private final List<String> titleList = new ArrayList();
    YamlConfiguration config = ConfigController.getConfig("customgui");
    private final Set<String> GUINames = config.getConfigurationSection("guis").getKeys(false);


    public CustomGUIProtectListener() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(String GUIName : GUINames) {
                    titleList.add(config.getString("guis." + GUIName + ".title"));
                }
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        for(String title : titleList) {
            if(e.getView().getTitle().contains(title)) {
                isGUIOpen = true;
                break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        for(String title : titleList) {
            if(e.getView().getTitle().contains(title)) {
                isGUIOpen = false;
                break;
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        for(String title : titleList) {
            if (e.getView().getTitle().contains(title)) {
                if(isGUIOpen) {
                    e.setCancelled(true);
                }
            }
        }
    }
}

package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.enums.ConfigsEnum;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.Objects;
import java.util.Set;


public class RightClickListener implements Listener {
    File guiFile = new File(ConfigsEnum.CUSTOMERGUI.toString());
    YamlConfiguration guiConfig = YamlConfiguration.loadConfiguration(guiFile);
    ConfigurationSection guis = guiConfig.getConfigurationSection("guis");
    Set<String> guiList = guis.getKeys(false);

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        for(String guiName : guiList) {
            String lore = guiConfig.getString("guis." + guiName + ".bind-lore");
            if(lore == null || !(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                return;
            }
            try {
                if (e.getItem().getItemMeta().getLore().contains(lore)) {
                    Objects.requireNonNull(Bukkit.getPlayer(e.getPlayer().getName())).performCommand("ultitools " + guiConfig.getString("guis." + guiName + ".command"));
                }
            } catch (NullPointerException exception) {
                return;
            }
        }
    }
}

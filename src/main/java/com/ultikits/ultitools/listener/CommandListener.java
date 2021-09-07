package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.enums.ConfigsEnum;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandListener implements Listener {
    File commandaliasFile = new File(ConfigsEnum.COMMANDALIAS.toString());
    YamlConfiguration commandaliasConfig = YamlConfiguration.loadConfiguration(commandaliasFile);
    Set<String> commands = commandaliasConfig.getKeys(false);

    @EventHandler
    public void onCommandSend(PlayerCommandPreprocessEvent e) {
        for(String command : commands) {
            List<String> alias = commandaliasConfig.getStringList(command);
            for(String alia : alias) {
                if(e.getMessage().equals("/" + alia)) {
                    Bukkit.getServer().getPlayer(e.getPlayer().getName()).performCommand(command);
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    public List<String> getCommandAliasList() {
        List<String> commandAliasList = new ArrayList<>();
        for(String command:commands) {
            List<String> alias = commandaliasConfig.getStringList(command);
            for(String alia : alias) {
                commandAliasList.add(alia);
            }
        }
        return commandAliasList;
    }
}

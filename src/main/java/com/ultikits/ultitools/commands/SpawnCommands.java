package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.ultikits.utils.MessagesUtils.info;

public class SpawnCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        switch (command.getName()){
            case "spawn":
                if (strings.length == 0){
                    player.teleport(player.getLocation().getWorld().getSpawnLocation());
                    return true;
                }
            case "setspawn":
                if (strings.length == 0 && player.hasPermission("ultikits.tools.admin")){
                    Location location = player.getLocation();
                    World world = location.getWorld();
                    if (world == null){
                        return false;
                    }
                    world.setSpawnLocation(location);
                    player.sendMessage(info(UltiTools.languageUtils.getString("spawn_new_spawn_set")));
                    return true;
                }
            default:
                return false;
        }
    }
}

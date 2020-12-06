package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (strings.length == 0){
            player.teleport(player.getLocation().getWorld().getSpawnLocation());
            return true;
        }
        return false;
    }
}

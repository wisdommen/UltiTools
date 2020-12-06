package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.listener.BackListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BackCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        Location location = BackListener.getPlayerLastDeathLocation(player);
        if (location != null){
            player.teleport(location);
        }else {
            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("back_havent_dead")));
        }
        return true;
    }
}

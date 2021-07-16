package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.listener.TeleportListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpbackCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        World world = player.getWorld();
        Location location = TeleportListener.getPlayerFinalTeleportLocation(player);
        if (!(location == null)) {
            world.getChunkAt(location).load();
            player.teleport(location);
            player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpback_success")));
        } else {
            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpback_location_not_found")));
        }
        return false;
    }
}

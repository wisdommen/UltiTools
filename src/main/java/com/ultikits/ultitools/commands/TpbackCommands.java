package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.listener.TeleportListener;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CmdExecutor(function = "tp-back", permission = "ultikits.tools.command.tpback", description = "tpback_function", alias = "tpback")
public class TpbackCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        World world = player.getWorld();
        Location location = TeleportListener.getPlayerFinalTeleportLocation(player);
        Location newLocation = new Location(location.getWorld(), location.getX(),location.getY()+2,location.getZ());
        if (!(location == null)) {
            newLocation.getWorld().getChunkAt(location).load();
            player.teleport(location);
            player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpback_success")));
        } else {
            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpback_location_not_found")));
        }
        return false;
    }
}

package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.SilentOpenUtils;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SilentOpenCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!command.getName().equalsIgnoreCase("silent")) return false;
        if (strings.length > 0) return false;
        if (!(player.isOp() || player.hasPermission("ultikits.tools.silent"))) {
            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("no_permission")));
            return true;
        }
        SilentOpenUtils.getPlayers().add(player);
        player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("click_chest_to_open_silently")));
        return true;
    }
}

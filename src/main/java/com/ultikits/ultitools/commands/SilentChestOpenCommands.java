package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.minecraft.Ultilevel.utils.Messages.info;
import static com.ultikits.utils.MessagesUtils.warning;

public class SilentChestOpenCommands extends AbstractPlayerCommandExecutor {
    public static final List<String> scoPlayer = new ArrayList<>();

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!player.isOp() || !player.hasPermission("ultikits.tools.command.sco")) {
            player.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
            return true;
        }
        if (strings.length != 0) return false;
        if (scoPlayer.contains(player.getName())) {
            scoPlayer.remove(player.getName());
            player.sendMessage(info(UltiTools.languageUtils.getString("sco_enabled")));
        } else {
            scoPlayer.add(player.getName());
            player.sendMessage(info(UltiTools.languageUtils.getString("sco_disabled")));
        }
        return true;
    }
}

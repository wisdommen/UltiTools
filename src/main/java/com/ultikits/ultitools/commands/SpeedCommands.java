package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static com.ultikits.utils.MessagesUtils.warning;

public class SpeedCommands extends AbstractTabExecutor {
    List<String> speeds = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (player.hasPermission("ultikits.tools.command.speed")) {
            if (!speeds.contains(strings[0])) {
                return false;
            }
            player.setWalkSpeed(Float.parseFloat(strings[0]) / 10);
            player.setFlySpeed(Float.parseFloat(strings[0]) / 10);
            player.sendMessage(ChatColor.YELLOW + String.format(UltiTools.languageUtils.getString("speed_set"), strings[0]));
            return true;
        }
        player.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
        return true;
    }

    @Nullable
    @Override
    protected List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!player.hasPermission("ultikits.tools.commands.speed")) {
            return null;
        }
        if (strings.length == 1) {
            return speeds;
        }
        return null;
    }
}

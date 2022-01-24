package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.ultikits.utils.MessagesUtils.warning;

public class HealCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (player.hasPermission("ultikits.tools.command.heal")) {
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("heal_player"));
            return true;
        }
        player.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
        return true;
    }
}

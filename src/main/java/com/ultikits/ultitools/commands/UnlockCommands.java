package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.ChestLockUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author wisdomme,qianmo
 *
 * Code refactoring by qianmo
 */

public class UnlockCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if ("unlock".equalsIgnoreCase(command.getName())) {
            ChestLockUtils.cleanMode(player);
            ChestLockUtils.getInUnlockMode().add(player.getName());
            player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("unlock_click_to_unlock"));
            return true;
        }
        return false;
    }
}

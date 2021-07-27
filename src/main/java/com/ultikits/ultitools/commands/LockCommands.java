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

public class LockCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if ("lock".equalsIgnoreCase(command.getName())) {
            ChestLockUtils.cleanMode(player);
            ChestLockUtils.getInLockMode().add(player.getName());
            player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("lock_click_to_lock"));
            return true;
        }
        return false;
    }
}

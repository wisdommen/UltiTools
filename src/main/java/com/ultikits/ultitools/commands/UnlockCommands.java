package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.services.ChestLockService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author wisdomme,qianmo
 *
 * Code refactoring by qianmo
 */

@CmdExecutor(function = "chest-locker", permission = "ultikits.tools.chest.unlock", description = "unlock_chest_function", alias = "unlock,ul")
public class UnlockCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if ("unlock".equalsIgnoreCase(command.getName())) {
            ChestLockService.cleanMode(player);
            ChestLockService.getInUnlockMode().add(player.getName());
            player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("unlock_click_to_unlock"));
            return true;
        }
        return false;
    }
}

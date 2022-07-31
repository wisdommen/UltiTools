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

@CmdExecutor(function = "lock", permission = "ultikits.tools.back", description = "lock_chest_function", alias = "lock,l")
public class LockCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if ("lock".equalsIgnoreCase(command.getName())) {
            if (strings.length == 0) {
                ChestLockService.cleanMode(player);
                ChestLockService.getInLockMode().add(player.getName());
                player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("lock_click_to_lock"));
                return true;
            }
            if ("add".equalsIgnoreCase(strings[0])) {
                ChestLockService.cleanMode(player);
                ChestLockService.getInAddMode().put(player.getName(), strings [1]);
                player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("chest_click_add_owner"));
                return true;
            }
            if("remove".equalsIgnoreCase(strings[0])) {
                ChestLockService.cleanMode(player);
                ChestLockService.getInRemoveMode().put(player.getName(), strings[1]);
                player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("chest_click_remove_owner"));
                return true;
            }
            if("transfer".equalsIgnoreCase(strings[0])) {
                ChestLockService.cleanMode(player);
                ChestLockService.getInTransferMode().put(player.getName(), strings [1]);
                player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("chest_click_transfer_owner"));
                return true;
            }
        }
        return false;
    }
}

package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.ChestLockUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChestOwnerCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if ("com".equalsIgnoreCase(command.getName())) {
            if ("add".equalsIgnoreCase(strings[0])) {
                ChestLockUtils.cleanMode(player);
                ChestLockUtils.getInAddMode().put(player.getName(), strings [1]);
                player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("chest_click_add_owner"));
            } else if("remove".equalsIgnoreCase(strings[0])) {
                ChestLockUtils.cleanMode(player);
                ChestLockUtils.getInRemoveMode().put(player.getName(), strings[1]);
                player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("chest_click_remove_owner"));
            }
            return true;
        }
        return false;
    }
}

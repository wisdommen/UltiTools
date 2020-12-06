package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.views.KitsView;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;


public class KitsCommands extends AbstractPlayerCommandExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (strings.length > 0) {
            return false;
        }
        Inventory inventory = KitsView.setUp(player);
        player.openInventory(inventory);
        return true;
    }
}

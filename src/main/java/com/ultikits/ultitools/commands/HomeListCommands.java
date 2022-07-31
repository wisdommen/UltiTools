package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.views.HomeListView;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

@CmdExecutor( function = "home", permission = "ultikits.tools.homelist", description = "listhome_function", alias = "homelist")
public class HomeListCommands extends AbstractPlayerCommandExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        Inventory homes = HomeListView.setUp(player);
        player.openInventory(homes);
        return true;
    }
}

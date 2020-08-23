package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstracts.AbstractPlayerCommandExecutor;
import com.minecraft.ultikits.utils.GUIUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoteBagCommands extends AbstractPlayerCommandExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (strings.length == 0 && "bag".equals(command.getName())) {
            GUIUtils.setPlayerRemoteChest(player);
            player.openInventory(GUIUtils.inventoryMap.get(player.getName()+".chest").getInventory());
            return true;
        }
        return false;
    }
}

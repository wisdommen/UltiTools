package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstracts.AbstractPlayerCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.minecraft.ultikits.utils.GUIUtils.inventoryMap;
import static com.minecraft.ultikits.utils.GUIUtils.setKit;


public class KitsCommands extends AbstractPlayerCommandExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!"kits".equalsIgnoreCase(command.getName())){
            return false;
        }
        setKit(player);
        player.openInventory(inventoryMap.get(player.getName()+".kits").getInventory());
        return true;
    }
}

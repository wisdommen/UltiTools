package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstracts.AbstractTabExecutor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeleportCommands extends AbstractTabExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        //TODO TPA指令
        return false;
    }

    @Override
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        return null;
    }
}

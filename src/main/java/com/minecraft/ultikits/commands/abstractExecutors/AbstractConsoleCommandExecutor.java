package com.minecraft.ultikits.commands.abstractExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractConsoleCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            commandSender.sendMessage(ChatColor.RED+"只可以在后台执行这个指令！");
            return false;
        }
        return onConsoleCommand(commandSender, command, strings);
    }

    protected abstract boolean onConsoleCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String[] strings);
}

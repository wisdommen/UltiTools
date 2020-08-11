package com.minecraft.ultikits.abstractClass;

import com.minecraft.economy.economyMain.UltiEconomyMain;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPlayerCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED+"只有游戏内可以执行这个指令！");
            return false;
        }
        Player player = (Player) commandSender;
        Economy economy = UltiEconomyMain.getEcon();
        return onPlayerCommand(command, strings, player, economy);
    }

    protected abstract boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player, @NotNull Economy economy);
}

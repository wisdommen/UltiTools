package com.minecraft.ultikits.abstractClass;

import com.minecraft.economy.economyMain.UltiEconomyMain;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractTabExecutor implements TabExecutor {
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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings){
        if (!(commandSender instanceof Player)) {
            return null;
        }
        return onPlayerTabComplete(command, strings);
    }

    protected abstract boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player, @NotNull Economy economy);

    protected abstract @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings);
}

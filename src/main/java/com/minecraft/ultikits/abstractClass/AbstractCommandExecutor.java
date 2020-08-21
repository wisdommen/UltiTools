package com.minecraft.ultikits.abstractClass;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractCommandExecutor extends BukkitCommand {

    protected AbstractCommandExecutor(@NotNull String name) {
        super(name);
    }

    protected abstract boolean playerExecute(@NotNull Player player, @NotNull String commandLabel, @NotNull String[] args);

    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "[错误]这个命令只能在游戏内调用！");
            return true;
        }
        if (!sender.hasPermission(Objects.requireNonNull(this.getPermission()))){
            sender.sendMessage(ChatColor.RED + "[错误]你无权限调用！");
            return true;
        }
        Player player = (Player) sender;
        return this.playerExecute(player, commandLabel, args);
    }
}

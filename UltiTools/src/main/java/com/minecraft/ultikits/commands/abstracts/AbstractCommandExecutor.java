package com.minecraft.ultikits.commands.abstracts;

import com.minecraft.ultikits.ultitools.UltiTools;
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
            sender.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("command_can_only_perform_in_game"));
            return true;
        }
        if (!sender.hasPermission(Objects.requireNonNull(this.getPermission()))){
            sender.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("no_permission"));
            return true;
        }
        Player player = (Player) sender;
        return this.playerExecute(player, commandLabel, args);
    }
}

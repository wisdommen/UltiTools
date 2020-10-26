package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.minecraft.ultikits.utils.MessagesUtils.warning;

public class ToolsCommands implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(command.getName().equalsIgnoreCase("ultitools"))) {
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!(player.isOp() || player.hasPermission("ultitools.tools.commands"))) {
                sender.sendMessage(warning(UltiTools.languageUtils.getWords("no_permission")));
                return false;
            }
        }
        if (!(args.length == 1 && args[0].equalsIgnoreCase("reload"))) {
            return false;
        }
        UltiTools.getInstance().reloadConfig();
        sender.sendMessage(warning(UltiTools.languageUtils.getWords("config_reloaded")));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player player = (Player) sender;
        if (args.length == 1) {
            List<String> tabCommands = new ArrayList<>();
            if (player.isOp() || player.hasPermission("ultikits.tools.commands")) {
                tabCommands.add("reload");
            }
            return tabCommands;
        }
        return null;
    }
}

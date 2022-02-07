package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.ultikits.utils.MessagesUtils.warning;

public class GameModeCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (player.hasPermission("ultikits.tools.command.gm.all")
                || player.hasPermission("ultikits.tools.command.gm.0")
                || player.hasPermission("ultikits.tools.command.gm.1")
                || player.hasPermission("ultikits.tools.command.gm.2")
                || player.hasPermission("ultikits.tools.command.gm.3")) {
            switch (strings[0]) {
                case "0":
                    if (player.hasPermission("ultikits.tools.command.gm.all")
                            || player.hasPermission("ultikits.tools.command.gm.0")) {
                        player.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage(ChatColor.YELLOW + String.format(UltiTools.languageUtils.getString("gamemode_changed"), UltiTools.languageUtils.getString("gamemode_0")));
                        return true;
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
                    return true;

                case "1":
                    if (player.hasPermission("ultikits.tools.command.gm.all")
                            || player.hasPermission("ultikits.tools.command.gm.1")) {
                        player.setGameMode(GameMode.CREATIVE);
                        player.sendMessage(ChatColor.YELLOW + String.format(UltiTools.languageUtils.getString("gamemode_changed"), UltiTools.languageUtils.getString("gamemode_1")));
                        return true;
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
                    return true;

                case "2":
                    if (player.hasPermission("ultikits.tools.command.gm.all")
                            || player.hasPermission("ultikits.tools.command.gm.2")) {
                        player.setGameMode(GameMode.ADVENTURE);
                        player.sendMessage(ChatColor.YELLOW + String.format(UltiTools.languageUtils.getString("gamemode_changed"), UltiTools.languageUtils.getString("gamemode_2")));
                        return true;
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
                    return true;

                case "3":
                    if (player.hasPermission("ultikits.tools.command.gm.all")
                            || player.hasPermission("ultikits.tools.command.gm.3")) {
                        player.setGameMode(GameMode.SPECTATOR);
                        player.sendMessage(ChatColor.YELLOW + String.format(UltiTools.languageUtils.getString("gamemode_changed"), UltiTools.languageUtils.getString("gamemode_3")));
                        return true;
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
                    return true;

                default:
                    return false;
            }
        }
        player.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
        return true;
    }
}

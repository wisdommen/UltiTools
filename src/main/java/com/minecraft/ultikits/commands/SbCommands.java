package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstractExecutors.AbstractTabExecutor;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SbCommands extends AbstractTabExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        List<String> players = UltiTools.getInstance().getConfig().getStringList("player_closed_sb");
        if ("sb".equalsIgnoreCase(command.getName()) && strings.length == 1){
            switch (strings[0]){
                case "open":
                    players.remove(player.getName());
                    UltiTools.getInstance().getConfig().set("player_closed_sb", players);
                    UltiTools.getInstance().saveConfig();
                    return true;
                case "close":
                    if (!players.contains(player.getName())){
                        players.add(player.getName());
                    }
                    UltiTools.getInstance().getConfig().set("player_closed_sb", players);
                    UltiTools.getInstance().saveConfig();
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (strings.length == 1){
            List<String> tabCommands = new ArrayList<>();
            tabCommands.add("open");
            tabCommands.add("close");
            return tabCommands;
        }
        return null;
    }
}

package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.utils.DatabasePlayerTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SocialSystemCommands extends AbstractTabExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        switch (strings.length){
            case 0:

                return true;
            case 2:
                switch (strings[0]){
                    case "add":
                        Player friend = Bukkit.getPlayerExact(strings[1]);
                        if (friend == null){
                            return false;
                        }
                        DatabasePlayerTools.addPlayerFriends(player, friend);
                        return true;
                    case "remove":
                        return true;
                }
                return true;
        }
        return false;
    }

    @Nullable
    @Override
    protected List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        return null;
    }
}

package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstracts.AbstractTabExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SbCommands extends AbstractTabExecutor {

    private final static File sbFile = new File(ConfigsEnum.SIDEBAR_DATA.toString());
    private final static YamlConfiguration sbConfig = YamlConfiguration.loadConfiguration(sbFile);

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        List<String> players = sbConfig.getStringList("player_closed_sb");
        if ("sb".equalsIgnoreCase(command.getName()) && strings.length == 1){
            switch (strings[0]){
                case "open":
                    players.remove(player.getName());
                    sbConfig.set("player_closed_sb", players);
                    try {
                        sbConfig.save(sbFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                case "close":
                    if (!players.contains(player.getName())){
                        players.add(player.getName());
                    }
                    sbConfig.set("player_closed_sb", players);
                    try {
                        sbConfig.save(sbFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

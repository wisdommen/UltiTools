package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

@CmdExecutor(function = "lobby", permission = "ultikits.tools.back", description = "back_function", alias = "setlobby,lobby")
public class LobbyCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        File lobbyFile = new File(ConfigsEnum.LOBBY.toString());
        YamlConfiguration lobbyFileConfig = YamlConfiguration.loadConfiguration(lobbyFile);
        World playerWorld = player.getLocation().getWorld();
        int playerX = player.getLocation().getBlockX();
        int playerY = player.getLocation().getBlockY();
        int playerZ = player.getLocation().getBlockZ();
        switch (command.getName()) {
            case "setlobby" :
                if(strings.length == 0 && player.hasPermission("ultikits.tools.admin")) {
                    lobbyFileConfig.set("serverLobby.World",playerWorld.getName());
                    lobbyFileConfig.set("serverLobby.X",playerX);
                    lobbyFileConfig.set("serverLobby.Y",playerY);
                    lobbyFileConfig.set("serverLobby.Z",playerZ);
                    try {
                        lobbyFileConfig.save(lobbyFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("new_lobby_set")));
                } else {
                    player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("no_permission")));
                }
                break;
            case "lobby" :
                if(strings.length == 0 && lobbyFileConfig.get("serverLobby.World") == null) {
                    player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("lobby_not_found")));
                } else {
                    playerWorld = Bukkit.getServer().getWorld((String) lobbyFileConfig.get("serverLobby.World"));
                    playerX = (int) lobbyFileConfig.get("serverLobby.X");
                    playerY = (int) lobbyFileConfig.get("serverLobby.Y");
                    playerZ = (int) lobbyFileConfig.get("serverLobby.Z");
                    Location lobbyLocation = new Location(playerWorld, playerX,playerY,playerZ);
                    player.teleport(lobbyLocation);
                    player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("lobby_teleport_success")));
                }
                break;
            default:
                return false;
        }
        return true;
    }
}

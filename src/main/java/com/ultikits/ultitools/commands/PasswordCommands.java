package com.ultikits.ultitools.commands;

import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.DatabasePlayerTools;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.ultikits.utils.MessagesUtils.info;
import static com.ultikits.utils.MessagesUtils.warning;

public class PasswordCommands implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender.isOp()) {
            if (strings.length == 2) {
                switch (strings[0]) {
                    case "reset" :
                        if(DatabasePlayerTools.isPlayerAccountExist(strings[1])) {
                            Random random = new Random();
                            StringBuilder pwd= new StringBuilder();
                            for (int i=0;i<6;i++)
                            {
                                pwd.append(random.nextInt(10));
                            }
                            DatabasePlayerTools.setPlayerPassword(strings[1], String.valueOf(pwd));
                            commandSender.sendMessage(info(UltiTools.languageUtils.getString("pwd_reset_success").replace("%s", strings[1])));
                            commandSender.sendMessage(info(String.valueOf(pwd)));
                        }else {
                            commandSender.sendMessage(warning(UltiTools.languageUtils.getString("pwd_player_not_found")));
                        }
                        break;
                    default:
                        return false;
                }
            }
        } else {
            commandSender.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            List<String> tabCommands = new ArrayList<>();
            switch (strings.length) {
                case 1:
                    tabCommands.add("reset");
                    return tabCommands;
                case 2:
                    for (OfflinePlayer player: Bukkit.getOfflinePlayers()) {
                        tabCommands.add(player.getName());
                    }
                    return tabCommands;
            }
        }
        return null;
    }
}

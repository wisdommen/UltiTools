package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractConsoleCommandExecutor;
import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static com.ultikits.utils.MessagesUtils.info;

@CmdExecutor(function = "remote-bag", permission = "ultikits.tools.admin", description = "bag_console_function", alias = "createbag")
public class RemoteBagConsoleCommands extends AbstractConsoleCommandExecutor {

    @Override
    protected boolean onConsoleCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String[] strings) {
        if (strings.length == 1) {
            File file = new File(ConfigsEnum.PLAYER_CHEST.toString(), strings[0] + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            int i = 1;
            while (true) {
                if (config.get(String.valueOf(i)) == null) {
                    config.set(String.valueOf(i), "");
                    break;
                }
                i++;
            }
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            commandSender.sendMessage(info(String.format(UltiTools.languageUtils.getString("bag_create_new_bag_for_someone_successfully"), strings[0])));
        }
        return false;
    }
}

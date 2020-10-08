package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstracts.AbstractConsoleCommandExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static com.minecraft.ultikits.utils.MessagesUtils.info;

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
            commandSender.sendMessage(info(String.format("已为%s创建一个新的背包！", strings[0])));
        }
        return false;
    }
}

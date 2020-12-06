package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class LockCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        File playerFile = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        if ("lock".equalsIgnoreCase(command.getName())) {
            playerData.set("lock", true);
            if (playerData.getBoolean("unlock")) {
                playerData.set("unlock", false);
            }
            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_file_save_failed"));
                return true;
            }
            player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("lock_click_to_lock"));
            return true;
        }
        return false;
    }
}

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

/**
 * @author wisdomme
 */
public class UnlockCommands extends AbstractPlayerCommandExecutor {
    @Override
    public boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        File playerFile = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

        if ("unlock".equalsIgnoreCase(command.getName())) {
            playerData.set("unlock", true);
            if (playerData.getBoolean("lock")) {
                playerData.set("lock", false);
            }
            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("unlock_file_save_failed"));
                return true;
            }
            player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("unlock_click_to_unlock"));
            return true;
        }
        return false;
    }
}

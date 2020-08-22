package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstractExecutors.AbstractPlayerCommandExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
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
                player.sendMessage(ChatColor.RED + "文件保存失败，解锁失败！重新输入/unlock指令。");
                return true;
            }
            player.sendMessage(ChatColor.GREEN + "请点击箱子解锁！");
            return true;
        }
        return false;
    }
}

package com.minecraft.ultikits.chestLock;

import com.minecraft.ultikits.abstractClass.AbstractPlayerCommandExecutor;
import com.minecraft.ultikits.config.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static com.minecraft.ultikits.utils.Messages.warning;

/**
 * @author wisdomme
 */
public class ChestLockCMD extends AbstractPlayerCommandExecutor {
    @Override
    public boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player, @NotNull Economy economy) {
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
                player.sendMessage(ChatColor.RED + "文件保存失败，上锁失败！重新输入/lock指令。");
                return true;
            }
            player.sendMessage(ChatColor.GREEN + "请点击箱子来上锁！");
            return true;
        } else if ("unlock".equalsIgnoreCase(command.getName())) {
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

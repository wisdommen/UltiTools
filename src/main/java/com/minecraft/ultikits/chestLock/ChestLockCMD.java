package com.minecraft.ultikits.chestLock;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * @author wisdomme
 */
public class ChestLockCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            File playerFile = new File(UltiTools.getInstance().getDataFolder() + "/playerData", player.getName()+".yml");
            YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

            if ("lock".equalsIgnoreCase(command.getName())){
                playerData.set("lock", true);
                if (playerData.getBoolean("unlock")){
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
            }else if ("unlock".equalsIgnoreCase(command.getName())){
                playerData.set("unlock", true);
                if (playerData.getBoolean("lock")){
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
        sender.sendMessage(ChatColor.RED+"此指令只可以在游戏内使用！");
        return true;
    }
}

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

public class ChestLockCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            File player_file = new File(UltiTools.getInstance().getDataFolder() + "/playerData", player.getName()+".yml");
            YamlConfiguration player_data = YamlConfiguration.loadConfiguration(player_file);

            if (command.getName().equalsIgnoreCase("lock")){
                player_data.set("lock", true);
                if (player_data.getBoolean("unlock")){
                    player_data.set("unlock", false);
                }
                try {
                    player_data.save(player_file);
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + "文件保存失败，上锁失败！重新输入/lock指令。");
                    return true;
                }
                player.sendMessage(ChatColor.GREEN + "请点击箱子来上锁！");
                return true;
            }else if (command.getName().equalsIgnoreCase("unlock")){
                player_data.set("unlock", true);
                if (player_data.getBoolean("lock")){
                    player_data.set("lock", false);
                }
                try {
                    player_data.save(player_file);
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

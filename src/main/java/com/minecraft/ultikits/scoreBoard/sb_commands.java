package com.minecraft.ultikits.scoreBoard;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class sb_commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            List<String> players = UltiTools.getInstance().getConfig().getStringList("player_closed_sb");
            if ("sb".equalsIgnoreCase(command.getName()) && strings.length == 1){
                switch (strings[0]){
                    case "open":
                        players.remove(player.getName());
                        UltiTools.getInstance().getConfig().set("player_closed_sb", players);
                        UltiTools.getInstance().saveConfig();
                        return true;
                    case "close":
                        if (!players.contains(player.getName())){
                            players.add(player.getName());
                        }
                        UltiTools.getInstance().getConfig().set("player_closed_sb", players);
                        UltiTools.getInstance().saveConfig();
                        return true;
                    default:
                        return false;
                }
            }
        }else {
            commandSender.sendMessage(ChatColor.RED + "这个指令只能在游戏内执行！");
            return true;
        }
        return false;
    }
}

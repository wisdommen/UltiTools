package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstracts.AbstractTabExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.minecraft.ultikits.utils.Utils.getHomeList;

public class DeleteHomeCommands extends AbstractTabExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");

        if (file.exists()) {
            String homeName;
            if (args.length ==0){
                homeName = "默认";
                return deleteHome(homeName, player, file);
            }else if (args.length == 1){
                homeName = args[0];
                return deleteHome(homeName, player, file);
            }else {
                return false;
            }

        } else {
            player.sendMessage(ChatColor.RED + "[家插件] 你还没有设置家哦！");
        }
        return true;
    }

    @Override
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        return getHomeList(player);
    }

    private boolean deleteHome(String homeName, Player player, File file){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> homeList = getHomeList(player);
        if (!homeList.contains(homeName)) {
            player.sendMessage(ChatColor.RED + "[家插件] 你没有这个家！");
            return true;
        }
        homeList.remove(homeName);
        String homeNameNew = homeName;
        if (homeName.equals("默认")) {
            homeNameNew = "Def";
        }
        if (config.get(player.getName() + "." + homeNameNew) != null) {
            config.set(player.getName() + "." + homeNameNew, "");
            config.set(player.getName() + ".homelist", homeList);
            player.sendMessage(ChatColor.RED + "[家插件] " + homeName + " 已被删除！");
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            player.sendMessage(ChatColor.RED + "[家插件] 你没有这个家！");
        }
        return false;
    }
}

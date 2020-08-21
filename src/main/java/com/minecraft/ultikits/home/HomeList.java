package com.minecraft.ultikits.home;

import com.minecraft.ultikits.abstractClass.AbstractPlayerCommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.minecraft.ultikits.utils.Utils.getHomeList;

public class HomeList extends AbstractPlayerCommandExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        player.sendMessage(ChatColor.GREEN + "-----你的家-----");
        for (String each : getHomeList(player)) {
            player.sendMessage(ChatColor.YELLOW + each);
        }
        return true;
    }
}

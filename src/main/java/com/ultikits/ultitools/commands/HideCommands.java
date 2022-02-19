package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.ultikits.utils.MessagesUtils.info;
import static com.ultikits.utils.MessagesUtils.warning;

public class HideCommands extends AbstractPlayerCommandExecutor {
    public static final List<String> hidePlayers = new ArrayList<>();

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (!player.isOp() && !player.hasPermission("ultikits.tools.command.hide")) {
            player.sendMessage(warning(UltiTools.languageUtils.getString("no_permission")));
            return true;
        }
        if (strings.length != 0) return false;
        if (hidePlayers.contains(player.getName())) {
            hidePlayers.remove(player.getName());
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) p.showPlayer(UltiTools.getInstance(), player);
                }
            }.runTaskAsynchronously(UltiTools.getInstance());
            player.sendMessage(info(UltiTools.languageUtils.getString("hide_hided")));
        } else {
            hidePlayers.add(player.getName());
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) p.hidePlayer(UltiTools.getInstance(), player);
                }
            }.runTaskAsynchronously(UltiTools.getInstance());
        }
        player.sendMessage(info(UltiTools.languageUtils.getString("hide_unhided")));
        return true;
    }
}

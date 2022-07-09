package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.services.HomeService;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static com.ultikits.ultitools.services.HomeService.setHome;

public class SetHomeCommands extends AbstractPlayerCommandExecutor {

    private final static File homeFile = new File(ConfigsEnum.HOME.toString());
    private final static YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        if (!player.hasPermission("ultikits.tools.sethome") && !player.isOp()) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("no_permission"));
            return true;
        }
        if (!isPlayerCanSetHome(player)) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("sethome_reached_limit"));
            return true;
        }
        if (args.length == 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    setHome(player, "Def");
                }
            }.runTaskAsynchronously(UltiTools.getInstance());
            return true;
        } else if (args.length == 1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    setHome(player, args[0]);
                }
            }.runTaskAsynchronously(UltiTools.getInstance());
            return true;
        } else {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("sethome_usage"));
            return false;
        }
    }

    private static boolean isPlayerCanSetHome(Player player) {
        if (player.hasPermission("ultikits.tools.admin")) return true;
        if (player.hasPermission("ultikits.tools.level1")) {
            if (homeConfig.getInt("home_pro") == 0) return true;
            return HomeService.getHomeList(player).size() < homeConfig.getInt("home_pro");
        } else if (player.hasPermission("ultikits.tools.level2")) {
            if (homeConfig.getInt("home_ultimate") == 0) return true;
            return HomeService.getHomeList(player).size() < homeConfig.getInt("home_ultimate");
        } else {
            if (homeConfig.getInt("home_normal") == 0) return true;
            return HomeService.getHomeList(player).size() < homeConfig.getInt("home_normal");
        }
    }
}

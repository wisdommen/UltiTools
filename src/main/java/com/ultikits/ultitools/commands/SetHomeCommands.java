package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.Utils;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SetHomeCommands extends AbstractPlayerCommandExecutor {

    private final static File homeFile = new File(ConfigsEnum.HOME.toString());
    private final static YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        if (!isPlayerCanSetHome(player)) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("sethome_reached_limit"));
            return true;
        }
        if (args.length == 0) {
            setHome(player, "Def");
            return true;
        } else if (args.length == 1) {
            if (Utils.getHomeList(player).contains(args[0])) {
                player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getWords("sethome_home_already_have")));
                return true;
            }
            setHome(player, args[0]);
            return true;
        } else {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("sethome_usage"));
            return false;
        }
    }

    private void setHome(Player player, String homeName) {
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        List<String> homelist = config.getStringList(player.getName() + ".homelist");
        config.set(player.getName() + "." + homeName + ".world", player.getWorld().getName());
        config.set(player.getName() + "." + homeName + ".x", player.getLocation().getBlockX());
        config.set(player.getName() + "." + homeName + ".y", player.getLocation().getBlockY());
        config.set(player.getName() + "." + homeName + ".z", player.getLocation().getBlockZ());
        if (homeName.equals("Def")) {
            homeName = UltiTools.languageUtils.getWords("default");
            homelist.remove(homeName);
        }
        homelist.add(homeName);
        config.set(player.getName() + ".homelist", homelist);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getWords("sethome_successfully"));
    }

    private static boolean isPlayerCanSetHome(Player player) {
        if (player.hasPermission("ultikits.tools.admin")) return true;
        if (player.hasPermission("ultikits.tools.level1")) {
            if (homeConfig.getInt("home_pro") == 0) return true;
            return Utils.getHomeList(player).size() < homeConfig.getInt("home_pro");
        } else if (player.hasPermission("ultikits.tools.level2")) {
            if (homeConfig.getInt("home_ultimate") == 0) return true;
            return Utils.getHomeList(player).size() < homeConfig.getInt("home_ultimate");
        } else {
            if (homeConfig.getInt("home_normal") == 0) return true;
            return Utils.getHomeList(player).size() < homeConfig.getInt("home_normal");
        }
    }
}

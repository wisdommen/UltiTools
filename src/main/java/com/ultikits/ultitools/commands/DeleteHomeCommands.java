package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DeleteHomeCommands extends AbstractTabExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");

        if (file.exists()) {
            String homeName;
            if (args.length == 0) {
                homeName = UltiTools.languageUtils.getString("default");
                return deleteHome(homeName, player, file);
            } else if (args.length == 1) {
                homeName = args[0];
                return deleteHome(homeName, player, file);
            } else {
                return false;
            }

        } else {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("home_have_no_home"));
        }
        return true;
    }

    @Override
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        return Utils.getHomeList(player);
    }

    private boolean deleteHome(String homeName, Player player, File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> homeList = Utils.getHomeList(player);
        if (!homeList.contains(homeName)) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("home_dont_have"));
            return true;
        }
        homeList.remove(homeName);
        String homeNameNew = homeName;
        if (homeName.equals(UltiTools.languageUtils.getString("default"))) {
            homeNameNew = "Def";
        }
        if (config.get(player.getName() + "." + homeNameNew) != null) {
            config.set(player.getName() + "." + homeNameNew, "");
            config.set(player.getName() + ".homelist", homeList);
            player.sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getString("delhome_successfully"), homeName));
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("home_dont_have"));
        }
        return false;
    }
}

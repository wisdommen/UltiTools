package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstracts.AbstractPlayerCommandExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.minecraft.ultikits.utils.MessagesUtils.warning;
import static com.minecraft.ultikits.utils.Utils.getHomeList;

public class SetHomeCommands extends AbstractPlayerCommandExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        if (!isPlayerCanSetHome(player)) {
            player.sendMessage(ChatColor.RED + "[家插件]你的家数量已经到达上限，删除家或者升级权限获得设置更多的家");
            return true;
        }
        if (args.length == 0) {
            setHome(player, "Def");
            return true;
        } else if (args.length == 1) {
            if (getHomeList(player).contains(args[0])) {
                player.sendMessage(warning("你已经有叫这个名字的家了！"));
                return true;
            }
            setHome(player, args[0]);
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "[家插件]用法：/sethome [家的名字（不设置则为默认）]");
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
            homeName = "默认";
            homelist.remove(homeName);
        }
        homelist.add(homeName);
        config.set(player.getName() + ".homelist", homelist);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(ChatColor.YELLOW + "[家插件]设置家成功！");
    }

    private static boolean isPlayerCanSetHome(Player player) {
        if (player.hasPermission("ultikits.tools.admin")) return true;
        if (player.hasPermission("ultikits.tools.level1")) {
            if (UltiTools.getInstance().getConfig().getInt("home_pro") == 0) return true;
            return getHomeList(player).size() < UltiTools.getInstance().getConfig().getInt("home_pro");
        } else if (player.hasPermission("ultikits.tools.level2")) {
            if (UltiTools.getInstance().getConfig().getInt("home_ultimate") == 0) return true;
            return getHomeList(player).size() < UltiTools.getInstance().getConfig().getInt("home_ultimate");
        } else {
            if (UltiTools.getInstance().getConfig().getInt("home_normal") == 0) return true;
            return getHomeList(player).size() < UltiTools.getInstance().getConfig().getInt("home_normal");
        }
    }
}

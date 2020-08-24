package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.enums.CleanTypeEnum;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.CleanerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.minecraft.ultikits.utils.CleanerUtils.sendMessage;
import static com.minecraft.ultikits.utils.MessagesUtils.warning;

public class CleanerCommands implements TabExecutor {
    final static YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(ConfigsEnum.CLEANER.toString()));
    final static String name = config.getString("cleaner_name");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("clean") && sender.hasPermission("ultikits.tools.clean")) {
            if (!UltiTools.isProVersion) {
                sender.sendMessage(warning("这是一个付费版功能，激活付费版之后就可以使用啦！"));
                return true;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    int cleanCount;
                    CleanTypeEnum cleanType;
                    switch (args.length) {
                        case 1:
                            cleanType = CleanTypeEnum.getTypeByAlis(args[0]);
                            if (cleanType == null) break;
                            cleanCount = CleanerUtils.run(cleanType);
                            sender.sendMessage(sendMessage(cleanType, name, cleanCount));
                            break;
                        case 2:
                            cleanType = CleanTypeEnum.getTypeByAlis(args[0]);
                            if (cleanType == null) break;
                            World world = Bukkit.getWorld(args[1]);
                            cleanCount = CleanerUtils.run(cleanType, Collections.singletonList(world));
                            sender.sendMessage(sendMessage(cleanType, name, cleanCount));
                            break;
                        default:
                            break;
                    }
                }
            }.runTaskAsynchronously(UltiTools.getInstance());
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player) {
            List<String> tabCommands = new ArrayList<>();
            switch (args.length) {
                case 1:
                    tabCommands.add("mobs");
                    tabCommands.add("items");
                    tabCommands.add("all");
                    tabCommands.add("check");
                    return tabCommands;
                case 2:
                    for (World world : Bukkit.getWorlds()) {
                        tabCommands.add(world.getName());
                    }
                    return tabCommands;
            }
        }
        return null;
    }

}

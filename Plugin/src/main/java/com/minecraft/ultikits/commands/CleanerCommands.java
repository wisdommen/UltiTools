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
import static com.minecraft.ultikits.utils.MessagesUtils.info;
import static com.minecraft.ultikits.utils.MessagesUtils.warning;

public class CleanerCommands implements TabExecutor {
    final static YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(ConfigsEnum.CLEANER.toString()));
    final static String name = config.getString("cleaner_name");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("clean") && (sender.hasPermission("ultikits.tools.clean") || sender.hasPermission("ultikits.tools.admin"))) {
            if (!UltiTools.isProVersion) {
                sender.sendMessage(warning(UltiTools.languageUtils.getWords("warning_pro_fuction")));
                return true;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    int cleanCount;
                    CleanTypeEnum cleanType;
                    switch (args.length) {
                        case 1:
                            if (args[0].equals("help")) {
                                sendHelp(sender);
                                return;
                            }
                            cleanType = CleanTypeEnum.getTypeByAlis(args[0]);
                            if (cleanType == null) break;
                            cleanCount = CleanerUtils.run(cleanType);
                            sender.sendMessage(sendMessage(cleanType, name, cleanCount));
                            break;
                        case 2:
                            if (args[0].equals("help")) {
                                sendHelp(sender);
                                return;
                            }
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
                    tabCommands.add("help");
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

    private static void sendHelp(CommandSender sender) {
        sender.sendMessage(info("/clean check  " + UltiTools.languageUtils.getWords("clean_usage_check")));
        sender.sendMessage(info("/clean item [world]  " + UltiTools.languageUtils.getWords("clean_usage_clean_item")));
        sender.sendMessage(info("/clean mobs [world]  " + UltiTools.languageUtils.getWords("clean_usage_mobs")));
        sender.sendMessage(info("/clean all [world]  " + UltiTools.languageUtils.getWords("clean_usage_all")));
        sender.sendMessage(info("/clean help  " + UltiTools.languageUtils.getWords("clean_usage_help")));
    }
}

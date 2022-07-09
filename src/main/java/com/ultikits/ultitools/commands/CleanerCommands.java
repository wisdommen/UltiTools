package com.ultikits.ultitools.commands;

import com.ultikits.ultitools.enums.CleanTypeEnum;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.services.CleanerService;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
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

import static com.ultikits.ultitools.services.CleanerService.sendMessage;
import static com.ultikits.utils.MessagesUtils.info;


public class CleanerCommands implements TabExecutor {
    final static YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(ConfigsEnum.CLEANER.toString()));
    final static String name = config.getString("cleaner_name");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("clean") && (sender.hasPermission("ultikits.tools.clean") || sender.hasPermission("ultikits.tools.admin"))) {
            if (!UltiTools.getInstance().getConfig().getBoolean("enable_pro") || !UltiTools.getInstance().getProChecker().getProStatus()) {
                sender.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("warning_pro_function")));
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
                            cleanCount = CleanerService.run(cleanType);
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
                            cleanCount = CleanerService.run(cleanType, Collections.singletonList(world));
                            sender.sendMessage(sendMessage(cleanType, name, cleanCount));
                            break;
                        default:
                            sendHelp(sender);
                            break;
                    }
                }
            }.runTask(UltiTools.getInstance());
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
        sender.sendMessage(info("/clean check  " + UltiTools.languageUtils.getString("clean_usage_check")));
        sender.sendMessage(info("/clean item [world]  " + UltiTools.languageUtils.getString("clean_usage_clean_item")));
        sender.sendMessage(info("/clean mobs [world]  " + UltiTools.languageUtils.getString("clean_usage_mobs")));
        sender.sendMessage(info("/clean all [world]  " + UltiTools.languageUtils.getString("clean_usage_all")));
        sender.sendMessage(info("/clean help  " + UltiTools.languageUtils.getString("clean_usage_help")));
    }
}

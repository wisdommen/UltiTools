package com.minecraft.ultikits.home;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Home implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            File file = new File(UltiTools.getInstance().getDataFolder() + "/playerData", player.getName() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            boolean enable_home = UltiTools.getInstance().getConfig().getBoolean("enable_home");

            if (cmd.getName().equalsIgnoreCase("sethome")) {
                if (enable_home) {
                    if (args.length == 0) {
                        String homelist = config.getString(player.getName() + ".homelist");
                        config.set(player.getName() + ".Def.world", player.getWorld().getName());
                        config.set(player.getName() + ".Def.x", player.getLocation().getBlockX());
                        config.set(player.getName() + ".Def.y", player.getLocation().getBlockY());
                        config.set(player.getName() + ".Def.z", player.getLocation().getBlockZ());
                        try {
                            if (!config.getString(player.getName() + ".homelist").contains("默认")) {
                                config.set(player.getName() + ".homelist", homelist + " 默认");
                            }
                        } catch (NullPointerException e) {
                            config.set(player.getName() + ".homelist", "默认");
                        }
                        try {
                            config.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage(ChatColor.YELLOW + "[家插件]设置家成功！");
                        return true;
                    } else if (args.length == 1) {
                        String homelist = config.getString(player.getName() + ".homelist");
                        config.set(player.getName() + "." + args[0] + ".world", player.getWorld().getName());
                        config.set(player.getName() + "." + args[0] + ".x", player.getLocation().getBlockX());
                        config.set(player.getName() + "." + args[0] + ".y", player.getLocation().getBlockY());
                        config.set(player.getName() + "." + args[0] + ".z", player.getLocation().getBlockZ());
                        if (!Objects.equals(config.getString(player.getName() + ".homelist"), null)) {
                            if (!config.getString(player.getName() + ".homelist").contains(args[0])) {
                                config.set(player.getName() + ".homelist", homelist + " " + args[0]);
                            }
                        } else {
                            config.set(player.getName() + ".homelist", args[0]);
                        }
                        try {
                            config.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage(ChatColor.YELLOW + "[家插件]设置家成功！");
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "[家插件]用法：/sethome [家的名字（不设置则为默认）]");
                        return false;
                    }
                }else {
                    player.sendMessage(ChatColor.RED + "[家插件]此功能已被屏蔽！");
                    return true;
                }
            } else if (cmd.getName().equalsIgnoreCase("home")) {
                if (enable_home) {
                    if (file.exists()) {
                        try {
                            if (args.length == 0) {
                                World world = Bukkit.getServer().getWorld(Objects.requireNonNull(config.getString(player.getName() + ".Def.world")));
                                int x = config.getInt(player.getName() + ".Def.x");
                                int y = config.getInt(player.getName() + ".Def.y");
                                int z = config.getInt(player.getName() + ".Def.z");
                                player.teleport(new Location(world, x, y, z));
                                player.sendMessage(ChatColor.GREEN + "[家插件]欢迎回家！");
                            } else if (args.length == 1) {
                                World world = Bukkit.getServer().getWorld(Objects.requireNonNull(config.getString(player.getName() + "." + args[0] + ".world")));
                                int x = config.getInt(player.getName() + "." + args[0] + ".x");
                                int y = config.getInt(player.getName() + "." + args[0] + ".y");
                                int z = config.getInt(player.getName() + "." + args[0] + ".z");
                                player.teleport(new Location(world, x, y, z));
                                player.sendMessage(ChatColor.GREEN + "[家插件]欢迎回家！");
                            } else {
                                player.sendMessage(ChatColor.RED + "[家插件]用法：/home [家的名字（不设置则为默认）]");
                                return false;
                            }
                        } catch (NullPointerException e) {
                            player.sendMessage(ChatColor.RED + "[家插件]你没有这个家！");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "[家插件]你还没有设置家！");
                    }
                    return true;
                }else {
                    player.sendMessage(ChatColor.RED + "[家插件]此功能已被屏蔽！");
                    return true;
                }
            } else if (cmd.getName().equalsIgnoreCase("homelist")) {
                if (enable_home) {
                    player.sendMessage(ChatColor.GREEN + "你的家：" + config.getString(player.getName() + ".homelist"));
                    return true;
                }else {
                    player.sendMessage(ChatColor.RED + "[家插件]此功能已被屏蔽！");
                    return true;
                }
            } else if (cmd.getName().equalsIgnoreCase("delhome")) {
                if (enable_home) {
                    if (file.exists() && args.length == 1) {
                        if (config.get(player.getName() + "." + args[0]) != null) {
                            config.set(player.getName() + "." + args[0], "");
                            config.set(player.getName() + ".homelist", config.getString(player.getName() + ".homelist").replace(args[0], ""));
                            player.sendMessage(ChatColor.RED + "[家插件]" + args[0] + "已被删除！");
                            try {
                                config.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        } else {
                            player.sendMessage(ChatColor.RED + "[家插件]你没有这个家！");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "[家插件]你还没有设置家哦！");
                    }
                }
            }else {
                player.sendMessage(ChatColor.RED + "[家插件]此功能已被屏蔽！");
                return true;
            }
            return false;
        } else {
            sender.sendMessage(ChatColor.RED + "[错误]这个命令只能在游戏内调用！");
            return true;
        }
    }
}

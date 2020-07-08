package com.minecraft.ultikits.multiworlds;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.minecraft.ultikits.utils.Messages.info;
import static com.minecraft.ultikits.utils.Messages.warning;
import static com.minecraft.ultikits.utils.Utils.getConfigFile;


public class multiWorlds implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            StringBuilder worldlist = new StringBuilder();
            for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                if (world.getName().equalsIgnoreCase("world_nether")) {
                    worldlist.append("\n").append("Nether");
                } else if (world.getName().equalsIgnoreCase("world_the_end")) {
                    worldlist.append("\n").append("End");
                } else if (world.getName().equalsIgnoreCase("world")) {
                    worldlist.append("\n").append("World");
                } else {
                    worldlist.append("\n").append(world.getName());
                }
            }
            if (command.getName().equalsIgnoreCase("mw") && strings.length == 1) {
                File file = getConfigFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                switch (strings[0]) {
                    case "help":
                        multiWorldsHelp(player);
                        return true;
                    case "list":
                        player.sendMessage(ChatColor.GREEN+"----世界列表----");
                        player.sendMessage(info(worldlist.toString()));
                        return true;
                }
                for (String world : config.getStringList("blocked_worlds")){
                    if (world.equals(strings[0]) && !player.isOp()){
                        player.sendMessage(warning("此世界已经被禁止进入！"));
                        return true;
                    }
                }
                for (String s1 : worldlist.toString().split("\n")) {
                    if (s1.equalsIgnoreCase(strings[0])) {
                        if (strings[0].equalsIgnoreCase("nether")) {
                            if (!player.getWorld().getName().equalsIgnoreCase("world_nether")) {
                                if (!config.getStringList("blocked_worlds").contains("world_nether")) {
                                    player.teleport(UltiTools.getInstance().getServer().getWorld("world_nether").getSpawnLocation());
                                }else {
                                    player.sendMessage(warning("此世界已经被禁止进入！"));
                                }
                            } else {
                                player.sendMessage(warning("你就在这个世界！"));
                            }
                            return true;
                        } else if (strings[0].equalsIgnoreCase("end")) {
                            if (!player.getWorld().getName().equalsIgnoreCase("world_the_end")) {
                                if (!config.getStringList("blocked_worlds").contains("world_the_end")) {
                                    player.teleport(UltiTools.getInstance().getServer().getWorld("world_the_end").getSpawnLocation());
                                }else {
                                    player.sendMessage(warning("此世界已经被禁止进入！"));
                                }
                            } else {
                                player.sendMessage(warning("你就在这个世界！"));
                            }
                            return true;
                        } else if (strings[0].equalsIgnoreCase("world")) {
                            if (!player.getWorld().getName().equalsIgnoreCase("world")) {
                                if (!config.getStringList("blocked_worlds").contains("world")) {
                                    player.teleport(UltiTools.getInstance().getServer().getWorld("world").getSpawnLocation());
                                }else {
                                    player.sendMessage(ChatColor.RED+"此世界已经被禁止进入！");
                                }
                            } else {
                                player.sendMessage(warning("你就在这个世界！"));
                            }
                            return true;
                        } else {
                            try {
                                if (!player.getWorld().getName().equalsIgnoreCase(strings[0])) {
                                    player.teleport(UltiTools.getInstance().getServer().getWorld(strings[0]).getSpawnLocation());
                                } else {
                                    player.sendMessage(warning("你就在这个世界！"));
                                    return true;
                                }
                            } catch (NullPointerException e) {
                                player.sendMessage(warning("不存在这个世界！"));
                                return false;
                            }
                        }
                        player.sendMessage(info("传送成功！"));
                        return true;
                    }
                }
            }else if (command.getName().equalsIgnoreCase("mw") && strings.length == 2 && player.isOp()){
                switch (strings[0]){
                    case "block":
                        for (World world : UltiTools.getInstance().getServer().getWorlds()){
                            if (strings[1].equals(world.getName())){
                                File file = getConfigFile();
                                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                                List<String> worlds = config.getStringList("blocked_worlds");
                                if (worlds.contains(strings[1])){
                                    player.sendMessage(ChatColor.RED + "此世界已经被禁止进入!");
                                    return true;
                                }
                                worlds.add(world.getName());
                                config.set("blocked_worlds", worlds);
                                try {
                                    config.save(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(ChatColor.RED + "已禁止玩家使用指令传送进入此世界!");
                                return true;
                            }
                        }
                        player.sendMessage(ChatColor.RED+"没有找到这个世界！");
                        return true;
                    case "unblock":
                        for (World world : UltiTools.getInstance().getServer().getWorlds()){
                            if (strings[1].equals(world.getName())){
                                File file = getConfigFile();
                                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                                List<String> worlds = config.getStringList("blocked_worlds");
                                if (worlds.contains(strings[1])){
                                    worlds.remove(world.getName());
                                    config.set("blocked_worlds", worlds);
                                    try {
                                        config.save(file);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    player.sendMessage(ChatColor.RED + "已取消禁止玩家使用指令传送进入此世界!");
                                    return true;
                                }
                                player.sendMessage(ChatColor.RED + "此世界并没有被禁止进入!");
                                return true;
                            }
                        }
                        player.sendMessage(ChatColor.RED+"没有找到这个世界！");
                        return true;
                }
            }
        }
        return false;
    }

    private void multiWorldsHelp(Player player) {
        player.sendMessage(ChatColor.GREEN+"----多世界帮助----");
        player.sendMessage(ChatColor.GOLD+"/mw help" + ChatColor.GRAY + "  打开这个帮助列表");
        player.sendMessage(ChatColor.GOLD+"/mw [世界名]" + ChatColor.GRAY + "  传送到某个世界");
        player.sendMessage(ChatColor.GOLD+"/mw list" + ChatColor.GRAY + "  打开世界列表");
        if (player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "/mw block [世界名]" + ChatColor.GRAY + "  禁止玩家进入某个世界");
            player.sendMessage(ChatColor.GOLD + "/mw unblock [世界名]" + ChatColor.GRAY + "  取消禁止玩家进入某个世界");
        }
    }
}

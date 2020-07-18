package com.minecraft.ultikits.multiworlds;

import com.minecraft.ultikits.prefix.Chat;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.minecraft.ultikits.utils.Messages.info;
import static com.minecraft.ultikits.utils.Messages.warning;
import static com.minecraft.ultikits.utils.Utils.getConfigFile;


public class multiWorlds implements TabExecutor {

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
                File world_file = new File(UltiTools.getInstance().getDataFolder(), "worlds.yml");
                YamlConfiguration worlds_config = YamlConfiguration.loadConfiguration(world_file);
                List<String> worlds = worlds_config.getStringList("worlds");

                switch (strings[0]){
                    case "block":
                        for (World world : UltiTools.getInstance().getServer().getWorlds()){
                            if (strings[1].equals(world.getName())){
                                File file = getConfigFile();
                                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                                List<String> blocked_worlds = config.getStringList("blocked_worlds");
                                if (blocked_worlds.contains(strings[1])){
                                    player.sendMessage(ChatColor.RED + "此世界已经被禁止进入!");
                                    return true;
                                }
                                blocked_worlds.add(world.getName());
                                config.set("blocked_worlds", blocked_worlds);
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
                                List<String> blocked_worlds = config.getStringList("blocked_worlds");
                                if (blocked_worlds.contains(strings[1])){
                                    blocked_worlds.remove(world.getName());
                                    config.set("blocked_worlds", blocked_worlds);
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
                    case "create":
                        for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                            if (strings[1].equals(world.getName())) {
                                player.sendMessage(ChatColor.RED+"此世界已存在！");
                                return false;
                            }
                        }

                        for (String each : worlds){
                            if (each.equals(strings[1])){
                                player.sendMessage(ChatColor.RED+"此世界已存在！");
                                return false;
                            }
                        }
                        player.sendMessage(ChatColor.RED+"处理中...请耐心等待");
                        WorldCreator worldCreator = new WorldCreator(strings[1]);
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.type(WorldType.NORMAL);
                        worldCreator.createWorld();
                        worlds.add(strings[1]);
                        worlds_config.set("worlds", worlds);
                        try {
                            worlds_config.save(world_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage(ChatColor.RED+"世界已成功生成！");
                        return true;
                    case "load":
                        for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                            if (strings[1].equals(world.getName())) {
                                player.sendMessage(ChatColor.RED+"此世界已存在！");
                                return false;
                            }
                        }
                        for (String each : worlds){
                            if (each.equals(strings[1])){
                                player.sendMessage(ChatColor.RED+"此世界已存在！");
                                return false;
                            }
                        }
                        player.sendMessage(ChatColor.RED+"处理中...请耐心等待");
                        UltiTools.getInstance().getServer().createWorld(new WorldCreator(strings[1]));
                        player.sendMessage(ChatColor.RED+"加载成功！");
                        worlds.add(strings[1]);
                        worlds_config.set("worlds", worlds);
                        try {
                            worlds_config.save(world_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    case "delete":
                        for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                            if (strings[1].equals(world.getName())) {
                                worlds.remove(strings[1]);
                                worlds_config.set("worlds", worlds);
                                try {
                                    worlds_config.save(world_file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(ChatColor.RED+"此世界已被删除，请重启服务器以生效！");
                                return true;
                            }
                        }
                        player.sendMessage(ChatColor.RED+"未找到这个世界！");
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
            player.sendMessage(ChatColor.GOLD + "/mw create [世界名]" + ChatColor.GRAY + "  生成一个新的世界");
            player.sendMessage(ChatColor.GOLD + "/mw load [世界名]" + ChatColor.GRAY + "  加载世界");
            player.sendMessage(ChatColor.GOLD + "/mw delete [世界名]" + ChatColor.GRAY + "  删除世界");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length == 1){
                List<String> tabCommands = new ArrayList<>();
                tabCommands.add("help");
                tabCommands.add("[世界名]");
                tabCommands.add("list");
                if (player.isOp()){
                    tabCommands.add("block");
                    tabCommands.add("unblock");
                    tabCommands.add("create");
                    tabCommands.add("delete");
                }
                return tabCommands;
            }else if (args.length==2 && player.isOp()){
                List<String> tabCommands = new ArrayList<>();
                tabCommands.add("[世界名]");
                return tabCommands;
            }
        }
        return null;
    }
}

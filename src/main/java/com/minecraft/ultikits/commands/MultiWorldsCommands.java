package com.minecraft.ultikits.commands;

import com.minecraft.ultikits.commands.abstracts.AbstractTabExecutor;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.views.WorldsListView;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.minecraft.ultikits.utils.MessagesUtils.info;
import static com.minecraft.ultikits.utils.MessagesUtils.warning;
import static com.minecraft.ultikits.utils.Utils.getConfig;
import static com.minecraft.ultikits.utils.Utils.getConfigFile;


public class MultiWorldsCommands extends AbstractTabExecutor {

    private static final File worldsFile = new File(ConfigsEnum.WORLDS.toString());
    private static final File file = getConfigFile();

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        List<String> worldList = getWorlds();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        YamlConfiguration worldConfig = YamlConfiguration.loadConfiguration(worldsFile);
        reviewOldVersionConfig(worldConfig, config);
        List<String> worlds = worldConfig.getStringList("worlds");
        List<String> blockedWorlds = worldConfig.getStringList("blocked_worlds");

        if (strings.length == 1) {
            switch (strings[0]) {
                case "help":
                    multiWorldsHelp(player);
                    return true;
                case "list":
                    Inventory inventory = WorldsListView.setUp();
                    player.openInventory(inventory);
                    return true;
            }
            if (worldConfig.getStringList("blocked_worlds").contains(strings[0]) && !player.isOp()) {
                player.sendMessage(warning("此世界已经被禁止进入！"));
                return true;
            }
            if (worldList.contains(strings[0])) {
                if (strings[0].equalsIgnoreCase("nether")) {
                    teleportPlayer(player, "world_the_nether");
                    return true;
                } else if (strings[0].equalsIgnoreCase("end")) {
                    teleportPlayer(player, "world_the_end");
                    return true;
                } else if (strings[0].equalsIgnoreCase("world")) {
                    teleportPlayer(player, "world");
                    return true;
                } else {
                    try {
                        teleportPlayer(player, strings[0]);
                    } catch (NullPointerException e) {
                        player.sendMessage(warning("不存在这个世界！"));
                        return true;
                    }
                }
                return true;
            }
        } else if (strings.length == 2 && player.isOp()) {
            switch (strings[0]) {
                case "block":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            if (blockedWorlds.contains(strings[1])) {
                                player.sendMessage(ChatColor.RED + "此世界已经被禁止进入!");
                                return true;
                            }
                            blockedWorlds.add(world.getName());
                            worldConfig.set("blocked_worlds", blockedWorlds);
                            try {
                                worldConfig.save(worldsFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(ChatColor.RED + "已禁止玩家使用指令传送进入此世界!");
                            return true;
                        }
                    }
                    player.sendMessage(warning("没有找到这个世界！"));
                    return true;
                case "unblock":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            if (blockedWorlds.contains(strings[1])) {
                                blockedWorlds.remove(world.getName());
                                worldConfig.set("blocked_worlds", blockedWorlds);
                                try {
                                    worldConfig.save(worldsFile);
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
                    player.sendMessage(warning("没有找到这个世界！"));
                    return true;
                case "create":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            player.sendMessage(warning("此世界已存在！"));
                            return false;
                        }
                    }

                    for (String each : worlds) {
                        if (each.equals(strings[1])) {
                            player.sendMessage(warning("此世界已存在！"));
                            return false;
                        }
                    }
                    player.sendMessage(warning("处理中...请耐心等待"));
                    WorldCreator worldCreator = new WorldCreator(strings[1]);
                    worldCreator.environment(World.Environment.NORMAL);
                    worldCreator.type(WorldType.NORMAL);
                    worldCreator.createWorld();
                    worlds.add(strings[1]);
                    worldConfig.set("worlds", worlds);
                    try {
                        worldConfig.save(worldsFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(warning("世界已成功生成！"));
                    return true;
                case "load":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            player.sendMessage(warning("此世界已存在！"));
                            return false;
                        }
                    }
                    for (String each : worlds) {
                        if (each.equals(strings[1])) {
                            player.sendMessage(warning("此世界已存在！"));
                            return false;
                        }
                    }
                    player.sendMessage(warning("处理中...请耐心等待"));
                    createWorld(worldConfig, worldsFile, worlds, strings[1], player);
                    return true;
                case "delete":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            worlds.remove(strings[1]);
                            worldConfig.set("worlds", worlds);
                            try {
                                worldConfig.save(worldsFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(warning("此世界已被删除，请重启服务器以生效！"));
                            return true;
                        }
                    }
                    player.sendMessage(warning("未找到这个世界！"));
                    return true;
            }
        }
        return false;
    }

    @Override
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        if (args.length == 1) {
            List<String> tabCommands = new ArrayList<>();
            tabCommands.add("help");
            tabCommands.add("[世界名]");
            tabCommands.add("list");
            if (player.isOp()) {
                tabCommands.add("block");
                tabCommands.add("unblock");
                tabCommands.add("create");
                tabCommands.add("delete");
            }
            return tabCommands;
        } else if (args.length == 2 && player.isOp()) {
            List<String> tabCommands = new ArrayList<>();
            tabCommands.add("[世界名]");
            return tabCommands;
        }
        return null;
    }


    private void multiWorldsHelp(Player player) {
        player.sendMessage(ChatColor.GREEN + "----多世界帮助----");
        player.sendMessage(ChatColor.GOLD + "/mw help" + ChatColor.GRAY + "  打开这个帮助列表");
        player.sendMessage(ChatColor.GOLD + "/mw [世界名]" + ChatColor.GRAY + "  传送到某个世界");
        player.sendMessage(ChatColor.GOLD + "/mw list" + ChatColor.GRAY + "  打开世界列表");
        if (player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "/mw block [世界名]" + ChatColor.GRAY + "  禁止玩家进入某个世界");
            player.sendMessage(ChatColor.GOLD + "/mw unblock [世界名]" + ChatColor.GRAY + "  取消禁止玩家进入某个世界");
            player.sendMessage(ChatColor.GOLD + "/mw create [世界名]" + ChatColor.GRAY + "  生成一个新的世界");
            player.sendMessage(ChatColor.GOLD + "/mw load [世界名]" + ChatColor.GRAY + "  加载世界");
            player.sendMessage(ChatColor.GOLD + "/mw delete [世界名]" + ChatColor.GRAY + "  删除世界");
        }
    }

    public void createWorld(YamlConfiguration worlds_config, File world_file, List<String> worlds, String worldName, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                WorldCreator worldCreator = new WorldCreator(worldName);
                worldCreator.environment(World.Environment.NORMAL);
                worldCreator.type(WorldType.NORMAL);
                worldCreator.createWorld();
                worlds.add(worldName);
                worlds_config.set("worlds", worlds);
                try {
                    worlds_config.save(world_file);
                } catch (IOException e) {
                    e.printStackTrace();
                    player.sendMessage(warning("世界生成失败！"));
                }
                player.sendMessage(warning("世界生成成功！"));
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
    }

    public List<String> getWorlds() {
        List<String> worldList = new ArrayList<>();
        for (World world : UltiTools.getInstance().getServer().getWorlds()) {
            if (world.getName().equalsIgnoreCase("world_nether")) {
                worldList.add("Nether");
            } else if (world.getName().equalsIgnoreCase("world_the_end")) {
                worldList.add("End");
            } else if (world.getName().equalsIgnoreCase("world")) {
                worldList.add("World");
            } else {
                worldList.add(world.getName());
            }
        }
        return worldList;
    }

    private void teleportPlayer(Player player, String world) {
        Location location = UltiTools.getInstance().getServer().getWorld(world).getSpawnLocation();
        if (!player.getWorld().getName().equalsIgnoreCase(world)) {
            teleport(player, location);
        } else {
            player.sendMessage(warning("你就在这个世界！"));
        }
    }

    private void teleport(Player player, Location location){
        HomeCommands.teleportingPlayers.put(player, true);
        Chunk chunk = location.getChunk();
        if(!chunk.isLoaded()){
            chunk.load();
        }
        new BukkitRunnable() {
            float time = 3;

            @Override
            public void run() {
                if (!HomeCommands.teleportingPlayers.get(player)){
                    player.sendTitle(ChatColor.RED + "[多世界插件]传送失败", "请勿移动！", 10, 50, 20);
                    this.cancel();
                    return;
                }
                if (time == 0) {
                    player.teleport(location);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
                    player.sendTitle(ChatColor.GREEN + "[多世界插件]传送成功！", "", 10, 50, 20);
                    HomeCommands.teleportingPlayers.put(player, false);
                    this.cancel();
                    return;
                }
                if ((time / 0.5 % 2) == 0) {
                    player.sendTitle(ChatColor.GREEN + "[多世界插件]正在传送...", "离传送还有" + (int) time + "秒", 10, 70, 20);
                }
                time -= 0.5;
            }
        }.runTaskTimer(UltiTools.getInstance(), 0, 10L);
    }

    private void reviewOldVersionConfig(YamlConfiguration newConfig, YamlConfiguration oldConfig) {
        if (newConfig.get("blocked_worlds") == null) {
            newConfig.set("blocked_worlds", oldConfig.getStringList("blocked_worlds"));
            oldConfig.set("注释38请勿修改", "禁止传送进入的世界(已移动至worlds.yml文件里)");

            for (String world : getWorlds()){
                newConfig.set("world."+world+".type", "GRASS_BLOCK");
                newConfig.set("world."+world+".describe", "无");
            }
            try {
                oldConfig.save(file);
                newConfig.save(worldsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

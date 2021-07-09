package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.enums.Sounds;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.Utils;
import com.ultikits.ultitools.views.WorldsListView;
import org.bukkit.*;
import org.bukkit.command.Command;
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

import static com.ultikits.utils.MessagesUtils.warning;

public class MultiWorldsCommands extends AbstractTabExecutor {

    private static final File worldsFile = new File(ConfigsEnum.WORLDS.toString());
    private static final File file = Utils.getConfigFile();

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
            replaceOriginalWorldName(blockedWorlds);
            if (blockedWorlds.contains(strings[0]) && !player.hasPermission("ultikits.tools.admin")) {
                player.sendMessage(warning(UltiTools.languageUtils.getString("world_not_allow_enter")));
                return true;
            }
            if (worldList.contains(strings[0])) {
                if (strings[0].equalsIgnoreCase("nether")) {
                    teleportPlayer(player, "world_nether");
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
                        player.sendMessage(warning(UltiTools.languageUtils.getString("world_world_not_exits")));
                        return true;
                    }
                }
            } else {
                player.sendMessage(warning(UltiTools.languageUtils.getString("world_world_not_exits")));
            }
            return true;
        } else if (strings.length == 2 && player.isOp()) {
            switch (strings[0]) {
                case "block":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            if (blockedWorlds.contains(strings[1])) {
                                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_not_allow_enter"));
                                return true;
                            }
                            blockedWorlds.add(world.getName());
                            worldConfig.set("blocked_worlds", blockedWorlds);
                            try {
                                worldConfig.save(worldsFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_denied_player_enter"));
                            return true;
                        }
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("world_world_not_found")));
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
                                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_cancel_denied_enter"));
                                return true;
                            }
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_world_does_not_deny_enter"));
                            return true;
                        }
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("world_world_not_found")));
                    return true;
                case "create":
                case "load":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            player.sendMessage(warning(UltiTools.languageUtils.getString("world_already_exists")));
                            return false;
                        }
                    }

                    for (String each : worlds) {
                        if (each.equals(strings[1])) {
                            player.sendMessage(warning(UltiTools.languageUtils.getString("world_already_exists")));
                            return false;
                        }
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("processing")));
                    createWorld(worldConfig, worldsFile, worlds, strings[1], player);
                    return true;
                case "delete":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            blockedWorlds.remove(strings[1]);
                            worlds.remove(strings[1]);
                            worldConfig.set("world." + strings[1], null);
                            worldConfig.set("blocked_worlds", blockedWorlds);
                            worldConfig.set("worlds", worlds);
                            try {
                                worldConfig.save(worldsFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(warning(UltiTools.languageUtils.getString("world_world_deleted")));
                            return true;
                        }
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("world_world_not_found")));
                    return true;
            }
        }else {
            player.sendMessage(warning(UltiTools.languageUtils.getString("wrong_format")));
            multiWorldsHelp(player);
        }
        return false;
    }

    public static void replaceOriginalWorldName(List<String> blockedWorlds) {
        if (blockedWorlds.contains("world_the_end")) {
            blockedWorlds.remove("world_the_end");
            blockedWorlds.add("End");
        }
        if (blockedWorlds.contains("world_nether")) {
            blockedWorlds.remove("world_nether");
            blockedWorlds.add("Nether");
        }
        if (blockedWorlds.contains("world")) {
            blockedWorlds.remove("world");
            blockedWorlds.add("World");
        }
    }

    @Override
    protected @Nullable List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
        if (args.length == 1) {
            List<String> tabCommands = new ArrayList<>();
            tabCommands.add("help");
            tabCommands.add("[" + UltiTools.languageUtils.getString("world_name") + "]");
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
            tabCommands.add("[" + UltiTools.languageUtils.getString("world_name") + "]");
            return tabCommands;
        }
        return null;
    }


    private void multiWorldsHelp(Player player) {
        player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("world_help_header"));
        player.sendMessage(ChatColor.GOLD + "/mw help" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_help"));
        player.sendMessage(ChatColor.GOLD + "/mw [" + UltiTools.languageUtils.getString("world_name") + "]" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_teleport"));
        player.sendMessage(ChatColor.GOLD + "/mw list" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_list"));
        if (player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "/mw block [" + UltiTools.languageUtils.getString("world_name") + "]" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_block"));
            player.sendMessage(ChatColor.GOLD + "/mw unblock [" + UltiTools.languageUtils.getString("world_name") + "]" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_unblock"));
            player.sendMessage(ChatColor.GOLD + "/mw create [" + UltiTools.languageUtils.getString("world_name") + "]" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_create"));
            player.sendMessage(ChatColor.GOLD + "/mw load [" + UltiTools.languageUtils.getString("world_name") + "]" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_load"));
            player.sendMessage(ChatColor.GOLD + "/mw delete [" + UltiTools.languageUtils.getString("world_name") + "]" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_delete"));
        }
    }

    public void createWorld(YamlConfiguration worlds_config, File world_file, List<String> worlds, String worldName, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!worlds.contains(worldName)) {
                    WorldCreator worldCreator = new WorldCreator(worldName);
                    worldCreator.environment(World.Environment.NORMAL);
                    worldCreator.type(WorldType.NORMAL);
                    worldCreator.createWorld();

                    worlds.add(worldName);
                    worlds_config.set("worlds", worlds);
                    worlds_config.set("world." + worldName + ".type", "GRASS_BLOCK");
                    worlds_config.set("world." + worldName + ".describe", UltiTools.languageUtils.getString("none"));
                    try {
                        worlds_config.save(world_file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        player.sendMessage(warning(UltiTools.languageUtils.getString("world_generate_failed")));
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("world_generate_successfully")));
                } else {
                    player.sendMessage(warning(UltiTools.languageUtils.getString("world_already_exists")));
                }
            }
        }.runTask(UltiTools.getInstance());
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
            player.sendMessage(warning(UltiTools.languageUtils.getString("world_you_are_in_this_world")));
        }
    }

    private void teleport(Player player, Location location) {
        HomeCommands.teleportingPlayers.put(player.getUniqueId(), true);
        Chunk chunk = location.getChunk();
        if (!chunk.isLoaded()) {
            chunk.load();
        }
        new BukkitRunnable() {
            float time = 3;

            @Override
            public void run() {
                if (!HomeCommands.teleportingPlayers.get(player.getUniqueId())) {
                    player.sendTitle(ChatColor.RED + UltiTools.languageUtils.getString("world_teleport_failed"), UltiTools.languageUtils.getString("do_not_move"), 10, 50, 20);
                    this.cancel();
                    return;
                }
                if (time == 0) {
                    player.teleport(location);
                    player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.ENTITY_ENDERMAN_TELEPORT), 1, 0);
                    player.sendTitle(ChatColor.GREEN + UltiTools.languageUtils.getString("world_teleport_successfully"), "", 10, 50, 20);
                    HomeCommands.teleportingPlayers.put(player.getUniqueId(), false);
                    this.cancel();
                    return;
                }
                if ((time / 0.5 % 2) == 0) {
                    player.sendTitle(ChatColor.GREEN + UltiTools.languageUtils.getString("world_teleporting"), String.format(UltiTools.languageUtils.getString("world_teleporting_countdown"), (int) time), 10, 70, 20);
                }
                time -= 0.5;
            }
        }.runTaskTimer(UltiTools.getInstance(), 0, 10L);
    }

    private void reviewOldVersionConfig(YamlConfiguration newConfig, YamlConfiguration oldConfig) {
        if (newConfig.get("blocked_worlds") == null) {
            newConfig.set("blocked_worlds", oldConfig.getStringList("blocked_worlds"));

            for (String world : getWorlds()) {
                newConfig.set("world." + world + ".type", "GRASS_BLOCK");
                newConfig.set("world." + world + ".describe", "无");
            }
            try {
                newConfig.save(worldsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

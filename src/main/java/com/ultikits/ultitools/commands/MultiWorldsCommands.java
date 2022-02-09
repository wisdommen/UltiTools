package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.DelayTeleportUtils;
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
import java.util.Objects;

import static com.ultikits.utils.MessagesUtils.warning;

public class MultiWorldsCommands extends AbstractTabExecutor {

    private static final File worldsFile = new File(ConfigsEnum.WORLDS.toString());
    private static final File file = Utils.getConfigFile();

    public static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    public static YamlConfiguration worldConfig = YamlConfiguration.loadConfiguration(worldsFile);

    public static List<String> worlds = worldConfig.getStringList("worlds");
    public static List<String> blockedWorlds = worldConfig.getStringList("blocked_worlds");
    public static List<String> protectedWorlds = worldConfig.getStringList("protected_worlds");
    public static List<String> noPvpWorlds = worldConfig.getStringList("nopvp_worlds");
    public static List<String> noSpawnWorlds = worldConfig.getStringList("nospawn_worlds");


    public MultiWorldsCommands() {
        reviewOldVersionConfig(worldConfig, config);

        replaceOriginalWorldName(blockedWorlds);
        replaceOriginalWorldName(protectedWorlds);
        replaceOriginalWorldName(noPvpWorlds);
        replaceOriginalWorldName(noSpawnWorlds);
    }

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        List<String> worldList = getWorlds();

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
                case "protect":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            if (blockedWorlds.contains(strings[1])) {
                                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_is_protected"));
                                return true;
                            }
                            protectedWorlds.add(world.getName());
                            worldConfig.set("protected_worlds", protectedWorlds);
                            try {
                                worldConfig.save(worldsFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_protected"));
                            return true;
                        }
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("world_world_not_found")));
                    return true;
                case "unprotect":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            if (protectedWorlds.contains(strings[1])) {
                                protectedWorlds.remove(world.getName());
                                worldConfig.set("protected_worlds", protectedWorlds);
                                try {
                                    worldConfig.save(worldsFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_is_unprotected"));
                                return true;
                            }
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_unprotected"));
                            return true;
                        }
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("world_world_not_found")));
                    return true;
                case "nopvp":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            if (noPvpWorlds.contains(strings[1])) {
                                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_is_denied_pvp"));
                                return true;
                            }
                            noPvpWorlds.add(world.getName());
                            worldConfig.set("nopvp_worlds", noPvpWorlds);
                            try {
                                worldConfig.save(worldsFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_denied_pvp"));
                            return true;
                        }
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("world_world_not_found")));
                    return true;
                case "pvp":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            if (noPvpWorlds.contains(strings[1])) {
                                noPvpWorlds.remove(world.getName());
                                worldConfig.set("protected_worlds", noPvpWorlds);
                                try {
                                    worldConfig.save(worldsFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_is_allow_pvp"));
                                return true;
                            }
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_allow_pvp"));
                            return true;
                        }
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("world_world_not_found")));
                    return true;
                case "nospawn":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            if (noPvpWorlds.contains(strings[1])) {
                                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_is_denied_spawn"));
                                return true;
                            }
                            noSpawnWorlds.add(world.getName());
                            worldConfig.set("nospawn_worlds", noSpawnWorlds);
                            try {
                                worldConfig.save(worldsFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_denied_spawn"));
                            return true;
                        }
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("world_world_not_found")));
                    return true;
                case "spawn":
                    for (World world : UltiTools.getInstance().getServer().getWorlds()) {
                        if (strings[1].equals(world.getName())) {
                            if (noSpawnWorlds.contains(strings[1])) {
                                noSpawnWorlds.remove(world.getName());
                                worldConfig.set("nospawn_worlds", noSpawnWorlds);
                                try {
                                    worldConfig.save(worldsFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_is_allow_spawn"));
                                return true;
                            }
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("world_allow_spawn"));
                            return true;
                        }
                    }
                    player.sendMessage(warning(UltiTools.languageUtils.getString("world_world_not_found")));
                    return true;
            }
        } else {
            player.sendMessage(warning(UltiTools.languageUtils.getString("wrong_format")));
            multiWorldsHelp(player);
        }
        return false;
    }

    public static void replaceOriginalWorldName(List<String> worlds) {
        if (worlds.contains("world_the_end")) {
            worlds.remove("world_the_end");
            worlds.add("End");
        }
        if (worlds.contains("world_nether")) {
            worlds.remove("world_nether");
            worlds.add("Nether");
        }
        if (worlds.contains("world")) {
            worlds.remove("world");
            worlds.add("World");
        }
    }

    @Override
    protected @Nullable
    List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] args, @NotNull Player player) {
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
                tabCommands.add("protect");
                tabCommands.add("unprotect");
                tabCommands.add("nopvp");
                tabCommands.add("pvp");
                tabCommands.add("nospawn");
                tabCommands.add("spawn");
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
            player.sendMessage(ChatColor.GOLD + "/mw protect [" + UltiTools.languageUtils.getString("world_name") + "]" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_protect"));
            player.sendMessage(ChatColor.GOLD + "/mw unprotect [" + UltiTools.languageUtils.getString("world_name") + "]" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_unprotect"));
            player.sendMessage(ChatColor.GOLD + "/mw nopvp [" + UltiTools.languageUtils.getString("world_name") + "]" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_nopvp"));
            player.sendMessage(ChatColor.GOLD + "/mw pvp [" + UltiTools.languageUtils.getString("world_name") + "]" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_pvp"));
            player.sendMessage(ChatColor.GOLD + "/mw nospawn [" + UltiTools.languageUtils.getString("world_name") + "]" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_nospawn"));
            player.sendMessage(ChatColor.GOLD + "/mw spawn [" + UltiTools.languageUtils.getString("world_name") + "]" + ChatColor.GRAY + "  " + UltiTools.languageUtils.getString("world_help_spawn"));
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
        Location location = Objects.requireNonNull(UltiTools.getInstance().getServer().getWorld(world)).getSpawnLocation();
        if (!player.getWorld().getName().equalsIgnoreCase(world)) {
            DelayTeleportUtils.delayTeleport(player, location, 3);
        } else {
            player.sendMessage(warning(UltiTools.languageUtils.getString("world_you_are_in_this_world")));
        }
    }

    private void reviewOldVersionConfig(YamlConfiguration newConfig, YamlConfiguration oldConfig) {
        if (newConfig.get("blocked_worlds") == null && oldConfig.get("blocked_worlds") != null) {
            newConfig.set("blocked_worlds", oldConfig.getStringList("blocked_worlds"));
            for (String world : getWorlds()) {
                newConfig.set("world." + world + ".type", "GRASS_BLOCK");
                newConfig.set("world." + world + ".describe", "None");
            }
        }

        if (newConfig.get("protected_worlds") == null) newConfig.set("protected_worlds", new ArrayList<String>());
        if (newConfig.get("nopvp_worlds") == null) newConfig.set("nopvp_worlds", new ArrayList<String>());
        if (newConfig.get("nospawn_worlds") == null) newConfig.set("nospawn_worlds", new ArrayList<String>());

        try {
            newConfig.save(worldsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
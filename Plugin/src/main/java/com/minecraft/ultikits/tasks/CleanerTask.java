package com.minecraft.ultikits.tasks;

import com.minecraft.ultikits.enums.CleanTypeEnum;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.CleanerUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CleanerTask extends BukkitRunnable {
    final static File file = new File(ConfigsEnum.CLEANER.toString());
    final static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    final static String name = config.getString("cleaner_name");
    final static int maxMob = config.getInt("mob_max");
    final static int maxItem = config.getInt("item_max");
    final static int period = config.getInt("clean_period");
    final static int maxEntity = config.getInt("total_entity_max");
    final static int maxChunkDistance = config.getInt("max_chunk_distance");
    final static boolean enableSmartClean = config.getBoolean("enable_smart_clean");
    final static boolean enableCleanEntityTask = config.getBoolean("clean_entity_task_enable");
    final static boolean enableUnloadChunkTask = config.getBoolean("unload_chunk_task_enable");
    final static List<String> cleanWorlds = config.getStringList("clean_worlds");
    final static List<String> cleanTypes = config.getStringList("clean_type");
    private static List<World> worlds = new ArrayList<>();
    private static boolean mobCleaning;
    private static boolean itemCleaning;
    private static boolean entityCleaning = (mobCleaning || itemCleaning);

    private static List<Chunk> chunks = new ArrayList<>();

    private static int time = 0;

    static {
        if (cleanWorlds.size() == 1 && cleanWorlds.contains("all")) {
            worlds = Bukkit.getWorlds();
        } else {
            for (String each : cleanWorlds) {
                World world = Bukkit.getWorld(each);
                if (world != null) {
                    worlds.add(world);
                }
            }
        }
    }

    public static List<Chunk> getUnusedChunks() {
        return chunks;
    }

    @Override
    public void run() {
        if (UltiTools.isProVersion) {
            time += 10;
            if (enableSmartClean) {
                if (CleanerUtils.checkMobs(worlds) > maxMob && !mobCleaning) {
                    notice(UltiTools.languageUtils.getWords("mobs"));
                    mobCleaning = true;
                }
                if (CleanerUtils.checkItems(worlds) > maxItem && !itemCleaning) {
                    notice(UltiTools.languageUtils.getWords("dropped_item"));
                    itemCleaning = true;
                }
                if (CleanerUtils.checkEntities(worlds) > maxEntity && !entityCleaning) {
                    notice(UltiTools.languageUtils.getWords("entity"));
                    entityCleaning = true;
                }
            } else if (enableCleanEntityTask && time >= period) {
                List<CleanTypeEnum> types = new ArrayList<>();
                for (String each : cleanTypes) {
                    types.add(CleanTypeEnum.getTypeByAlis(each));
                }
                for (CleanTypeEnum typeEnum : types) {
                    notice(typeEnum.toString());
                }
            }
            if (enableUnloadChunkTask) {
                chunks = getChucksAwayFromPlayer();
            }
        }
    }

    private static void notice(String typeName) {
        Bukkit.broadcastMessage(ChatColor.GREEN + "[+" + name + String.format("+] " + UltiTools.languageUtils.getWords("clean_start_task_after_30s"), typeName));
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.GREEN + "[+" + name + String.format("+] " + UltiTools.languageUtils.getWords("clean_start_task_after_10s"), typeName));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        int count = 0;
                        if (typeName.equals(UltiTools.languageUtils.getWords("mobs"))) {
                            count = CleanerUtils.cleanMobs(worlds);
                            mobCleaning = false;
                        } else if (typeName.equals(UltiTools.languageUtils.getWords("dropped_item"))) {
                            count = CleanerUtils.cleanDroppedItem(worlds);
                            itemCleaning = false;
                        } else if (typeName.equals(UltiTools.languageUtils.getWords("entity"))) {
                            count = CleanerUtils.cleanEntities(worlds);
                            entityCleaning = (mobCleaning || itemCleaning);
                        }
                        Bukkit.broadcastMessage(CleanerUtils.sendCleanMessage(typeName, name, count));
                        time = 0;
                        cancel();
                    }
                }.runTaskLater(UltiTools.getInstance(), 10 * 20L);
                cancel();
            }
        }.runTaskLater(UltiTools.getInstance(), 20 * 20L);
    }

    private static List<Chunk> getChucksAwayFromPlayer() {
        List<Chunk> chunks = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            if (world.getPlayers().size() == 0) {
                chunks.addAll(Arrays.asList(world.getLoadedChunks()));
                continue;
            }
            for (Player player : world.getPlayers()) {
                for (Chunk chunk : world.getLoadedChunks()) {
                    if (chunk.isLoaded() && world.isChunkInUse(chunk.getX(), chunk.getZ())) {
                        Location playerLocation = player.getLocation();
                        int chunk_x = chunk.getX();
                        int chunk_z = chunk.getZ();
                        Location chunkLocation = new Location(player.getWorld(), chunk_x, playerLocation.getY(), chunk_z);
                        if (playerLocation.distance(chunkLocation) > maxChunkDistance) {
                            chunks.add(chunk);
                        }
                    }
                }
            }
        }
        return chunks;
    }
}

package com.ultikits.ultitools.services;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.CleanTypeEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.*;

import java.util.List;

public class CleanerService {

    private CleanerService() {
    }

    public static int run(CleanTypeEnum cleanType) {
        return run(cleanType, null);
    }

    public static int run(CleanTypeEnum cleanType, List<World> worlds) {
        if (worlds == null) {
            worlds = Bukkit.getWorlds();
        }
        switch (cleanType) {
            case MOBS:
                return cleanMobs(worlds);
            case ITEMS:
                return cleanDroppedItem(worlds);
            case ENTITIES:
                return cleanEntities(worlds);
            case CHECK:
                return checkEntities(worlds);
            default:
                return 0;
        }
    }

    public static int cleanDroppedItem(List<World> worlds) {
        int count = 0;
        for (World world : worlds) {
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof LivingEntity)) {
                    entity.remove();
                    count++;
                }
            }
        }
        return count;
    }

    public static int cleanMobs(List<World> worlds) {
        int count = 0;
        for (World world : worlds) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof LivingEntity) {
                    if (!(entity instanceof Player)) {
                        if (canMobBeClean((LivingEntity) entity)) {
                            List<String> list = ConfigController.getConfig("cleaner").getStringList("clean_whitelist");
                            boolean doClean = true;
                            if (!(list == null || list.size() == 0)) {
                                for (String whiteListEntity : list) {
                                    if (entity.getType() == EntityType.valueOf(whiteListEntity)) {
                                        doClean = false;
                                    }
                                }
                            }
                            if (doClean) {
                                entity.remove();
                                count++;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    public static int cleanEntities(List<World> worlds) {
        return cleanMobs(worlds) + cleanDroppedItem(worlds);
    }

    public static int checkEntities(List<World> worlds) {
        return checkItems(worlds) + checkMobs(worlds);
    }

    public static int checkItems(List<World> worlds) {
        int count = 0;
        for (World world : worlds) {
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof LivingEntity)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int checkMobs(List<World> worlds) {
        int count = 0;
        for (World world : worlds) {
            for (Entity entity : world.getEntities()) {
                if ((entity instanceof LivingEntity) && !(entity instanceof Player)) {
                    if (canMobBeClean((LivingEntity) entity)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private static boolean canMobBeClean(LivingEntity entity) {
        if (!entity.hasGravity()) return false;
        if (entity.getCustomName() != null) return false;
        if (entity instanceof Tameable) {
            if (((Tameable) entity).isTamed()) {
                return false;
            }
        }
        List<String> list = ConfigController.getConfig("cleaner").getStringList("clean_whitelist");
        if (!(list == null || list.size() == 0)) {
            for (String whiteListEntity : list) {
                if (entity.getType() == EntityType.valueOf(whiteListEntity)) {
                    return false;
                }
            }
        }
        return !entity.isCustomNameVisible();
    }

    public static String sendMessage(CleanTypeEnum cleanType, String name, int cleanCount) {
        switch (cleanType) {
            case CHECK:
                return ChatColor.GREEN + String.format(UltiTools.languageUtils.getString("clean_check_report"), name, ChatColor.RED + String.valueOf(cleanCount) + ChatColor.GREEN, ChatColor.RED + cleanType.toString() + ChatColor.GREEN);
            default:
                return ChatColor.GREEN + String.format(UltiTools.languageUtils.getString("clean_clean_report"), name, ChatColor.RED + String.valueOf(cleanCount) + ChatColor.GREEN, ChatColor.RED + cleanType.toString());
        }
    }

    public static String sendCleanMessage(String cleanName, String name, int cleanCount) {
        return ChatColor.GREEN + String.format(UltiTools.languageUtils.getString("clean_clean_report"), name, ChatColor.RED + String.valueOf(cleanCount) + ChatColor.GREEN, ChatColor.RED + cleanName);
    }
}

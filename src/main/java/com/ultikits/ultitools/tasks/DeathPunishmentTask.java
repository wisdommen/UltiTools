package com.ultikits.ultitools.tasks;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.DeathPunishUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DeathPunishmentTask {
    protected static List<UUID> punishQueue = new ArrayList<>();

    static {
        new Timer().runTaskTimerAsynchronously(UltiTools.getInstance(), 0, 20);
    }

    public static void addPlayerToQueue(Player player) {
        if (punishQueue.contains(player.getUniqueId())){
            return;
        }
        punishQueue.add(player.getUniqueId());
    }

    public static boolean isEnableMoneyDrop() {
        return (boolean) ConfigController.getValue("enable_money_drop");
    }

    public static boolean isEnablePunishCommands() {
        return (boolean) ConfigController.getValue("enable_punish_commands");
    }

    public static boolean isEnableItemDrop() {
        return (boolean) ConfigController.getValue("enable_item_drop");
    }

    public static int getMoneyDrop() {
        return (int) ConfigController.getValue("money_dropped_ondeath");
    }

    public static int getItemDrop() {
        return (int) ConfigController.getValue("item_dropped_ondeath");
    }

    public static List<String> getPunishCommands() {
        return ConfigController.getStringList("punish_command");
    }

    public static List<String> getWorldsEnabledItemDrop() {
        return ConfigController.getStringList("worlds_enabled_item_drop");
    }

    public static List<String> getWorldsEnabledMoneyDrop() {
        return ConfigController.getStringList("worlds_enabled_money_drop");
    }

    public static List<String> getWorldsEnabledPunishCommand() {
        return ConfigController.getStringList("worlds_enabled_punish_commands");
    }
}

class Timer extends BukkitRunnable{

    @Override
    public void run() {
        Iterator<UUID> iterator = DeathPunishmentTask.punishQueue.iterator();
        while (iterator.hasNext()){
            UUID uuid;
            try {
                uuid = iterator.next();
            }catch (ConcurrentModificationException e){
                continue;
            }
            Player player = Bukkit.getPlayer(uuid);
            if (player==null){
                continue;
            }
            String world = player.getWorld().getName();
            List<String> list;
            if (DeathPunishmentTask.isEnableItemDrop()) {
                list = DeathPunishmentTask.getWorldsEnabledItemDrop();
                for (String s : list) {
                    if (s.equals(world)) {
                        DeathPunishUtils.takeItem(player, DeathPunishmentTask.getItemDrop());
                        player.sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getString("punish_item_dropped"), DeathPunishmentTask.getItemDrop()));
                    }
                }
            }

            if (DeathPunishmentTask.isEnableMoneyDrop()) {
                list = DeathPunishmentTask.getWorldsEnabledMoneyDrop();
                for (String s : list) {
                    if (s.equals(world)) {
                        DeathPunishUtils.takeMoney(player, DeathPunishmentTask.getMoneyDrop());
                        player.sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getString("punish_money_dropped"), DeathPunishmentTask.getMoneyDrop()));
                    }
                }
            }

            if (DeathPunishmentTask.isEnablePunishCommands()) {
                list = DeathPunishmentTask.getWorldsEnabledPunishCommand();
                for (String s : list) {
                    if (s.equals(world)) {
                        DeathPunishUtils.Exec(DeathPunishmentTask.getPunishCommands(), player.getName());
                    }
                }
            }
            DeathPunishmentTask.punishQueue.remove(uuid);
        }
    }
}

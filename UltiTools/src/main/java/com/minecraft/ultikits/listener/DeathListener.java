package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.config.ConfigController;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.DeathPunishUtils;
import com.minecraft.ultikits.utils.LanguageUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

/**
 * @author qianmo, wisdomme
 */
public class DeathListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Player player;
        List<String> list;
        String world;
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
            player = (Player) event.getEntity();
            world = player.getWorld().getName();

        if (player.getHealth() > event.getFinalDamage()) {
            return;
        }

        if (isEnableItemDrop()) {
            list = getWorldsEnabledItemDrop();
            for (String s : list) {
                if (s.equals(world)) {
                    DeathPunishUtils.takeItem(player, getItemDrop());
                    player.sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getWords("punish_item_dropped"), getItemDrop()));
                }
            }
        }

        if (isEnableMoneyDrop()) {
            list = getWorldsEnabledMoneyDrop();
            for (String s : list) {
                if (s.equals(world)) {
                    DeathPunishUtils.takeMoney(player, getMoneyDrop());
                    player.sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getWords("punish_money_dropped"), getMoneyDrop()));
                }
            }
        }

        if (isEnablePunishCommands()) {
            list = getWorldsEnabledPunishCommand();
            for (String s : list) {
                if (s.equals(world)) {
                    DeathPunishUtils.Exec(getPunishCommands(), player.getName());
                }
            }
        }
    }

    private boolean isEnableMoneyDrop() {
        return (boolean) ConfigController.getValue("enable_money_drop");
    }

    private boolean isEnablePunishCommands() {
        return (boolean) ConfigController.getValue("enable_punish_commands");
    }

    private boolean isEnableItemDrop() {
        return (boolean) ConfigController.getValue("enable_item_drop");
    }

    private int getMoneyDrop() {
        return (int) ConfigController.getValue("money_dropped_ondeath");
    }

    private int getItemDrop() {
        return (int) ConfigController.getValue("item_dropped_ondeath");
    }

    private List<String> getPunishCommands() {
        return (List<String>) ConfigController.getValue("punish_command");
    }

    private List<String> getWorldsEnabledItemDrop() {
        return (List<String>) ConfigController.getValue("worlds_enabled_item_drop");
    }

    private List<String> getWorldsEnabledMoneyDrop() {
        return (List<String>) ConfigController.getValue("worlds_enabled_money_drop");
    }

    private List<String> getWorldsEnabledPunishCommand() {
        return (List<String>) ConfigController.getValue("worlds_enabled_punish_commands");
    }
}

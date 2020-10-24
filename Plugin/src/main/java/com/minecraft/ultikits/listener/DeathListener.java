package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.config.ConfigController;
import com.minecraft.ultikits.ultitools.UltiTools;
import com.minecraft.ultikits.utils.DeathPunishUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        if (isEnabled()) {
            if(isEnableItemDrop()) {
                DeathPunishUtils.takeItem(player, 1);
            }
        }
    }

    private boolean isEnabled() {
        return UltiTools.getInstance().getConfig().getBoolean("death_punish");
    }

    private boolean isEnableMoneyDrop() {
        return (boolean) ConfigController.getValue("enable_money_drop");
    }

    private boolean isEnablePunishCommands() {
        return (boolean) ConfigController.getValue("enable_punish_commands");
    }

    public boolean isEnableItemDrop() {
        return (boolean) ConfigController.getValue("enable_item_drop");
    }
}

package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (isEnabled()) {
            //TODO 处理玩家死亡事件
        }
    }

    private boolean isEnabled() {
        return UltiTools.getInstance().getConfig().getBoolean("death_punish");
    }
}

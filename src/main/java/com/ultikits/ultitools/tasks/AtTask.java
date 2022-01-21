package com.ultikits.ultitools.tasks;

import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AtTask extends BukkitRunnable {

    private final Player player;
    private final String message;
    double timer = 1.00;

    public AtTask (Player To, String Message) {
        player = To;
        message = Message;
    }

    @Override
    public void run() {
        if (timer > 0.01) {
            UltiTools.versionAdaptor.sendActionBar(player, ChatColor.BOLD + message);
            timer = timer - 0.01;
        } else {
            this.cancel();
        }
    }
}

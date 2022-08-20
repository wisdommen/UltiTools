package com.ultikits.ultitools.tasks;

import com.ultikits.ultitools.services.ChatService;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class AtCDTask extends BukkitRunnable {
    private final int cd;
    private final String playerName;

    public AtCDTask (int cd, String playerName) {
        ChatService.AtCD.add(playerName);
        this.cd = cd;
        this.playerName = playerName;
    }

    @Override
    public void run() {
        Date d = new Date(System.currentTimeMillis() + cd * 1000L);
        while(new Date().before(d)){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ChatService.AtCD.remove(playerName);
    }
}

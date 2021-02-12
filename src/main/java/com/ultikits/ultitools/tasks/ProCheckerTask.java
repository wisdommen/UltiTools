package com.ultikits.ultitools.tasks;

import com.ultikits.ultitools.checker.NewProChecker;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class ProCheckerTask extends BukkitRunnable {
    @Override
    public void run() {
        if (UltiTools.getInstance().getConfig().getBoolean("enable_pro")) {
            try {
                String res = NewProChecker.validatePro();
                if (res.equals("Pro Version Activated!")) {
                    UltiTools.isProVersion = true;
                } else {
                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiTools] " + res);
                    UltiTools.isProVersion = false;
                }
            } catch (Exception ignored) {
            }
        }
    }
}

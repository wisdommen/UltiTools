package com.ultikits.ultitools.tasks;

import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class ProCheckerTask extends BukkitRunnable {
    @Override
    public void run() {
        if (UltiTools.getInstance().getConfig().getBoolean("enable_pro")) {
            try {
                String res = UltiTools.getInstance().getProChecker().validatePro();
                if (!res.equals("Pro Version Activated!")) {
                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiTools] " + res);
                }
            } catch (Exception ignored) {
                try {
                    UltiTools.getInstance().getProChecker().validatePro();
                }catch (Exception ignored1){
                }
            }
        }
    }
}

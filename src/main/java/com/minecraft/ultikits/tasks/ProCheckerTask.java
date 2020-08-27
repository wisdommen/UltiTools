package com.minecraft.ultikits.tasks;

import com.minecraft.ultikits.checker.prochecker.ProChecker;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.net.ConnectException;

public class ProCheckerTask extends BukkitRunnable {
    @Override
    public void run() {
        if (UltiTools.getInstance().getConfig().getBoolean("enable_pro")) {
            try {
                int res = ProChecker.run();
                if (res==200) {
                    UltiTools.isProVersion = true;
                }
            }catch (Exception ignored){
            }
        }
    }
}

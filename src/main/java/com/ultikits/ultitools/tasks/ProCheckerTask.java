package com.ultikits.ultitools.tasks;

import com.ultikits.beans.CheckResponse;
import com.ultikits.ultitools.checker.prochecker.ProChecker;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.scheduler.BukkitRunnable;

public class ProCheckerTask extends BukkitRunnable {
    @Override
    public void run() {
        if (UltiTools.getInstance().getConfig().getBoolean("enable_pro")) {
            try {
                CheckResponse res = ProChecker.run();
                if (res.code.equals("200")) {
                    UltiTools.isProVersion = true;
                }
            }catch (Exception ignored){
            }
        }
    }
}

package com.minecraft.ultikits.tasks;

import com.minecraft.ultikits.beans.CheckResponse;
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
                CheckResponse res = ProChecker.run();
                if (res.code.equals("200")) {
                    UltiTools.isProVersion = true;
                }
            }catch (Exception ignored){
            }
        }
    }
}

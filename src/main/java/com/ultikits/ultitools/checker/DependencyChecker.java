package com.ultikits.ultitools.checker;

import org.bukkit.Bukkit;

public class DependencyChecker {

    private DependencyChecker() {
    }

    public static boolean isUltiCoreUpToDate(){
        int UltiCoreVersionRequired = 117;
        int UltiCoreVersionCurrent = Integer.parseInt(Bukkit.getPluginManager().getPlugin("UltiCore").getDescription().getVersion().replaceAll("\\.", ""));
        return UltiCoreVersionCurrent >= UltiCoreVersionRequired;
    }
}

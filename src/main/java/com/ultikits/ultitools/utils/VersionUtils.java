package com.ultikits.ultitools.utils;

import com.ultikits.ultitools.ultitools.UltiTools;

public class VersionUtils {

    public static boolean isLegacyMCVersion(){
        String version = UltiTools.getInstance().getServer().getVersion();
        return Integer.parseInt(version.split("MC: ")[1].replaceAll("[.)]", "")) < 1130;
    }
}

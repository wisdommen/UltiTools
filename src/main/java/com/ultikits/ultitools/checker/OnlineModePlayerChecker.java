package com.ultikits.ultitools.checker;

import java.net.MalformedURLException;
import java.net.URL;

public class OnlineModePlayerChecker {

    public static boolean isOnlineMode(String PlayerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/oyo123" + PlayerName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

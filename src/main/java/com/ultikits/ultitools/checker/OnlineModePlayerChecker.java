package com.ultikits.ultitools.checker;

import java.net.HttpURLConnection;
import java.net.URL;

public class OnlineModePlayerChecker {

    public static boolean isOnlineMode(String PlayerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + PlayerName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == 200) return true;
            if (connection.getResponseCode() == 204) return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}

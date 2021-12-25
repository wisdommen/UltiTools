package com.ultikits.ultitools.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SilentOpenUtils {
    private static List<Player> players = new ArrayList<>();

    public static List<Player> getPlayers() {
        return players;
    }

    public static void setPlayers(List<Player> players) {
        SilentOpenUtils.players = players;
    }
}

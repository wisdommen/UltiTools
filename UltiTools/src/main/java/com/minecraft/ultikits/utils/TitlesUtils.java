package com.minecraft.ultikits.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TitlesUtils {

    public static void setPrefixSuffix(Player player, String prefix, String suffix) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.canSee(player)) {
                continue;
            }
            Scoreboard scoreboard = online.getScoreboard();
            if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            }
            Team team = scoreboard.getTeam(player.getName());
            if (team == null) {
                team = scoreboard.registerNewTeam(player.getName());
            }
            team.setPrefix(prefix);
            team.setSuffix(suffix);
            team.addEntry(player.getName());
            online.setScoreboard(scoreboard);
        }
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.canSee(player)) {
                continue;
            }
            if (online.getUniqueId().equals(player.getUniqueId()))
                continue;
            Scoreboard scoreboard = player.getScoreboard();
            if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            }
            Team team = scoreboard.getTeam(online.getName());
            if (team == null) {
                team = scoreboard.registerNewTeam(online.getName());
            }

            team.setPrefix(player.getScoreboard().getTeam(online.getName()).getPrefix());
            team.setSuffix(player.getScoreboard().getTeam(online.getName()).getSuffix());
            team.addEntry(online.getName());
            player.setScoreboard(scoreboard);
        }
    }
}

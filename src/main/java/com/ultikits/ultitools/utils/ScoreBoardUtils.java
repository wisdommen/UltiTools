package com.ultikits.ultitools.utils;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreBoardUtils {

    public static Map<UUID, Map<Integer, String>> boardMap = new HashMap<>();
    public static ScoreboardManager sb = Bukkit.getScoreboardManager();
    public static Map<UUID, Scoreboard> scoreboardMap = new HashMap<>();
    private static Scoreboard newScoreboard = sb.getNewScoreboard();


    static {
        for (Player player : Bukkit.getOnlinePlayers()) {
            scoreboardMap.put(player.getUniqueId(), sb.getNewScoreboard());
        }
    }

    public static void registerPlayer(UUID playerId) {
        scoreboardMap.put(playerId, sb.getNewScoreboard());
    }

    public static void unregisterPlayer(UUID playerId) {
        scoreboardMap.remove(playerId);
        boardMap.remove(playerId);
    }

    /**
     * 可以使用这个方法非常方便的更新侧边栏内容。
     * 此方法将侧边栏的分数作为唯一键，分数不可
     * 重复否则后者覆盖前者。设置line参数为null
     * 可将此行隐藏。
     *
     * @param player 需要更新侧边栏的玩家
     * @param line 需要更新的内容
     * @param scoreSlot 需要更新的行数
     */
    public static void updateLine(Player player, String line, int scoreSlot) {
        if (!Bukkit.getOnlinePlayers().contains(player) || (player.getScoreboard() == null)) {
            return;
        }
        Objective information = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
        Scoreboard scoreboard = scoreboardMap.get(player.getUniqueId());
        if (information == null) {
            String title = "Welcome!";
            try {
                title = ConfigController.getConfig("sidebar").getString("scoreBoardTitle");
            } catch (NullPointerException ignored) {
            }
            if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) {
                String name = player.getName();
                while (name.length() > 16) {
                    name = name.substring(0, 15);
                }
                information = UltiTools.versionAdaptor.registerNewObjective(scoreboard, name, "", ChatColor.DARK_AQUA + title);
                information.setDisplaySlot(DisplaySlot.SIDEBAR);
            }
        }
        Map<Integer, String> scoreMap = boardMap.get(player.getUniqueId());
        if (scoreMap == null) {
            scoreMap = new HashMap<>();
        }
        if (scoreMap.containsKey(scoreSlot)) {
            String str = scoreMap.get(scoreSlot);
            if (str.equals(line)){
                return;
            }
            player.getScoreboard().resetScores(str);
        }
        if (line != null){
            scoreMap.put(scoreSlot, line);
            boardMap.put(player.getUniqueId(), scoreMap);
            information.getScore(line).setScore(scoreSlot);
        }
        player.setScoreboard(scoreboard);
    }

    public static void clearScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(sb.getNewScoreboard());
        }
    }

    public static Scoreboard getNewScoreboard(){
        return newScoreboard;
    }

}

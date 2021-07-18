package com.ultikits.ultitools.tasks;

import com.google.common.collect.Lists;
import com.ultikits.beans.EmailContentBean;
import com.ultikits.ultitools.beans.ArmorsBean;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.listener.ChatListener;
import com.ultikits.ultitools.manager.EmailManager;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.EconomyUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

import static com.minecraft.Ultilevel.utils.checkLevel.*;


public class SideBarTask extends BukkitRunnable {

    boolean isPAPILoaded = UltiTools.isPAPILoaded;
    static ScoreboardManager sb = Bukkit.getScoreboardManager();
    static Map<UUID, Scoreboard> scoreboardMap = new HashMap<>();
    static Map<UUID, Map<Integer, String>> boardMap = new HashMap<>();

    static {
        for (Player player : Bukkit.getOnlinePlayers()) {
            scoreboardMap.put(player.getUniqueId(), sb.getNewScoreboard());
        }
        scoreboardMap.put(null, sb.getNewScoreboard());
    }

    public static void registerPlayer(UUID playerId) {
        scoreboardMap.put(playerId, sb.getNewScoreboard());
    }

    @Override
    public void run() {
        File sbFile = new File(ConfigsEnum.SIDEBAR_DATA.toString());
        YamlConfiguration sbConfig = YamlConfiguration.loadConfiguration(sbFile);
        List<String> players = sbConfig.getStringList("player_closed_sb");
        for (Player player : Bukkit.getOnlinePlayers()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!players.contains(player.getName())) {
                        setUpPlayerSideBar(player);
                        player.setScoreboard(scoreboardMap.get(player.getUniqueId()));
                    } else {
                        player.setScoreboard(scoreboardMap.get(null));
                    }
                }
            }.runTaskAsynchronously(UltiTools.getInstance());
        }
    }

    public void setUpPlayerSideBar(Player player) {
        String name = "";
        String onLinePlayers = "";
        String CDq = "";
        String CDw = "";
        String CDe = "";
        String CDr = "";
        String money = "";
        String deposit = "";
        String level_num = "";
        String exp = "";
        String max_exp = "";
        String mp = "";
        String max_mp = "";
        String hp = "";
        String max_hp = "";
        boolean isWizard = false;
        String occupation = "";
        String LatestAtt = "";

        if (isPAPILoaded && UltiTools.getInstance().getConfig().getBoolean("enable_PAPI")) {
            name = setPlaceholderString(player, "name");
            onLinePlayers = setPlaceholderString(player, "online_player");
            CDq = setPlaceholderString(player, "CDq");
            CDw = setPlaceholderString(player, "CDw");
            CDe = setPlaceholderString(player, "CDe");
            CDr = setPlaceholderString(player, "CDr");
            money = setPlaceholderString(player, "money");
            deposit = setPlaceholderString(player, "deposit");
            level_num = setPlaceholderString(player, "level");
            exp = setPlaceholderString(player, "exp");
            max_exp = setPlaceholderString(player, "max_exp");
            mp = setPlaceholderString(player, "mp");
            hp = setPlaceholderString(player, "hp");
            max_hp = setPlaceholderString(player, "max_hp");
            max_mp = setPlaceholderString(player, "max_mp");
            occupation = setPlaceholderString(player, "occupation");
            isWizard = true;
        } else {
            name = player.getName();
            onLinePlayers = Bukkit.getOnlinePlayers().size() + "";
            money = EconomyUtils.checkMoney(player) + "";
            deposit = EconomyUtils.checkBank(player) + "";
            if (Bukkit.getPluginManager().getPlugin("UltiLevel") != null) {
                DecimalFormat format = new DecimalFormat("0.0");
                CDq = coolDown(player, "CDq") + "";
                CDw = coolDown(player, "CDw") + "";
                CDe = coolDown(player, "CDe") + "";
                CDr = coolDown(player, "CDr") + "";
                level_num = checkLevel(player) + "";
                exp = checkExp(player) + "";
                max_exp = ((Integer.parseInt(level_num) - 1) * 5 + 100) + "";
                mp = getPlayerMagicPoint(player) + "";
                hp = format.format(player.getHealth());
                max_hp = format.format(getPlayerMaxHealth(player)) + "";
                max_mp = getPlayerMaxMagicPoint(player) + "";
                occupation = checkJob(player);
                isWizard = isWizard(player);
            }
        }

        if(ChatListener.getLatestAtt().containsKey(player.getName())) {
            LatestAtt = ChatListener.getLatestAtt().get(player.getName());
        }

        setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_money") + " " + ChatColor.GOLD, money, 97);
        setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_deposit") + " " + ChatColor.GOLD, deposit, 96);
        setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_level") + " " + ChatColor.GOLD, level_num, 95);
        setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_job") + " " + ChatColor.GOLD, occupation, 98);
        setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_name") + " " + ChatColor.GOLD, name, 99);
        setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_online_player") + " " + ChatColor.GOLD, onLinePlayers, 0);
        setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_health") + " " + ChatColor.YELLOW + hp + ChatColor.BOLD + " / " + ChatColor.GOLD, max_hp, 93);
        setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("chat_att_tip") + " " + ChatColor.LIGHT_PURPLE, LatestAtt, 91);
        int unread = getUnReadEmailNum(player);
        setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_new_email") + " " + ChatColor.GOLD, unread + UltiTools.languageUtils.getString("feng"), 92);
        if (unread == 0) {
            setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_new_email") + " " + ChatColor.GOLD, unread + UltiTools.languageUtils.getString("feng"), 92, true);
        }
        if (!max_exp.equals("") && !exp.equals("")) {
            setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_exp") + " " + ChatColor.YELLOW, exp + ChatColor.BOLD + " / " + ChatColor.GOLD + max_exp, 94);
        }
        if (CDq != null && !CDq.equals("") && Integer.parseInt(CDq) > 0) {
            setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_Q_countdown") + " " + ChatColor.GOLD, CDq + UltiTools.languageUtils.getString("second"), 89);
        }
        if (CDw != null && !CDw.equals("") && Integer.parseInt(CDw) > 0) {
            setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_W_countdown") + " " + ChatColor.GOLD, CDw + UltiTools.languageUtils.getString("second"), 88);
        }
        if (CDe != null && !CDe.equals("") && Integer.parseInt(CDe) > 0) {
            setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_E_countdown") + " " + ChatColor.GOLD, CDe + UltiTools.languageUtils.getString("second"), 87);
        }
        if (CDr != null && !CDr.equals("") && Integer.parseInt(CDr) > 0) {
            setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_R_countdown") + " " + ChatColor.GOLD, CDr + UltiTools.languageUtils.getString("second"), 86);
        }
        if (isWizard && mp != null && max_mp != null && !mp.equals("") && !max_mp.equals("")) {
            setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_magic") + "" + ChatColor.YELLOW, mp + ChatColor.BOLD + "/" + ChatColor.GOLD + max_mp, 90);
        }

        if (UltiTools.getInstance().getConfig().getBoolean("enable_armor_check")) {
            ArmorsBean armorsManager = new ArmorsBean(player);
            String H = setArmorString(String.valueOf(armorsManager.getHatDurability()));
            String C = setArmorString(String.valueOf(armorsManager.getChestDurability()));
            String L = setArmorString(String.valueOf(armorsManager.getLegDurability()));
            String B = setArmorString(String.valueOf(armorsManager.getBootsDurability()));
            String M = setArmorString(String.valueOf(armorsManager.getMainHandDurability()));
            String O = setArmorString(String.valueOf(armorsManager.getOffHandDurability()));

            updatePerLine(player, ChatColor.GREEN + "H " + H + " C " + C, -1);
            updatePerLine(player, ChatColor.GREEN + "L " + L + " B " + B, -2);
            updatePerLine(player, ChatColor.GREEN + "M " + M + " O " + O, -3);
        }
        setCustomLine(player);
    }

    public static Integer getUnReadEmailNum(Player player) {
        if (!UltiTools.getInstance().getConfig().getBoolean("enable_email")) {
            return 0;
        }
        File file = new File(UltiTools.getInstance().getDataFolder() + "/emailData", player.getName() + ".yml");
        EmailManager emailManager = new EmailManager(file);
        Map<String, EmailContentBean> emailContentManagerMap = emailManager.getEmails();
        int i = 0;
        for (String each : emailContentManagerMap.keySet()) {
            EmailContentBean emailContentManager = emailContentManagerMap.get(each);
            if (!emailContentManager.getRead()) {
                i++;
            }
        }
        return i;
    }

    public String setPlaceholderString(Player player, String string) {
        try {
            return Objects.requireNonNull(PlaceholderAPI.setPlaceholders(player, ConfigController.getConfig("sidebar").getString(string)));
        } catch (Exception e) {
            return "";
        }
    }

    public static void setScoreboard(Player player, String prefixString, String content, int score) {
        setScoreboard(player, prefixString, content, score, false);
    }

    public static void setScoreboard(Player player, String prefixString, String content, int score, boolean reset) {
        if (!content.equals("")) {
            updatePerLine(player, prefixString + content, score, reset);
        }
    }

    public String setArmorString(String string) {
        if ("-1".equals(string)) {
            string = UltiTools.languageUtils.getString("none");
        } else if ("0".equals(string)) {
            string = "∞";
        }
        return string;
    }

    public void setCustomLine(Player player) {
        List<String> customer_line = Lists.reverse(ConfigController.getConfig("sidebar").getStringList("customerline"));
        int i = 1;
        for (String each_line : customer_line) {
            updatePerLine(player, PlaceholderAPI.setPlaceholders(player, each_line), i);
            i++;
        }
    }

    public static void updatePerLine(Player player, String line, int scoreSlot) {
        updatePerLine(player, line, scoreSlot, false);
    }

    public static void updatePerLine(Player player, String line, int scoreSlot, boolean reset) {
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
            player.getScoreboard().resetScores(str);
        }
        scoreMap.put(scoreSlot, line);
        boardMap.put(player.getUniqueId(), scoreMap);
        if (reset){
            player.getScoreboard().resetScores(line);
        }else {
            information.getScore(line).setScore(scoreSlot);
        }
        player.setScoreboard(scoreboard);
    }

    public static void clearScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(sb.getNewScoreboard());
        }
    }
}

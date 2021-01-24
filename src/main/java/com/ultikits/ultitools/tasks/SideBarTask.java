package com.ultikits.ultitools.tasks;

import com.google.common.collect.Lists;
import com.ultikits.beans.EmailContentBean;
import com.ultikits.ultitools.beans.ArmorsBean;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.minecraft.Ultilevel.utils.checkLevel.*;


public class SideBarTask extends BukkitRunnable {

    boolean isPAPILoaded = UltiTools.isPAPILoaded;

    @Override
    public void run() {
        File sbFile = new File(ConfigsEnum.SIDEBAR_DATA.toString());
        YamlConfiguration sbConfig = YamlConfiguration.loadConfiguration(sbFile);
        List<String> players = sbConfig.getStringList("player_closed_sb");
        for (Player player : Bukkit.getOnlinePlayers()) {
            // 创建一个计分板管理对象
            ScoreboardManager sb = Bukkit.getScoreboardManager();
            // 创建一个计分板
            Scoreboard scoreboard = sb.getNewScoreboard();

            //创建一个计分板对象
            String title="Welcome!";
            try {
                title = ConfigController.getConfig("sidebar").getString("scoreBoardTitle");
            }catch (NullPointerException ignored){
            }
            Objective information = UltiTools.versionAdaptor.registerNewObjective(scoreboard, "侧边栏", "", ChatColor.DARK_AQUA + title);
            //设置计分板样式
            information.setDisplaySlot(DisplaySlot.SIDEBAR);

            if (!players.contains(player.getName())) {
                setUpPlayerSideBar(information, player);
                player.setScoreboard(scoreboard);
            } else {
                player.setScoreboard(sb.getNewScoreboard());
            }
        }

    }

    public void setUpPlayerSideBar(Objective scoreboard, Player player){
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

        setScoreboard(scoreboard, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_money")+" " + ChatColor.GOLD , money, 97);
        setScoreboard(scoreboard, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_deposit")+" " + ChatColor.GOLD , deposit, 96);
        setScoreboard(scoreboard, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_level")+" " + ChatColor.GOLD , level_num, 95);
        setScoreboard(scoreboard, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_job")+" " + ChatColor.GOLD , occupation, 98);
        setScoreboard(scoreboard, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_name")+" " + ChatColor.GOLD , name, 99);
        setScoreboard(scoreboard,ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_online_player")+" " + ChatColor.GOLD , onLinePlayers,0);
        setScoreboard(scoreboard, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_health")+" " + ChatColor.YELLOW + hp + ChatColor.BOLD + " / " + ChatColor.GOLD , max_hp, 93);
        if (getUnReadEmailNum(player) > 0) {
            Score mail = scoreboard.getScore(ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_new_email")+" " + ChatColor.GOLD + getUnReadEmailNum(player) + UltiTools.languageUtils.getString("feng"));
            mail.setScore(92);
        }
        if (!max_exp.equals("") && !exp.equals("")) {
            Score level = scoreboard.getScore(ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_exp")+" " + ChatColor.YELLOW + exp + ChatColor.BOLD + " / " + ChatColor.GOLD + max_exp);
            level.setScore(94);
        }
        if (CDq != null && !CDq.equals("") && Integer.parseInt(CDq) > 0) {
            Score CD = scoreboard.getScore(ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_Q_countdown")+" " + ChatColor.GOLD + CDq + UltiTools.languageUtils.getString("second"));
            CD.setScore(89);
        }
        if (CDw != null && !CDw.equals("") && Integer.parseInt(CDw) > 0) {
            Score CD = scoreboard.getScore(ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_W_countdown")+" " + ChatColor.GOLD + CDw + UltiTools.languageUtils.getString("second"));
            CD.setScore(88);
        }
        if (CDe != null && !CDe.equals("") && Integer.parseInt(CDe) > 0) {
            Score CD = scoreboard.getScore(ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_E_countdown")+" " + ChatColor.GOLD + CDe + UltiTools.languageUtils.getString("second"));
            CD.setScore(87);
        }
        if (CDr != null && !CDr.equals("") && Integer.parseInt(CDr) > 0) {
            Score CD = scoreboard.getScore(ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_R_countdown")+" " + ChatColor.GOLD + CDr + UltiTools.languageUtils.getString("second"));
            CD.setScore(86);
        }
        if (isWizard && mp != null && max_mp != null && !mp.equals("")&& !max_mp.equals("")) {
            Score magic = scoreboard.getScore(ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_magic")+"" + ChatColor.YELLOW + mp + ChatColor.BOLD + "/" + ChatColor.GOLD + max_mp);
            magic.setScore(90);
        }

        if (UltiTools.getInstance().getConfig().getBoolean("enable_armor_check")) {
            ArmorsBean armorsManager = new ArmorsBean(player);
            String H = setArmorString(String.valueOf(armorsManager.getHatDurability()));
            String C = setArmorString(String.valueOf(armorsManager.getChestDurability()));
            String L = setArmorString(String.valueOf(armorsManager.getLegDurability()));
            String B = setArmorString(String.valueOf(armorsManager.getBootsDurability()));
            String M = setArmorString(String.valueOf(armorsManager.getMainHandDurability()));
            String O = setArmorString(String.valueOf(armorsManager.getOffHandDurability()));

            Score equipment1 = scoreboard.getScore(ChatColor.GREEN + "H " + H + " C " + C);
            Score equipment2 = scoreboard.getScore(ChatColor.GREEN + "L " + L + " B " + B);
            Score equipment3 = scoreboard.getScore(ChatColor.GREEN + "M " + M + " O " + O);
            equipment1.setScore(-1);
            equipment2.setScore(-2);
            equipment3.setScore(-3);
        }

        setCustomLine(scoreboard, player);
    }

    public static Integer getUnReadEmailNum(Player player) {
        if (!UltiTools.getInstance().getConfig().getBoolean("enable_email")){
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

    public void setScoreboard(Objective scoreboard,String prefixString, String content, int score){
        if (!content.equals("")) {
            Score scoreboardScore = scoreboard.getScore(prefixString + content);
            scoreboardScore.setScore(score);
        }
    }

    public String setArmorString(String string){
        if ("-1".equals(string)) {
            string = UltiTools.languageUtils.getString("none");
        } else if ("0".equals(string)) {
            string = "∞";
        }
        return string;
    }

    public void setCustomLine(Objective scoreboard, Player player){
        List<String> tempList = new ArrayList<>();
        try {
            tempList = ConfigController.getConfig("sidebar").getStringList("customerline");
        }catch (NullPointerException ignored){
        }
        List<String> customer_line = Lists.reverse(tempList);
        int i = 1;
        for (String each_line : customer_line) {
            Score customer;
            if (each_line.contains("%")) {
                each_line.replace("%", "");
                if (!each_line.contains("%")) {
                    customer = scoreboard.getScore(ChatColor.WHITE + each_line);
                } else {
                    customer = scoreboard.getScore(ChatColor.WHITE + PlaceholderAPI.setPlaceholders(player, each_line));
                }
            } else {
                customer = scoreboard.getScore(ChatColor.WHITE + each_line);
            }
            customer.setScore(i);
            i++;
        }
    }
}

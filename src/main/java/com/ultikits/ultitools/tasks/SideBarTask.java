package com.ultikits.ultitools.tasks;

import com.google.common.collect.Lists;
import com.ultikits.beans.EmailContentBean;
import com.ultikits.ultitools.beans.ArmorsBean;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.manager.EmailManager;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.ScoreBoardUtils;
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
import static com.ultikits.ultitools.utils.ScoreBoardUtils.updateLine;


public class SideBarTask extends BukkitRunnable {

    boolean isPAPILoaded = UltiTools.isPAPILoaded;

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
//                        player.setScoreboard(ScoreBoardUtils.scoreboardMap.get(player.getUniqueId()));
                    } else {
                        player.setScoreboard(ScoreBoardUtils.scoreboardMap.get(null));
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

        updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_money") + " " + ChatColor.GOLD+ money, 97);
        updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_deposit") + " " + ChatColor.GOLD+ deposit, 96);
        updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_level") + " " + ChatColor.GOLD+ level_num, 95);
        updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_job") + " " + ChatColor.GOLD+ occupation, 98);
        updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_name") + " " + ChatColor.GOLD+ name, 99);
        updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_online_player") + " " + ChatColor.GOLD+ onLinePlayers, 0);
        updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_health") + " " + ChatColor.YELLOW + hp + ChatColor.BOLD + " / " + ChatColor.GOLD+ max_hp, 93);
        int unread = getUnReadEmailNum(player);
//        setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_new_email") + " " + ChatColor.GOLD, unread + UltiTools.languageUtils.getString("feng"), 92);
        if (unread == 0) {
            updateLine(player, null, 92);
//            setScoreboard(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_new_email") + " " + ChatColor.GOLD, unread + UltiTools.languageUtils.getString("feng"), 92, true);
        }else {
            updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_new_email") + " " + ChatColor.GOLD+ unread + UltiTools.languageUtils.getString("feng"), 92);
        }
        if (!max_exp.equals("") && !exp.equals("")) {
            updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_exp") + " " + ChatColor.YELLOW+ exp + ChatColor.BOLD + " / " + ChatColor.GOLD + max_exp, 94);
        }
        if (CDq != null && !CDq.equals("") && Integer.parseInt(CDq) > 0) {
            updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_Q_countdown") + " " + ChatColor.GOLD+ CDq + UltiTools.languageUtils.getString("second"), 89);
        }
        if (CDw != null && !CDw.equals("") && Integer.parseInt(CDw) > 0) {
            updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_W_countdown") + " " + ChatColor.GOLD+ CDw + UltiTools.languageUtils.getString("second"), 88);
        }
        if (CDe != null && !CDe.equals("") && Integer.parseInt(CDe) > 0) {
            updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_E_countdown") + " " + ChatColor.GOLD+ CDe + UltiTools.languageUtils.getString("second"), 87);
        }
        if (CDr != null && !CDr.equals("") && Integer.parseInt(CDr) > 0) {
            updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_R_countdown") + " " + ChatColor.GOLD+ CDr + UltiTools.languageUtils.getString("second"), 86);
        }
        if (isWizard && mp != null && max_mp != null && !mp.equals("") && !max_mp.equals("")) {
            updateLine(player, ChatColor.WHITE + UltiTools.languageUtils.getString("sidebar_magic") + "" + ChatColor.YELLOW+ mp + ChatColor.BOLD + "/" + ChatColor.GOLD + max_mp, 90);
        }

        if (UltiTools.getInstance().getConfig().getBoolean("enable_armor_check")) {
            ArmorsBean armorsManager = new ArmorsBean(player);
            String H = setArmorString(String.valueOf(armorsManager.getHatDurability()));
            String C = setArmorString(String.valueOf(armorsManager.getChestDurability()));
            String L = setArmorString(String.valueOf(armorsManager.getLegDurability()));
            String B = setArmorString(String.valueOf(armorsManager.getBootsDurability()));
            String M = setArmorString(String.valueOf(armorsManager.getMainHandDurability()));
            String O = setArmorString(String.valueOf(armorsManager.getOffHandDurability()));

            updateLine(player, ChatColor.GREEN + "H " + H + " C " + C, -1);
            updateLine(player, ChatColor.GREEN + "L " + L + " B " + B, -2);
            updateLine(player, ChatColor.GREEN + "M " + M + " O " + O, -3);
        }
        setCustomLine(player);
    }

    public static Integer getUnReadEmailNum(Player player) {
        if (!UltiTools.getInstance().getConfig().getBoolean("enable_email")) {
            return 0;
        }
        EmailManager emailManager = new EmailManager(player);
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

//    public void setScoreboard(Player player, String prefixString, String content, int score) {
//        setScoreboard(player, prefixString, content, score, false);
//    }
//
//    public void setScoreboard(Player player, String prefixString, String content, int score, boolean reset) {
//        if (!content.equals("")) {
//            updateLine(player, prefixString + content, score, reset);
//        }
//    }

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
            updateLine(player, PlaceholderAPI.setPlaceholders(player, each_line), i);
            i++;
        }
    }

}

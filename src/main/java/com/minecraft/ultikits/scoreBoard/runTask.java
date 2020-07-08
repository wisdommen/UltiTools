package com.minecraft.ultikits.scoreBoard;

import com.google.common.collect.Lists;
import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.ultikits.ultitools.UltiTools;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.minecraft.Ultilevel.utils.checkLevel.*;

public class runTask extends BukkitRunnable {

    UltiEconomy economy = UltiTools.getEconomy();

    boolean isPAPILoaded = UltiTools.isPAPILoaded;

    @Override
    public void run() {

            FileConfiguration tool_config = UltiTools.getInstance().getConfig();
            List<String> players = tool_config.getStringList("player_closed_sb");
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!players.contains(p.getName())) {
                    File fileM = new File(UltiTools.getInstance().getDataFolder() + "/playerData", p.getName() + ".yml");
                    YamlConfiguration configM = YamlConfiguration.loadConfiguration(fileM);

                    String CDq;
                    String CDw;
                    String CDe;
                    String CDr;
                    String money;
                    String deposit;
                    String level_num;
                    String exp;
                    String max_exp;
                    String mp;
                    String max_mp;
                    String max_hp;
                    boolean isWizard;
                    String occupation;

                    if (isPAPILoaded && tool_config.getBoolean("enable_PAPI")) {
                        try {
                            CDq = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("CDq")));
                        } catch (Exception e) {
                            CDq = null;
                        }
                        try {
                            CDw = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("CDw")));
                        } catch (Exception e) {
                            CDw = null;
                        }
                        try {
                            CDe = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("CDe")));
                        } catch (Exception e) {
                            CDe = null;
                        }
                        try {
                            CDr = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("CDr")));
                        } catch (Exception e) {
                            CDr = null;
                        }
                        try {
                            money = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("money")));
                        } catch (Exception e) {
                            money = null;
                        }
                        try {
                            deposit = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("deposit")));
                        } catch (Exception e) {
                            deposit = null;
                        }
                        try {
                            level_num = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("level")));
                        } catch (Exception e) {
                            level_num = null;
                        }
                        try {
                            exp = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("exp")));
                        } catch (Exception e) {
                            exp = null;
                        }
                        try {
                            max_exp = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("max_exp")));
                        } catch (Exception e) {
                            max_exp = null;
                        }
                        try {
                            mp = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("mp")));
                        } catch (Exception e) {
                            mp = null;
                        }
                        try {
                            max_hp = String.format("%.1f", Double.parseDouble(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("max_hp")))));
                        } catch (Exception e) {
                            max_hp = null;
                        }
                        try {
                            max_mp = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("max_mp")));
                        } catch (Exception e) {
                            max_mp = null;
                        }
                        try {
                            occupation = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("occupation")));
                        } catch (Exception e) {
                            occupation = null;
                        }
                        isWizard = true;
                    } else {
                        DecimalFormat format = new DecimalFormat("0.0");
                        CDq = coolDown(p, "CDq") + "";
                        CDw = coolDown(p, "CDw") + "";
                        CDe = coolDown(p, "CDe") + "";
                        CDr = coolDown(p, "CDr") + "";
                        money = economy.checkMoney(p.getName()) + "";
                        deposit = economy.checkBank(p.getName()) + "";
                        level_num = checkLevel(p) + "";
                        exp = checkExp(p) + "";
                        max_exp = ((Integer.parseInt(level_num) - 1) * 5 + 100) + "";
                        mp = getPlayerMagicPoint(p) + "";
                        max_hp = format.format(getPlayerMaxHealth(p)) + "";
                        max_mp = getPlayerMaxMagicPoint(p) + "";
                        occupation = checkJob(p);
                        isWizard = isWizard(p);
                    }

                    // 创建一个计分板管理对象
                    ScoreboardManager sb = Bukkit.getScoreboardManager();
                    // 创建一个计分板
                    Scoreboard scoreboard = sb.getNewScoreboard();
                    // 创建队伍
//            if (isTeamLeader(p)) {
//                Team team = scoreboard.registerNewTeam(p.getName());
//                List<String> teamMember = getFileConfig("teams").getStringList(p.getName());
//                for (String each : teamMember){
//                    team.addEntry(each);
//                }
//                team.setDisplayName(ChatColor.AQUA+"["+p.getName()+"的小队]");
//                team.setCanSeeFriendlyInvisibles(true);
//                team.setAllowFriendlyFire(false);
//            }else if (isInATeam(p)){
//                String team_leader = getTeamLeader(p);
//                Team team = scoreboard.registerNewTeam(team_leader);
//                List<String> teamMember = getFileConfig("teams").getStringList(team_leader);
//                for (String each : teamMember){
//                    team.addEntry(each);
//                }
//                team.setDisplayName(ChatColor.AQUA+"["+team_leader+"的小队]");
//                team.setCanSeeFriendlyInvisibles(true);
//                team.setAllowFriendlyFire(false);
//            }
//        创建一个计分板对象
                    String title = UltiTools.getInstance().getConfig().getString("scoreBoardTitle");

                    Objective information = scoreboard.registerNewObjective("金币：", "", ChatColor.DARK_AQUA + title);

//        设置计分板样式
                    information.setDisplaySlot(DisplaySlot.SIDEBAR);
//            team.setDisplaySlot(DisplaySlot.PLAYER_LIST);
//
//            Score team1 = team.getScore("成员："+p.getName());
//            team1.setScore(1);

                    if (money != null && !money.equals("")) {
                        Score gold = information.getScore(ChatColor.WHITE + "金币： " + ChatColor.GOLD + money);
                        gold.setScore(97);
                    }
                    if (deposit != null && !deposit.equals("")) {
                        Score bank = information.getScore(ChatColor.WHITE + "存款： " + ChatColor.GOLD + deposit);
                        bank.setScore(96);
                    }
                    if (configM.getInt("count") > 0) {
                        Score mail = information.getScore(ChatColor.WHITE + "新邮件： " + ChatColor.GOLD + configM.getInt("count") + "封");
                        mail.setScore(92);
                    }
                    if (level_num != null && !level_num.equals("")) {
                        Score level = information.getScore(ChatColor.WHITE + "等级： " + ChatColor.GOLD + level_num);
                        level.setScore(95);
                    }
                    if (exp != null && max_exp != null && !max_exp.equals("") && !exp.equals("")) {
                        Score level = information.getScore(ChatColor.WHITE + "经验值： " + ChatColor.YELLOW + exp + ChatColor.BOLD + " / " + ChatColor.GOLD + max_exp);
                        level.setScore(94);
                    }
                    if (CDq != null && Integer.parseInt(CDq) > 0 && !CDq.equals("")) {
                        Score CD = information.getScore(ChatColor.WHITE + "Q技能CD还剩 " + ChatColor.GOLD + CDq + "秒");
                        CD.setScore(89);
                    }
                    if (CDw != null && Integer.parseInt(CDw) > 0 && !CDw.equals("")) {
                        Score CD = information.getScore(ChatColor.WHITE + "W技能CD还剩 " + ChatColor.GOLD + CDw + "秒");
                        CD.setScore(88);
                    }
                    if (CDe != null && Integer.parseInt(CDe) > 0 && !CDe.equals("")) {
                        Score CD = information.getScore(ChatColor.WHITE + "E技能CD还剩 " + ChatColor.GOLD + CDe + "秒");
                        CD.setScore(87);
                    }
                    if (CDr != null && Integer.parseInt(CDr) > 0 && !CDr.equals("")) {
                        Score CD = information.getScore(ChatColor.WHITE + "R技能CD还剩 " + ChatColor.GOLD + CDr + "秒");
                        CD.setScore(86);
                    }
                    if (occupation != null && !occupation.equals("")) {
                        Score job = information.getScore(ChatColor.WHITE + "职业： " + ChatColor.GOLD + occupation);
                        job.setScore(98);
                    }
                    if (isWizard && mp != null && max_mp != null && !mp.equals("")) {
                        Score magic = information.getScore(ChatColor.WHITE + "魔力值：" + ChatColor.YELLOW + mp + ChatColor.BOLD + "/" + ChatColor.GOLD + max_mp);
                        magic.setScore(90);
                    }
                    Score name = information.getScore(ChatColor.WHITE + "名字： " + ChatColor.GOLD + p.getName());
                    name.setScore(99);
                    DecimalFormat format1 = new DecimalFormat("0.0");
                    if (max_hp != null && !"".equals(max_hp)) {
                        Score health = information.getScore(ChatColor.WHITE + "生命值： " + ChatColor.YELLOW + format1.format(p.getHealth()) + ChatColor.BOLD + " / " + ChatColor.GOLD + max_hp);
                        health.setScore(93);
                    }
                    Score onlineplayer = information.getScore(ChatColor.WHITE + "在线人数： " + ChatColor.GOLD + Bukkit.getOnlinePlayers().size());
                    onlineplayer.setScore(0);

                    if (UltiTools.getInstance().getConfig().getBoolean("enable_armor_check")) {
                        ArmorsManager armorsManager = new ArmorsManager(p);
                        String H = String.valueOf(armorsManager.getHatDurability());
                        String C = String.valueOf(armorsManager.getChestDurability());
                        String L = String.valueOf(armorsManager.getLegDurability());
                        String B = String.valueOf(armorsManager.getBootsDurability());
                        String M = String.valueOf(armorsManager.getMainHandDurability());
                        String O = String.valueOf(armorsManager.getOffHandDurability());
                        if ("-1".equals(H)) {
                            H = "无";
                        } else if ("0".equals(H)) {
                            H = "∞";
                        }
                        if ("-1".equals(C)) {
                            C = "无";
                        } else if ("0".equals(C)) {
                            C = "∞";
                        }
                        if ("-1".equals(L)) {
                            L = "无";
                        } else if ("0".equals(L)) {
                            L = "∞";
                        }
                        if ("-1".equals(B)) {
                            B = "无";
                        } else if ("0".equals(B)) {
                            B = "∞";
                        }
                        if ("-1".equals(M)) {
                            M = "无";
                        } else if ("0".equals(M)) {
                            M = "∞";
                        }
                        if ("-1".equals(O)) {
                            O = "无";
                        } else if ("0".equals(O)) {
                            O = "∞";
                        }
                        Score equipment1 = information.getScore(ChatColor.GREEN + "H " + H + " C " + C);
                        Score equipment2 = information.getScore(ChatColor.GREEN + "L " + L + " B " + B);
                        Score equipment3 = information.getScore(ChatColor.GREEN + "M " + M + " O " + O);
                        equipment1.setScore(-1);
                        equipment2.setScore(-2);
                        equipment3.setScore(-3);
                    }

                    List<String> tempList = tool_config.getStringList("customerline");
                    List<String> customer_line = Lists.reverse(tempList);
                    int i = 1;
                    for (String each_line : customer_line) {
                        Score customer;
                        if (each_line.contains("%")) {
                            each_line.replace("%", "");
                            if (!each_line.contains("%")) {
                                customer = information.getScore(ChatColor.WHITE + each_line);
                            } else {
                                customer = information.getScore(ChatColor.WHITE + PlaceholderAPI.setPlaceholders(p, each_line));
                            }
                        } else {
                            customer = information.getScore(ChatColor.WHITE + each_line);
                        }
                        customer.setScore(i);
                        i++;
                    }
                    p.setScoreboard(scoreboard);
                } else {
                    p.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());
                }
            }

    }
}

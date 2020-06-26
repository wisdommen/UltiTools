package com.minecraft.ultikits.scoreBoard;

import com.minecraft.Ultilevel.level.level.levelMain;
import com.minecraft.Ultilevel.utils.checkLevel;
import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.ultikits.ultitools.UltiTools;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import static com.minecraft.Ultilevel.utils.checkLevel.*;

public class runTask extends BukkitRunnable {

    FileConfiguration config = levelMain.getInstance().getConfig();
    boolean isCustomized = config.getBoolean("enable_customize_rpg");
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

                int CDq;
                int CDw;
                int CDe;
                int CDr;
                int money;
                int deposit;
                int level_num;
                int exp;
                int max_exp;
                int mp;
                double max_mp;
                double max_hp;
                String occupation;

                if (isPAPILoaded && tool_config.getBoolean("enable_PAPI")){
                    try {
                        CDq = Integer.parseInt(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("CDq"))));
                    } catch (Exception e) {
                        CDq = 0;
                    }
                    try {
                        CDw = Integer.parseInt(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("CDw"))));
                    } catch (Exception e) {
                        CDw = 0;
                    }
                    try {
                        CDe = Integer.parseInt(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("CDe"))));
                    } catch (Exception e) {
                        CDe = 0;
                    }
                    try {
                        CDr = Integer.parseInt(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("CDr"))));
                    } catch (Exception e) {
                        CDr = 0;
                    }
                    try {
                        money = Integer.parseInt(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("money"))));
                    } catch (Exception e) {
                        money = 0;
                    }
                    try {
                        deposit = Integer.parseInt(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("deposit"))));
                    } catch (Exception e) {
                        deposit = 0;
                    }
                    try {
                        level_num = Integer.parseInt(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("level"))));
                    } catch (Exception e) {
                        level_num = 0;
                    }
                    try {
                        exp = Integer.parseInt(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("exp"))));
                    } catch (Exception e) {
                        exp = 0;
                    }
                    try {
                        max_exp = Integer.parseInt(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("exp"))));
                    } catch (Exception e) {
                        max_exp = 0;
                    }
                    try {
                        mp = Integer.parseInt(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("mp"))));
                    } catch (Exception e) {
                        mp = 0;
                    }
                    try {
                        max_mp = Integer.parseInt(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("max_mp"))));
                    } catch (Exception e) {
                        max_mp=0;
                    }
                    try {
                        max_hp = Integer.parseInt(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("max_hp"))));
                    } catch (Exception e) {
                        max_hp=0;
                    }
                    try {
                        occupation = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(p, tool_config.getString("occupation")));
                    } catch (Exception e) {
                        occupation = null;
                    }
                }else {
                    CDq = coolDown(p, "CDq");
                    CDw = coolDown(p, "CDw");
                    CDe = coolDown(p, "CDe");
                    CDr = coolDown(p, "CDr");
                    money = economy.checkMoney(p.getName());
                    deposit = economy.checkBank(p.getName());
                    level_num = checkLevel(p);
                    exp = checkExp(p);
                    max_exp = ((level_num-1) * 5 + 100);
                    mp = getPlayerMagicPoint(p);
                    max_hp = getPlayerMaxHealth(p);
                    if (!isCustomized) {
                        max_mp = (1000 + (level_num-1) * 10);
                    }else {
                        max_mp = config.getDouble("player_max_mp");
                    }
                    occupation = checkJob(p);
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

                Score gold = information.getScore(ChatColor.WHITE + "金币： " + ChatColor.GOLD + money);
                gold.setScore(97);
                Score bank = information.getScore(ChatColor.WHITE + "存款： " + ChatColor.GOLD + deposit);
                bank.setScore(96);
                if (configM.getInt("count") > 0) {
                    Score mail = information.getScore(ChatColor.WHITE + "新邮件： " + ChatColor.GOLD + configM.getInt("count") + "封");
                    mail.setScore(92);
                }
                if (level_num > 0) {
                    Score level = information.getScore(ChatColor.WHITE + "等级： " + ChatColor.GOLD + level_num);
                    level.setScore(95);
                }
                if (exp >= 0 && max_exp > 0) {
                    Score level = information.getScore(ChatColor.WHITE + "经验值： " + ChatColor.YELLOW + exp + ChatColor.BOLD + " / " + ChatColor.GOLD + max_exp);
                    level.setScore(94);
                }
                if (CDq != 0) {
                    Score CD = information.getScore(ChatColor.WHITE + "Q技能CD还剩 " + ChatColor.GOLD + CDq + "秒");
                    CD.setScore(89);
                }
                if (CDw != 0) {
                    Score CD = information.getScore(ChatColor.WHITE + "W技能CD还剩 " + ChatColor.GOLD + CDw + "秒");
                    CD.setScore(88);
                }
                if (CDe != 0) {
                    Score CD = information.getScore(ChatColor.WHITE + "E技能CD还剩 " + ChatColor.GOLD + CDe + "秒");
                    CD.setScore(87);
                }
                if (CDr != 0) {
                    Score CD = information.getScore(ChatColor.WHITE + "R技能CD还剩 " + ChatColor.GOLD + CDr + "秒");
                    CD.setScore(86);
                }
                if (occupation != null) {
                    Score job = information.getScore(ChatColor.WHITE + "职业： " + ChatColor.GOLD + occupation);
                    job.setScore(98);
                }
                if (isWizard(p) && mp >= 0 && max_mp >0) {
                    Score magic = information.getScore(ChatColor.WHITE + "魔力值：" + ChatColor.YELLOW + mp + ChatColor.BOLD + "/" + ChatColor.GOLD + max_mp);
                    magic.setScore(90);
                }
                Score name = information.getScore(ChatColor.WHITE + "名字： " + ChatColor.GOLD + p.getName());
                name.setScore(99);
                DecimalFormat format1 = new DecimalFormat("0.0");
                Score health = information.getScore(ChatColor.WHITE + "生命值： " + ChatColor.YELLOW + format1.format(p.getHealth()) + ChatColor.BOLD + " / " + ChatColor.GOLD + format1.format(max_hp));
                health.setScore(93);
                Score onlineplayer = information.getScore(ChatColor.WHITE + "在线人数： " + ChatColor.GOLD + Bukkit.getOnlinePlayers().size());
                onlineplayer.setScore(84);
                p.setScoreboard(scoreboard);
            } else {
                p.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());
            }
        }

    }
}

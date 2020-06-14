package com.minecraft.ultikits.scoreBoard;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import static com.minecraft.Ultilevel.utils.checkLevel.*;

public class runTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            UltiEconomy economy = UltiTools.getEconomy();
            File folderM = new File(UltiTools.getInstance().getDataFolder() + "/playerData");
            File fileM = new File(folderM, p.getName() + ".yml");
            File folderL = new File("plugins/Level/playerData");
            File fileL = new File(folderL, p.getName() + ".yml");
            YamlConfiguration configM;
            configM = YamlConfiguration.loadConfiguration(fileM);
            YamlConfiguration configL;
            configL = YamlConfiguration.loadConfiguration(fileL);
            //        创建一个计分板管理对象
            ScoreboardManager sb = Bukkit.getScoreboardManager();
//        创建一个计分板
            Scoreboard scoreboard = sb.getNewScoreboard();
            //创建队伍
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

            Score gold = information.getScore(ChatColor.WHITE + "金币： " + ChatColor.GOLD + economy.checkMoney(p.getName()));
            gold.setScore(97);
            Score bank = information.getScore(ChatColor.WHITE + "存款： " + ChatColor.GOLD + economy.checkBank(p.getName()));
            bank.setScore(96);
            if (configM.getInt("count") > 0) {
                Score mail = information.getScore(ChatColor.WHITE + "新邮件： " + ChatColor.GOLD + configM.getInt("count") + "封");
                mail.setScore(92);
            }
            if (checkLevel(p) > 0) {
                Score level = information.getScore(ChatColor.WHITE + "等级： " + ChatColor.GOLD + checkLevel(p));
                level.setScore(95);
            }
            if (checkExp(p) >= 0) {
                Score level = information.getScore(ChatColor.WHITE + "经验值： " + ChatColor.YELLOW + checkExp(p) + ChatColor.BOLD + " / " + ChatColor.GOLD + 100);
                level.setScore(94);
            }
            if (configL.getInt("CDq") != 0) {
                Score CD = information.getScore(ChatColor.WHITE + "Q技能CD还剩 " + ChatColor.GOLD + configL.getInt("CDq") + "秒");
                CD.setScore(89);
            }
            if (configL.getInt("CDw") != 0) {
                Score CD = information.getScore(ChatColor.WHITE + "W技能CD还剩 " + ChatColor.GOLD + configL.getInt("CDw") + "秒");
                CD.setScore(88);
            }
            if (configL.getInt("CDe") != 0) {
                Score CD = information.getScore(ChatColor.WHITE + "E技能CD还剩 " + ChatColor.GOLD + configL.getInt("CDe") + "秒");
                CD.setScore(87);
            }
            if (configL.getInt("CDr") != 0) {
                Score CD = information.getScore(ChatColor.WHITE + "R技能CD还剩 " + ChatColor.GOLD + configL.getInt("CDr") + "秒");
                CD.setScore(86);
            }
            if (checkJob(p) != null) {
                Score job = information.getScore(ChatColor.WHITE + "职业： " + ChatColor.GOLD + checkJob(p));
                job.setScore(98);
            }
            if (isWizard(p) && configL.getInt("Magic") >= 0) {
                Score magic = information.getScore(ChatColor.WHITE + "魔力值：" + ChatColor.YELLOW + configL.getDouble("Magic") + ChatColor.BOLD + "/" + ChatColor.GOLD + 1000);
                magic.setScore(90);
            }
            Score name = information.getScore(ChatColor.WHITE + "名字： " + ChatColor.GOLD + p.getName());
            name.setScore(99);
            DecimalFormat format1 = new DecimalFormat("0.0");
            Score health = information.getScore(ChatColor.WHITE + "生命值： " + ChatColor.YELLOW + format1.format(p.getHealth()) + ChatColor.BOLD + " / " + ChatColor.GOLD + format1.format(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
            health.setScore(93);
            Score onlineplayer = information.getScore(ChatColor.WHITE + "在线人数： " + ChatColor.GOLD + Bukkit.getOnlinePlayers().size());
            onlineplayer.setScore(84);
            p.setScoreboard(scoreboard);
        }

    }
}

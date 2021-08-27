package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.tasks.AtTask;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.util.*;

import static com.ultikits.enums.Sounds.BLOCK_NOTE_BLOCK_BELL;

/**
 * @author wisdomme,qianmo,Shpries
 */
public class ChatListener implements Listener {
    private static final List<String> ultilevelStrings = Arrays.asList("%ul_level%", "%ul_job%", "%ul_exp%", "%ul_mp%",
            "%ul_max_mp%", "%ul_max_exp%", "%ul_health%", "%ul_max_health%", "%ul_q_cd%", "%ul_w_cd%", "%ul_e_cd%",
            "%ul_r_cd%");

/*
* at玩家功能
* Code rewritten by Shpries
*/
    private void atNotification (Player player,Player sender) {
        player.sendMessage(Objects.requireNonNull(UltiTools.languageUtils.getString("chat_atted")).replaceAll("%player%",sender.getName()));
        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(BLOCK_NOTE_BLOCK_BELL), 10, 1);
        new AtTask(player,UltiTools.languageUtils.getString("chat_atted").replaceAll("%player%",sender.getName())).runTaskTimerAsynchronously(UltiTools.getInstance(),0,2L);
    }
    @EventHandler
    public void onPlayerAt(AsyncPlayerChatEvent e) {
        if (ConfigController.getConfig("config").getBoolean("enable_chat_att")) {
            String msg = e.getMessage();
            Player sender = e.getPlayer();
            if(msg.contains("@")) {
                if(msg.toLowerCase().contains("@" + UltiTools.languageUtils.getString("chat_at_all")) || msg.toLowerCase().contains("@ " + UltiTools.languageUtils.getString("chat_at_all"))) {
                    if(sender.hasPermission("ultikits.tools.atall") || sender.isOp() || sender.hasPermission("ultitools.tools.admin")) {
                        String msg0 = msg.replace("@",ChatColor.DARK_GREEN + "@" + ChatColor.RESET);
                        sender.sendMessage(UltiTools.languageUtils.getString("chat_at_you_at_all"));
                        e.setMessage(msg0.replace(UltiTools.languageUtils.getString("chat_at_all"),ChatColor.DARK_GREEN + "" + ChatColor.BOLD + UltiTools.languageUtils.getString("chat_at_all") + ChatColor.RESET));
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            atNotification(player,sender);
                        }
                        return;
                    } else {
                        sender.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("no_permission")));
                        e.setCancelled(true);
                        return;
                    }
                }
                //被@的玩家的列表
                List<Player> atedPlayer = new ArrayList();
                int sum = 0;
                for(Player player : Bukkit.getOnlinePlayers()) {
                    //无视大小写比较
                    if(msg.toLowerCase().contains("@" + player.getName().toLowerCase()) || msg.toLowerCase().contains("@ " + player.getName().toLowerCase())) {
                        atNotification(player,sender);
                        atedPlayer.add(player);
                        sum++;
                    }
                }
                if(sum != 0) {
                    //@成功
                    String msg1 = msg.replace("@",ChatColor.DARK_GREEN + "@" + ChatColor.RESET);
                    msg1 += " ";
                    for(Player player: atedPlayer) {
                         String playerName = player.getName();
                         //校正大小写的玩家名字
                         String name = "";
                         int nameLength = playerName.length();
                         int msg1Length = msg1.length();
                         //读取到需要校正大小写的玩家名字
                        for(int i = 0; i < msg1Length - nameLength ;i++) {
                            name = msg1.substring(i, i + nameLength);
                            if (name.equalsIgnoreCase(playerName)) {
                                break;
                            }
                        }
                        msg1 = msg1.replace(name,ChatColor.DARK_GREEN + "" + ChatColor.BOLD + playerName + ChatColor.RESET);
                    }
                    e.setMessage(msg1);
                    sender.sendMessage(Objects.requireNonNull(UltiTools.languageUtils.getString("chat_at_success")).replaceAll("%num%", String.valueOf(sum)));
                } else {
                    //@不成功
                    sender.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("chat_at_error")));
                    String msg2 = msg.replace("@",ChatColor.RED + "@" + ChatColor.RESET);
                    e.setMessage(msg2);
                }
            }
        }
    }


    @EventHandler
    public void onPlayerChatColor(AsyncPlayerChatEvent event){
        //玩家彩色字体处理
        if(!ConfigController.getConfig("config").getBoolean("enable_chat_color")) return;
        //不启用
        //如果事件已经被取消
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        String Message = event.getMessage();
        //玩家说的内容
        if (Message.contains("&")){
            //使用了彩色字体
            if (player.hasPermission(new Permission("ultikits.tools.chatcolor"))){
                //如果玩家有"ultikits.tools.chatcolor"权限
                event.setMessage(event.getMessage().replace("&","§"));
                //输出颜色变量字体
            }else{
                //提醒玩家没权限
                player.sendMessage(Objects.requireNonNull(UltiTools.languageUtils.getString("chat_color_nopermission_reply")));
                //撤销发送操作
                //event.setCancelled(true);
            }
        }//没有使用不操作
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String prefixes_str = ConfigController.getConfig("chat").getString("chat_prefix").replaceAll("%player_name%", player.getName()).replaceAll("%player_world%", player.getLocation().getWorld().getName()).replaceAll("&", "§");
        if (UltiTools.getInstance().getServer().getPluginManager().getPlugin("UltiLevel") == null) {
            prefixes_str = validateUltiLevelVariable(prefixes_str);
        }
        String papiMassage = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(player, prefixes_str));
        String message = papiMassage + ChatColor.WHITE + " %2$s";
        event.setFormat(message.replaceAll("&", "§"));
    }

    @EventHandler
    public void onPlayerChatReply(AsyncPlayerChatEvent event) {
        if (ConfigController.getConfig("config").getBoolean("enable_auto-reply") && UltiTools.getInstance().getProChecker().getProStatus()) {
            String message = event.getMessage().replace(" ", "_");
            File file = new File(ConfigsEnum.CHAT.toString());
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            Set<String> keys = config.getConfigurationSection("auto-reply").getKeys(false);
            String bestMatch = null;
            for (String each : keys) {
                if (message.contains(each)) {
                    if (bestMatch != null) {
                        if (bestMatch.length() < each.length()) {
                            bestMatch = each;
                        }
                    } else {
                        bestMatch = each;
                    }
                }
            }
            String reply = config.getString("auto-reply."+bestMatch);
            if (reply != null) {
                Bukkit.broadcastMessage(reply.replaceAll("&", "§"));
            }
        }
    }

    private static String validateUltiLevelVariable(String string) {
        for (String each : ultilevelStrings) {
            string = string.replace(each, "");
        }
        return string;
    }
}

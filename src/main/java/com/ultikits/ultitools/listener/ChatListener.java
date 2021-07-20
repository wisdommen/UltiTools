package com.ultikits.ultitools.listener;

import com.ultikits.enums.Sounds;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;


public class ChatListener implements Listener {
    private static final List<String> ultilevelStrings = Arrays.asList("%ul_level%", "%ul_job%", "%ul_exp%", "%ul_mp%",
            "%ul_max_mp%", "%ul_max_exp%", "%ul_health%", "%ul_max_health%", "%ul_q_cd%", "%ul_w_cd%", "%ul_e_cd%",
            "%ul_r_cd%");

    @EventHandler
    public void onPlayerChatAtt(AsyncPlayerChatEvent event){
        //玩家 @ 玩家 处理
        // 不能多线程处理
        if(!ConfigController.getConfig("config").getBoolean("enable_chat_att")) return;
        //不启用
        Player player = event.getPlayer();
        StringBuilder Message = new StringBuilder(event.getMessage());
        Server server = UltiTools.getInstance().getServer();
        //玩家说的内容
        int AttHand = Message.indexOf("@");
        if (AttHand == -1 || event.isCancelled()) return;
        //没有@玩家 , 或者事件已经被取消
        List<String> Name = new ArrayList<>();
        //name 是被@的所有玩家
        while (AttHand != -1){
            //如果有玩家
            //提取玩家名字
            int AttEnd = Message.indexOf(";" , AttHand);
            if (AttEnd == -1){
                //找不到 ;
                AttEnd = Message.length();
                //且末尾增加;
                Message.append(";");
            }
            //获取末尾索引
            String Linshi = Message.substring(AttHand + 1 , AttEnd);
            //提取玩家名字，正常大小写
            if (Name.contains(Linshi)){
                //如果有重复名字
                player.sendMessage(Objects.requireNonNull(UltiTools.languageUtils.getString("chat_att_repeat")).replaceAll("%player%",Linshi));
                //并且取消事件
                event.setCancelled(true);
                return;
            }else if (Linshi.length() <= 1){
                //如果名字小于1个字母 , 名字错误
                player.sendMessage(Objects.requireNonNull(UltiTools.languageUtils.getString("chat_att_errname")).replaceAll("%player%",Linshi));
                //取消事件
                event.setCancelled(true);
                return;
            }
            //判断玩家是否在线
            if (server.getPlayerExact(Linshi) == null){
                //玩家不在线
                player.sendMessage(Objects.requireNonNull(UltiTools.languageUtils.getString("chat_att_offonline")).replaceAll("%player%",Linshi));
                //取消事件
                event.setCancelled(true);
                return;
            }
            Name.add(Linshi);
            AttHand = Message.indexOf("@" , AttEnd);
        }
        event.setMessage(Message.toString());
        //更新玩家说话信息
        //发布信息给玩家
        for (String toPlayer : Name){
            new BukkitRunnable(){
                @Override
                public void run() {
                    //多线程处理
                    /*
                     * 这里是对被艾特玩家进行操作
                     * 参数说明：
                     * 变量   参数类型    备注
                     * toPlayer String 被艾特玩家ID
                     * player Player 发布信息玩家
                     * server Server 此服务器队形
                     */
                    Objects.requireNonNull(server.getPlayerExact(toPlayer)).sendMessage(Objects.requireNonNull(UltiTools.languageUtils.getString("chat_att_beatt")).replaceAll("%player%",player.getName()));
                    //发送消息
                    Player bePlayer = server.getPlayerExact(toPlayer);
                    //获取bePlayer 被艾特的玩家
                    //Sounds.BLOCK_NOTE_BLOCK_BELL 高
                    //Sounds.BLOCK_NOTE_BLOCK_CHIME  中
                    //Sounds.BLOCK_NOTE_BLOCK_HAT 低
                    assert bePlayer != null;






                    bePlayer.playSound(
                            bePlayer.getLocation(), UltiTools.versionAdaptor.getSound(
                                    Sounds.BLOCK_NOTE_BLOCK_BELL
                            )
                            , 1, 1);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            bePlayer.playSound(
                                    bePlayer.getLocation(), UltiTools.versionAdaptor.getSound(
                                            Sounds.BLOCK_NOTE_BLOCK_BELL
                                    )
                                    , 1, 1);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    bePlayer.playSound(
                                            bePlayer.getLocation(), UltiTools.versionAdaptor.getSound(
                                                    Sounds.BLOCK_NOTE_BLOCK_CHIME
                                            )
                                            , 1, 1);
                                    new BukkitRunnable(){
                                        @Override
                                        public void run() {
                                            bePlayer.playSound(
                                                    bePlayer.getLocation(), UltiTools.versionAdaptor.getSound(
                                                            Sounds.BLOCK_NOTE_BLOCK_HAT
                                                    )
                                                    , 1, 1);
                                        }
                                    }.runTaskLaterAsynchronously(UltiTools.getInstance(),10);
                                }
                            }.runTaskLaterAsynchronously(UltiTools.getInstance(),10);
                        }
                    }.runTaskLaterAsynchronously(UltiTools.getInstance(),5);
                    //20 kit = 1秒
                    //}.runTaskAsynchronously(UltiTools.getInstance());
                }
            }.runTaskAsynchronously(UltiTools.getInstance());
        }
        player.sendMessage(Objects.requireNonNull(UltiTools.languageUtils.getString("chat_att_succ")).replaceAll("%playersize%",Name.size() + ""));
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
        if (ConfigController.getConfig("config").getBoolean("enable_auto-reply") && UltiTools.isProVersion) {
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
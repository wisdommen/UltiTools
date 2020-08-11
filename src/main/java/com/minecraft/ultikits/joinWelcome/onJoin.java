package com.minecraft.ultikits.joinWelcome;

import com.minecraft.ultikits.UpdateChecker.VersionChecker;
import com.minecraft.ultikits.config.ConfigsEnum;
import com.minecraft.ultikits.scoreBoard.SideBar;
import com.minecraft.ultikits.ultitools.UltiTools;
//import net.minecraft.server.v1_15_R1.IChatBaseComponent;
//import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
//import net.minecraft.server.v1_15_R1.PlayerConnection;
//import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.reflect.Method;


public class onJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String website = UltiTools.getInstance().getConfig().getString("joinWebsite");

//        Class PlayerConnectionClass;
//        Class IChatBaseComponentClass;
//        Class PacketPlayOutChatClass;
//        Class CraftPlayerClass = null;
//
//        try {
//            PlayerConnectionClass = Class.forName("net.minecraft.server.v1_16_R1.PlayerConnection");
//            IChatBaseComponentClass = Class.forName("net.minecraft.server.v1_16_R1.IChatBaseComponent");
//            PacketPlayOutChatClass = Class.forName("net.minecraft.server.v1_16_R1.PacketPlayOutChat");
//            CraftPlayerClass = Class.forName("org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer");
//
//            Method CraftPlayerMethod = CraftPlayerClass.getMethod("getHandle");
//
//            Object craftPlayer = CraftPlayerClass.newInstance();
//
//            Object object = CraftPlayerMethod.invoke(craftPlayer);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        Object handle = methodGetHandle.of(player).call();
//        Object connection = fieldPlayerConnection.of(handle).get();

//        PlayerConnection connection = ((CraftPlayer) event.getPlayer()).getHandle().playerConnection;
//        PacketPlayOutChat packet;
//        if (!website.equals("None")) {
//            packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(String.format("[\"\",{\"text\":\"使用地图\",\"color\":\"aqua\"},{\"text\":\"打开游戏菜单\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/menu\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"点我就能打开菜单辣！\",\"color\":\"light_purple\"}]}},{\"text\":\"\n\"},{\"text\":\"点击\",\"color\":\"aqua\"},{\"text\":\"物品包/礼包中心\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/open kits\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"点我打开礼包中心！！\",\"color\":\"light_purple\"}]}},{\"text\":\"即可领取新手礼包哦！\",\"color\":\"aqua\"},{\"text\":\"\n\"},{\"text\":\"点击\",\"color\":\"aqua\"},{\"text\":\"打开服务器网址\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%s\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"点我打开网址！！\",\"color\":\"light_purple\"}]}}]", website)));
//        }else {
//            packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("[\"\",{\"text\":\"使用地图\",\"color\":\"aqua\"},{\"text\":\"打开游戏菜单\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/menu\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"点我就能打开菜单辣！\",\"color\":\"light_purple\"}]}},{\"text\":\"\n\"},{\"text\":\"点击\",\"color\":\"aqua\"},{\"text\":\"物品包/礼包中心\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/open kits\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"点我打开礼包中心！！\",\"color\":\"light_purple\"}]}},{\"text\":\"即可领取新手礼包哦！\",\"color\":\"aqua\"}]}}]"));
//        }

        if (event.getPlayer().isOp()) {
            event.setJoinMessage(null);
            if (VersionChecker.isOutDate) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        player.sendMessage(ChatColor.AQUA+"[UltiTools]有新的更新！下载地址：https://www.mcbbs.net/thread-1062730-1-1.html");
                    }
                }.runTaskLater(UltiTools.getInstance(), 4);
            }
            Bukkit.broadcastMessage(ChatColor.RED + "[管理员]" + ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.RED + "已上线！");
            Bukkit.broadcastMessage(ChatColor.RED + "有问题请询问他！");

        } else {
            event.setJoinMessage(null);
            Bukkit.broadcastMessage(ChatColor.AQUA + "[玩家]" + event.getPlayer().getName() + "加入了服务器。");
        }


        new BukkitRunnable() {

            @Override
            public void run() {

                if (config.getInt("count") >= 0) {
                    player.sendMessage(ChatColor.AQUA + "欢迎加入服务器，" + event.getPlayer().getName());
//                    methodSendPacket.of(connection).call(packet);
//                    connection.sendPacket(packet);
                    player.sendMessage(ChatColor.AQUA + "当前在线人数：" + ChatColor.YELLOW + UltiTools.getInstance().getServer().getOnlinePlayers().size() + ChatColor.AQUA + " ，快和他们打个招呼吧！");
                    player.sendMessage(ChatColor.AQUA + "你有 " + ChatColor.YELLOW + SideBar.getUnReadEmailNum(player) + ChatColor.AQUA + " 封未读邮件！");
                    if (!"none".equalsIgnoreCase(website)) {
                        player.sendMessage(ChatColor.AQUA + "服务器网址：" + ChatColor.YELLOW + website);
                    }
                }

            }

        }.runTaskLater(UltiTools.getInstance(), UltiTools.getInstance().getConfig().getInt("sendMessageDelay") * 20);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().isOp()) {
            event.setQuitMessage(null);
            Bukkit.broadcastMessage(ChatColor.RED + "[管理员]" + ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.RED + "已下线！");
        } else {
            event.setQuitMessage(null);
            Bukkit.broadcastMessage(ChatColor.AQUA + "[玩家]" + ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.AQUA + "已下线！");
        }
    }
}

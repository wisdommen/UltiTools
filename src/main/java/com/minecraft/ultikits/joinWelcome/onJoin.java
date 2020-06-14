package com.minecraft.ultikits.joinWelcome;

import com.minecraft.ultikits.ultitools.UltiTools;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;


public class onJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        File folder = new File(UltiTools.getInstance().getDataFolder() + "/playerData");
        File file = new File(folder, event.getPlayer().getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        Player player = event.getPlayer();

        String website = UltiTools.getInstance().getConfig().getString("joinWebsite");
        PlayerConnection connection = ((CraftPlayer) event.getPlayer()).getHandle().playerConnection;
        PacketPlayOutChat packet;
        if (!website.equals("None")) {
            packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(String.format("[\"\",{\"text\":\"使用地图\",\"color\":\"aqua\"},{\"text\":\"打开游戏菜单\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/menu\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"点我就能打开菜单辣！\",\"color\":\"light_purple\"}]}},{\"text\":\"\n\"},{\"text\":\"点击\",\"color\":\"aqua\"},{\"text\":\"物品包/礼包中心\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/open kits\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"点我打开礼包中心！！\",\"color\":\"light_purple\"}]}},{\"text\":\"即可领取新手礼包哦！\",\"color\":\"aqua\"},{\"text\":\"\n\"},{\"text\":\"点击\",\"color\":\"aqua\"},{\"text\":\"打开服务器网址\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%s\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"点我打开网址！！\",\"color\":\"light_purple\"}]}}]", website)));
        }else {
            packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("[\"\",{\"text\":\"使用地图\",\"color\":\"aqua\"},{\"text\":\"打开游戏菜单\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/menu\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"点我就能打开菜单辣！\",\"color\":\"light_purple\"}]}},{\"text\":\"\n\"},{\"text\":\"点击\",\"color\":\"aqua\"},{\"text\":\"物品包/礼包中心\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/open kits\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"点我打开礼包中心！！\",\"color\":\"light_purple\"}]}},{\"text\":\"即可领取新手礼包哦！\",\"color\":\"aqua\"}]}}]"));
        }

        if (event.getPlayer().isOp()) {
            event.setJoinMessage(null);
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
                    event.getPlayer().sendMessage(ChatColor.AQUA + "欢迎加入服务器，" + event.getPlayer().getName());
                    connection.sendPacket(packet);
                    event.getPlayer().sendMessage(ChatColor.AQUA + "当前在线人数：" + ChatColor.YELLOW + UltiTools.getInstance().getServer().getOnlinePlayers().size() + ChatColor.AQUA + " ，快和他们打个招呼吧！");
                    event.getPlayer().sendMessage(ChatColor.AQUA + "你有 " + ChatColor.YELLOW + config.getInt("count") + ChatColor.AQUA + " 封未读邮件！");

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

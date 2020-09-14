package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.checker.updatechecker.VersionChecker;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.tasks.SideBarTask;
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
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

import static com.minecraft.ultikits.checker.updatechecker.VersionChecker.*;


public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String website = UltiTools.getInstance().getConfig().getString("joinWebsite");


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
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendMessage(ChatColor.RED + String.format("[UltiTools] 工具插件最新版为%s，你的版本是%s！请下载最新版本！", version, current_version));
                        player.sendMessage(ChatColor.RED + "[UltiTools] 你知道吗？现在UltiTools可以自动更新啦！在配置文件中打开自动更新，更新再也不用麻烦！");
                    }
                }.runTaskLaterAsynchronously(UltiTools.getInstance(), 80L);
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
                    player.sendMessage(ChatColor.AQUA + "你有 " + ChatColor.YELLOW + SideBarTask.getUnReadEmailNum(player) + ChatColor.AQUA + " 封未读邮件！");
                    if (!"none".equalsIgnoreCase(website)) {
                        player.sendMessage(ChatColor.AQUA + "服务器网址：" + ChatColor.YELLOW + website);
                    }
                }

            }

        }.runTaskLater(UltiTools.getInstance(), UltiTools.getInstance().getConfig().getInt("sendMessageDelay") * 20);
    }

    @EventHandler
    public void onJoinCreateEmailData(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File folder = new File(ConfigsEnum.PLAYER_EMAIL.toString());
        File file = new File(folder, player.getName() + ".yml");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    @EventHandler
    public void onJoinSaveIP(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        InetAddress ipAddress = event.getAddress();
        String ip = ipAddress.getHostAddress().replaceAll("\\.", "_");
        System.out.println(ip);
        File file = new File(ConfigsEnum.LOGIN.toString());
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.get("ip." + ip + ".players") == null) {
            config.set("ip." + ip + ".players", Collections.singletonList(player.getUniqueId().toString()));
        } else {
            List<String> playerList = config.getStringList("ip." + ip + ".players");
            if (playerList.contains(player.getUniqueId().toString())) {
                return;
            }
            int playerCount = playerList.size();
            int playerLimit = config.getInt("playerLimitForOneIP");
            if (playerCount < playerLimit) {
                playerList.add(player.getUniqueId().toString());
                config.set("ip." + ip + ".players", playerList);
            } else {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "此IP已达到注册上限，无法登入！");
                return;
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

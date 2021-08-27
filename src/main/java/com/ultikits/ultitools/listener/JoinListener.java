package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.checker.VersionChecker;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.tasks.SideBarTask;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.ScoreBoardUtils;
import com.ultikits.utils.YamlFileUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

import static com.ultikits.ultitools.checker.VersionChecker.*;

public class JoinListener implements Listener {

    File file = new File(ConfigsEnum.JOIN_WELCOME.toString());
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    List<String> welcomeMessage = config.getStringList("welcome_message");
    String firstJoinBroadcast = UltiTools.languageUtils.getString("first_join_broadcast");
    String opJoinMessage = config.getString("op_join");
    String opQuitMessage = config.getString("op_quit");
    String playerJoinMessage = config.getString("player_join");
    String playerQuitMessage = config.getString("player_quit");
    int sendMessageDelay = config.getInt("send_message_delay");

    File loginFile = new File(ConfigsEnum.LOGIN.toString());
    YamlConfiguration loginConfig = YamlConfiguration.loadConfiguration(loginFile);

    //first_join_broadcast相关准备-----开始   PLAYERLIST是到 yml文件
    File playerlistFile = new File(ConfigsEnum.PLAYERLIST.toString());
    YamlConfiguration playerlistConfig = YamlConfiguration.loadConfiguration(playerlistFile);
    ConfigurationSection playerlistConfigSection = playerlistConfig.getConfigurationSection("playerlist");    //加载区域

    private void addPlayerlist(String UUID,String playerName) {
        playerlistConfig.set("playerlist." + UUID,playerName);
        try {
            playerlistConfig.save(playerlistFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //first_join_broadcast相关准备-----结束

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (UltiTools.getInstance().getConfig().getBoolean("enable_scoreboard")) {
            ScoreBoardUtils.registerPlayer(player.getUniqueId());
        }
        if (loginConfig.getBoolean("enableFixPointLogin")) {
            try {
                String worldName = loginConfig.getString("loginPoint.world");
                double x = loginConfig.getDouble("loginPoint.x");
                double y = loginConfig.getDouble("loginPoint.y");
                double z = loginConfig.getDouble("loginPoint.z");
                Location loginLocation = new Location(Bukkit.getWorld(worldName), x, y, z);
                player.teleport(loginLocation);
            } catch (Exception ignored) {
            }
        }
        if (UltiTools.getInstance().getConfig().getBoolean("enable_onjoin")) {
            String vanillaJoinMessage = event.getJoinMessage() == null ? "" : event.getJoinMessage();
            event.setJoinMessage(null);
            if (player.isOp()) {
                if (VersionChecker.isOutDate) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getString("join_send_update_reminding"), version, current_version));
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("join_send_update_tip"));
                        }
                    }.runTaskLaterAsynchronously(UltiTools.getInstance(), 80L);
                }
                Bukkit.broadcastMessage(PlaceholderAPI.setPlaceholders(player, opJoinMessage == null ? vanillaJoinMessage : opJoinMessage.replace("%player_name%", player.getName())));
            } else {
                Bukkit.broadcastMessage(PlaceholderAPI.setPlaceholders(player, playerJoinMessage == null ? vanillaJoinMessage : playerJoinMessage.replace("%player_name%", player.getName())));
            }

            new BukkitRunnable() {

                @Override
                public void run() {
                    for (String each : welcomeMessage) {
                        player.sendMessage(PlaceholderAPI.setPlaceholders(player, each.replaceAll("%player_name%", player.getName())));
                    }
                }

            }.runTaskLater(UltiTools.getInstance(), sendMessageDelay * 20L);
        }

        //新玩家全服欢迎公告功能
        String UUID = player.getUniqueId().toString();
        String playerName = player.getName();
        if (UltiTools.getInstance().getConfig().getBoolean("enable_first_join_broadcast")) {
            if (!playerlistConfigSection.getKeys(false).contains(UUID)) {
                addPlayerlist(UUID,playerName);
                Bukkit.broadcastMessage(PlaceholderAPI.setPlaceholders(player,firstJoinBroadcast.replaceAll("%player_name%",player.getName())));
            }
        } else {
            if (!playerlistConfigSection.getKeys(false).contains(UUID)) {
                addPlayerlist(UUID,playerName);
            }
        }
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
        Player player = event.getPlayer();
        String vanillaQuitMessage = event.getQuitMessage() == null ? "" : event.getQuitMessage();
        event.setQuitMessage(null);
        if (event.getPlayer().isOp()) {
            Bukkit.broadcastMessage(PlaceholderAPI.setPlaceholders(player, opQuitMessage == null ? vanillaQuitMessage : opQuitMessage.replace("%player_name%", player.getName())));
        } else {
            Bukkit.broadcastMessage(PlaceholderAPI.setPlaceholders(player, playerQuitMessage == null ? vanillaQuitMessage : playerQuitMessage.replace("%player_name%", player.getName())));
        }
        if (UltiTools.getInstance().getConfig().getBoolean("enable_scoreboard")) {
            ScoreBoardUtils.unregisterPlayer(player.getUniqueId());
        }
    }

    @EventHandler
    public void onJoinSaveIP(PlayerLoginEvent event) {
        if (!UltiTools.getInstance().getProChecker().getProStatus()) {
            return;
        }
        Player player = event.getPlayer();
        InetAddress ipAddress = event.getAddress();
        String ip = ipAddress.getHostAddress().replaceAll("\\.", "_");
        File file = new File(ConfigsEnum.LOGIN.toString());
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.getBoolean("enablePlayerLimitForOneIP")) {
            return;
        }
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
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + UltiTools.languageUtils.getString("login_ip_limit_warning"));
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

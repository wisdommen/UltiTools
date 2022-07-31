package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import java.io.File;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;


/**
 * @author Shpries
 */

@EventListener(function = "ban")
public class BanListener implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e){
        YamlConfiguration banListConfig = YamlConfiguration.loadConfiguration(new File(ConfigsEnum.BANLIST.toString()));
        ConfigurationSection banListSectionOfPlayer = banListConfig.getConfigurationSection("banlist.banned-players");
        ConfigurationSection banListSectionOfIP = banListConfig.getConfigurationSection("banlist.banned-ips");
        InetAddress ipAddress = e.getAddress();
        String ip = ipAddress.getHostAddress().replaceAll("\\.", "_");

        if(banListSectionOfIP != null && banListSectionOfIP.getKeys(false).contains(ip)) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, MessagesUtils.warning(UltiTools.languageUtils.getString("ban_your_ip_is_banned")) + "\n"
                                                                                        + banListConfig.getString("banlist.banned-ips." + ip));
        }
        if (banListSectionOfPlayer != null && banListSectionOfPlayer.getKeys(false).contains(getUUID(e.getPlayer()))) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, MessagesUtils.warning(UltiTools.languageUtils.getString("ban_your_account_is_banned")) + "\n"
                                                                                       + banListConfig.getString("banlist.banned-players." + getUUID(e.getPlayer()) + ".reason"));
        }
    }
    private String getUUID(Player p) {
        if(Bukkit.getServer().getOnlineMode()) {
            return p.getUniqueId().toString();
        } else {
            return java.util.UUID.nameUUIDFromBytes(String.format("OfflinePlayer:%s", p.getName()).getBytes(StandardCharsets.UTF_8)).toString();
        }
    }
}

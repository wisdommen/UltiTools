package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class ChatListener implements Listener {
    private static final List<String> ultilevelStrings = Arrays.asList("%ul_level%", "%ul_job%", "%ul_exp%", "%ul_mp%",
            "%ul_max_mp%", "%ul_max_exp%", "%ul_health%", "%ul_max_health%", "%ul_q_cd%", "%ul_w_cd%", "%ul_e_cd%",
            "%ul_r_cd%");

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String prefixes_str = ConfigController.getConfig("chat").getString("chat_prefix").replaceAll("%player_name%", player.getName()).replaceAll("%player_world%", player.getLocation().getWorld().getName()).replaceAll("&", "§");
        if (UltiTools.getInstance().getServer().getPluginManager().getPlugin("UltiLevel") == null) {
            prefixes_str = validateUltiLevelVariable(prefixes_str);
        }
        String papiMassage = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(player, prefixes_str));
        String message = papiMassage + ChatColor.WHITE + " %2$s";
        event.setFormat(message);
    }

    @EventHandler
    public void onPlayerChatReply(AsyncPlayerChatEvent event) {
        if (ConfigController.getConfig("main").getBoolean("enable_auto-reply") && UltiTools.isProVersion) {
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
                Bukkit.broadcastMessage(reply);
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
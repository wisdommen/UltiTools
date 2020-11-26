package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.ultitools.UltiTools;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;


public class ChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String prefixes_str = UltiTools.getInstance().getConfig().getString("chat_prefix");
        String papiMassage;
        try{
            papiMassage = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(player, prefixes_str));
        }catch (Exception e){
            UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] "+UltiTools.languageUtils.getWords("chat_prefix_downloading_papi"));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "papi ecloud download Player");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "papi reload");
            papiMassage = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(player, prefixes_str));
        }
        String message = papiMassage+ChatColor.WHITE+" %2$s";
        event.setFormat(message);
    }
}
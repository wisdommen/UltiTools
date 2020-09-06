package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.ultitools.UltiTools;
import me.clip.placeholderapi.PlaceholderAPI;
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
        event.setMessage(event.getMessage());
        event.setFormat(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(player, prefixes_str))+ChatColor.WHITE+" %2$s");
    }
}
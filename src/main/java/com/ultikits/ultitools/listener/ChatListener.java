package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.annotations.EventListener;
import com.ultikits.ultitools.services.ChatService;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author wisdomme,qianmo,Shpries
 */

@EventListener(function = "chat")
public class ChatListener implements Listener {

    /*
     * UltiTools 现在只有这一个消息处理入口
     * 不要再添加别的了！！
     * 请在 ChatService 类中添加 filter 或 handle 方法来处理消息内容
     * 请统一使用 Chat Component API 处理消息，确保事件最后是被取消的！
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        ChatService.filterMessage(event);
        if (event.isCancelled()) return;
        BaseComponent[] prefix = ChatService.compilePrefix(event.getPlayer());
        ChatService.filterAt(event);
        if (event.isCancelled()) return;
        ChatService.filterChatColor(event);
        // ==============================请在上方处理消息==============================//
        BaseComponent[] FinalMessage = new ComponentBuilder("")
                .append(prefix)
                .append(ChatService.compileMessage(event.getMessage())).create();
        Bukkit.spigot().broadcast(FinalMessage);
        Bukkit.getConsoleSender().spigot().sendMessage(FinalMessage);
        ChatService.handleAutoReply(event.getMessage());
        event.setCancelled(true);
    }
}

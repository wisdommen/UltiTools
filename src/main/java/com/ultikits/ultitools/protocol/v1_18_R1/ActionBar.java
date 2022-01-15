package com.ultikits.ultitools.protocol.v1_18_R1;

import com.ultikits.ultitools.apis.ActionBarAPI;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar implements ActionBarAPI {
    @Override
    public void sendActionbar(Player player, String message) {
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(iChatBaseComponent, ChatMessageType.c, player.getUniqueId());
        ((CraftPlayer) player).getHandle().b.a(packetPlayOutChat);
    }
}

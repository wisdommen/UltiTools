package com.ultikits.ultitools.listener;

import com.ultikits.packet.*;
import com.ultikits.ultitools.commands.SilentChestOpenCommands;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SilentChestOpenListener implements PacketListener, Listener {
    @PacketHandler(type = PacketType.OUT, packet = "PacketPlayOutBlockAction")
    public void onPacketOut(PacketEvent event) {
        event.setCancelled(SilentChestOpenCommands.scoPlayer.contains(event.getPlayer().getBukkitPlayer().getName()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTask(UltiTools.getInstance(), PacketController.getPlayer(event.getPlayer())::hook);
    }
}

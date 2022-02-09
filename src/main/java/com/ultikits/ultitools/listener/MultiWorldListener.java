package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.commands.MultiWorldsCommands;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class MultiWorldListener implements Listener {

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        event.setCancelled(!event.getPlayer().isOp() && MultiWorldsCommands.protectedWorlds.contains(event.getBlock().getWorld().getName()));
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        event.setCancelled(!event.getPlayer().isOp() && MultiWorldsCommands.protectedWorlds.contains(event.getBlock().getWorld().getName()));
    }

    @EventHandler
    public void onPlayerInteractBlock(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        event.setCancelled(!event.getPlayer().isOp() && MultiWorldsCommands.protectedWorlds.contains(event.getClickedBlock().getWorld().getName()));
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        event.setCancelled(!MultiWorldsCommands.protectedWorlds.contains(event.getBlock().getWorld().getName()));
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        event.setCancelled(!Objects.requireNonNull(((Player) event.getDamager()).getPlayer()).isOp() && MultiWorldsCommands.noPvpWorlds.contains(event.getDamager().getWorld().getName()));
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        event.setCancelled(MultiWorldsCommands.noSpawnWorlds.contains(event.getEntity().getWorld().getName()));
    }
}

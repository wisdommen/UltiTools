package com.ultikits.ultitools.listener;

import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.SilentOpenUtils;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class SilentOpenListener implements Listener {
    @EventHandler
    public void onClickChest(PlayerInteractEvent event) {
        if (
                Objects.requireNonNull(event.getClickedBlock()).getType() != Material.CHEST
                        || Objects.requireNonNull(event.getClickedBlock()).getType() != Material.ENDER_CHEST
                        || Objects.requireNonNull(event.getClickedBlock()).getType() != Material.SHULKER_BOX
        ) return;
        if (!SilentOpenUtils.getPlayers().contains(event.getPlayer())) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        Chest chest = (Chest) event.getClickedBlock();
        Inventory inventory = chest.getBlockInventory();
        player.openInventory(inventory);
        player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("you_have_open_the_chest_silently")));
        SilentOpenUtils.getPlayers().remove(player);
    }
}

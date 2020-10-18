package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.enums.Sounds;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.minecraft.ultikits.utils.MessagesUtils.info;

/**
 * @author wisdomme
 */
public class ChestLockListener implements Listener {

    static File chestConfigFile = new File(ConfigsEnum.CHEST_LOCK.toString());
    static YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestConfigFile);

    @EventHandler
    public void onPlacePlaceChest(BlockPlaceEvent event){
        Block placedBlock = event.getBlock();
        if (placedBlock.getType() == Material.CHEST){
            Player player = event.getPlayer();
            player.sendMessage(info(UltiTools.languageUtils.getWords("lock_tip")));
        }
    }

    @EventHandler
    public void onPlayerOpenChest(@NotNull PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST) {
            Player player = event.getPlayer();
            Location chestLocation = event.getClickedBlock().getLocation();
            File chestFile = new File(ConfigsEnum.CHEST.toString());
            File playerFile = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
            YamlConfiguration chestData = YamlConfiguration.loadConfiguration(chestFile);
            YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            List<String> chests = chestData.getStringList("locked");

            String world = Objects.requireNonNull(chestLocation.getWorld()).getName();
            double x = chestLocation.getX();
            double y = chestLocation.getY();
            double z = chestLocation.getZ();
            String local = player.getName() + "/" + world + "/" + x + "/" + y + "/" + z;

            if (playerData.getBoolean("lock")) {
                event.setCancelled(true);
                playerData.set("lock", false);
                try {
                    playerData.save(playerFile);
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("lock_file_save_fail_click"));
                    return;
                }
                if (chests.contains(local)) {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("lock_chest_already_locked"));
                    return;
                } else {
                    for (String each : chests) {
                        if (each.contains("/" + world + "/" + x + "/" + y + "/" + z)) {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("lock_you_cannot_lock_others_chest"));
                            return;
                        }
                    }
                }
                chests.add(local);
                chestData.set("locked", chests);
                try {
                    chestData.save(chestFile);
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED +UltiTools.languageUtils.getWords("lock_file_save_failed"));
                    return;
                }
                player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getWords("lock_successfully"));
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("lock_tip_after_lock"));
                player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_CHEST_LOCKED), 10, 1);
                return;
            } else if (playerData.getBoolean("unlock")) {
                event.setCancelled(true);
                playerData.set("unlock", false);
                try {
                    playerData.save(playerFile);
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("unlock_file_save_fail_click"));
                    return;
                }
                if (chests.contains(local)) {
                    chests.remove(local);
                    chestData.set("locked", chests);
                    try {
                        chestData.save(chestFile);
                    } catch (IOException e) {
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("unlock_file_save_failed"));
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getWords("unlock_successfully"));
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("unlock_tip_after_unlock"));
                    player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_CHEST_LOCKED), 10, 1);
                    return;
                } else {
                    for (String each : chests) {
                        if (each.contains("/" + world + "/" + x + "/" + y + "/" + z)) {
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("unlock_you_cannot_unlock_others_chest"));
                            return;
                        }
                    }
                }
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("unlock_chest_not_locked"));
                return;
            }
            if (!chests.contains(local)) {
                for (String each : chests) {
                    if (each.contains("/" + world + "/" + x + "/" + y + "/" + z)) {
                        if ((chestConfig.getBoolean("op_unlock") && player.isOp() && event.getAction() == Action.RIGHT_CLICK_BLOCK) || (chestConfig.getBoolean("op_break_locked") && player.isOp() && event.getAction() == Action.LEFT_CLICK_BLOCK)) {
                            player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getWords("lock_op_warning"));
                            return;
                        }
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("lock_this_is_others_chest"));
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDestroyChest(@NotNull BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.CHEST) {
            Location chestLocation = event.getBlock().getLocation();
            Player player = event.getPlayer();
            File chestFile = new File(ConfigsEnum.CHEST.toString());
            YamlConfiguration chestData = YamlConfiguration.loadConfiguration(chestFile);
            List<String> chests = chestData.getStringList("locked");
            int sizeBefore = chests.size();

            String world = Objects.requireNonNull(chestLocation.getWorld()).getName();
            double x = chestLocation.getX();
            double y = chestLocation.getY();
            double z = chestLocation.getZ();
            String local = player.getName() + "/" + world + "/" + x + "/" + y + "/" + z;

            if (player.isOp()) {
                chests.removeIf(each -> each.contains("/" + world + "/" + x + "/" + y + "/" + z));
            } else {
                chests.remove(local);
            }
            if (sizeBefore>chests.size()) {
                chestData.set("locked", chests);
                try {
                    chestData.save(chestFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("lock_chest_deleted"));
            }
        }
    }

    @EventHandler
    public void onItemRemovedByHopper(@NotNull InventoryMoveItemEvent event){
        Location chestLocation = event.getSource().getLocation();
        File chestFile = new File(ConfigsEnum.CHEST.toString());
        YamlConfiguration chestData = YamlConfiguration.loadConfiguration(chestFile);
        List<String> chests = chestData.getStringList("locked");

        String world = Objects.requireNonNull(chestLocation.getWorld()).getName();
        double x = chestLocation.getX();
        double y = chestLocation.getY();
        double z = chestLocation.getZ();
        String local = world + "/" + x + "/" + y + "/" + z;

        for (String each : chests){
            if (each.contains(local)){
                event.setCancelled(true);
            }
        }
    }
}

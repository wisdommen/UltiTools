package com.ultikits.ultitools.listener;

import com.ultikits.enums.ChestDirection;
import com.ultikits.enums.Sounds;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.utils.ChestLockUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.Random;

import static com.ultikits.utils.MessagesUtils.info;

/**
 * @author wisdomme, qianmo
 * <p>
 * Code refactoring by qianmo
 */

public class ChestLockListener implements Listener {

    @EventHandler
    public void onPlayerPlaceChest(BlockPlaceEvent event) {
        Block chest = event.getBlock();
        Player player = event.getPlayer();

        Map<ChestDirection, Block> blockMap;

        Block right;
        Block left;

        //判断被放置的方块是否为箱子
        if (chest.getType() != Material.CHEST) return;

        //判断被放置的箱子的旁边是否有上锁的箱子
        blockMap = ChestLockUtils.getBlocksBesides(chest);
        right = blockMap.get(ChestDirection.RIGHT);
        left = blockMap.get(ChestDirection.LEFT);

        if (!(right.getType() == Material.CHEST || left.getType() == Material.CHEST)) {
            //提示
            Random random = new Random();
            int i = random.nextInt(3);
            if (i <= 1) {
                player.sendMessage(info(UltiTools.languageUtils.getString("lock_tip")));
            }
        } else {
            Boolean result = ChestLockUtils.checkNewChestCanPlace(chest, left, right, player);
            event.setCancelled(!result);
        }
    }

    @EventHandler
    public void onPlayerOpenChest(PlayerInteractEvent event) {
        Block chest = event.getClickedBlock();
        Player player = event.getPlayer();

        if (chest == null) return;

        //判断被点击的方块是否为箱子
        if (chest.getType() != Material.CHEST) return;

        //判断玩家是否处于选择模式
        if (ChestLockUtils.getInAddMode().containsKey(player.getName())) {
            if (ChestLockUtils.hasChestData(chest)) {
                if (ChestLockUtils.isChestOwner(chest, player)) {
                    ChestLockUtils.addChestOwner(chest, ChestLockUtils.getInAddMode().get(player.getName()));
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("chest_add_owner_successfully"));
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_this_is_others_chest"));
                }
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("chest_data_not_found"));
            }
            ChestLockUtils.getInAddMode().remove(player.getName());
            event.setCancelled(true);
            return;
        }
        if (ChestLockUtils.getInRemoveMode().containsKey(player.getName())) {
            if (ChestLockUtils.hasChestData(chest)) {
                if (ChestLockUtils.isChestOwner(chest, player)) {
                    ChestLockUtils.removeChestOwner(chest, ChestLockUtils.getInRemoveMode().get(player.getName()));
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("chest_remove_owner_successfully"));
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_this_is_others_chest"));
                }
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("chest_data_not_found"));
            }
            ChestLockUtils.getInRemoveMode().remove(player.getName());
            event.setCancelled(true);
            return;
        }
        if (ChestLockUtils.getInLockMode().contains(player.getName())) {
            if (ChestLockUtils.hasChestData(chest)) {
                if (ChestLockUtils.isChestOwner(chest, player)) {
                    if (ChestLockUtils.isChestLocked(chest)) {
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_chest_already_locked"));
                    } else {
                        ChestLockUtils.lockChest(chest);
                        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_CHEST_LOCKED), 10, 1);
                        player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("lock_successfully"));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_you_cannot_lock_others_chest"));
                }
            } else {
                ChestLockUtils.addChestData(chest, player, true);
                player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_CHEST_LOCKED), 10, 1);
                player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("lock_successfully"));
            }
            ChestLockUtils.getInLockMode().remove(player.getName());
            event.setCancelled(true);
            return;
        }
        if (ChestLockUtils.getInUnlockMode().contains(player.getName())) {
            if (ChestLockUtils.hasChestData(chest)) {
                if (ChestLockUtils.isChestOwner(chest, player)) {
                    if (ChestLockUtils.isChestLocked(chest)) {
                        ChestLockUtils.unlockChest(chest);
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("unlock_successfully"));
                    } else {
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("unlock_chest_not_locked"));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_this_is_others_chest"));
                }
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("unlock_chest_not_locked"));
            }
            ChestLockUtils.getInUnlockMode().remove(player.getName());
            event.setCancelled(true);
            return;
        }
        if (player.isOp()) {
            if (ConfigController.getConfig("chestlock").getBoolean("op_unlock")) {
                return;
            }
        }
        if (ChestLockUtils.hasChestData(chest)) {
            if (ChestLockUtils.isChestOwner(chest, player)) return;
            if (ChestLockUtils.isChestLocked(chest)) {
                player.sendMessage(String.format(ChatColor.RED + UltiTools.languageUtils.getString("lock_no_permission"), ChestLockUtils.getChestOwner(chest)));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerBreakChest(BlockBreakEvent event) {
        Block chest = event.getBlock();
        Player player = event.getPlayer();

        //判断被破坏的方块是否为箱子
        if (chest.getType() != Material.CHEST) return;

        if (!ChestLockUtils.hasChestData(chest)) return;

        if (player.isOp() && ConfigController.getConfig("chestlock").getBoolean("op_break_locked")) {
            ChestLockUtils.removeChestData(chest);
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_chest_deleted"));
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_op_warning"));
            return;
        }
        if (ChestLockUtils.isChestOwner(chest, player)) {
            ChestLockUtils.removeChestData(chest);
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_chest_deleted"));
        } else {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_this_is_others_chest"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemRemovedByHopper(InventoryMoveItemEvent event) {
        Location location = event.getSource().getLocation();
        if (location == null) return;
        if (ChestLockUtils.isChestLocked(location)) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if ((event.getEntity() instanceof Creeper) || (event.getEntity() instanceof TNTPrimed)) {
            for (Block block : event.blockList().toArray(new Block[event.blockList().size()])) {
                if (block.getType() == Material.CHEST) {
                    if (ChestLockUtils.isChestLocked(block)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}

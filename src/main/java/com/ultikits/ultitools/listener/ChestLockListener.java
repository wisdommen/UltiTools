package com.ultikits.ultitools.listener;

import com.ultikits.enums.ChestDirection;
import com.ultikits.enums.Sounds;
import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.services.ChestLockService;
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
import org.bukkit.event.player.PlayerMoveEvent;

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
        Block  chest  = event.getBlock();
        Player player = event.getPlayer();

        Map<ChestDirection, Block> blockMap;

        Block right;
        Block left;

        //判断被放置的方块是否为箱子
        if (chest.getType() != Material.CHEST) return;

        //判断被放置的箱子的旁边是否有上锁的箱子
        blockMap = ChestLockService.getBlocksBesides(chest);
        right    = blockMap.get(ChestDirection.RIGHT);
        left     = blockMap.get(ChestDirection.LEFT);

        if (!(right.getType() == Material.CHEST || left.getType() == Material.CHEST)) {
            //提示
            Random random = new Random();
            int i = random.nextInt(3);
            if (i <= 1) player.sendMessage(info(UltiTools.languageUtils.getString("lock_tip")));
        } else {
            Boolean result = ChestLockService.checkNewChestCanPlace(chest, left, right, player);
            event.setCancelled(!result);
        }
    }

    @EventHandler
    public void onPlayerOpenChest(PlayerInteractEvent event) {
        Block  chest  = event.getClickedBlock();
        Player player = event.getPlayer();

        if (chest == null) return;

        //判断被点击的方块是否为箱子
        if (chest.getType() != Material.CHEST) return;

        //判断玩家是否处于选择模式
        if (ChestLockService.getInAddMode().containsKey(player.getName())) {
            if (ChestLockService.hasChestData(chest)) {
                if (ChestLockService.isChestAdmin(chest, player)) {
                    ChestLockService.addChestOwner(chest, ChestLockService.getInAddMode().get(player.getName()));
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("chest_add_owner_successfully"));
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("must_be_chest_admin"));
                }
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("chest_data_not_found"));
            }
            ChestLockService.getInAddMode().remove(player.getName());
            event.setCancelled(true);
            return;
        }
        if (ChestLockService.getInRemoveMode().containsKey(player.getName())) {
            if (ChestLockService.hasChestData(chest)) {
                if (ChestLockService.isChestAdmin(chest, player)) {
                    ChestLockService.removeChestOwner(chest, ChestLockService.getInRemoveMode().get(player.getName()));
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("chest_remove_owner_successfully"));
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("must_be_chest_admin"));
                }
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("chest_data_not_found"));
            }
            ChestLockService.getInRemoveMode().remove(player.getName());
            event.setCancelled(true);
            return;
        }
        if (ChestLockService.getInLockMode().contains(player.getName())) {
            if (ChestLockService.hasChestData(chest)) {
                if (ChestLockService.isChestOwner(chest, player)) {
                    if (ChestLockService.isChestLocked(chest)) {
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_chest_already_locked"));
                    } else {
                        ChestLockService.lockChest(chest);
                        player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_CHEST_LOCKED), 10, 1);
                        player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("lock_successfully"));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_you_cannot_lock_others_chest"));
                }
            } else {
                ChestLockService.addChestData(chest, player, true);
                player.playSound(player.getLocation(), UltiTools.versionAdaptor.getSound(Sounds.BLOCK_CHEST_LOCKED), 10, 1);
                player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("lock_successfully"));
            }
            ChestLockService.getInLockMode().remove(player.getName());
            event.setCancelled(true);
            return;
        }
        if (ChestLockService.getInUnlockMode().contains(player.getName())) {
            if (ChestLockService.hasChestData(chest)) {
                if (ChestLockService.isChestOwner(chest, player)) {
                    if (ChestLockService.isChestLocked(chest)) {
                        ChestLockService.unlockChest(chest);
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
            ChestLockService.getInUnlockMode().remove(player.getName());
            event.setCancelled(true);
            return;
        }
        if (ChestLockService.getInTransferMode().containsKey(player.getName())) {
            if (ChestLockService.hasChestData(chest)) {
                if (ChestLockService.isChestAdmin(chest, player)) {
                    ChestLockService.transferChest(chest, player.getName(), ChestLockService.getInTransferMode().get(player.getName()));
                    player.sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getString("chest_transferred"));
                } else {
                    player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("must_be_chest_owner"));
                }
            } else {
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("chest_data_not_found"));
            }
            ChestLockService.getInTransferMode().remove(player.getName());
            event.setCancelled(true);
            return;
        }
        if (player.isOp()) {
            if (ConfigController.getConfig("chestlock").getBoolean("op_unlock")) {
                return;
            }
        }
        if (ChestLockService.hasChestData(chest)) {
            if (ChestLockService.isChestOwner(chest, player)) return;
            if (ChestLockService.isChestLocked(chest)) {
                player.sendMessage(String.format(ChatColor.RED + UltiTools.languageUtils.getString("lock_no_permission"), ChestLockService.getChestOwner(chest)));
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

        if (!ChestLockService.hasChestData(chest)) return;

        if (player.isOp() && ConfigController.getConfig("chestlock").getBoolean("op_break_locked")) {
            ChestLockService.removeChestData(chest);
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_chest_deleted"));
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_op_warning"));
            return;
        }
        if (ChestLockService.isChestOwner(chest, player)) {
            ChestLockService.removeChestData(chest);
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
        if (ChestLockService.isChestLocked(location)) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if ((event.getEntity() instanceof Creeper) || (event.getEntity() instanceof TNTPrimed)) {
            for (Block block : event.blockList().toArray(new Block[event.blockList().size()])) {
                if (block.getType() == Material.CHEST) {
                    if (ChestLockService.isChestLocked(block)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player  = event.getPlayer();
        Block  block   = player.getTargetBlock(null, 5);

        if (block.getType() != Material.CHEST) return;
        if (!ChestLockService.hasChestData(block)) return;

        String  owner         = ChestLockService.getChestAdmin(block);
        Boolean locked        = ChestLockService.isChestLocked(block);

        UltiTools.versionAdaptor.sendActionBar(
                player,
                "" +
                        ChatColor.YELLOW +
                        UltiTools.languageUtils.getString("lock_chest_owner") +
                        owner +
                        ChatColor.AQUA +
                        "  |  " +
                        (locked ? ChatColor.RED + UltiTools.languageUtils.getString("locked") : ChatColor.GREEN + UltiTools.languageUtils.getString("unlocked"))
        );
    }
}

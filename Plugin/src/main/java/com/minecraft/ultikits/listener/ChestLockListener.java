package com.minecraft.ultikits.listener;

import com.minecraft.ultikits.config.ConfigController;
import com.minecraft.ultikits.enums.ConfigsEnum;
import com.minecraft.ultikits.enums.Sounds;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.minecraft.ultikits.utils.MessagesUtils.info;

/**
 * @author wisdomme
 */
public class ChestLockListener implements Listener {

    static File chestConfigFile = new File(ConfigsEnum.CHEST_LOCK.toString());
    static YamlConfiguration chestConfig = YamlConfiguration.loadConfiguration(chestConfigFile);

    @EventHandler
    public void onPlacePlaceChest(BlockPlaceEvent event) {
        Block placedBlock = event.getBlock();
        if (placedBlock.getType() == Material.CHEST) {
            Player player = event.getPlayer();
            Map<Direction, Block> blockMap = getBlocksBesides(placedBlock);
            Block right = blockMap.get(Direction.RIGHT);
            Block left = blockMap.get(Direction.LEFT);
            List<String> chests = (List<String>) ConfigController.getValue("locked");
            if (!(right.getType() == Material.CHEST || left.getType() == Material.CHEST)) {
                Random random = new Random();
                int i = random.nextInt(3);
                if (i <= 1) {
                    player.sendMessage(info(UltiTools.languageUtils.getWords("lock_tip")));
                }
            } else {
                BlockData blockData = placedBlock.getBlockData();
                BlockFace blockFace = ((Directional) blockData).getFacing();

                Location blockLocation = right.getLocation();
                if (!checkNewChestBlock(right, placedBlock, blockFace, blockLocation, player, chests)) {
                    event.setCancelled(true);
                }
                blockLocation = left.getLocation();
                if (!checkNewChestBlock(left, placedBlock, blockFace, blockLocation, player, chests)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerOpenChest(@NotNull PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST) {
            Player player = event.getPlayer();
            Location chestLocation = event.getClickedBlock().getLocation();
            File playerFile = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
            YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            List<String> chests = (List<String>) ConfigController.getValue("locked");

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
                ConfigController.setValue("locked", chests);
                ConfigController.saveConfig("chestData");
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
                    ConfigController.setValue("locked", chests);
                    ConfigController.saveConfig("chestData");
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
            List<String> chests = (List<String>) ConfigController.getValue("locked");
            int sizeBefore = chests.size();

            String world = Objects.requireNonNull(chestLocation.getWorld()).getName();
            double x = chestLocation.getX();
            double y = chestLocation.getY();
            double z = chestLocation.getZ();
            String local = getFormattedChestLocation(player, chestLocation);

            if (player.isOp()) {
                chests.removeIf(each -> each.contains("/" + world + "/" + x + "/" + y + "/" + z));
            } else {
                chests.remove(local);
            }
            if (sizeBefore > chests.size()) {
                ConfigController.setValue("locked", chests);
                ConfigController.saveConfig("chestData");
                player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("lock_chest_deleted"));
            }
        }
    }

    @EventHandler
    public void onItemRemovedByHopper(@NotNull InventoryMoveItemEvent event) {
        Location chestLocation = event.getSource().getLocation();
        List<String> chests = (List<String>) ConfigController.getValue("locked");

        String world = Objects.requireNonNull(chestLocation.getWorld()).getName();
        double x = chestLocation.getX();
        double y = chestLocation.getY();
        double z = chestLocation.getZ();
        String local = world + "/" + x + "/" + y + "/" + z;

        for (String each : chests) {
            if (each.contains(local)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if ((event.getEntity() instanceof Creeper) || (event.getEntity() instanceof TNTPrimed)) {
            for (Block block : event.blockList().toArray(new Block[event.blockList().size()])) {
                if (block.getType() == Material.CHEST) {
                    List<String> chests = (List<String>) ConfigController.getValue("locked");
                    Location chestLocation = block.getLocation();
                    String world = Objects.requireNonNull(chestLocation.getWorld()).getName();
                    double x = chestLocation.getX();
                    double y = chestLocation.getY();
                    double z = chestLocation.getZ();
                    for (String each : chests) {
                        if (each.contains("/" + world + "/" + x + "/" + y + "/" + z)) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    private Map<Direction, Block> getBlocksBesides(Block placedBlock) {
        Map<Direction, Block> blockMap = new HashMap<>();
        if (placedBlock.getState() instanceof Chest) {
            BlockData blockData = placedBlock.getBlockData();
            BlockFace blockFace = ((Directional) blockData).getFacing();

            Block right = null;
            Block left = null;
            switch (blockFace) {
                case EAST:
                    right = placedBlock.getRelative(BlockFace.NORTH);
                    left = placedBlock.getRelative(BlockFace.SOUTH);
                    break;
                case SOUTH:
                    right = placedBlock.getRelative(BlockFace.EAST);
                    left = placedBlock.getRelative(BlockFace.WEST);
                    break;
                case NORTH:
                    right = placedBlock.getRelative(BlockFace.WEST);
                    left = placedBlock.getRelative(BlockFace.EAST);
                    break;
                case WEST:
                    right = placedBlock.getRelative(BlockFace.SOUTH);
                    left = placedBlock.getRelative(BlockFace.NORTH);
                    break;
            }
            blockMap.put(Direction.RIGHT, right);
            blockMap.put(Direction.LEFT, left);
        }
        return blockMap;
    }

    private String getFormattedChestLocation(Player player, Location chestLocation) {
        String world = Objects.requireNonNull(chestLocation.getWorld()).getName();
        double x = chestLocation.getX();
        double y = chestLocation.getY();
        double z = chestLocation.getZ();
        return player.getName() + "/" + world + "/" + x + "/" + y + "/" + z;
    }

    private boolean checkCanPlaceChest(Location blockLocation, Player player, List<String> registeredChests) {
        String world = Objects.requireNonNull(blockLocation.getWorld()).getName();
        double x = blockLocation.getX();
        double y = blockLocation.getY();
        double z = blockLocation.getZ();
        String location = getFormattedChestLocation(player, blockLocation);
        if (registeredChests.contains(location)) {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("lock_auto_lock"));
            return true;
        } else {
            for (String each : registeredChests) {
                if (each.contains("/" + world + "/" + x + "/" + y + "/" + z)) {
                    player.sendMessage(info(UltiTools.languageUtils.getWords("lock_locked_chest_besides")));
                    return false;
                }
            }
        }
        return true;
    }

    private void saveNewChestLocation(Player player, Block placedBlock, List<String> chests) {
        String loc = getFormattedChestLocation(player, placedBlock.getLocation());
        chests.add(loc);
        ConfigController.setValue("locked", chests);
        ConfigController.saveConfig("chestData");
    }

    private void saveNewChest(Location blockLocation, Player player, Block placedBlock, List<String> chests) {
        String world = Objects.requireNonNull(blockLocation.getWorld()).getName();
        double x = blockLocation.getX();
        double y = blockLocation.getY();
        double z = blockLocation.getZ();
        for (String each : chests) {
            if (each.contains("/" + world + "/" + x + "/" + y + "/" + z)) {
                saveNewChestLocation(player, placedBlock, chests);
            }
        }
    }

    private boolean checkNewChestBlock(Block block, Block placedBlock, BlockFace blockFace, Location blockLocation, Player player, List<String> chests) {
        if (block.getType() == Material.CHEST) {
            BlockData leftBlockData = block.getBlockData();
            BlockFace leftBlockFace = ((Directional) leftBlockData).getFacing();
            if (leftBlockFace != blockFace) {
                return true;
            }
            if (!checkCanPlaceChest(blockLocation, player, chests)) {
                return false;
            } else {
                saveNewChest(blockLocation, player, placedBlock, chests);
                return true;
            }
        }
        return true;
    }
}

enum Direction {
    LEFT, RIGHT
}

package com.ultikits.ultitools.utils;

import com.ultikits.enums.ChestDirection;
import com.ultikits.ultitools.beans.DoubleChestLocationBean;
import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.ultikits.utils.MessagesUtils.info;

/**
 * @author qianmo
 */

public class ChestLockUtils {
    private static final File file = new File(ConfigsEnum.CHEST.toString());
    private static final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

    private static List<String> inLockMode = new ArrayList<>();
    private static Map<String, String> inAddMode = new HashMap<>();
    private static Map<String, String> inRemoveMode = new HashMap<>();
    private static List<String> inUnlockMode = new ArrayList<>();

    public static Map<String, String> getInRemoveMode() {
        return inRemoveMode;
    }

    public static void setInRemoveMode(Map<String, String> inRemoveMode) {
        ChestLockUtils.inRemoveMode = inRemoveMode;
    }

    public static List<String> getInUnlockMode() {
        return inUnlockMode;
    }

    public static void setInUnlockMode(List<String> inUnlockMode) {
        ChestLockUtils.inUnlockMode = inUnlockMode;
    }

    public static List<String> getInLockMode() {
        return inLockMode;
    }

    public static void setInLockMode(List<String> inLockMode) {
        ChestLockUtils.inLockMode = inLockMode;
    }

    public static Map<String, String> getInAddMode() {
        return inAddMode;
    }

    public static void setInAddMode(Map<String, String> inAddMode) {
        ChestLockUtils.inAddMode = inAddMode;
    }

    public static Map<ChestDirection, Block> getBlocksBesides(Block placedBlock) {
        Map<ChestDirection, Block> blockMap = new HashMap<>();
        if (placedBlock.getState() instanceof Chest) {
            BlockFace blockFace = UltiTools.versionAdaptor.getBlockFace(placedBlock);

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
            blockMap.put(ChestDirection.RIGHT, right);
            blockMap.put(ChestDirection.LEFT, left);
        }
        return blockMap;
    }

    public static String getFormattedChestLocation(Location location) {
        String world = Objects.requireNonNull(location.getWorld()).getName();
        int x = (int) location.getX();
        int y = (int) location.getY();
        int z = (int) location.getZ();
        return world + "/" + x + "/" + y + "/" + z;
    }

    public static Boolean checkNewChestCanPlace(Block chest, Block left, Block right, Player player) {
        BlockFace placeBlockFace = UltiTools.versionAdaptor.getBlockFace(chest);
        Boolean checkLeft = checkChestBeside(placeBlockFace, left, player);
        Boolean checkRight = checkChestBeside(placeBlockFace, right, player);
        if (checkLeft == null && checkRight == null) {
            return true;
        }
        if (checkLeft != null && checkLeft) {
            addChestData(chest, player, true);
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_auto_lock").replace("\\n", "\n"));
            return true;
        }
        if (checkRight != null && checkRight) {
            addChestData(chest, player, true);
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("lock_auto_lock").replace("\\n", "\n"));
            return true;
        }
        player.sendMessage(info(UltiTools.languageUtils.getString("lock_locked_chest_besides").replace("\\n", "\n")));
        return false;
    }

    private static Boolean checkChestBeside(BlockFace placeBlockFace, Block side, Player player) {
        if (side.getType() != Material.CHEST) {
            return null;
        }
        BlockFace leftBlockFace = UltiTools.versionAdaptor.getBlockFace(side);
        if (leftBlockFace != placeBlockFace || !hasChestData(side) || !isChestLocked(side)) {
            return null;
        }
        return isChestOwner(side, player);
    }

    public static void addChestData(Block chest, Player player, Boolean locked) {
        if (isDoubleChest(chest)) {
            DoubleChestLocationBean doubleChestLocationBean = new DoubleChestLocationBean(chest);
            String formattedRightChestLocation = getFormattedChestLocation(doubleChestLocationBean.getRightSideLocation());
            String formattedLeftChestLocation = getFormattedChestLocation(doubleChestLocationBean.getLeftSideLocation());
            if (configuration.get(formattedRightChestLocation) == null) {
                configuration.set(formattedRightChestLocation + "." + "location", chest.getLocation());
                configuration.set(formattedRightChestLocation + "." + "owners", Collections.singletonList(player.getName()));
                configuration.set(formattedRightChestLocation + "." + "locked", locked);
            }
            if (configuration.get(formattedLeftChestLocation) == null) {
                configuration.set(formattedLeftChestLocation + "." + "location", chest.getLocation());
                configuration.set(formattedLeftChestLocation + "." + "owners", Collections.singletonList(player.getName()));
                configuration.set(formattedLeftChestLocation + "." + "locked", locked);
            }
        } else {
            String formattedChestLocation = getFormattedChestLocation(chest.getLocation());
            if (configuration.get(formattedChestLocation) == null) {
                configuration.set(formattedChestLocation + "." + "location", chest.getLocation());
                configuration.set(formattedChestLocation + "." + "owners", Collections.singletonList(player.getName()));
                configuration.set(formattedChestLocation + "." + "locked", locked);
            }
        }

        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setChestOwner(Block chest, List<String> players) {
        if (isDoubleChest(chest)) {
            DoubleChestLocationBean doubleChestLocationBean = new DoubleChestLocationBean(chest);
            String formattedRightChestLocation = getFormattedChestLocation(doubleChestLocationBean.getRightSideLocation());
            String formattedLeftChestLocation = getFormattedChestLocation(doubleChestLocationBean.getLeftSideLocation());
            if (configuration.get(formattedRightChestLocation) != null) {
                configuration.set(formattedRightChestLocation + "." + "owners", players);
            }
            if (configuration.get(formattedLeftChestLocation) != null) {
                configuration.set(formattedLeftChestLocation + "." + "owners", players);
            }
        } else {
            String formattedChestLocation = getFormattedChestLocation(chest.getLocation());
            configuration.set(formattedChestLocation + "." + "owners", players);
        }
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addChestOwner(Block chest, String player) {
        if (isDoubleChest(chest)) {
            DoubleChestLocationBean doubleChestLocationBean = new DoubleChestLocationBean(chest);
            String formattedRightChestLocation = getFormattedChestLocation(doubleChestLocationBean.getRightSideLocation());
            String formattedLeftChestLocation = getFormattedChestLocation(doubleChestLocationBean.getLeftSideLocation());
            if (configuration.get(formattedRightChestLocation) != null) {
                List<String> players = configuration.getStringList(formattedRightChestLocation + "." + "owners");
                if (!players.contains(player)) {
                    players.add(player);
                    configuration.set(formattedRightChestLocation + "." + "owners", players);
                }
            }
            if (configuration.get(formattedLeftChestLocation) != null) {
                List<String> players = configuration.getStringList(formattedLeftChestLocation + "." + "owners");
                if (!players.contains(player)) {
                    players.add(player);
                    configuration.set(formattedLeftChestLocation + "." + "owners", players);
                }
            }
        } else {
            String formattedChestLocation = getFormattedChestLocation(chest.getLocation());
            List<String> players = configuration.getStringList(formattedChestLocation + "." + "owners");
            if (players.contains(player)) return;
            players.add(player);
            configuration.set(formattedChestLocation + "." + "owners", players);
        }
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeChestOwner(Block chest, String player) {
        if (isDoubleChest(chest)) {
            DoubleChestLocationBean doubleChestLocationBean = new DoubleChestLocationBean(chest);
            String formattedRightChestLocation = getFormattedChestLocation(doubleChestLocationBean.getRightSideLocation());
            String formattedLeftChestLocation = getFormattedChestLocation(doubleChestLocationBean.getLeftSideLocation());
            if (configuration.get(formattedRightChestLocation) != null) {
                List<String> players = configuration.getStringList(formattedRightChestLocation + "." + "owners");
                if (players.contains(player)) {
                    players.remove(player);
                    configuration.set(formattedRightChestLocation + "." + "owners", players);
                }
            }
            if (configuration.get(formattedLeftChestLocation) != null) {
                List<String> players = configuration.getStringList(formattedLeftChestLocation + "." + "owners");
                if (players.contains(player)) {
                    players.remove(player);
                    configuration.set(formattedLeftChestLocation + "." + "owners", players);
                }
            }
        } else {
            String formattedChestLocation = getFormattedChestLocation(chest.getLocation());
            List<String> players = configuration.getStringList(formattedChestLocation + "." + "owners");
            if (!players.contains(player)) return;
            players.remove(player);
            configuration.set(formattedChestLocation + "." + "owners", players);
        }
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void lockChest(Block chest) {
        if (isDoubleChest(chest)) {
            DoubleChestLocationBean doubleChestLocationBean = new DoubleChestLocationBean(chest);
            String formattedRightChestLocation = getFormattedChestLocation(doubleChestLocationBean.getRightSideLocation());
            String formattedLeftChestLocation = getFormattedChestLocation(doubleChestLocationBean.getLeftSideLocation());
            if (configuration.get(formattedRightChestLocation) != null) {
                configuration.set(formattedRightChestLocation + "." + "locked", true);
            }
            if (configuration.get(formattedLeftChestLocation) != null) {
                configuration.set(formattedLeftChestLocation + "." + "locked", true);
            }
        } else {
            String formattedChestLocation = getFormattedChestLocation(chest.getLocation());
            configuration.set(formattedChestLocation + "." + "locked", true);
        }
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unlockChest(Block chest) {
        if (isDoubleChest(chest)) {
            DoubleChestLocationBean doubleChestLocationBean = new DoubleChestLocationBean(chest);
            String formattedRightChestLocation = getFormattedChestLocation(doubleChestLocationBean.getRightSideLocation());
            String formattedLeftChestLocation = getFormattedChestLocation(doubleChestLocationBean.getLeftSideLocation());
            if (configuration.get(formattedRightChestLocation) != null) {
                configuration.set(formattedRightChestLocation + "." + "locked", false);
            }
            if (configuration.get(formattedLeftChestLocation) != null) {
                configuration.set(formattedLeftChestLocation + "." + "locked", false);
            }
        } else {
            String formattedChestLocation = getFormattedChestLocation(chest.getLocation());
            configuration.set(formattedChestLocation + "." + "locked", false);
        }
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeChestData(Block chest) {
        if (!hasChestData(chest)) {
            return;
        }
        if (isDoubleChest(chest)) {
            DoubleChestLocationBean doubleChestLocationBean = new DoubleChestLocationBean(chest);
            String formattedRightChestLocation = getFormattedChestLocation(doubleChestLocationBean.getRightSideLocation());
            String formattedLeftChestLocation = getFormattedChestLocation(doubleChestLocationBean.getLeftSideLocation());
            configuration.set(formattedRightChestLocation, null);
            configuration.set(formattedLeftChestLocation, null);
        } else {
            String formattedChestLocation = getFormattedChestLocation(chest.getLocation());
            configuration.set(formattedChestLocation, null);
        }
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void cleanMode(Player player) {
        getInLockMode().remove(player.getName());
        getInAddMode().remove(player.getName());
        getInRemoveMode().remove(player.getName());
        getInLockMode().remove(player.getName());
    }

    public static void cleanMode(String player) {
        getInLockMode().remove(player);
        getInAddMode().remove(player);
        getInRemoveMode().remove(player);
        getInLockMode().remove(player);
    }

    public static void transformOldData() {
        List<String> list = configuration.getStringList("locked");
        if (!list.isEmpty()) {
            for (String data : list) {
                List<String> owners = new ArrayList<>();

                String[] strings = data.split("/");
                owners.add(strings[0]);

                int X = Double.valueOf(strings[2]).intValue();
                int Y = Double.valueOf(strings[3]).intValue();
                int Z = Double.valueOf(strings[4]).intValue();

                Location location = new Location(
                        Bukkit.getWorld(strings[1]),
                        Double.parseDouble(strings[2]),
                        Double.parseDouble(strings[3]),
                        Double.parseDouble(strings[4])
                );

                String key = strings[1] + "/" + X + "/" + Y + "/" + Z;

                configuration.set(key + "." + "location", location);
                configuration.set(key + "." + "owners", owners);
                configuration.set(key + "." + "locked", true);
            }
        }
        configuration.set("locked", null);

        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean isChestLocked(Block chest) {
        String formattedChestLocation = getFormattedChestLocation(chest.getLocation());
        return configuration.getBoolean(formattedChestLocation + "." + "locked");
    }

    public static Boolean isChestLocked(Location location) {
        String formattedChestLocation = getFormattedChestLocation(location);
        return configuration.getBoolean(formattedChestLocation + "." + "locked");
    }

    public static List<String> getChestOwner(Block chest) {
        String formattedChestLocation = getFormattedChestLocation(chest.getLocation());
        return configuration.getStringList(formattedChestLocation + "." + "owners");
    }

    public static Boolean hasChestData(Block chest) {
        String formattedChestLocation = getFormattedChestLocation(chest.getLocation());
        return configuration.contains(formattedChestLocation);
    }

    public static Boolean isChestOwner(Block chest, Player player) {
        String formattedChestLocation = getFormattedChestLocation(chest.getLocation());
        return configuration.getStringList(formattedChestLocation + "." + "owners").contains(player.getName());
    }

    public static Boolean isDoubleChest(Block block) {
        BlockState chestState = block.getState();
        if (chestState instanceof Chest) {
            Chest chest = (Chest) chestState;
            Inventory inventory = chest.getInventory();
            return (inventory instanceof DoubleChestInventory);
        }
        return false;
    }
}

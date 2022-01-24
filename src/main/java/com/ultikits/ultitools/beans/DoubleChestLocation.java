package com.ultikits.ultitools.beans;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class DoubleChestLocation {
    private Location rightSideLocation;
    private Location leftSideLocation;

    public DoubleChestLocation(Block chest) {
        Chest state = (Chest) chest.getState();
        Inventory inventory = state.getInventory();
        DoubleChest doubleChest = (DoubleChest) inventory.getHolder();
        assert doubleChest != null;
        InventoryHolder rightSide = doubleChest.getRightSide();
        InventoryHolder leftSide = doubleChest.getLeftSide();
        if (rightSide == null || leftSide == null) {
            return;
        }
        Location doubleChestLocation = doubleChest.getLocation();
        rightSideLocation = new Location(doubleChestLocation.getWorld(), doubleChestLocation.getX(), doubleChestLocation.getY(), doubleChestLocation.getZ() - 0.5);
        leftSideLocation = new Location(doubleChestLocation.getWorld(), doubleChestLocation.getX(), doubleChestLocation.getY(), doubleChestLocation.getZ() + 0.5);
    }

    public Location getRightSideLocation() {
        return rightSideLocation;
    }

    public Location getLeftSideLocation() {
        return leftSideLocation;
    }
}

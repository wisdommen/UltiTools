package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RandomTpCommands extends AbstractPlayerCommandExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                World world = player.getWorld();
                while (true) {
                    player.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("random_tp_searching"));
                    int randomX = new Random().nextInt(1600) - 800;
                    int randomZ = new Random().nextInt(1600) - 800;
                    Block block = world.getHighestBlockAt(randomX, randomZ);
                    Location location = new Location(world, block.getLocation().getX(), block.getLocation().getY() + 1, block.getLocation().getZ());
                    Block blockY1 = world.getBlockAt(location);
                    if (block.getRelative(BlockFace.DOWN).getType() == Material.AIR ||
                            block.getRelative(BlockFace.DOWN).getType() == Material.LAVA ||
                            block.getRelative(BlockFace.DOWN).getType() == Material.WATER ||
                            blockY1.getRelative(BlockFace.DOWN).getType() == Material.LAVA ||
                            blockY1.getRelative(BlockFace.DOWN).getType() == Material.WATER) {
                        continue;
                    }
                    world.getChunkAt(location).load();
                    player.teleport(location);
                    player.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("random_tp_success"));
                    return;
                }
            }
        }.runTask(UltiTools.getInstance());
        return true;
    }
}


package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.annotations.CmdExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@CmdExecutor(function = "random-tp", permission = "ultikits.tools.command.wild", description = "random_tp_function", alias = "wild")
public class RandomTpCommands extends AbstractPlayerCommandExecutor {

    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {

        if(!player.isOp() || !player.hasPermission("ultikits.tools.command.wild")) {
            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("no_permission")));
            return false;
        }
        World.Environment environment = player.getWorld().getEnvironment();
        if (environment == World.Environment.NETHER || environment == World.Environment.THE_END) {
            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("random_tp_banned")));
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    World world = player.getWorld();
                    while (true) {
                        player.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("random_tp_searching"));
                        int randomX = new Random().nextInt(1600) - 800;
                        int randomZ = new Random().nextInt(1600) - 800;
                        Block block = world.getHighestBlockAt(randomX, randomZ);   //得到随机点最高不可通过方块
                        Location location = new Location(world, block.getLocation().getX(), block.getLocation().getY() + 1, block.getLocation().getZ());  //方块上方，即传送点
                        Location location2 = new Location(world, block.getLocation().getX(), block.getLocation().getY() + 2, block.getLocation().getZ());
                        Block blockY1 = world.getBlockAt(location);    //方块上方，为传送点
                        Block blockY2 = world.getBlockAt(location2);   //方块上方第二个，为了检测Y1（传送点）
                        if (blockY2.getRelative(BlockFace.DOWN).getType() == Material.WATER ||
                                blockY2.getRelative(BlockFace.DOWN).getType() == Material.LAVA ||
                                blockY2.getRelative(BlockFace.DOWN).getType() == Material.WATER) {
                            continue;
                        }
                        world.getChunkAt(location).load();
                        player.teleport(location);
                        player.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("random_tp_success"));
                        return;
                    }
                }
            }.runTask(UltiTools.getInstance());
        }

        return true;
    }
}


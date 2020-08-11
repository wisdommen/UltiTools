package com.minecraft.ultikits.chestLock;

import com.minecraft.ultikits.config.Configs;
import com.minecraft.ultikits.config.ConfigsEnum;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author wisdomme
 */
public class ChestLock implements Listener {

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
                    player.sendMessage(ChatColor.RED + "文件保存失败，上锁失败！请再次点击。");
                    return;
                }
                if (chests.contains(local)) {
                    player.sendMessage(ChatColor.RED + "这个箱子已经上锁了！");
                    return;
                } else {
                    for (String each : chests) {
                        if (each.contains("/" + world + "/" + x + "/" + y + "/" + z)) {
                            player.sendMessage(ChatColor.RED + "这是别人的箱子，你无法上锁！");
                            return;
                        }
                    }
                }
                chests.add(local);
                chestData.set("locked", chests);
                try {
                    chestData.save(chestFile);
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + "文件保存失败，上锁失败！重新输入/lock指令。");
                    return;
                }
                player.sendMessage(ChatColor.GREEN + "上锁成功！");
                player.sendMessage(ChatColor.RED + "如果是大箱子，请将另一半也锁上");
                player.sendMessage(ChatColor.RED + "或者你可以让另一个人锁，即为共享箱子");
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 10, 1);
                return;
            } else if (playerData.getBoolean("unlock")) {
                event.setCancelled(true);
                playerData.set("unlock", false);
                try {
                    playerData.save(playerFile);
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + "文件保存失败，解锁失败！请再次点击。");
                    return;
                }
                if (chests.contains(local)) {
                    chests.remove(local);
                    chestData.set("locked", chests);
                    try {
                        chestData.save(chestFile);
                    } catch (IOException e) {
                        player.sendMessage(ChatColor.RED + "文件保存失败，解锁失败！重新输入/lock指令。");
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + "解锁成功！");
                    player.sendMessage(ChatColor.RED + "如果是大箱子，请将另一半也解锁");
                    player.sendMessage(ChatColor.RED + "如果是共享箱子，请也告知你的同伴");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 10, 1);
                    return;
                } else {
                    for (String each : chests) {
                        if (each.contains("/" + world + "/" + x + "/" + y + "/" + z)) {
                            player.sendMessage(ChatColor.RED + "这是别人的箱子，你无法解锁！");
                            return;
                        }
                    }
                }
                player.sendMessage(ChatColor.RED + "这个箱子没有上锁哦！");
                return;
            }
            if (!chests.contains(local)) {
                for (String each : chests) {
                    if (each.contains("/" + world + "/" + x + "/" + y + "/" + z)) {
                        if ((UltiTools.getInstance().getConfig().getBoolean("op_unlock") && player.isOp() && event.getAction() == Action.RIGHT_CLICK_BLOCK) || (UltiTools.getInstance().getConfig().getBoolean("op_break_locked") && player.isOp() && event.getAction() == Action.LEFT_CLICK_BLOCK)) {
                            player.sendMessage(ChatColor.GREEN + "虽然你是OP，但是请不要为所欲为哦");
                            return;
                        }
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "这是别人的箱子，你无权操作哦");
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
                player.sendMessage(ChatColor.RED + "你删除了一个上锁的箱子！");
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

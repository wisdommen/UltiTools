package com.minecraft.ultikits.chestLock;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ChestLock implements Listener {

    @EventHandler
    public void onPlayerOpenChest(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST) {
            Player player = event.getPlayer();
            Location chest_location = event.getClickedBlock().getLocation();
            File chest_file = new File(UltiTools.getInstance().getDataFolder(), "chestData.yml");
            File player_file = new File(UltiTools.getInstance().getDataFolder() + "/playerData", player.getName() + ".yml");
            YamlConfiguration chest_data = YamlConfiguration.loadConfiguration(chest_file);
            YamlConfiguration player_data = YamlConfiguration.loadConfiguration(player_file);
            List<String> chests = chest_data.getStringList("locked");

            String world = Objects.requireNonNull(chest_location.getWorld()).getName();
            double x = chest_location.getX();
            double y = chest_location.getY();
            double z = chest_location.getZ();
            String local = player.getName() + "/" + world + "/" + x + "/" + y + "/" + z;

            if (player_data.getBoolean("lock")) {
                event.setCancelled(true);
                player_data.set("lock", false);
                try {
                    player_data.save(player_file);
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
                chest_data.set("locked", chests);
                try {
                    chest_data.save(chest_file);
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + "文件保存失败，上锁失败！重新输入/lock指令。");
                    return;
                }
                player.sendMessage(ChatColor.GREEN + "上锁成功！");
                player.sendMessage(ChatColor.RED + "如果是大箱子，请将另一半也锁上");
                player.sendMessage(ChatColor.RED + "或者你可以让另一个人锁，即为共享箱子");
                return;
            } else if (player_data.getBoolean("unlock")) {
                event.setCancelled(true);
                player_data.set("unlock", false);
                try {
                    player_data.save(player_file);
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + "文件保存失败，解锁失败！请再次点击。");
                    return;
                }
                if (chests.contains(local)) {
                    chests.remove(local);
                    chest_data.set("locked", chests);
                    try {
                        chest_data.save(chest_file);
                    } catch (IOException e) {
                        player.sendMessage(ChatColor.RED + "文件保存失败，解锁失败！重新输入/lock指令。");
                        return;
                    }
                    player.sendMessage(ChatColor.GREEN + "解锁成功！");
                    player.sendMessage(ChatColor.RED + "如果是大箱子，请将另一半也解锁");
                    player.sendMessage(ChatColor.RED + "如果是共享箱子，请也告知你的同伴");
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
    public void onPlayerDestroyChest(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.CHEST) {
            Location chest_location = event.getBlock().getLocation();
            Player player = event.getPlayer();
            File chest_file = new File(UltiTools.getInstance().getDataFolder(), "chestData.yml");
            YamlConfiguration chest_data = YamlConfiguration.loadConfiguration(chest_file);
            List<String> chests = chest_data.getStringList("locked");
            int size_before = chests.size();

            String world = Objects.requireNonNull(chest_location.getWorld()).getName();
            double x = chest_location.getX();
            double y = chest_location.getY();
            double z = chest_location.getZ();
            String local = player.getName() + "/" + world + "/" + x + "/" + y + "/" + z;

            if (player.isOp()) {
                chests.removeIf(each -> each.contains("/" + world + "/" + x + "/" + y + "/" + z));
            } else {
                chests.remove(local);
            }
            if (size_before>chests.size()) {
                chest_data.set("locked", chests);
                try {
                    chest_data.save(chest_file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.sendMessage(ChatColor.RED + "你删除了一个上锁的箱子！");
            }
        }
    }
}

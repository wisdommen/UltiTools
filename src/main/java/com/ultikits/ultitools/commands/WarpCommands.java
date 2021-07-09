package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractPlayerCommandExecutor;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.ultitools.views.WarpsView;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class WarpCommands extends AbstractPlayerCommandExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        switch (command.getName()) {
            case "warp":
                if (strings.length != 1) {
                    return false;
                }
                if (!(player.hasPermission("ultitools.warp.use") || player.hasPermission("ultikits.tools.admin"))) {
                    player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("no_permission")));
                    return true;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        File file = new File(UltiTools.getInstance().getDataFolder() + "/warps", strings[0] + ".yml");
                        if (!file.exists()) {
                            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("warp_not_exists")));
                            return;
                        }
                        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                        String world = config.getString("world");
                        double x = config.getDouble("x");
                        double y = config.getDouble("y");
                        double z = config.getDouble("z");
                        float yaw = (float) config.getDouble("yaw");
                        float pitch = (float) config.getDouble("pitch");
                        String name = config.getString("name");
                        Location location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                        player.teleport(location);
                        player.sendMessage(MessagesUtils.info(String.format(UltiTools.languageUtils.getString("warp_teleport_successfully"), name)));
                    }
                }.runTask(UltiTools.getInstance());
                return true;
            case "warps":
                if (!(player.hasPermission("ultitools.warp.use") || player.hasPermission("ultikits.tools.admin"))) {
                    player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("no_permission")));
                    return true;
                }
                Inventory inventory = WarpsView.setUp();
                player.openInventory(inventory);
                return true;
            case "setwarp":
                if (!(player.hasPermission("ultitools.warp.admin") || player.hasPermission("ultikits.tools.admin"))) {
                    player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("no_permission")));
                    return true;
                }
                if (strings.length != 1) {
                    player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("warp_name_shall_not_empty")));
                    return false;
                }
                File newFile = new File(UltiTools.getInstance().getDataFolder() + "/warps", strings[0] + ".yml");
                if (newFile.exists()) {
                    player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("warp_already_exists")));
                    return true;
                }
                YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(newFile);
                Location location1 = player.getLocation();
                String newWorld = location1.getWorld().getName();
                double newX = location1.getX();
                double newY = location1.getY();
                double newZ = location1.getZ();
                float newYaw = location1.getYaw();
                float newPitch = location1.getPitch();
                String newName = strings[0];
                newConfig.set("world", newWorld);
                newConfig.set("x", newX);
                newConfig.set("y", newY);
                newConfig.set("z", newZ);
                newConfig.set("yaw", newYaw);
                newConfig.set("pitch", newPitch);
                newConfig.set("name", newName);
                newConfig.set("lastowner", player.getUniqueId().toString());
                try {
                    newConfig.save(newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.sendMessage(MessagesUtils.info(String.format(UltiTools.languageUtils.getString("warp_set_successfully"), newName)));
                return true;
            case "delwarp":
                if (!(player.hasPermission("ultitools.warp.admin") || player.hasPermission("ultikits.tools.admin"))) {
                    player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("no_permission")));
                    return true;
                }
                if (strings.length != 1) {
                    player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("warp_name_shall_not_empty")));
                    return false;
                }
                File delFile = new File(UltiTools.getInstance().getDataFolder() + "/warps", strings[0] + ".yml");
                if (!delFile.exists()) {
                    player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("warp_not_exists")));
                    return true;
                }
                if (delFile.delete()) {
                    player.sendMessage(MessagesUtils.info(String.format(UltiTools.languageUtils.getString("warp_deleted"), strings[0])));
                }
        }
        return false;
    }

}

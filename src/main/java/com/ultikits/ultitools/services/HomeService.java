package com.ultikits.ultitools.services;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeService {

    private static final File homeFile = new File(ConfigsEnum.HOME.toString());
    private static final YamlConfiguration homeConfig = YamlConfiguration.loadConfiguration(homeFile);

    public static void setHome(Player player, String homeName) {
        setHome(player, homeName, null);
    }

    public static void setHome(Player player, String homeName, Location homeLocation) {
        if (getHomeList(player).contains(homeName) && homeLocation == null) {
            player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("sethome_home_already_have")));
            return;
        }
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        List<String> stringList = config.getStringList(player.getName() + ".homelist");
        config.set(player.getName() + "." + homeName, homeLocation == null ? player.getLocation() : homeLocation);
        if (homeName.equals("Def")) {
            homeName = UltiTools.languageUtils.getString("default");
        }
        if (!stringList.contains(homeName)) stringList.add(homeName);
        config.set(player.getName() + ".homelist", stringList);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(ChatColor.YELLOW + UltiTools.languageUtils.getString("sethome_successfully"));
    }

    public static void goHome(Player player, String[] args) {
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        if (file.exists()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    Location location;
                    try {
                        String path = player.getName() + ".Def";
                        if (args.length == 1 && !args[0].equals(UltiTools.languageUtils.getString("default"))) {
                            path = player.getName() + "." + args[0];
                        } else if (args.length > 1){
                            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("home_usage"));
                            return;
                        }
                        location = getLocation(config, path);
                        DelayTeleportService.delayTeleport(player, location, homeConfig.getInt("home_tpwait"));
                    } catch (NullPointerException e) {
                        player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("home_dont_have"));
                    }
                }
            }.runTaskAsynchronously(UltiTools.getInstance());
        } else {
            player.sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("home_have_no_home"));
        }
    }

    public static List<String> getHomeList(Player player) {
        List<String> homeList = new ArrayList<>();
        File file = new File(ConfigsEnum.PLAYER.toString(), player.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (file.exists() && config.get(player.getName() + ".homelist") != null) {
            if (config.get(player.getName() + ".homelist") instanceof String) {
                String homelist = config.getString(player.getName() + ".homelist");
                if (homelist.contains(" ")) {
                    String[] list = homelist.split(" ");
                    for (String each : list) {
                        if (each.contains(" ")) {
                            each.replaceAll(" ", "");
                        }
                        homeList.add(each);
                    }
                    homeList.removeIf(each -> each.equals(""));
                    config.set(player.getName() + ".homelist", homeList);
                    try {
                        config.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                homeList = config.getStringList(player.getName() + ".homelist");
                homeList.removeIf(each -> each.equals(""));
                config.set(player.getName() + ".homelist", homeList);
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return homeList;
            }
        }
        return homeList;
    }

    public static Location getLocation(YamlConfiguration config, String path) {
        try {
            return (Location) config.get(path);
        }catch (Exception e){
            World world = Bukkit.getServer().getWorld(Objects.requireNonNull(config.getString(path + ".world")));
            double x = config.getDouble(path + ".x");
            double y = config.getDouble(path + ".y");
            double z = config.getDouble(path + ".z");
            if (config.get(path + ".yam") != null && config.get(path + ".pitch") != null) {
                float yam = (float) config.getDouble(path + ".yam");
                float pitch = (float) config.getDouble(path + ".pitch");
                return new Location(world, x, y, z, yam, pitch);
            }
            return new Location(world, x, y, z);
        }
    }
}

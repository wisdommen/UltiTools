package com.ultikits.ultitools.utils;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HomeUtils {

    public static void setHome(Player player, String homeName) {
        setHome(player, homeName, null);
    }

    public static void setHome(Player player, String homeName, Location homeLocation) {
        if (Utils.getHomeList(player).contains(homeName) && homeLocation == null) {
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
}

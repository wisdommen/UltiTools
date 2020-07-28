package com.minecraft.ultikits.scoreBoard;

import com.minecraft.ultikits.prefix.Titles;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static com.minecraft.ultikits.scoreBoard.SideBar.tool_config;
import static com.minecraft.ultikits.ultitools.UltiTools.isPAPILoaded;

public class NamePrefixSuffix extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String prefix;
            String suffix;
            if (isPAPILoaded && tool_config.getBoolean("enable_PAPI") ){
                try {
                    prefix = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(player, tool_config.getString("name_prefix")));
                    suffix = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(player, tool_config.getString("name_suffix")));
                }catch (Exception e){
                    prefix = "";
                    suffix = "";
                }
            } else {
                prefix = "";
                suffix = "";
            }
            Titles.setPrefixSuffix(player, prefix, suffix);
        }
    }
}

package com.ultikits.ultitools.tasks;

import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.TitlesUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class NamePrefixSuffixTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String prefix;
            String suffix;
            if (UltiTools.isPAPILoaded && UltiTools.getInstance().getConfig().getBoolean("enable_PAPI") ){
                try {
                    prefix = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(player, UltiTools.getInstance().getConfig().getString("name_prefix")));
                    suffix = Objects.requireNonNull(PlaceholderAPI.setPlaceholders(player, UltiTools.getInstance().getConfig().getString("name_suffix")));
                }catch (Exception e){
                    prefix = "";
                    suffix = "";
                }
            } else {
                prefix = "";
                suffix = "";
            }
            TitlesUtils.setPrefixSuffix(player, prefix, suffix);
        }
    }
}

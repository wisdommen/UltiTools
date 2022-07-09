package com.ultikits.ultitools.utils;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Utils {

    private Utils(){}

    @Contract(" -> new")
    public static @NotNull File getConfigFile(){
        return new File(UltiTools.getInstance().getDataFolder(), "config.yml");
    }

    public static @NotNull YamlConfiguration getConfig(File file){
        return YamlConfiguration.loadConfiguration(file);
    }

    public static @NotNull Integer checkOnlineTime(@NotNull Player player){
        return getConfig(new File(UltiTools.getInstance().getDataFolder()+"/playerData", player.getName()+".yml")).getInt("online_time");
    }

    public static String convertMillisecondsToRegularTime(Long milliseconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        if (UltiTools.language.equals("en")) {
            return  day + UltiTools.languageUtils.getString("day") + (month + 1) + UltiTools.languageUtils.getString("month") + " "
                    + hour + ":" + minute + ":" + second;
        }else {
            return (month + 1) + UltiTools.languageUtils.getString("month") + day + UltiTools.languageUtils.getString("day") + " "
                    + hour + ":" + minute + ":" + second;
        }
    }

    public static @NotNull Integer getRandomNumber(int range){
        final long l = System.currentTimeMillis();
        final int i = (int) (l % 100);
        Random random = new Random(i);
        return random.nextInt(range);
    }

    public static @Nullable List<File> getFiles(String path) {
        File folder = new File(path);
        if (folder.listFiles() != null) {
            return Arrays.asList(Objects.requireNonNull(folder.listFiles()));
        }
        return null;
    }

    public static @NotNull FileConfiguration getToolsConfig(){
        return UltiTools.getInstance().getConfig();
    }
}

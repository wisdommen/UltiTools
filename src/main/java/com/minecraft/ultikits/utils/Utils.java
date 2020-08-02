package com.minecraft.ultikits.utils;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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

    public static String convertMinutesToRegularTime(Integer minutes){
        if (minutes>60) {
            int hours = minutes / 60;
            int minute = minutes % 60;
            return String.format("%02d小时 %02d分钟", hours, minute);
        }else {
            return String.format("0小时 %02d分钟", minutes);
        }
    }

    public static @NotNull Integer getRandomNumber(int range){
        final long l = System.currentTimeMillis();
        final int i = (int) (l % 100);
        Random random = new Random(i);
        return random.nextInt(range);
    }

    public static @Nullable List<File> getFile(String path) {
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

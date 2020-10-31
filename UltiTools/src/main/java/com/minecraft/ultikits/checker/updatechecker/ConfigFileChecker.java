package com.minecraft.ultikits.checker.updatechecker;

import com.minecraft.ultikits.config.ConfigController;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.minecraft.ultikits.checker.updatechecker.VersionChecker.*;
import static com.minecraft.ultikits.utils.Utils.getFiles;

public class ConfigFileChecker {

    private ConfigFileChecker() {
    }

    private static Map<String, Object> getAllConfigs() {
        Map<String, Object> config = new HashMap<>();

        for (String key : UltiTools.getInstance().getConfig().getKeys(false)) {
            Object value = UltiTools.getInstance().getConfig().get(key);
            config.put(key, value);
        }

        return config;
    }

    private static Map<String, Object> getAllConfigs(YamlConfiguration yamlConfig) {
        Map<String, Object> config = new HashMap<>();

        for (String key : yamlConfig.getKeys(false)) {
            Object value = yamlConfig.get(key);
            config.put(key, value);
        }

        return config;
    }

    public static void reviewMainConfigFile() {
        Map<String, Object> config = getAllConfigs();
        File file = new File(UltiTools.getInstance().getDataFolder(), "config.yml");
        transferOldConfigs(config);
        if (file.delete()) {
            UltiTools.yaml.saveYamlFile(UltiTools.getInstance().getDataFolder().getPath(), "config.yml", UltiTools.language + "_config.yml");
            File newFile = new File(UltiTools.getInstance().getDataFolder(), "config.yml");
            YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(file);
            for (String key : config.keySet()) {
                if (newConfig.getKeys(true).contains(key)) {
                    newConfig.set(key, config.get(key));
                }
            }
            try {
                newConfig.save(newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        boolean change = false;
//        Configuration defaults = YamlConfiguration.loadConfiguration(new File());
//        for (String defaultKey : defaults.getKeys(true)) {
//            if (!UltiTools.getInstance().getConfig().contains(defaultKey)) {
//                UltiTools.getInstance().getConfig().set(defaultKey, defaults.get(defaultKey));
//                change = true;
//            }
//        }
//        if (change) UltiTools.getInstance().saveConfig();
    }

    public static void transferOldConfigs(Map<String, Object> oldConfig) {
        if (oldConfig.containsKey("config_version")) {
            return;
        }
        for (String key : oldConfig.keySet()) {
            try {
                ConfigController.setValue(key, oldConfig.get(key));
            } catch (NullPointerException ignore) {
            }
        }
        ConfigController.saveConfigs();
    }

    public static void reviewConfigFile(YamlConfiguration configuration, String filePath) {
        File file = new File(filePath);

    }

    public static void downloadNewVersion() {
        new BukkitRunnable() {

            @Override
            public void run() {
                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + UltiTools.languageUtils.getWords("downloading"));
                if (download()) {
                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + UltiTools.languageUtils.getWords("download_successfully"));
                    this.cancel();
                    return;
                }
                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + String.format("[UltiTools] " + UltiTools.languageUtils.getWords("download_failed"), "https://www.mcbbs.net/thread-1062730-1-1.html"));
            }

        }.runTaskAsynchronously(UltiTools.getInstance());
    }

    public static boolean download() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/wisdommen/wisdommen.github.io/master/collections/Ultitools/UltiTools-" + version + ".jar");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(UltiTools.getInstance().getDataFolder().getPath().replace(java.io.File.separator + "UltiTools", "") + java.io.File.separator + "UltiTools-" + version + ".jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void deleteOldVersion() {
        List<File> files = getFiles(UltiTools.getInstance().getDataFolder().getPath().replace(java.io.File.separator + "UltiTools", ""));
        for (File file : files) {
            if (file.getName().contains("UltiTools-") && !file.getName().equals("UltiTools-" + version + ".jar")) {
                file.delete();
            }
        }
    }
}

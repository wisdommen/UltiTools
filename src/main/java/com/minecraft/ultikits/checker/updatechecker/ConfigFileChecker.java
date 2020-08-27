package com.minecraft.ultikits.checker.updatechecker;

import com.minecraft.ultikits.config.ConfigController;
import com.minecraft.ultikits.enums.ConfigsEnum;
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

import static com.minecraft.ultikits.checker.updatechecker.VersionChecker.isOutDate;
import static com.minecraft.ultikits.checker.updatechecker.VersionChecker.version;
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

        if (file.delete()) {
            UltiTools.getInstance().saveDefaultConfig();
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            for (String key : configuration.getKeys(false)) {
                if (config.containsKey(key)) {
                    configuration.set(key, config.get(key));
                }
            }
            try {
                configuration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void reviewConfigFile(YamlConfiguration configuration, String filePath) {
        File file = new File(filePath);

    }

    public static void downloadNewVersion() {
        new BukkitRunnable() {

            @Override
            public void run() {
                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] 正在下载更新...");
                if (download()){
                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] 下载完成, 重载/reload插件以应用更新！");
                    this.cancel();
                }
                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + String.format("[UltiTools] 下载失败，请前往 %s 手动下载！", "https://www.mcbbs.net/thread-1062730-1-1.html"));
            }


        }.runTaskAsynchronously(UltiTools.getInstance());
    }

        public static boolean download (){
            try {
                URL url = new URL("https://raw.githubusercontent.com/wisdommen/wisdommen.github.io/master/collections/Ultitools/UltiTools-" + version + ".jar");
                ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                FileOutputStream fos = new FileOutputStream(UltiTools.getInstance().getDataFolder().getPath().replace("\\UltiTools", "") + "\\UltiTools-" + version + ".jar");
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        public static void deleteOldVersion () {
            List<File> files = getFiles(UltiTools.getInstance().getDataFolder().getPath().replace("\\UltiTools", ""));
            for (File file : files) {
                if (file.getName().contains("UltiTools-") && !file.getName().equals("UltiTools-" + version + ".jar")) {
                    file.delete();
                }
            }
        }
    }

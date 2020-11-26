package com.ultikits.ultitools.checker.updatechecker;

import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.ultikits.ultitools.checker.updatechecker.ConfigFileChecker.deleteOldVersion;
import static com.ultikits.ultitools.checker.updatechecker.ConfigFileChecker.downloadNewVersion;

public class VersionChecker {

    public static boolean isOutDate = false;
    public static String version;
    public static String current_version;


    public static void runTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    //连接
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://wisdommen.github.io").openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    //获取输入流
                    InputStream input = connection.getInputStream();
                    //将字节输入流转换为字符输入流
                    InputStreamReader streamReader = new InputStreamReader(input, StandardCharsets.UTF_8);
                    //为字符输入流添加缓冲
                    BufferedReader br = new BufferedReader(streamReader);
                    // 读取返回结果
                    String data = br.readLine();
                    while (data != null) {
                        //获取带有附件id的文本
                        if (data.contains("UltiTools")) {
                            String target = br.readLine();
                            //获取版本
                            version = target.split("version: ")[1].split("<")[0];
                            current_version = UltiTools.getInstance().getDescription().getVersion();
                            int currentVersion = getVersion(current_version);
                            int onlineVersion = getVersion(version);
                            UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] "+UltiTools.languageUtils.getWords("checking_update"));
                            if (currentVersion < onlineVersion) {
                                isOutDate = true;
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getWords("join_send_update_reminding"), version, current_version));
                                if (UltiTools.getInstance().getConfig().getBoolean("enable_auto_update")) {
                                    downloadNewVersion();
                                } else {
                                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("download_url"));
                                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + UltiTools.languageUtils.getWords("join_send_update_tip"));
                                }
                            }
                            if (!isOutDate) {
                                if(UltiTools.getInstance().getConfig().getBoolean("enable_auto_update")) {
                                    deleteOldVersion();
                                }
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + UltiTools.languageUtils.getWords("plugin_up_to_date"));
                                break;
                            }
                            break;
                        }
                        data = br.readLine();
                    }
                    // 释放资源
                    br.close();
                    streamReader.close();
                    input.close();
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(UltiTools.getInstance());
    }

    private static int getVersion(String version) {
        while (version.contains(".")) {
            version = version.replace(".", "");
        }
        return Integer.parseInt(version);
    }
}

package com.minecraft.ultikits.checker.updatechecker;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.minecraft.ultikits.checker.updatechecker.ConfigFileChecker.deleteOldVersion;
import static com.minecraft.ultikits.checker.updatechecker.ConfigFileChecker.downloadNewVersion;

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
                            UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] 正在检查更新...");
                            if (currentVersion < onlineVersion) {
                                isOutDate = true;
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + String.format("[UltiTools] 工具插件最新版为%s，你的版本是%s！请下载最新版本！", version, current_version));
                                if (UltiTools.getInstance().getConfig().getBoolean("enable_auto_update")) {
//                                    if (currentVersion<321 && onlineVersion >=321){
//                                        UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] 自动更新失败！");
//                                        UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] 新版插件与旧版插件有重大更新，请前往帖子查看注意事项后更新！");
//                                        UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] 下载地址：https://www.mcbbs.net/thread-1062730-1-1.html");
//                                        return;
//                                    }
                                    downloadNewVersion();
                                } else {
                                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] 下载地址：https://www.mcbbs.net/thread-1062730-1-1.html");
                                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] 你知道吗？现在UltiTools可以自动更新啦！在配置文件中打开自动更新，更新再也不用麻烦！");
                                }
                            }
                            if (!isOutDate) {
                                deleteOldVersion();
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools]太棒了！你的插件是最新的！保持最新的版本可以为你带来最好的体验！");
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

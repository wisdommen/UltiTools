package com.minecraft.ultikits.UpdateChecker;

import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class VersionChecker {


    public static void setupThread() {
        Thread checkVersionThread = new Thread() {
            public void run() {
                try {
                    //连接
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://wisdommen.github.io").openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    //伪装
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
                            boolean isOutDate = false;
                            String target = br.readLine();
                            //获取版本
                            String version = target.split("version: ")[1].split("<")[0];
                            String current_version = UltiTools.getInstance().getDescription().getVersion();
                            List<String> current_version_list = Arrays.asList(current_version.split("\\."));
                            List<String> online_version_list = Arrays.asList(version.split("\\."));
                            UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiTools] 正在检查更新...");
                            for (int i = 0; i < 3; i++) {
                                String a = current_version_list.get(i);
                                String b = online_version_list.get(i);
                                if (Integer.parseInt(a) < Integer.parseInt(b)) {
                                    if (i <= 1) {
                                        UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] 工具插件有 重要 更新，请下载最新版本！");
                                    } else {
                                        UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "[UltiTools] 工具插件有更新，请下载最新版本！");
                                    }
                                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiTools] 下载地址：https://www.mcbbs.net/thread-1060351-1-1.html");
                                    isOutDate = true;
                                    break;
                                }
                            }
                            if (!isOutDate) {
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiTools]太棒了！你的插件是最新的！保持最新的版本可以为你带来最好的体验！");
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
                    this.interrupt();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    this.interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                    this.interrupt();
                }
            }
        };
        checkVersionThread.start();
    }
}

package com.ultikits.ultitools.checker;

import com.ultikits.ultitools.ultitools.ExceptionCatcher;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.ultikits.ultitools.utils.Utils.getFiles;

public class VersionChecker {

    public static boolean isOutDate = false;
    public static String version;
    public static String current_version;


    public static void runTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    String url = "https://wisdommen.github.io";
                    if (UltiTools.language.equals("zh")) {
                        url = "https://download.ultikits.com/index.markdown";
                    }
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
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
                            UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + UltiTools.languageUtils.getString("checking_update"));
                            if (currentVersion < onlineVersion) {
                                isOutDate = true;
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + String.format(UltiTools.languageUtils.getString("join_send_update_reminding"), version, current_version));
                                if (UltiTools.getInstance().getConfig().getBoolean("enable_auto_update") && !(onlineVersion >= 400 && currentVersion < 400)) {
                                    downloadNewVersion();
                                } else {
                                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + UltiTools.languageUtils.getString("download_url"));
                                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + String.format("[UltiTools] " + UltiTools.languageUtils.getString("join_send_update_tip"), "UltiTools"));
                                }
                            }
                            if (!isOutDate) {
                                if (UltiTools.getInstance().getConfig().getBoolean("enable_auto_update")) {
                                    deleteOldVersion();
                                }
                                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + UltiTools.languageUtils.getString("plugin_up_to_date"));
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
                } catch (Exception e) {
                    ExceptionCatcher.catchException(e);
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

    private static void downloadNewVersion() {
        new BukkitRunnable() {

            @Override
            public void run() {
                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + UltiTools.languageUtils.getString("downloading"));
                if (startDownload()) {
                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + UltiTools.languageUtils.getString("download_successfully"));
                    this.cancel();
                    return;
                }
                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + String.format("[UltiTools] " + UltiTools.languageUtils.getString("download_failed"), "https://www.mcbbs.net/thread-1062730-1-1.html"));
            }

        }.runTaskAsynchronously(UltiTools.getInstance());
    }

    private static boolean startDownload() {
        if (!downloadFromGitHub()) {
            return downloadFromOwnServer();
        }
        return true;
    }

    private static boolean downloadFromOwnServer() {
        try {
            String urlString = "https://download.ultikits.com/collections/Ultitools/UltiTools-" + version + ".jar";
            return download(urlString, "UltiTools-" + version + ".jar");
        } catch (Exception e) {
            ExceptionCatcher.catchException(e);
            return false;
        }
    }

    private static boolean downloadFromGitHub() {
        try {
            String urlString = "https://raw.githubusercontent.com/wisdommen/wisdommen.github.io/master/collections/Ultitools/UltiTools-" + version + ".jar";
            return download(urlString, "UltiTools-" + version + ".jar");
        } catch (Exception e) {
            ExceptionCatcher.catchException(e);
            return false;
        }
    }

    public static boolean download(String urlString, String outputName) throws IOException {
        URL url = new URL(urlString);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(UltiTools.getInstance().getDataFolder().getPath().replace(File.separator + "UltiTools", "") + File.separator + outputName);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        return true;
    }

    private static void deleteOldVersion() {
        List<File> files = getFiles(UltiTools.getInstance().getDataFolder().getPath().replace(File.separator + "UltiTools", ""));
        for (File file : files) {
            if (file.getName().contains("UltiTools-") && !file.getName().equals("UltiTools-" + version + ".jar")) {
                file.delete();
            }
        }
    }
}

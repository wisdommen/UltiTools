package com.ultikits.ultitools.checker;

import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ultikits.ultitools.checker.VersionChecker.*;
import static com.ultikits.ultitools.utils.Utils.getFiles;

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

    public static void downloadNewVersion() {
        new BukkitRunnable() {

            @Override
            public void run() {
                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + UltiTools.languageUtils.getString("downloading"));
                if (download()) {
                    UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[UltiTools] " + UltiTools.languageUtils.getString("download_successfully"));
                    this.cancel();
                    return;
                }
                UltiTools.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + String.format("[UltiTools] " + UltiTools.languageUtils.getString("download_failed"), "https://www.mcbbs.net/thread-1062730-1-1.html"));
            }

        }.runTaskAsynchronously(UltiTools.getInstance());
    }

    public static boolean download() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/wisdommen/wisdommen.github.io/master/collections/Ultitools/UltiTools-" + version + ".jar");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(UltiTools.getInstance().getDataFolder().getPath().replace(File.separator + "UltiTools", "") + File.separator + "UltiTools-" + version + ".jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //网络上的链接路径，下载保存的文件名,保存路径
    public static void download(String urlstring,String fileName,String savePath) throws IOException{
        //和网上资源建立连接
        URL url=new URL(urlstring);
        //获取URLConnection对象，从而获得输入流
        URLConnection conn=url.openConnection();
        //获取和网络上资源连接的输入流
        InputStream is=conn.getInputStream();

        //InputStream is=url.openStream();使用该方法也可以获取输入流

        //用于缓存的字节数组，大小为1kb
        byte[] buff=new byte[1024];
        int len=0;
        //建立File对象
        File file=new File(savePath);
        //检查File所对应对象的路径是否存在，如果不存在就自己建立好路径
        if(!file.exists()){
            file.mkdirs();
        }
        //获取将网上资源写入本地的输出流，路径+待保存的文件名
        OutputStream os=new FileOutputStream(file.getPath()+"/"+fileName);
        while((len=is.read(buff))!=-1){
            os.write(buff, 0, len);
        }
        //释放资源
        os.close();
        is.close();
    }

    public static void deleteOldVersion() {
        List<File> files = getFiles(UltiTools.getInstance().getDataFolder().getPath().replace(File.separator + "UltiTools", ""));
        for (File file : files) {
            if (file.getName().contains("UltiTools-") && !file.getName().equals("UltiTools-" + version + ".jar")) {
                file.delete();
            }
        }
    }
}

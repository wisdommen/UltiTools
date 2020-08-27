package com.minecraft.ultikits.checker.prochecker;

import com.minecraft.ultikits.ultitools.UltiTools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ProChecker {
    private static final String name = UltiTools.getInstance().getConfig().getString("pro_name");
    private static final String key = UltiTools.getInstance().getConfig().getString("pro_key");

    public static int run() throws IOException {
        Socket sock = new Socket(getValidateServer(), 6666);
        int result = 500;
        try (InputStream input = sock.getInputStream()) {
            try (OutputStream output = sock.getOutputStream()) {
                result = handle(input, output);
            }
        }
        sock.close();
        return result;
    }

    private static int handle(InputStream input, OutputStream output) {
        int resp = 500;

        if (name == null || key == null || name.equals("") || key.equals("")) return 400;
        try {
            //连接
            HttpURLConnection connection = (HttpURLConnection) new URL("http://myip.ipip.net/").openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //获取输入流
            String data;
            try {
                InputStream input2 = connection.getInputStream();
                //将字节输入流转换为字符输入流
                InputStreamReader streamReader = new InputStreamReader(input2, StandardCharsets.UTF_8);
                //为字符输入流添加缓冲
                BufferedReader br = new BufferedReader(streamReader);
                // 读取返回结果
                data = br.readLine();
                data = data.split("IP：")[1].split("  来自于")[0];
                br.close();
                streamReader.close();
            } catch (IOException e) {
                data = "0.0.0.0";
            }
            connection.disconnect();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            String preSending = data + "!@" + name + "!@" + key;
            writer.write(preSending);
            writer.newLine();
            writer.flush();
            resp = Integer.parseInt(reader.readLine());
            writer.write("100");
            writer.newLine();
            writer.flush();
        } catch (SocketException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    private static String getValidateServer() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://raw.githubusercontent.com/wisdommen/wisdommen.github.io/master/GemCoach.tcl").openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //获取输入流
            InputStream input2 = connection.getInputStream();
            //将字节输入流转换为字符输入流
            InputStreamReader streamReader = new InputStreamReader(input2, StandardCharsets.UTF_8);
            //为字符输入流添加缓冲
            BufferedReader br = new BufferedReader(streamReader);
            // 读取返回结果
            String data = br.readLine();
            br.close();
            streamReader.close();
            connection.disconnect();
            return data.split("12.")[1].split(".21")[0];
        } catch (Exception e) {
            return "20.188.203.23";
        }
    }
}

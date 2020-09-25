package com.minecraft.ultikits.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.minecraft.ultikits.beans.CheckResponse;
import com.minecraft.ultikits.beans.EmailBean;
import com.minecraft.ultikits.ultitools.UltiTools;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class SendEmailUtils {

    public static CheckResponse sendEmail(String target, String title, String content) {
        CheckResponse response = new CheckResponse();

        try {
            EmailBean emailBean = new EmailBean(target, title, content);
            String serializedEmail = null;
            try {
                serializedEmail = SerializationUtils.serialize(emailBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (serializedEmail == null){
                return response;
            }
            //String url = "http://api.ultikits.com:8080/UltikitsServer/mail.api?data=" + serializedEmail;
            String url = "http://localhost:8080/mail.api?data=" + serializedEmail;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            String data;
            try {
                InputStream input2 = connection.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(input2, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(streamReader);
                data = br.readLine();
                JSONObject userJson = JSONObject.parseObject(data);
                response = JSON.toJavaObject(userJson, CheckResponse.class);

                br.close();
                streamReader.close();
                return response;
            } catch (IOException ignored) {
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}

package com.ultikits.ultitools.checker.prochecker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ultikits.beans.CheckResponse;
import com.ultikits.ultitools.ultitools.UltiTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ProChecker {
    private static final String name = UltiTools.getInstance().getConfig().getString("pro_name");
    private static final String key = UltiTools.getInstance().getConfig().getString("pro_key");

    public static CheckResponse run() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://api.ultikits.com:8080/UltikitsServer/validate.api?name="+name+"&key="+key).openConnection();
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        String data;
        CheckResponse response = new CheckResponse();
        try {
            InputStream input2 = connection.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(input2, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(streamReader);
            data = br.readLine();
            JSONObject userJson = JSONObject.parseObject(data);
            response = JSON.toJavaObject(userJson,CheckResponse.class);

            br.close();
            streamReader.close();
        } catch (IOException ignored) {
        } finally {
            connection.disconnect();
        }
        return response;
    }
}

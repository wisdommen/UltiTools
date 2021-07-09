package com.ultikits.ultitools.checker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

public class NewProChecker {
    private static final String name = UltiTools.getInstance().getConfig().getString("pro_name");
    private static final String password = UltiTools.getInstance().getConfig().getString("pro_password");
    private static String id;

    public static String doPost(String url) {
        return doPost(url, null, null);
    }

    public static String doPost(String url, String token, Map<String, String> body) {
        //创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";
        try {
            //创建http请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json");
            if (token != null) httpPost.addHeader("Authorization", "Bearer " + token);
            //创建请求内容
            if (body != null) {
                String jsonStr = JSON.toJSONString(body);
                StringEntity entity = new StringEntity(jsonStr);
                httpPost.setEntity(entity);
            }
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), "utf-8");
//            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }

    private static String getToken() {
        String res = doPost("https://panel.ultikits.com:4433/user/getToken?username=" + name + "&password=" + password);
        Map<String, Object> map = JSON.parseObject(res, new TypeReference<Map<String, Object>>() {
        });
        if (map.containsKey("access_token")) {
            id = String.valueOf(map.get("id"));
            return (String) map.get("access_token");
        }
        return null;
    }

    public static String validatePro() {
        String token = getToken();
        if (token == null) {
            return "Login Failed!";
        }
        String result = doPost("https://panel.ultikits.com:4433/user/" + id + "?field=pro", token, null);
        Map<String, Object> map = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
        });
        boolean isPro = map.get("msg").equals("true");
        if (isPro){
            String ipAddress = doPost("https://panel.ultikits.com:4433/check/getip");
            String activatedServerString = doPost("https://panel.ultikits.com:4433/user/" + id + "/activatedservers", token, null);
            List<String> activatedServerList = JSONObject.parseObject(activatedServerString, new TypeReference<List<String>>() {});
            for (String each : activatedServerList){
                Map<String, Object> map2 = JSON.parseObject(each, new TypeReference<Map<String, Object>>() {});
                String serverIp = (String) map2.get("serverIp");
                if (serverIp.equals(ipAddress)){
                    return "Pro Version Activated!";
                }
            }
        }
        return "Pro Activate Failed!";
    }
}

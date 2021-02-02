package com.ultikits.ultitools.checker;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewProChecker {
//    private static final String name = UltiTools.getInstance().getConfig().getString("pro_name");
//    private static final String password = UltiTools.getInstance().getConfig().getString("pro_password");
//
//    public static CheckResponse run() throws IOException {
//        HttpURLConnection connection = (HttpURLConnection) new URL("http://api.ultikits.com:8080/UltikitsServer/validate.api?name="+name+"&key="+ password).openConnection();
//        connection.setRequestMethod("GET");
//        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//        String data;
//        CheckResponse response = new CheckResponse();
//        try {
//            InputStream input2 = connection.getInputStream();
//            InputStreamReader streamReader = new InputStreamReader(input2, StandardCharsets.UTF_8);
//            BufferedReader br = new BufferedReader(streamReader);
//            data = br.readLine();
//            JSONObject userJson = JSONObject.parseObject(data);
//            response = JSON.toJavaObject(userJson,CheckResponse.class);
//
//            br.close();
//            streamReader.close();
//        } catch (IOException ignored) {
//        } finally {
//            connection.disconnect();
//        }
//        return response;
//    }

    private static final String tokenUrl = "http://panel.ultikits.com:8082/user/getToken";
    private static final String refreshTokenUrl = "http://panel.ultikits.com:8082/user/refreshToken";

    public static String getToken(String userName, String password) {
        BufferedReader reader = null;
        HttpURLConnection connection = null;
        String returnValue = "";
        try {
            URL url = new URL(tokenUrl+"?username="+userName+"&password="+password);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringWriter out = new StringWriter(connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            returnValue = out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            connection.disconnect();
        }
        return returnValue;
    }

    public static String refreshToken(String refreshToken) {
        BufferedReader reader = null;
        HttpURLConnection connection = null;
        String returnValue = "";
        try {
            URL url = new URL(refreshTokenUrl+"?refresh_token="+refreshToken);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            StringWriter out = new StringWriter(connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            returnValue = out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            connection.disconnect();
        }
        return returnValue;
    }

}

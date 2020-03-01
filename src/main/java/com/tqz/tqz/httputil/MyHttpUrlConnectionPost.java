package com.tqz.tqz.httputil;


import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MyHttpUrlConnectionPost {

    private static String content;

    public static String sendHttpRequest(String address) {
        HttpURLConnection connection = null;
        try {
            // URL url = newest URL(address);
            String arr = URLEncoder.encode(address, "utf-8");
            URL url = new URL(arr);
            connection = (HttpURLConnection) url.openConnection();
            //设置最多连接时间
            connection.setConnectTimeout(1000);
            //设置最多读取时间
            connection.setReadTimeout(8000);
            InputStreamReader in = new InputStreamReader(connection.getInputStream(), "utf-8");

            String content = "";
            int i;

            while ((i = in.read()) != -1) {
                content = content + (char) i;
            }
            in.close();
            return content;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return content;
    }
}

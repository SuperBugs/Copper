package com.tqz.tqz.httputil;


import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyHttpUrlConnectionGet {

    private static String content;
    public static String sendHttpRequest(String address) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            //设置最多连接时间
            connection.setConnectTimeout(2000);
            //设置最多读取时间
            connection.setReadTimeout(8000);
            InputStreamReader in = new InputStreamReader(connection.getInputStream(), "UTF-8");

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

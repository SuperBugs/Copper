package com.tqz.tqz.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.tqz.tqz.post.DiskCache;
import com.tqz.tqz.post.GetUserDate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CommonDateFunction {
    String result;

    //开启线程，保存用户成功登录状态，从网络获取用户数据保存到本地
    public void setUserDate(final String userPhoneNumber, final String password, final Context context) {
        //保存用户登录信息
        SharedPreferences.Editor editor = context.getSharedPreferences("UserInformation", Context.MODE_PRIVATE).edit();
        editor.putString("phoneNumber", userPhoneNumber);
        editor.putString("userPassword", password);
        editor.putBoolean("loginState", true);
        editor.commit();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonFunction commonFunction = new CommonFunction();
                //有网络获取信息，保存信息到本地
                if (commonFunction.IsInternet(context)) {
                    GetUserDate getUserDate;
                    ArrayList<HashMap<String, String>> date;
                    getUserDate = new GetUserDate();
                    date = getUserDate.getDate(userPhoneNumber, password);
                    if (date != null) {
                        commonFunction.putDate("userDate", date, context);
                    }
                }
            }
        }).start();
    }

    //上传用户头像到服务器,返回url地址
    public String postUserHeadImage(Bitmap bitmap, Context context, String uesrPhoneNumber) {
        CommonConfig commonConfig;
        commonConfig = new CommonConfig();
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        //保存图片
        DiskCache diskCache = new DiskCache();
        diskCache.putUserImage("ImageUserHead", bitmap, context);
        //图片本地地址
        String srcPath = commonConfig.getCacheDir() + "ImageUserHead";
        try {
            URL url = new URL(commonConfig.getUrlPostUserHeadImageFunction());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置每次传输的数据大小,防止内存使用过多
            connection.setChunkedStreamingMode(128 * 1024);
            //连接服务器时限
            connection.setReadTimeout(4000);
            //设置允许输出
            connection.setDoOutput(true);
            //设置允许输入
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            //设置维持长连接
            connection.setRequestProperty("Connection", "Keep-Alive");
            //设置文件字符集为utf-8
            connection.setRequestProperty("Charsert", "UTF-8");
            //设置文件类型(必要)
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            //打开输出流
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            //模仿表单写入表单开头
            dos.writeBytes(twoHyphens + boundary + end);
            //写入文件名称
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + uesrPhoneNumber + ".png" + "\"" + end);
            dos.writeBytes(end);
            //打开文件输入流
            FileInputStream fis = new FileInputStream(srcPath);
            //每次读取的数据为8k
            byte[] buffer = new byte[8192];
            int count = 0;
            //读取文件
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            //关闭文件输入流
            fis.close();
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            //关闭url输出流
            dos.flush();
            //获取返回的结果
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            result = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return result;
    }

}

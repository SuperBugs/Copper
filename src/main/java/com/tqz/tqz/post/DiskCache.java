package com.tqz.tqz.post;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tqz.tqz.common.CommonConfig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;

//文件缓存图片
public class DiskCache implements com.tqz.tqz.Interface.ImageCache, Serializable {
    CommonConfig config = new CommonConfig();

    //从文件中获取图片
    @Override
    public Bitmap get(String url, Context context) {
        if (url == null) {
            return null;
        }
        url = url.replace("/", "");
        File f = new File(config.getCacheDir() + url.substring(url.indexOf("I"), url.length() - 4));
        return BitmapFactory.decodeFile(config.getCacheDir() + url.substring(url.indexOf("I"), url.length() - 4));
        //return BitmapFactory.decodeFile(config.getCacheDir() + url.replace("/", ""));

    }

    //写入网络图片
    @Override
    public void put(String url, Bitmap bitmap, Context context) {
        try {
            File f = new File(config.getCacheDir());
            if (!f.exists()) {
                f.mkdirs();
            }
            url = url.replace("/", "");
            File file = new File(config.getCacheDir() + url.substring(url.indexOf("I"), url.length() - 4));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //写入用户图片
    public void putUserImage(String url, Bitmap bitmap, Context context) {
        try {
            File f = new File(config.getCacheDir());
            if (!f.exists()) {
                f.mkdir();
            }
            File file = new File(config.getCacheDir() + url);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取用户图片
    @Override
    public Bitmap getUserImage(String url, Context context) {
        if (url == null) {
            return null;
        }
        return BitmapFactory.decodeFile(config.getCacheDir() + url);
    }
}

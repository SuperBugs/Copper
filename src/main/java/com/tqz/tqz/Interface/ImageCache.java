package com.tqz.tqz.Interface;

import android.content.Context;
import android.graphics.Bitmap;



//文件缓存和内存缓存
public interface ImageCache {
    //获取缓存图片,先从内存缓存中获取，没有再从图片缓存中获取
    public Bitmap get(String url,Context context);
    //获取缓存图片,先从内存缓存中获取，没有再从图片缓存中获取
    public Bitmap getUserImage(String url,Context context);
    //写入缓存图片
    public void put(String url , Bitmap bitmap, Context context);
    //写入缓存图片
    public void putUserImage(String url , Bitmap bitmap, Context context);
}

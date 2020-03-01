package com.tqz.tqz.post;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.widget.ImageView;

import com.tqz.tqz.Interface.ImageCache;
import com.tqz.tqz.common.CommonFunction;

import java.net.HttpURLConnection;
import java.net.URL;

//图片加载类
public class ImageLoaderTwo {
    Context context;
    CommonFunction commonFunction = new CommonFunction();
    //图片缓存
    ImageCache mImageCache = new DiskCache();
    Bitmap bitmap;
    //注入缓存实现
    public void setmImageCache(ImageCache cache) {
        this.mImageCache = cache;
    }

    public void displayUrlImage(String imageUrl, ImageView imageView, Context context) {
        this.context = context;
        Bitmap bitmap = mImageCache.get(imageUrl, context);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            //图片未曾缓存，提交到线程池下载
            submitLoadRequest(imageUrl, imageView);
            return;
        }

    }

    public void displayRoundImage(String imageUrl, ImageView imageView, Context context) {
        this.context = context;
        Bitmap bitmap = mImageCache.get(imageUrl, context);
        if (bitmap != null) {
            Bitmap b = commonFunction.makeRoundImage(bitmap);
            imageView.setImageBitmap(b);
        } else {
            //图片未曾缓存，提交到线程池下载
            submitRoundLoadRequest(imageUrl, imageView);
        }

    }

    private void submitLoadRequest(final String imageUrl, final ImageView imageView) {
        final android.os.Handler mHandler;
        mHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (imageView.getTag().equals(imageUrl)) {
                            imageView.setImageBitmap(bitmap);
                            //缓存图片
                            mImageCache.put(imageUrl, bitmap, context);
                        }

                        break;
                    case 2:
                        break;
                }
            }
        };
        if (imageUrl == null) {
            return;
        } else {
            imageView.setTag(imageUrl);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bitmap = downloadImage(imageUrl);
                    if(bitmap!=null){
                        Message m = new Message();
                        m.what = 1;
                        mHandler.sendMessage(m);
                    }
                }
            }).start();
        }
    }

    private void submitRoundLoadRequest(final String imageUrl, final ImageView imageView) {

        final android.os.Handler mHandler;
        mHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (imageView.getTag().equals(imageUrl)) {
                            imageView.setImageBitmap(commonFunction.makeRoundImage(bitmap));
                            //缓存图片
                            mImageCache.put(imageUrl, bitmap, context);
                        }
                        break;
                    case 2:
                        break;
                }
            }
        };
        if (imageUrl == null) {
            return;
        } else {
            imageView.setTag(imageUrl);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bitmap = downloadImage(imageUrl);
                    if(bitmap!=null){
                        Message m = new Message();
                        m.what = 1;
                        mHandler.sendMessage(m);
                    }

                }
            }).start();
            return;
        }
    }

    //下载图片
    private Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(commonFunction.transition(imageUrl));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}

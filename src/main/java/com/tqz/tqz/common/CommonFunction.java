package com.tqz.tqz.common;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.Uri;

import com.tqz.tqz.Interface.ImageCache;
import com.tqz.tqz.post.DiskCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class CommonFunction implements Serializable {

    //判断是否有网络
    public Boolean IsInternet(Context context) {
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        //判断网络
        if (manager.getActiveNetworkInfo() != null) {
            return manager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    //文件保存对象数据
    public void putDate(String dateName, Object date, Context context) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream in = null;
        CommonConfig config = new CommonConfig();
        try {
            fileOutputStream = context.openFileOutput(dateName, Context.MODE_PRIVATE);
            in = new ObjectOutputStream(fileOutputStream);
            in.writeObject(date);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //文件读取obj数据
    public Object getDate(String dateName, Context context) {
        Object date = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream in = null;
        CommonConfig config = new CommonConfig();
        try {
            fileInputStream = context.openFileInput(dateName);
            in = new ObjectInputStream(fileInputStream);
            date = in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    //绘制圆形图像
    public Bitmap makeCirculaImage(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        //正方形的边长
        int r;
        //取得图片的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //设置正方形边长，取短的一边作为边长
        if (width >= height) {
            r = width;
        } else {
            r = height;
        }
        //构建一个bitmap
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在output上画图
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        //正方形
        RectF rectF = new RectF(0, 0, r, r);
        //抗锯齿
        paint.setAntiAlias(true);
        //画圆角矩形（当x和y方向上的长度相等时，就是一个圆）
        canvas.drawRoundRect(rectF, r / 2, r / 2, paint);
        //设置当两个图形相交时的模式为SRC_IN，代表保留相交部分的上层，去掉其余部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //将图片画在output上
        canvas.drawBitmap(bitmap, null, rectF, paint);
        return output;
    }
    //绘制圆角图像
    public Bitmap makeRoundImage(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        //正方形的边长
        int r;
        //取得图片的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //设置正方形边长，取短的一边作为边长
        if (width >= height) {
            r = width;
        } else {
            r = height;
        }
        //构建一个bitmap
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在output上画图
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        //正方形
        RectF rectF = new RectF(0, 0, r, r);
        //抗锯齿
        paint.setAntiAlias(true);
        //画圆角矩形（当x和y方向上的长度相等时，就是一个圆）
        canvas.drawRoundRect(rectF, r / 4, r / 4, paint);
        //设置当两个图形相交时的模式为SRC_IN，代表保留相交部分的上层，去掉其余部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //将图片画在output上
        canvas.drawBitmap(bitmap, null, rectF, paint);
        return output;
    }

    //下载网络图片，返回图片而且缓存图片
    public Bitmap downLoadImage(String imageUrl, Context context) {

        Bitmap bitmap = null;

        if (imageUrl == null) {
            return null;
        } else {
            //下载图片
            try {
                Bitmap bitmap1 = null;
                URL url = new URL(imageUrl);
                final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                bitmap1 = BitmapFactory.decodeStream(conn.getInputStream());
                conn.disconnect();
                if (bitmap1 == null) {
                    //图片缓存器
                    ImageCache mImageCache = new DiskCache();
                    //缓存图片
                    mImageCache.put(imageUrl, bitmap1, context);
                }
                bitmap = bitmap1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 下载服务器上的文件
     * @param httpUrl
     * @param fileName
     * @param path
     * @return
     */
    public int downLoadFile(String httpUrl, String fileName, String path) {
        FileOutputStream fos = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        // 当存放文件的文件目录不存在的时候创建文件目录
        File tmpFile = new File(path);
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        // 获取文件对象
        File file = new File(path + "/" + fileName);
        try {
            URL url = new URL(httpUrl);
            try {
                conn = (HttpURLConnection) url.openConnection();
                is = conn.getInputStream();// 获得http请求返回的InputStream对象。
                fos = new FileOutputStream(file);// 获得文件输出流对象来写文件用的
                byte[] buf = new byte[256];
                conn.connect();// http请求服务器
                double count = 0;
                // http请求取得响应的时候
                if (conn.getResponseCode() >= 400) {
                    System.out.println("nono");
                    return 0;
                } else {
                    while (count <= 100) {
                        if (is != null) {
                            int numRead = is.read(buf);
                            if (numRead <= 0) {
                                break;
                            } else {
                                fos.write(buf, 0, numRead);
                            }

                        } else {
                            break;
                        }
                    }
                }
                conn.disconnect();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fos = null;
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    is = null;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }
    /**
     * @param imageUrl
     * @return
     * 中文处理
     */
    public  String transition(String imageUrl) {

        File f = new File(imageUrl);
        if(f.exists()){
            //正常逻辑代码
        }else{
            //处理中文路径
			/*try {
					imageUrl = URLEncoder.encode(imageUrl,UTF-8);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}*/
            imageUrl = Uri.encode(imageUrl);
        }
        imageUrl = imageUrl.replace("%3A", ":");
        imageUrl = imageUrl.replace("%2F", "/");
        return imageUrl;
    }
    public void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }
}

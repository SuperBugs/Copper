package com.tqz.tqz.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import com.tqz.tqz.R;
import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.common.CommonFunction;
import com.tqz.tqz.post.GetAppVersion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class UpdateApp {
    private Boolean result = false;
    private CommonConfig commonConfig;
    private int localVersion;
    private HashMap<String, String> serviceData;
    private int serviceVersion;
    private GetAppVersion getAppVersion;
    private CommonFunction commonFunction;
    private Activity activity;
    private ProgressDialog progressDialog;
    private Handler handler1;
    private Handler handler2;
    private Boolean isShowTest = false;
    private int code;
    private int curProgress;

    public boolean cheakApp(Activity activity, Boolean isShowTest) {
        this.activity = activity;
        this.isShowTest = isShowTest;
        result = cheak();
        return result;
    }

    private boolean cheak() {

        commonConfig = new CommonConfig();
        localVersion = commonConfig.getAppVersion();
        getAppVersion = new GetAppVersion();
        handler1 = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        showNoticeDialog();
                        break;
                    case 0:
                        if (isShowTest) {
                            Toast toastTwo = Toast.makeText(activity, activity.getString(R.string.toast_app_new), Toast.LENGTH_SHORT);
                            toastTwo.show();
                        }
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                serviceData = getAppVersion.getData();
                if (serviceData != null) {
                    serviceVersion = Integer.parseInt(serviceData.get("AppVersion"));
                    if (serviceVersion > localVersion) {
                        Message message1 = new Message();
                        message1.what = 1;
                        handler1.sendMessage(message1);
                    } else {
                        Message message1 = new Message();
                        message1.what = 0;
                        handler1.sendMessage(message1);
                        result = false;
                    }
                }
            }
        }).start();

        return result;
    }

    /**
     * 显示提示更新对话框
     */
    private void showNoticeDialog() {
        if (serviceData.get("AppForceUpdate").equals("yes")) {
            new AlertDialog.Builder(activity)
                    .setTitle("检测到新版本")
                    .setMessage(serviceData.get("AppNewVersionDetailes"))
                    .setCancelable(false)//设置不能按返回键返回
                    .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            showDownloadDialog();
                        }
                    }).setNegativeButton("残忍退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }
            }).setOnKeyListener(new DialogInterface.OnKeyListener() {  //屏蔽按键
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                        return true;
                    } else {
                        return false; //默认返回 false
                    }
                }
            }).create().show();
        } else {
            new AlertDialog.Builder(activity)
                    .setTitle("检测到新版本")
                    .setMessage(serviceData.get("AppNewVersionDetailes"))
                    .setCancelable(false)//设置不能按返回键返回
                    .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            showDownloadDialog();
                        }
                    }).setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setOnKeyListener(new DialogInterface.OnKeyListener() {  //屏蔽按键
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                        return true;
                    } else {
                        return false; //默认返回 false
                    }
                }
            }).create().show();
        }

    }

    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog() {

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(curProgress);
        progressDialog.show();
        handler2 = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (code != 0) {
                            File file = new File(commonConfig.getCacheDir() + "/apk/" + serviceData.get("AppVersion") + "tqz.apk");
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                            activity.startActivity(intent);
                            activity.finish();
                        }
                        break;
                    case 0:
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                code = downLoadFile(serviceData.get("AppUrl"), serviceData.get("AppVersion") + "tqz.apk", commonConfig.getCacheDir() + "/apk");
                Message message2 = new Message();
                message2.what = 1;
                handler2.sendMessage(message2);
            }
        }).start();

    }

    /**
     * 下载服务器上的文件
     *
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
                long fileLength = conn.getContentLength();//文件总长度
                is = conn.getInputStream();// 获得http请求返回的InputStream对象。
                fos = new FileOutputStream(file);// 获得文件输出流对象来写文件用的
                byte[] buf = new byte[256];
                conn.connect();// http请求服务器
                double count = 0;
                long readLength = 0;
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
                                readLength += numRead;
                                curProgress = (int) (((float) readLength / fileLength) * 100);
                                progressDialog.setProgress(curProgress);
                            }

                        } else {
                            break;
                        }
                    }
                }
                conn.disconnect();
                fos.close();
                is.close();
                return 1;
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
    }
}

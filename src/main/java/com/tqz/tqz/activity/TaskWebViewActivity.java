package com.tqz.tqz.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tqz.tqz.R;
import com.tqz.tqz.util.SystemBarTintManager;

public class TaskWebViewActivity extends Activity {
    private WebView mWebView;
    private String mUrl;
    private String mName;
    private Intent mIntent;
    private ImageView mRefreshIV;
    private ImageButton mBackBtn;
    private ProgressBar pg1;
    private ImageView mShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_web_view);
        mRefreshIV = (ImageView) findViewById(R.id.activity_task_refresh_IV);
        mBackBtn = (ImageButton) findViewById(R.id.activity_task_back);
        mShare=(ImageView)findViewById(R.id.activity_task_share);
        mIntent = getIntent();
        mUrl = mIntent.getStringExtra("TaskUrl");
        mName = mIntent.getStringExtra("TaskName");
        setTitles();
        //mWebView.setWebViewClient(new WebViewClient());
        /*mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });*/
        init();
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        mWebView.loadUrl(mUrl);

        setListener();
    }
    private void init() {
        // TODO 自动生成的方法存根
        mWebView=(WebView) findViewById(R.id.activity_task_wv);
        pg1=(ProgressBar) findViewById(R.id.progressBar1);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if( url.startsWith("http:") || url.startsWith("https:") ) {
                    return false;
                }

                // Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity( intent );
                return true;
            }
        });
        WebSettings seting=mWebView.getSettings();
        seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本
        seting.setDomStorageEnabled(true);//解决页面显示不完整
        seting.setCacheMode(WebSettings.LOAD_NO_CACHE);//禁止缓存
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根
                if(newProgress==100){
                    pg1.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg1.setProgress(newProgress);//设置进度值
                }

            }
        });

    }
    public void setTitles(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.title_blue);//通知栏所需颜色
    }
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


    private void setListener() {
        mRefreshIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
            }
        });
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/*");
                sendIntent.putExtra(Intent.EXTRA_TEXT,mUrl);
                startActivity(sendIntent);
            }
        });
    }

    //获取返回键点击事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

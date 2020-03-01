package com.tqz.tqz.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tqz.tqz.R;
import com.tqz.tqz.post.PostShareData;
import com.tqz.tqz.util.SystemBarTintManager;

public class ShareTaskActivity extends Activity {
    private EditText mNameEt;
    private EditText mAddressEt;
    private EditText mShareNumberEt;
    private EditText mIntroduceEt;
    private String mName;
    private String mAddress;
    private String mShareNumber;
    private String mIntroduce;
    private Button mSubmitBtn;
    private ProgressDialog diolog;
    private Handler mHandler;
    private ImageButton mBackIb;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_task);
        activity=this;
        findView();
        setListener();
        setHandler();
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
    private void findView(){
        mAddressEt = (EditText)findViewById(R.id.f_share_address_et);
        mIntroduceEt = (EditText)findViewById(R.id.f_share_introduce_et);
        mShareNumberEt = (EditText)findViewById(R.id.f_share_number_et);
        mNameEt = (EditText)findViewById(R.id.f_share_name_et);
        mSubmitBtn = (Button)findViewById(R.id.f_share_submit_btn);
        mBackIb=(ImageButton)findViewById(R.id.back_btn);
    }
    private void setHandler(){
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        diolog.dismiss();
                        Toast.makeText(activity, "提交失败,请稍后再试!", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        diolog.dismiss();
                        Toast.makeText(activity, "提交成功!", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(activity, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        };
    };
    private void getData() {
        mAddress = mAddressEt.getText().toString();
        mIntroduce = mIntroduceEt.getText().toString();
        mShareNumber = mShareNumberEt.getText().toString();
        mName = mNameEt.getText().toString();
    }

    private void setListener() {
        mBackIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diolog =ProgressDialog.show(activity, null, getString(R.string.f_share_dialog_now), false, true);
                //获取用户的登录状态，已经登录则发送数据
                SharedPreferences read =activity.getSharedPreferences("UserInformation", Context.MODE_PRIVATE);
                if(read.getBoolean("loginState", false)) {
                    getData();//获取数据
                    postData();//发送数据
                }else{
                    Toast.makeText(activity, "登录后才可提交共享", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void postData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PostShareData postShareData=new PostShareData();
                if(postShareData.postShareData(mName,mAddress,mIntroduce,mShareNumber)){
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);
                }else{
                    Message message = new Message();
                    message.what = 0;
                    mHandler.sendMessage(message);
                }
            }
        }).start();
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

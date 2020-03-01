package com.tqz.tqz.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.tqz.tqz.common.CommonFunction;
import com.tqz.tqz.fragment.UserFragment;
import com.tqz.tqz.post.GetUserDate;
import com.tqz.tqz.post.UserChangeNickName;
import com.tqz.tqz.util.SystemBarTintManager;

import java.util.ArrayList;
import java.util.HashMap;


public class UserChangeNickNameActivity extends Activity {
    private ImageButton mBack;
    private Button mChangeNickNameBtn;
    private EditText mNewNickNameEt;
    private String mNewNickName;
    private Intent mIntent;
    private String mAccount;
    private Handler mHandler;
    private Activity activity;
    private ProgressDialog diolog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_nick_name);
        activity = this;
        mIntent = getIntent();
        getData();
        getView();
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
    public void setHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        diolog.dismiss();
                        Toast.makeText(UserChangeNickNameActivity.this, "修改失败,请稍后再!", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        diolog.dismiss();
                        UserFragment.mNickNameTextView.setText(mNewNickName);
                        //MainActivity.mSildingMenuUserNickName.setText(mNewNickName);
                        Toast.makeText(UserChangeNickNameActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }
        };
    }

    private void getData() {
        mAccount = mIntent.getStringExtra("account");
    }

    private void getView() {
        mBack = (ImageButton) findViewById(R.id.activity_user_change_nick_name_back);
        mChangeNickNameBtn = (Button) findViewById(R.id.activity_user_change_nick_name_now);
        mNewNickNameEt = (EditText) findViewById(R.id.activity_user_change_nick_name_et);
    }

    private void setListener() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mChangeNickNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diolog =ProgressDialog.show(UserChangeNickNameActivity.this, null, getString(R.string.f_user_nick_name_change_now), false, true);
                if (mNewNickNameEt.getText().length() < 9) {
                    mNewNickName = mNewNickNameEt.getText().toString();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserChangeNickName user = new UserChangeNickName();
                            if (user.changeNickName(mNewNickName, mAccount)) {
                                GetUserDate gud = new GetUserDate();
                                CommonFunction cf = new CommonFunction();
                                ArrayList<HashMap<String, String>> data = (ArrayList<HashMap<String, String>>) cf.getDate("userDate", activity);
                                cf.putDate("userDate",gud.getDate(mAccount, data.get(0).get("UserPassword")),activity);
                                Message message = new Message();
                                message.what = 1;
                                mHandler.sendMessage(message);
                            } else {
                                Message message = new Message();
                                message.what = 0;
                                mHandler.sendMessage(message);
                            }
                        }
                    }).start();

                } else {
                    Toast.makeText(UserChangeNickNameActivity.this, "请输入8位以内的字符!", Toast.LENGTH_SHORT).show();
                }
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

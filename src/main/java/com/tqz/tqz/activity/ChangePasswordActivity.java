package com.tqz.tqz.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tqz.tqz.R;
import com.tqz.tqz.common.CommonDateFunction;
import com.tqz.tqz.post.ChangeUserPassword;
import com.tqz.tqz.security.MD5;
import com.tqz.tqz.util.SystemBarTintManager;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 作用:改变用户密码
 * 流程：activity初始化mob提供的SMSSDK对象，开启短信验证码回调监听事件，
 * 用户输入手机号和要修改的新密码，点击获取验证码，
 * activity检验手机号是否为11位，将验证手机号码发送给mob短信验证服务器，
 * 用户从短信获取验证码填入activity并点提交，
 * activity将验证码发送给mob短信验证服务器，
 * 在短信验证码回调监听事件中获取验证结果，
 * 向应用服务器发送数据，获取返回结果，根据结果通知用户
 */
public class ChangePasswordActivity extends Activity {
    private int change_state = 0;//改变密码状态0为失败1为成功
    private ImageButton backBtn;
    private EditText et_new_number;
    private EditText et_new_password;
    private EditText et_change_security;
    private Button btn_get_security;
    private Button btn_change_now;
    private ProgressDialog diolog;
    private String phoneNumber;
    private String newPassword;
    private String appKey;
    private String appPassword;
    private Activity activity;
    private Handler changeTimeHandler;
    private int securityTime = 60;
    private static final String TAG = "ChangePasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        activity = this;
        backBtn = (ImageButton) findViewById(R.id.btn_find_password_back);
        et_new_number = (EditText) findViewById(R.id.et_change_number);
        et_new_password = (EditText) findViewById(R.id.et_change_password);
        et_change_security = (EditText) findViewById(R.id.et_change_security);
        btn_get_security = (Button) findViewById(R.id.btn_new_security);
        btn_change_now = (Button) findViewById(R.id.btn_change_now);
        appKey = getString(R.string.mob_app_key);
        appPassword = getString(R.string.mob_app_secret);
        //初始化SMSSDK
        SMSSDK.initSDK(this, appKey, appPassword);
        //注册短信回调监听
        SMSSDK.registerEventHandler(event);
        //定时改变验证码获取时间
        changeTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (securityTime <= 1) {
                            btn_get_security.setEnabled(true);
                            btn_get_security.setText(activity.getText(R.string.btn_register_test));
                            securityTime = 60;
                        } else {
                            securityTime = securityTime - 1;
                            btn_get_security.setText(Integer.toString(securityTime) + "s");
                            changeTimeHandler.sendEmptyMessageDelayed(0, 1000);
                        }
                        break;
                }
            }
        };

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //防止Handler重复启动，加快时间速度
                changeTimeHandler.removeMessages(0);
                finish();
            }
        });
        //发送手机号获取验证码
        btn_get_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = et_new_number.getText().toString().trim();
                //发送短信，传入国家号码--使用SMSSDK核心类之前一定要初始化，否则不能使用
                if (phoneNumber.length() == 11) {

                    //发送验证码并向用户通知验证码发送成功注意查收
                    SMSSDK.getVerificationCode(getString(R.string.country_number), phoneNumber);

                    Log.d(TAG, "NewphoneNumberL " + "v = [" + phoneNumber.length() + "]");
//                    MyToast myToast = newest MyToast(ChangePasswordActivity.this);
//                    myToast.getToast(ChangePasswordActivity.this, getString(R.string.toast_post_security),
//                            "long").show();
                    Toast toastTwo = Toast.makeText(activity, getString(R.string.toast_post_security), Toast.LENGTH_SHORT);
                    toastTwo.show();

                    //设置获取验证码按钮不可用
                    btn_get_security.setEnabled(false);
                    btn_get_security.setAlpha(0.8f);
                    //发送消息让获取验证码按钮显示时间，并且动态改变
                    changeTimeHandler.sendEmptyMessageDelayed(0, 0);
                } else {
                    //提醒用户输入11位手机号
//                    MyToast myToast = newest MyToast(ChangePasswordActivity.this);
//                    myToast.getToast(ChangePasswordActivity.this,
//                    getString(R.string.toast_register_number), "long").show();
                    Toast toastTwo = Toast.makeText(activity, getString(R.string.toast_register_number), Toast.LENGTH_SHORT);
                    toastTwo.show();
                }
            }
        });
        btn_change_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检验密码是否合法
                if (et_new_password.getText().toString().length() > 5 &&
                        et_new_password.getText().toString().length() < 21) {
                    //密码合法，发送验证码到服务器检验
                    ckeckPasswordAndPostSecurity();
                } else {
                    //提醒用户输入密码的正确个数
//                    MyToast myToast = newest MyToast(ChangePasswordActivity.this);
//                    myToast.getToast(ChangePasswordActivity.this, getString(R.string.toast_register_password),
//                            "long").show();
                    Toast toastTwo = Toast.makeText(activity, getString(R.string.toast_register_password), Toast.LENGTH_SHORT);
                    toastTwo.show();
                }
            }
        });
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
    //检查加密密码是否合法并发送验证码
    private void ckeckPasswordAndPostSecurity() {
        String password1 = null;
        try {
            password1 = MD5.getMd5(MD5.getMd5(MD5.getMd5(et_new_password.getText().toString().trim())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        newPassword = password1;
        String security = et_change_security.getText().toString().trim();
        if (!TextUtils.isEmpty(security)) {
            diolog = ProgressDialog.show(ChangePasswordActivity.this, null, getString(R.string.dialog_change_account_now), false, true);
            SMSSDK.submitVerificationCode(getString(R.string.country_number), phoneNumber, security);
        } else {
            // 提醒用户验证码为空
//            MyToast myToast = newest MyToast(ChangePasswordActivity.this);
//            myToast.getToast(ChangePasswordActivity.this, getString(R.string.toast_post_security_null),
//                    "long").show();
            Toast toastTwo = Toast.makeText(activity, getString(R.string.toast_post_security_null), Toast.LENGTH_SHORT);
            toastTwo.show();
        }

    }

    //接受注册信息，为Activity添加提示注册是否成功Tost
    Handler tostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //提示用户改变密码成功
//                    MyToast myToast = newest MyToast(ChangePasswordActivity.this);
//                    myToast.getToast(ChangePasswordActivity.this, getString(R.string.toast_change_user_date_success),
//                            "long").show();
                    Toast toastTwo = Toast.makeText(activity, getString(R.string.toast_change_user_date_success), Toast.LENGTH_SHORT);
                    toastTwo.show();
                    //保存用户数据
                    CommonDateFunction commonDateFunction = new CommonDateFunction();
                    commonDateFunction.setUserDate(phoneNumber, newPassword, activity);
                    //进入首页
                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 0:
                    //提示用户改变密码失败
//                    MyToast myToastFail = newest MyToast(ChangePasswordActivity.this);
//                    myToastFail.getToast(ChangePasswordActivity.this, getString(R.string.toast_change_user_date_fail),
//                            "long").show();
                    Toast toastThree = Toast.makeText(activity, getString(R.string.toast_change_user_date_fail), Toast.LENGTH_SHORT);
                    toastThree.show();
                    break;
                case 2:
                    //提示用户账号不存在
//                    MyToast myToastnull = newest MyToast(ChangePasswordActivity.this);
//                    myToastnull.getToast(ChangePasswordActivity.this, getString(R.string.toast_login_null),
//                            "long").show();
                    Toast toastFour = Toast.makeText(activity, getString(R.string.toast_login_null), Toast.LENGTH_SHORT);
                    toastFour.show();
                    break;
            }
        }
    };
    //监听回调事件，获取验证结果,提示用户
    private EventHandler event = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object date) {
            if (result == SMSSDK.RESULT_COMPLETE) {//回调完成
                //如提交验证码成功则返回数据类型为HashMap<number,code>
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    HashMap<String, Object> mDate = (HashMap<String, Object>) date;
                    String phoneNumberBack = (String) mDate.get("phone");//返回验证手机号
                    if (phoneNumber.equals(phoneNumberBack)) {
                        try {
                            newPassword = MD5.getMd5(MD5.getMd5(MD5.getMd5(et_new_password.getText().toString().trim())));
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        //向服务器发送数据返回注册int结果1表示成功0表示失败
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ChangeUserPassword postRegisterDate = new ChangeUserPassword();
                                change_state = postRegisterDate.postUserDate(phoneNumber, newPassword);
                                //发送注册状态码，更新UI
                                Message message = new Message();
                                message.what = change_state;
                                tostHandler.sendMessage(message);
                                diolog.dismiss();
                            }
                        }).start();

                    }

                }
            }
        }
    };
    //获取返回键点击事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

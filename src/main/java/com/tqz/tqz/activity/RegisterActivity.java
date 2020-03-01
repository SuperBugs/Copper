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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tqz.tqz.R;
import com.tqz.tqz.common.CommonDateFunction;
import com.tqz.tqz.common.CommonFunction;
import com.tqz.tqz.post.PostRegisterDate;
import com.tqz.tqz.security.MD5;
import com.tqz.tqz.util.SystemBarTintManager;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 作用:用户注册
 * 流程：activity初始化mob提供的SMSSDK对象，开启短信验证码回调监听事件，
 * 用户输入手机号和密码，点击获取验证码，
 * activity检验手机号是否为11位，将验证手机号码发送给mob短信验证服务器，
 * 用户从短信获取验证码填入activity并点提交，
 * activity将验证码发送给mob短信验证服务器，
 * 在短信验证码回调监听事件中获取验证结果，
 * 向应用服务器发送数据，获取返回结果，根据结果通知用户
 */
public class RegisterActivity extends Activity {
    private ImageButton btn_register_back;
    private EditText et_phone_number;
    private EditText et_password_one;
    private EditText et_password_two;
    private EditText et_security;
    private TextView userArgumentTv;
    private Button btn_get_security;
    private Button btn_register_now;
    private ProgressDialog diolog;
    private CheckBox checkBox;
    private String phoneNumber;
    private String userPassword;
    private String appKey;
    private String appPassword;
    private CommonFunction commonFunction;
    private Activity activity;
    private int securityTime = 60;
    Handler changeTimeHandler;
    int register_state = 0;   //0为注册失败1为注册成功2为已经注册
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        activity = this;
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_password_one = (EditText) findViewById(R.id.et_password_one);
        et_password_two = (EditText) findViewById(R.id.et_password_two);
        et_security = (EditText) findViewById(R.id.et_security);
        btn_get_security = (Button) findViewById(R.id.btn_security);
        btn_register_now = (Button) findViewById(R.id.btn_register_now);
        btn_register_back = (ImageButton) findViewById(R.id.btn_register_back);
        checkBox = (CheckBox) findViewById(R.id.register_activity_check_box);
        userArgumentTv = (TextView) findViewById(R.id.register_activity_user_Agreement);
        appKey = getString(R.string.mob_app_key);
        appPassword = getString(R.string.mob_app_secret);
        //初始化SMSSDK
        SMSSDK.initSDK(this, appKey, appPassword);
        //注册短信回调监听
        SMSSDK.registerEventHandler(ev);
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

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (btn_register_now.isClickable()) {
                    btn_register_now.setClickable(false);
                    btn_register_now.setBackgroundResource(R.color.little_gray);
                } else {
                    btn_register_now.setClickable(true);
                    btn_register_now.setBackgroundResource(R.drawable.register_btn);
                }
            }
        });
        userArgumentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, UserArgumentActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 发送手机号获取验证码
         */
        btn_get_security.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //判断是否有网络连接，有则继续注册
                commonFunction = new CommonFunction();
                if (commonFunction.IsInternet(activity)) {

                    phoneNumber = et_phone_number.getText().toString().trim();
                    //发送短信，传入国家号码--使用SMSSDK核心类之前一定要初始化，否则不能使用
                    if (phoneNumber.length() == 11) {
                        //发送验证码并向用户通知验证码发送成功注意查收
                        SMSSDK.getVerificationCode(getString(R.string.country_number), phoneNumber);
                        Log.d(TAG, "phoneNumberL " + "v = [" + phoneNumber.length() + "]");
//                        MyToast myToast = newest MyToast(RegisterActivity.this);
//                        myToast.getToast(RegisterActivity.this, getString(R.string.toast_post_security),
//                                "long").show();
                        Toast toast = Toast.makeText(activity, getString(R.string.toast_post_security), Toast.LENGTH_SHORT);
                        toast.show();
                        phoneNumber = et_phone_number.getText().toString().trim();

                        //设置获取验证码按钮不可用
                        btn_get_security.setEnabled(false);
                        btn_get_security.setAlpha(0.8f);
                        //发送消息让获取验证码按钮显示时间，并且动态改变
                        changeTimeHandler.sendEmptyMessageDelayed(0, 0);
                    } else {
                        //提醒用户输入11位手机号
//                        MyToast myToast = newest MyToast(RegisterActivity.this);
//                        myToast.getToast(RegisterActivity.this, getString(R.string.toast_register_number), "long").show();
                        Toast toast = Toast.makeText(activity, getString(R.string.toast_register_number), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    //提醒用户网络不可用
//                    MyToast myToast = newest MyToast(RegisterActivity.this);
//                    myToast.getToast(RegisterActivity.this, getString(R.string.toast_internet_wrong), "short").show();
                    Toast toast = Toast.makeText(activity, getString(R.string.toast_internet_wrong), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        /**
         * 判断用户账号是否合法，调用ckeckPasswordAndPostSecurity()获取注册信息判断两次密码是否相同，且使用MD53次加密
         *并向短信验证服务器提交验证码，在监听回调中判断是否通过验证
         */
        btn_register_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_phone_number.getText().length() == 11) {

                    if (et_password_one.getText().toString().length() > 5 &&
                            et_password_one.getText().toString().length() < 21) {
                        //检查密码是否相同并发送验证码
                        ckeckPasswordAndPostSecurity();

                    } else {
                        //提醒用户输入密码的正确个数
//                        MyToast myToast = newest MyToast(RegisterActivity.this);
//                        myToast.getToast(RegisterActivity.this, getString(R.string.toast_register_password),
//                                "long").show();
                        Toast toast = Toast.makeText(activity, getString(R.string.toast_register_password), Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else {
                    //提醒用户输入11位号码
//                    MyToast myToast = newest MyToast(RegisterActivity.this);
//                    myToast.getToast(RegisterActivity.this, getString(R.string.toast_register_number),
//                            "long").show();
                    Toast toast = Toast.makeText(activity, getString(R.string.toast_register_number), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }


        });

        btn_register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //防止Handler重复启动，加快时间速度
                changeTimeHandler.removeMessages(0);
                finish();
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

    //检查加密密码是否相同并发送验证码
    private void ckeckPasswordAndPostSecurity() {
        try {
            String password1 = MD5.getMd5(MD5.getMd5(MD5.getMd5(et_password_one.getText().toString().trim())));
            String password2 = MD5.getMd5(MD5.getMd5(MD5.getMd5(et_password_two.getText().toString().trim())));
            if (password1.equals(password2)) {
                userPassword = password1;
                String security = et_security.getText().toString().trim();
                if (!TextUtils.isEmpty(security)) {
                    diolog = ProgressDialog.show(RegisterActivity.this, null, getString(R.string.dialog_register_now), false, true);
                    SMSSDK.submitVerificationCode(getString(R.string.country_number), phoneNumber, security);
                } else {
                    // 提醒用户验证码为空
//                    MyToast myToast = newest MyToast(RegisterActivity.this);
//                    myToast.getToast(RegisterActivity.this, getString(R.string.toast_post_security_null),
//                            "long").show();
                    Toast toast = Toast.makeText(activity, getString(R.string.toast_post_security_null), Toast.LENGTH_SHORT);
                    toast.show();
                }

            } else {
//                //提醒用户两次输入的密码不同
//                MyToast myToast = newest MyToast(RegisterActivity.this);
//                myToast.getToast(RegisterActivity.this, getString(R.string.toast_register_password_error),
//                        "long").show();
                Toast toast = Toast.makeText(activity, getString(R.string.toast_register_password_error), Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //接受注册信息，为Activity添加提示注册是否成功Tost
    Handler tostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast toast = Toast.makeText(activity, getString(R.string.toast_register_success), Toast.LENGTH_SHORT);
                    toast.show();
                    //保存用户数据
                    CommonDateFunction commonDateFunction = new CommonDateFunction();
                    commonDateFunction.setUserDate(phoneNumber, userPassword, activity);
                    MainActivity.mainActivity.finish();
                    //进入首页
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case 0:
                    //提示用户注册失败
//                    MyToast myToastFail = newest MyToast(RegisterActivity.this);
//                    myToastFail.getToast(RegisterActivity.this, getString(R.string.toast_register_fail),
//                            "long").show();
                    Toast toastTwo = Toast.makeText(activity, getString(R.string.toast_register_fail), Toast.LENGTH_SHORT);
                    toastTwo.show();
                    break;
                case 2:
                    //提示用户此手机号已经注册
//                    MyToast myToasthave = newest MyToast(RegisterActivity.this);
//                    myToasthave.getToast(RegisterActivity.this, getString(R.string.toast_register_have),
//                            "long").show();
                    Toast toastThree = Toast.makeText(activity, getString(R.string.toast_register_have), Toast.LENGTH_SHORT);
                    toastThree.show();
                    break;
            }
        }
    };
    //监听回调事件，获取验证结果,提示用户
    private EventHandler ev = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object date) {
            if (result == SMSSDK.RESULT_COMPLETE) {//回调完成
                //如提交验证码成功则返回数据类型为HashMap<number,code>
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                    HashMap<String, Object> mDate = (HashMap<String, Object>) date;
                    String phoneNumberBack = (String) mDate.get("phone");//返回注册手机号


                    //如果两次加密密码相同则保存密码
                    if (phoneNumber.equals(phoneNumberBack)) {
                        try {
                            userPassword = MD5.getMd5(MD5.getMd5(MD5.getMd5(et_password_one.getText().toString().trim())));
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "测试 " + phoneNumber);
                        //向服务器发送数据返回注册int结果1表示成功0表示失败
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PostRegisterDate postRegisterDate = new PostRegisterDate();
                                register_state = postRegisterDate.postRegisterDate(phoneNumber, userPassword);
                                //发送注册状态码，更新UI
                                Message message = new Message();
                                message.what = register_state;
                                tostHandler.sendMessage(message);
                                diolog.dismiss();
                            }
                        }).start();


                    }

                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity销毁时反注册，否则会造成内存泄漏
        SMSSDK.unregisterAllEventHandler();
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

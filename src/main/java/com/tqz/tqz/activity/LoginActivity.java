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
import android.widget.ImageView;
import android.widget.Toast;

import com.tqz.tqz.R;
import com.tqz.tqz.common.Code;
import com.tqz.tqz.common.CommonDateFunction;
import com.tqz.tqz.post.CheakLoginDate;
import com.tqz.tqz.security.MD5;
import com.tqz.tqz.util.SystemBarTintManager;

import java.security.NoSuchAlgorithmException;

/**
 * 作用:用户登录
 * 流程：activity初始化mob提供的SMSSDK对象，开启短信验证码回调监听事件，
 * 用户输入手机号和密码，点击获取验证码，
 * activity检验手机号是否为11位，将验证手机号码发送给mob短信验证服务器，
 * 用户从短信获取验证码填入activity并点提交，
 * activity将验证码发送给mob短信验证服务器，
 * 在短信验证码回调监听事件中获取验证结果，
 * 向应用服务器发送数据，获取返回结果，根据结果通知用户
 */
public class LoginActivity extends Activity {
    public static int login_state = 0;//登陆状态0为失败1为成功
    private String phoneNumber;
    private String userPassword;
    private ProgressDialog diolog;
    private Button registerBtn;
    private Button forgetPasswordBtn;
    private EditText et_login_number;
    private EditText et_login_password;
    private ImageButton backBtn;
    private Button loginBtn;
    private Activity activity;
    private ImageView iv_showCode;
    private EditText et_phoneCode;
    //产生的验证码
    private String realCode;
    private static int mLoginCount = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        activity = this;
        getView();
        setClickListener();
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
    //接受返回信息，为Activity添加提示登录是否成功Tost
    Handler tostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast toastOne = Toast.makeText(activity, getString(R.string.
                            toast_login_success), Toast.LENGTH_SHORT);
                    toastOne.show();
                    //保存用户数据
                    CommonDateFunction commonDateFunction = new CommonDateFunction();
                    commonDateFunction.setUserDate(phoneNumber, userPassword, activity);
                    //销毁前一个mainFragment
                    MainActivity.mainActivity.finish();
                    //进入首页
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    //结束此activity
                    finish();
                    break;
                case 0:
                    Toast toast = Toast.makeText(activity, getString(R.string.
                            toast_login_fail), Toast.LENGTH_SHORT);
                    toast.show();
                    mLoginCount = mLoginCount + 1;
                    if (mLoginCount > 1) {
                        iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
                        realCode = Code.getInstance().getCode().toLowerCase();
                        et_phoneCode.setVisibility(View.VISIBLE);
                        iv_showCode.setVisibility(View.VISIBLE);
                        et_phoneCode.setFocusable(true);
                    }
                    break;
                case 2:
                    Toast toastTwo = Toast.makeText(activity, getString(R.string.
                            toast_login_null), Toast.LENGTH_SHORT);
                    toastTwo.show();
                    break;
                case 3:
                    Toast toastThree = Toast.makeText(activity, getString(R.string.
                            toast_login_wrong), Toast.LENGTH_SHORT);
                    toastThree.show();
                    break;
                case 4:
                    Toast toastFour = Toast.makeText(activity, getString(R.string.
                            toast_login_num), Toast.LENGTH_SHORT);
                    toastFour.show();
                    break;
                case 5:
                    Toast toastFive = Toast.makeText(activity, getString(R.string.
                            toast_login_num_wrong), Toast.LENGTH_SHORT);
                    toastFive.show();
                    iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
                    realCode = Code.getInstance().getCode().toLowerCase();

            }
        }
    };

    private void cheakLoginDate() {

        //向服务器发送登录信息
        CheakLoginDate postRegisterDate = new CheakLoginDate();
        login_state = postRegisterDate.getloginResult(phoneNumber, userPassword);
        //发送登录状态码，更新UI
        Message message = new Message();
        message.what = login_state;
        tostHandler.sendMessage(message);
        //去除diolog
        diolog.dismiss();
    }

    //获取返回键点击事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void getView(){
        registerBtn = (Button) findViewById(R.id.register_btn);
        backBtn = (ImageButton) findViewById(R.id.btn_login_back);
        loginBtn = (Button) findViewById(R.id.btn_login);
        et_login_password = (EditText) findViewById(R.id.et_login_user_password);
        et_login_number = (EditText) findViewById(R.id.et_login_phone_number);
        forgetPasswordBtn = (Button) findViewById(R.id.btn_forgetPassword);
        iv_showCode = (ImageView) findViewById(R.id.iv_showCode);
        et_phoneCode = (EditText) findViewById(R.id.et_phoneCodes);//验证码
    }
    public void setClickListener(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLoginCount > 1) {
                    if (et_phoneCode.getText().toString() == null) {
                        login_state = 4;
                        Message message = new Message();
                        message.what = login_state;
                        tostHandler.sendMessage(message);
                    } else {
                        String phoneCode = et_phoneCode.getText().toString().toLowerCase();
                        if (!phoneCode.equals(realCode)) {
                            login_state = 5;
                            Message message = new Message();
                            message.what = login_state;
                            tostHandler.sendMessage(message);
                        } else {
                            //获取用户密码和账号
                            phoneNumber = et_login_number.getText().toString();
                            if (et_login_number.getText().length() == 11) {
                                try {
                                    userPassword = MD5.getMd5(MD5.getMd5(MD5.getMd5(et_login_password.getText().toString().trim())));
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                                //提示用户正在登录
                                diolog = ProgressDialog.show(LoginActivity.this, null, getString(R.string.dialog_login_now), false, true);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        cheakLoginDate();
                                    }
                                }).start();

                            } else {
                                login_state = 3;
                                Message message = new Message();
                                message.what = login_state;
                                tostHandler.sendMessage(message);
                            }
                        }
                    }

                } else {
                    //获取用户密码和账号
                    phoneNumber = et_login_number.getText().toString();
                    if (et_login_number.getText().length() == 11) {
                        try {
                            userPassword = MD5.getMd5(MD5.getMd5(MD5.getMd5(et_login_password.getText().toString().trim())));
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        //提示用户正在登录
                        diolog = ProgressDialog.show(LoginActivity.this, null, getString(R.string.dialog_login_now), false, true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                cheakLoginDate();
                            }
                        }).start();

                    } else {
                        login_state = 3;
                        Message message = new Message();
                        message.what = login_state;
                        tostHandler.sendMessage(message);
                    }
                }


            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
        iv_showCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
            }
        });
    }
}


package com.tqz.tqz.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tqz.tqz.R;
import com.tqz.tqz.activity.LoginActivity;
import com.tqz.tqz.activity.MainActivity;
import com.tqz.tqz.activity.RegisterActivity;
import com.tqz.tqz.activity.UserChangeNickNameActivity;
import com.tqz.tqz.activity.UserHonorExplainActivity;
import com.tqz.tqz.common.CommonFunction;
import com.tqz.tqz.post.DiskCache;

import java.util.ArrayList;
import java.util.HashMap;

public class UserFragment extends Fragment {
    private View userView;
    private Button loginBtn;
    private Button registerBtn;
    public static ImageView mUserHeadVi;
    private Activity activity;
    private Boolean mLoginState;
    private CommonFunction commonFunction;
    private DiskCache mDiskCache;
    public static Bitmap mUserHead;
    public static ArrayList<HashMap<String, String>> date = null;
    //    private Handler showUserHead;
    public static TextView mExperienceTextView;
    public static TextView mIncomeTextView;
    private TextView mHonorNameTextView;
    private TextView mAccountTextView;
    public static TextView mNickNameTextView;
    private TextView mChangeNickNameTextView;
    private TextView mExplainHonorNameTextView;
    private String name;
    public static boolean isLogin = false;
    public static int ex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this.getActivity();
        //获取用户的登录状态，已经登录则获取用户数据
        SharedPreferences read = activity.getSharedPreferences("UserInformation", Context.MODE_PRIVATE);
        mLoginState = read.getBoolean("loginState", false);
        commonFunction = new CommonFunction();
        //接受显示头像信息，0表示显示网络图片，一表示显示默认图片
//        showUserHead = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case 0:
//                        if (mUserHead == null) {
//                            break;
//                        }
//                        mUserHead = commonFunction.makeCirculaImage(mUserHead);
//                        mUserHeadVi.setImageBitmap(mUserHead);
//                        break;
//                    case 1:
//                        mUserHead = BitmapFactory.decodeResource(activity.getResources(), R.drawable.app_icon);
//                        mUserHead = commonFunction.makeCirculaImage(mUserHead);
//                        mUserHeadVi.setImageBitmap(mUserHead);
//                }
//            }
//        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        userView = inflater.inflate(R.layout.f_user_layout, container, false);
        loginBtn = (Button) userView.findViewById(R.id.log_on);
        registerBtn = (Button) userView.findViewById(R.id.register_now_btn);
        mUserHeadVi = (ImageView) userView.findViewById(R.id.btn_user_head);
        mExperienceTextView = (TextView) userView.findViewById(R.id.f_user_experience);
        mIncomeTextView = (TextView) userView.findViewById(R.id.f_user_income);
        mHonorNameTextView = (TextView) userView.findViewById(R.id.f_user_honor_name);
        mAccountTextView = (TextView) userView.findViewById(R.id.f_user_account);
        mNickNameTextView = (TextView) userView.findViewById(R.id.f_user_nickname);
        mChangeNickNameTextView = (TextView) userView.findViewById(R.id.f_user_nickname_change);
        mExplainHonorNameTextView = (TextView) userView.findViewById(R.id.f_user_honor_name_explain);
        //获取本地用户数据
        date = (ArrayList<HashMap<String, String>>) commonFunction.getDate("userDate", activity);
        checkLogin();//判断用户是否登录
        setListener();
        if ((date != null) && mLoginState) {
            isLogin = true;
            setView();
        } else {
            mUserHeadVi.setImageBitmap(commonFunction.makeCirculaImage(BitmapFactory.decodeResource(activity.getResources(), R.drawable.make_one)));
        }
        return userView;
    }

    private void setListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        mUserHeadVi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mLoginState) {
//                    Intent intent = new Intent(getActivity(), ChangeHeadImageActivity.class);
//                    startActivity(intent);
//                }
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginState) {
                    //修改登录状态
                    SharedPreferences.Editor editor = activity.getSharedPreferences("UserInformation", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("loginState", false);
                    editor.commit();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(activity, "退出登录成功!", Toast.LENGTH_SHORT).show();
                    activity.finish();
                } else {
                    Intent intent = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(intent);
                }
            }
        });

        mChangeNickNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginState == true) {
                    Intent intent = new Intent(activity, UserChangeNickNameActivity.class);
                    intent.putExtra("account", date.get(0).get("UserAccount"));
                    startActivity(intent);
                }

            }
        });
        mExplainHonorNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UserHonorExplainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setView() {
        mExperienceTextView.setText(date.get(0).get("UserExperience"));
        mIncomeTextView.setText(date.get(0).get("UserIncome") + " RMB");
        mNickNameTextView.setText(date.get(0).get("UserNickName"));
        mAccountTextView.setText(date.get(0).get("UserAccount"));
        ex = Integer.parseInt(date.get(0).get("UserExperience"));
        if (ex >= 5000) {
            name = activity.getResources().getString(R.string.f_user_level_zero);
            mUserHeadVi.setImageBitmap(commonFunction.makeCirculaImage(BitmapFactory.decodeResource(activity.getResources(), R.drawable.make_six)));
        } else if (ex > 2000) {
            name = activity.getResources().getString(R.string.f_user_level_one);
            mUserHeadVi.setImageBitmap(commonFunction.makeCirculaImage(BitmapFactory.decodeResource(activity.getResources(), R.drawable.make_five)));
        } else if (ex > 1000) {
            name = activity.getResources().getString(R.string.f_user_level_two);
            mUserHeadVi.setImageBitmap(commonFunction.makeCirculaImage(BitmapFactory.decodeResource(activity.getResources(), R.drawable.make_four)));
        } else if (ex > 500) {
            name = activity.getResources().getString(R.string.f_user_level_three);
            mUserHeadVi.setImageBitmap(commonFunction.makeCirculaImage(BitmapFactory.decodeResource(activity.getResources(), R.drawable.make_three)));
        } else if (ex > 100) {
            name = activity.getResources().getString(R.string.f_user_level_four);
            mUserHeadVi.setImageBitmap(commonFunction.makeCirculaImage(BitmapFactory.decodeResource(activity.getResources(), R.drawable.make_two)));
        } else if (ex >= 0) {
            name = activity.getResources().getString(R.string.f_user_level_five);
            mUserHeadVi.setImageBitmap(commonFunction.makeCirculaImage(BitmapFactory.decodeResource(activity.getResources(), R.drawable.make_one)));
        }
        mHonorNameTextView.setText(name);

    }

    private void checkLogin() {
        if (mLoginState.equals(true)) {
            loginBtn.setEnabled(false);//隐藏登录按钮且不可用
            loginBtn.setVisibility(View.INVISIBLE);
            registerBtn.setText("退出登录");//设置登录注册按钮为退出登录
            //判断本地数据是否存在
            if (date != null) {
                mDiskCache = new DiskCache();
                mUserHead = mDiskCache.getUserImage("ImageUserHead", activity);
                //本地头像是否存在
//                if (mUserHead == null) {
//                    //获取网络图片
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //如果网络图片url不存在，则发送消息显示默认图片
//                            if (date.get(0).get("UserHeadUrl").equals("")) {
//                                Message message = new Message();
//                                message.what = 1;
//                                showUserHead.sendMessage(message);
//                            } else {
//                                //下载网络图片,而且缓存到本地
//                                mUserHead = commonFunction.downLoadImage(date.get(0).get("UserHeadUrl"), activity);
//                                showUserHead.sendEmptyMessage(0);
//                                //发送消息展示
//                                Message message = new Message();
//                                message.what = 0;
//                                showUserHead.sendMessage(message);
//                            }
//                        }
//                    }).start();
//                } else {
//                    //显示本地头像
//                    mUserHead = commonFunction.makeCirculaImage(mUserHead);
//                    mUserHeadVi.setImageBitmap(mUserHead);
//                }
            }
        } else {
            //用户没有登录显示默认图片
            mUserHead = BitmapFactory.decodeResource(activity.getResources(), R.drawable.default_user_head);
            mUserHead = commonFunction.makeCirculaImage(mUserHead);
            mUserHeadVi.setScaleType(ImageView.ScaleType.FIT_XY);
            mUserHeadVi.setImageBitmap(mUserHead);
        }
    }
}

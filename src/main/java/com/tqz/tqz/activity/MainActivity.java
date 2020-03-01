package com.tqz.tqz.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tqz.tqz.R;
import com.tqz.tqz.common.CommonFunction;
import com.tqz.tqz.fragment.MainFragment;
import com.tqz.tqz.fragment.MakeMoneyFragment;
import com.tqz.tqz.fragment.SetFragment;
import com.tqz.tqz.fragment.UserFragment;
import com.tqz.tqz.fragment.YuiHuiQuanFragment;
import com.tqz.tqz.update.UpdateApp;
import com.tqz.tqz.util.SystemBarTintManager;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private MainFragment mainFragment;
    private UserFragment userFragment;
    private YuiHuiQuanFragment yuiHuiQuanFragment;
    private MakeMoneyFragment makeMoneyFragment;
    private SetFragment setFragment;
    private FragmentManager fragmentManger;
    private TextView titleTV;
    private String name;
    public static Activity mainActivity;
    private ImageView mHome;
    private ImageView mUser;
    private ImageView mSet;
    private ImageView mYuiHuiQuan;
    private ImageView mMakeMoney;
    private TextView mHomeTv;
    private TextView mUserTv;
    private TextView mSetTv;
    private TextView mYuiHuiQuanTv;
    private TextView mMakeMoneyTv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        initData();//初始化数据
        initObj(); //创建需要的对象
        findById();//获取组件
        setView();
        fragmentManger = getSupportFragmentManager();
        clickListener();//为组件设置监听
        addMainFragment();//添加首页fragment
        setTitle();//设置状态栏颜色
    }
    private void setTitle(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.title_blue);//通知栏所需颜色
        UpdateApp updateApp = new UpdateApp();
        updateApp.cheakApp(this, false);
    }
    private void setView() {
        CommonFunction commonFunction = new CommonFunction();
        ArrayList<HashMap<String, String>> data = (ArrayList<HashMap<String, String>>) commonFunction.getDate("userDate", this);
        SharedPreferences read = this.getSharedPreferences("UserInformation", Context.MODE_PRIVATE);
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

    private void findById() {
        titleTV = (TextView) findViewById(R.id.titles);
        mHome = (ImageView) findViewById(R.id.main_home);
        mSet = (ImageView) findViewById(R.id.main_set);
        mUser = (ImageView) findViewById(R.id.main_user);
        mMakeMoney = (ImageView) findViewById(R.id.main_make_money);
        mYuiHuiQuan = (ImageView) findViewById(R.id.main_yui_hui_quan);
        mHomeTv=(TextView)findViewById(R.id.main_bottom_home_tv);
        mYuiHuiQuanTv=(TextView)findViewById(R.id.main_bottom_yuihuiquan_tv);
        mMakeMoneyTv=(TextView)findViewById(R.id.main_bottom_makemoney_tv);
        mUserTv=(TextView)findViewById(R.id.main_bottom_user_tv);
        mSetTv=(TextView)findViewById(R.id.main_bottom_set_tv);
    }

    private void initData() {
    }

    private void initObj() {
        mainFragment = new MainFragment();
        userFragment = new UserFragment();
        makeMoneyFragment = new MakeMoneyFragment();
        yuiHuiQuanFragment = new YuiHuiQuanFragment();
        setFragment = new SetFragment();
    }

    private void clickListener() {
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTV.setText(R.string.ac_main_bottom_first);
                replaceMainFragment();
            }
        });
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTV.setText(R.string.left_menu_six);
                replaceSetFragment();
            }
        });
        mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceUserFragment();
                titleTV.setText(R.string.ac_main_bottom_four);
            }
        });
        mYuiHuiQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTV.setText(R.string.main_tv_yuhuiquan);
                replaceYuiHuiQuanFragment();
            }
        });
        mMakeMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTV.setText(R.string.ac_main_bottom_three);
                replaceMakeMoneyFragment();
            }
        });
        mHomeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTV.setText(R.string.ac_main_bottom_first);
                replaceMainFragment();
            }
        });
        mSetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTV.setText(R.string.left_menu_six);
                replaceSetFragment();
            }
        });
        mUserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceUserFragment();
                titleTV.setText(R.string.ac_main_bottom_four);
            }
        });
        mYuiHuiQuanTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTV.setText(R.string.main_tv_yuhuiquan);
                replaceYuiHuiQuanFragment();
            }
        });
        mMakeMoneyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTV.setText(R.string.ac_main_bottom_three);
                replaceMakeMoneyFragment();
            }
        });
    }

    private void replaceMainFragment() {
        FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction();
        fragmentTransaction
                .show(mainFragment)
                .hide(yuiHuiQuanFragment)
                .hide(userFragment)
                .hide(makeMoneyFragment)
                .hide(setFragment)
                .commit();
        mHome.setBackgroundResource(R.drawable.homepress);
        mHomeTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_press));
        mYuiHuiQuan.setBackgroundResource(R.drawable.yuihuilift);
        mYuiHuiQuanTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mMakeMoney.setBackgroundResource(R.drawable.makemoneylift);
        mMakeMoneyTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mUser.setBackgroundResource(R.drawable.userlift);
        mUserTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mSet.setBackgroundResource(R.drawable.setlift);
        mSetTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
    }

    public void replaceUserFragment() {
        FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction();
        fragmentTransaction
                .show(userFragment)
                .hide(mainFragment)
                .hide(yuiHuiQuanFragment)
                .hide(makeMoneyFragment)
                .hide(setFragment)
                .commit();
        mHome.setBackgroundResource(R.drawable.homelift);
        mHomeTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mYuiHuiQuan.setBackgroundResource(R.drawable.yuihuilift);
        mYuiHuiQuanTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mMakeMoney.setBackgroundResource(R.drawable.makemoneylift);
        mMakeMoneyTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mUser.setBackgroundResource(R.drawable.userpress);
        mUserTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_press));
        mSet.setBackgroundResource(R.drawable.setlift);
        mSetTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));

    }

    private void replaceYuiHuiQuanFragment() {
        FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction();
        fragmentTransaction
                .hide(mainFragment)
                .hide(userFragment)
                .hide(makeMoneyFragment)
                .hide(setFragment)
                .show(yuiHuiQuanFragment).commit();
        mHome.setBackgroundResource(R.drawable.homelift);
        mHomeTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mYuiHuiQuan.setBackgroundResource(R.drawable.yuihuipress);
        mYuiHuiQuanTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_press));
        mMakeMoney.setBackgroundResource(R.drawable.makemoneylift);
        mMakeMoneyTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mUser.setBackgroundResource(R.drawable.userlift);
        mUserTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mSet.setBackgroundResource(R.drawable.setlift);
        mSetTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
    }

    private void replaceMakeMoneyFragment() {
        FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction();
        fragmentTransaction
                .hide(mainFragment)
                .hide(userFragment)
                .hide(yuiHuiQuanFragment)
                .hide(setFragment)
                .show(makeMoneyFragment).commit();
        mHome.setBackgroundResource(R.drawable.homelift);
        mHomeTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mYuiHuiQuan.setBackgroundResource(R.drawable.yuihuilift);
        mYuiHuiQuanTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mMakeMoney.setBackgroundResource(R.drawable.makemoneypress);
        mMakeMoneyTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_press));
        mUser.setBackgroundResource(R.drawable.userlift);
        mUserTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mSet.setBackgroundResource(R.drawable.setlift);
        mSetTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
    }


    private void replaceSetFragment() {
        FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction();
        fragmentTransaction
                .hide(mainFragment)
                .hide(yuiHuiQuanFragment)
                .hide(userFragment)
                .hide(makeMoneyFragment)
                .show(setFragment).commit();
        mHome.setBackgroundResource(R.drawable.homelift);
        mHomeTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mYuiHuiQuan.setBackgroundResource(R.drawable.yuihuilift);
        mYuiHuiQuanTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mMakeMoney.setBackgroundResource(R.drawable.makemoneylift);
        mMakeMoneyTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mUser.setBackgroundResource(R.drawable.userlift);
        mUserTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mSet.setBackgroundResource(R.drawable.setpress);
        mSetTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_press));
    }

    private void addMainFragment() {
        FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction();
        fragmentTransaction
                .add(R.id.fragment_container, yuiHuiQuanFragment)
                .add(R.id.fragment_container, mainFragment)
                .add(R.id.fragment_container, userFragment)
                .add(R.id.fragment_container, makeMoneyFragment)
                .add(R.id.fragment_container, setFragment)
                .hide(makeMoneyFragment)
                .hide(setFragment)
                .hide(userFragment)
                .hide(yuiHuiQuanFragment)
                .show(mainFragment).commit();
        mHome.setBackgroundResource(R.drawable.homepress);
        mHomeTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_press));
        mYuiHuiQuan.setBackgroundResource(R.drawable.yuihuilift);
        mYuiHuiQuanTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mMakeMoney.setBackgroundResource(R.drawable.makemoneylift);
        mMakeMoneyTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mUser.setBackgroundResource(R.drawable.userlift);
        mUserTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
        mSet.setBackgroundResource(R.drawable.setlift);
        mSetTv.setTextColor(mainActivity.getResources().getColor(R.color.ac_main_bottom_tv_lift));
    }

    private boolean isTopActivity() {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains(TAG)) {
            isTop = true;
        }
        return isTop;
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                titleTV.setText(R.string.ac_main_bottom_first);
                replaceMainFragment();

                if (isTopActivity()) {
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                        //Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        firstTime = secondTime;//更新firstTime
                        return true;
                    } else {                                                    //两次按键小于2秒时，退出应用
                        System.exit(0);
                    }
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}

package com.tqz.tqz.guide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.Window;

import com.baidu.appx.BDSplashAd;
import com.tqz.tqz.R;
import com.tqz.tqz.activity.MainActivity;
import com.tqz.tqz.common.CommonDateFunction;
import com.tqz.tqz.common.CommonFunction;



public class GuideActivity extends Activity {
    private ViewPager viewPager;
    private boolean isFirstIn = false;
    private static final int TIME = 0;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private CommonFunction commonFunction;
    private Activity activity;
    private BDSplashAd b;
    private String BD_id="SSh4lI9fhTrIv3WsbqMqsPVL";
    private String BD_key="uyD18zpZ20jYTl6m7nTpwwVn9GkYvZtk";

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;

                case GO_GUIDE:
                    goHome(); //            goGuide();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        activity = this;
        //选择是否进入图片导航activity
        init();
        ViewGroup vi=(ViewGroup) findViewById(R.id.ad_view);

    }

    private void init() {

        SharedPreferences perPreferences = getSharedPreferences("tqz",
                MODE_PRIVATE);
        isFirstIn = perPreferences.getBoolean("isFirstIn", true);
        if (!isFirstIn) {
            //如果用户不是第一次登录，而且有网络,刷新用户数据
            SharedPreferences read = getSharedPreferences("UserInformation", Context.MODE_PRIVATE);
            CommonDateFunction commonDateFunction = new CommonDateFunction();
            CommonFunction commonFunction = new CommonFunction();
            if (read.getBoolean("loginState", false) && commonFunction.IsInternet(activity)) {
                //保存用户数据
                commonDateFunction.setUserDate(read.getString("phoneNumber", ""), read.getString("userPassword", ""), activity);
            }
            mHandler.sendEmptyMessageDelayed(GO_HOME, TIME);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, TIME);

            SharedPreferences.Editor editor = perPreferences.edit();

            editor.putBoolean("isFirstIn", false);

            editor.commit();
        }

    }

    private void goHome() {
        Intent i = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

 /*   private void goGuide() {
        Intent i = newest Intent(GuideActivity.this, Guide.class);
        startActivity(i);
        finish();
    } */

}

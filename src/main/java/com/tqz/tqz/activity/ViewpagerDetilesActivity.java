package com.tqz.tqz.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tqz.tqz.Interface.ImageCache;
import com.tqz.tqz.R;
import com.tqz.tqz.fragment.UserFragment;
import com.tqz.tqz.post.DiskCache;
import com.tqz.tqz.post.ImageLoader;
import com.tqz.tqz.post.PostExperienceAndIncomeData;
import com.tqz.tqz.util.SystemBarTintManager;

public class ViewpagerDetilesActivity extends Activity {

    private String mTaskImageUrl;
    private String mTaskName;
    private String mTaskIncome;
    private String mTaskType;
    private String mTaskDate;
    private String mTaskAddressUrl;
    private String mShareNumber;
    private String mTaskMoney;
    private Intent mIntent;
    private ImageView mTaskImageView;
    private TextView mTaskNameTextView;
    private TextView mTaskIncomeTextView;
    private TextView mTaskTypeTextView;
    private TextView mTaskDateTextView;
    private Button mStart;
    private ImageButton mBack;
    private ImageCache imageCache;
    private Activity activity;
    private String mAppTextOne;
    private String mAppTextTwo;
    private String mAppTextThree;
    private String mAppTextFour;
    private String mAppTextFive;
    private String mAppImageOneUrl;
    private String mAppImageTwoUrl;
    private String mAppImageThreeUrl;
    private String mAppImageFourUrl;
    private String mAppImageFiveUrl;
    private LinearLayout mTaskProcessLayout;
    private int mIvW;
    private int mIvH;
    private static final String TAG = "ViewpagerDetilesActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_detiles);
        activity = this;
        mIntent = getIntent();//获取intent
        getData();//获取上一个activity传递的数据
        getViewById();//获取是视图组件
        setView();//处理组件视图
        setListener();//设置监听事件
        addProgressView();
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

    private void changeUserFragmentView() {
        if (UserFragment.isLogin = true && UserFragment.date.get(0).get("UserIncome") != null && UserFragment.date.get(0).get("UserExperience") != null) {
            int experience = Integer.parseInt(UserFragment.date.get(0).get("UserExperience"));
            UserFragment.mExperienceTextView.setText(Integer.toString(experience + 100));
            int income = Integer.parseInt(UserFragment.date.get(0).get("UserIncome"));
            UserFragment.mIncomeTextView.setText(Integer.toString(income) + " RMB");
        }

    }

    private void addProgressView() {
        //定义图片大小
        Display display = getWindowManager().getDefaultDisplay();
        mIvW=display.getWidth()/4*3;
        mIvH=display.getWidth()/3*4;
        //第一步
        if (!mAppTextOne.equals("none")) {
            TextView textView = new TextView(activity);
            textView.setText("  " + mAppTextOne);
            mTaskProcessLayout.addView(textView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textView.getLayoutParams());
            layoutParams.setMargins(0, 20, 0, 20);
            textView.setLayoutParams(layoutParams);
        }
        if (!mAppImageOneUrl.equals("none")) {
            ImageView imageView = new ImageView(activity);
            ImageLoader imageLoader=new ImageLoader();
            imageLoader.displayUrlImage(mAppImageOneUrl, imageView, activity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(mIvW,mIvH));
            mTaskProcessLayout.addView(imageView);
        }
        Log.d(TAG, "超军"+mAppImageOneUrl);
        //第二步
        if (!mAppTextTwo.equals("none")) {
            TextView textView = new TextView(activity);
            textView.setText("  " + mAppTextTwo);
            mTaskProcessLayout.addView(textView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textView.getLayoutParams());
            layoutParams.setMargins(0, 30, 0, 20);
            textView.setLayoutParams(layoutParams);
        }
        if (!mAppImageTwoUrl.equals("none")) {
            ImageView imageView = new ImageView(activity);
            ImageLoader imageLoader=new ImageLoader();
            imageLoader.displayUrlImage(mAppImageTwoUrl, imageView, activity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(mIvW, mIvH));
            mTaskProcessLayout.addView(imageView);
        }
        //第三步
        if (!mAppTextThree.equals("none")) {
            TextView textView = new TextView(activity);
            textView.setText("  " + mAppTextThree);
            mTaskProcessLayout.addView(textView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textView.getLayoutParams());
            layoutParams.setMargins(0, 30, 0, 20);
            textView.setLayoutParams(layoutParams);
        }
        if (!mAppImageThreeUrl.equals("none")) {
            ImageView imageView = new ImageView(activity);
            ImageLoader imageLoader=new ImageLoader();
            imageLoader.displayUrlImage(mAppImageThreeUrl, imageView, activity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(mIvW, mIvH));
            mTaskProcessLayout.addView(imageView);
        }
        //第四步
        if (!mAppTextFour.equals("none")) {
            TextView textView = new TextView(activity);
            textView.setText("  " + mAppTextFour);
            mTaskProcessLayout.addView(textView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textView.getLayoutParams());
            layoutParams.setMargins(0, 30, 0, 20);
            textView.setLayoutParams(layoutParams);
        }
        if (!mAppImageFourUrl.equals("none")) {
            ImageView imageView = new ImageView(activity);
            ImageLoader imageLoader=new ImageLoader();
            imageLoader.displayUrlImage(mAppImageFourUrl, imageView, activity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(mIvW, mIvH));
            mTaskProcessLayout.addView(imageView);
        }
        //第五步
        if (!mAppTextFive.equals("none")) {
            TextView textView = new TextView(activity);
            textView.setText("  " + mAppTextFive);
            mTaskProcessLayout.addView(textView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textView.getLayoutParams());
            layoutParams.setMargins(0, 30, 0, 20);//设置text view的填充
            textView.setLayoutParams(layoutParams);
        }
        if (!mAppImageFiveUrl.equals("none")) {
            ImageView imageView = new ImageView(activity);
            ImageLoader imageLoader=new ImageLoader();
            imageLoader.displayUrlImage(mAppImageFiveUrl, imageView, activity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(mIvW, mIvH));
            mTaskProcessLayout.addView(imageView);
        }
    }

    private void setListener() {

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(ViewpagerDetilesActivity.this, TaskWebViewActivity.class);
                it.putExtra("TaskUrl", mTaskAddressUrl);
                it.putExtra("TaskName", mTaskName);
                startActivity(it);

                final android.os.Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 1:
                                Toast toastOne = Toast.makeText(activity, getString(R.string.
                                        activity_app_detailes_expercience), Toast.LENGTH_SHORT);
                                toastOne.show();
                                changeUserFragmentView();
                                break;
                        }
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences read = activity.getSharedPreferences("UserInformation", Context.MODE_PRIVATE);
                        Boolean mLoginState = read.getBoolean("loginState", false);
                        String mAccount = read.getString("phoneNumber", "null");
                        if (mLoginState && mAccount != "null" && mTaskMoney != null) {
                            PostExperienceAndIncomeData pd = new PostExperienceAndIncomeData();
                            boolean result = pd.postData(mTaskMoney, getResources().getString(R.string.experience_add), mAccount);
                            if (result) {
                                Message message = new Message();
                                message.what = 1;
                                mHandler.sendMessage(message);
                            }
                        }

                    }
                }).start();
            }
        });
    }

    private void getViewById() {
        mBack = (ImageButton) findViewById(R.id.app_detaile_back);
        mTaskImageView = (ImageView) findViewById(R.id.app_detaile_image);
        mTaskNameTextView = (TextView) findViewById(R.id.app_detaile_name);
        mTaskIncomeTextView = (TextView) findViewById(R.id.app_detaile_income);
        mTaskTypeTextView = (TextView) findViewById(R.id.app_detaile_type);
        mTaskDateTextView = (TextView) findViewById(R.id.app_detaile_date);
        //mTaskAddressTextView = (TextView) findViewById(R.id.app_detaile_url);
        mStart = (Button) findViewById(R.id.app_detaile_start);
        mTaskProcessLayout = (LinearLayout) findViewById(R.id.ac_app_process);
    }

    private void getData() {

        mTaskImageUrl = mIntent.getStringExtra("TaskImageUrl");
        mTaskName = mIntent.getStringExtra("TaskName");
        mTaskIncome = mIntent.getStringExtra("TaskIncome");
        mTaskType = mIntent.getStringExtra("TaskType");
        mTaskDate = mIntent.getStringExtra("TaskDate");
        mTaskAddressUrl = mIntent.getStringExtra("TaskAddressUrl");
        mShareNumber = mIntent.getStringExtra("TaskShareNumber");
        mTaskMoney = mIntent.getStringExtra("TaskMoney");


        mAppTextOne=mIntent.getStringExtra("AppProcessOneText");
        mAppTextTwo=mIntent.getStringExtra("AppProcessTwoText");
        mAppTextThree=mIntent.getStringExtra("AppProcessThreeText");
        mAppTextFour=mIntent.getStringExtra("AppProcessFourText");
        mAppTextFive=mIntent.getStringExtra("AppProcessFiveText");
        mAppImageOneUrl=mIntent.getStringExtra("AppProcessOneImageUrl");
        mAppImageTwoUrl=mIntent.getStringExtra("AppProcessTwoImageUrl");
        mAppImageThreeUrl=mIntent.getStringExtra("AppProcessThreeImageUrl");
        mAppImageFourUrl=mIntent.getStringExtra("AppProcessFourImageUrl");
        mAppImageFiveUrl=mIntent.getStringExtra("AppProcessFiveImageUrl");
    }

    private void setView() {
        ImageLoader imageLoader=new ImageLoader();
        imageCache = new DiskCache();
        imageLoader.setmImageCache(imageCache);
        imageLoader.displayUrlImage(mTaskImageUrl, mTaskImageView, activity);
        mTaskNameTextView.setText(mTaskName);//显示任务名称
        mTaskIncomeTextView.setText(mTaskIncome);//显示任务收益
        mTaskTypeTextView.setText(mTaskType);//显示任务类型
        mTaskDateTextView.setText(mTaskDate);//显示任务时间
        //mTaskAddressTextView.setText(mTaskAddressUrl);//显示url地址
        mTaskProcessLayout = (LinearLayout) findViewById(R.id.ac_app_process);
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

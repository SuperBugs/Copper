package com.tqz.tqz.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
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

import com.tqz.tqz.R;
import com.tqz.tqz.post.ImageLoader;
import com.tqz.tqz.post.PostExperienceAndIncomeData;
import com.tqz.tqz.util.SystemBarTintManager;

import static android.widget.ImageView.ScaleType;

public class YuiHuiQuanDetailesActivity extends Activity {

    private String mYuHuiQuanImageUrl;
    private String mYuHuiQuanName;
    private String mYuHuiQuanType;
    private String mYuHuiQuanDate;
    private String mYuHuiQuanIncome;
    private String mYuHuiQuanAddressUrl;
    private String mYuHuiQuanMoney;
    private String mYuHuiQuanTextOne;
    private String mYuHuiQuanTextTwo;
    private String mYuHuiQuanTextThree;
    private String mYuHuiQuanTextFour;
    private String mYuHuiQuanTextFive;
    private String mYuHuiQuanImageOneUrl;
    private String mYuHuiQuanImageTwoUrl;
    private String mYuHuiQuanImageThreeUrl;
    private String mYuHuiQuanImageFourUrl;
    private String mYuHuiQuanImageFiveUrl;
    private Intent mIntent;
    private ImageView mYuHuiQuanImageView;
    private TextView mYuHuiQuanNameTextView;
    private TextView mYuHuiQuanTypeTextView;
    private TextView mYuHuiQuanDateTextView;
    private TextView mYuHuiQuanIncomeTextView;
    private Button mStart;
    private ImageButton mBack;
    private ImageLoader imageLoader;
    private Activity activity;
    private LinearLayout mProgressLayout;
    private static final String TAG = "YuiHuiQuanDetailesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuhuiquan_detailes);
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

    private void addProgressView() {
        //第一步
        if (!mYuHuiQuanTextOne.equals("none")) {
            TextView textView = new TextView(activity);
            textView.setText(" " + mYuHuiQuanTextOne);
            mProgressLayout.addView(textView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textView.getLayoutParams());
            layoutParams.setMargins(0, 20, 0, 20);
            textView.setLayoutParams(layoutParams);
        }
        if (!mYuHuiQuanImageOneUrl.equals("none")) {
            ImageView imageView = new ImageView(activity);
            imageLoader.displayUrlImage(mYuHuiQuanImageOneUrl, imageView, activity);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(600, 1000));
            mProgressLayout.addView(imageView);
        }
        //第二步
        if (!mYuHuiQuanTextTwo.equals("none")) {
            TextView textView = new TextView(activity);
            textView.setText(" " + mYuHuiQuanTextTwo);
            mProgressLayout.addView(textView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textView.getLayoutParams());
            layoutParams.setMargins(0, 30, 0, 20);
            textView.setLayoutParams(layoutParams);
        }
        if (!mYuHuiQuanImageTwoUrl.equals("none")) {
            ImageView imageView = new ImageView(activity);
            imageLoader.displayUrlImage(mYuHuiQuanImageTwoUrl, imageView, activity);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(600, 1000));
            mProgressLayout.addView(imageView);
        }
        //第三步
        if (!mYuHuiQuanTextThree.equals("none")) {
            TextView textView = new TextView(activity);
            textView.setText(" " + mYuHuiQuanTextThree);
            mProgressLayout.addView(textView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textView.getLayoutParams());
            layoutParams.setMargins(0, 30, 0, 20);
            textView.setLayoutParams(layoutParams);
        }
        if (!mYuHuiQuanImageThreeUrl.equals("none")) {
            ImageView imageView = new ImageView(activity);
            imageLoader.displayUrlImage(mYuHuiQuanImageThreeUrl, imageView, activity);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(600, 1000));
            mProgressLayout.addView(imageView);
        }
        //第四步
        if (!mYuHuiQuanTextFour.equals("none")) {
            TextView textView = new TextView(activity);
            textView.setText(" " + mYuHuiQuanTextFour);
            mProgressLayout.addView(textView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textView.getLayoutParams());
            layoutParams.setMargins(0, 30, 0, 20);
            textView.setLayoutParams(layoutParams);
        }
        if (!mYuHuiQuanImageFourUrl.equals("none")) {
            ImageView imageView = new ImageView(activity);
            imageLoader.displayUrlImage(mYuHuiQuanImageFourUrl, imageView, activity);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(600, 1000));
            mProgressLayout.addView(imageView);
        }
        //第五步
        if (!mYuHuiQuanTextFive.equals("none")) {
            TextView textView = new TextView(activity);
            textView.setText(" " + mYuHuiQuanTextFive);
            mProgressLayout.addView(textView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textView.getLayoutParams());
            layoutParams.setMargins(0, 30, 0, 20);//设置text view的填充
            textView.setLayoutParams(layoutParams);
        }
        if (!mYuHuiQuanImageFiveUrl.equals("none")) {
            ImageView imageView = new ImageView(activity);
            imageLoader.displayUrlImage(mYuHuiQuanImageFiveUrl, imageView, activity);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(600, 1000));
            mProgressLayout.addView(imageView);
        }
    }

    //将dp转化为px,此方法不能用否则scrollview不会滑动
    private int getPixelsFromDp(int size) {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
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
                Intent it = new Intent(YuiHuiQuanDetailesActivity.this, TaskWebViewActivity.class);
                it.putExtra("TaskUrl", mYuHuiQuanAddressUrl);
                it.putExtra("TaskName", mYuHuiQuanName);
                startActivity(it);
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(mYuHuiQuanAddressUrl);
//                intent.setData(content_url);
//                startActivity(intent);
                final Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 1:
                                Toast toastOne = Toast.makeText(activity, getString(R.string.
                                        activity_app_detailes_expercience), Toast.LENGTH_SHORT);
                                toastOne.show();
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
                        if (mLoginState && mAccount != "null") {
                            PostExperienceAndIncomeData pd = new PostExperienceAndIncomeData();
                            boolean result = pd.postData(mYuHuiQuanMoney, getResources().getString(R.string.experience_add), mAccount);
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
        mBack = (ImageButton) findViewById(R.id.yuhuiquan_detaile_back);
        // mYuHuiQuanAddressTextView = (TextView) findViewById(R.id.yuhuiquan_detaile_url);
        mYuHuiQuanImageView = (ImageView) findViewById(R.id.yuihuiquan_detaile_image);
        mYuHuiQuanNameTextView = (TextView) findViewById(R.id.yuihuiquan_name);
        mYuHuiQuanTypeTextView = (TextView) findViewById(R.id.yuihuiquan_detaile_type);
        mYuHuiQuanDateTextView = (TextView) findViewById(R.id.yuihuiquan_detaile_date);
        mStart = (Button) findViewById(R.id.yuihuiquan_start);
        mProgressLayout = (LinearLayout) findViewById(R.id.ac_yuhuiquan_process);
        mYuHuiQuanIncomeTextView = (TextView) findViewById(R.id.yuihuiquan_income);
    }

    private void getData() {
        mYuHuiQuanImageUrl = mIntent.getStringExtra("YuHuiQuanImageUrl");
        mYuHuiQuanName = mIntent.getStringExtra("YuHuiQuanName");
        mYuHuiQuanType = mIntent.getStringExtra("YuHuiQuanType");
        mYuHuiQuanDate = mIntent.getStringExtra("YuHuiQuanDate");
        mYuHuiQuanIncome=mIntent.getStringExtra("YuHuiQuanIncome");
        mYuHuiQuanAddressUrl = mIntent.getStringExtra("YuHuiQuanAddressUrl");

        mYuHuiQuanMoney = mIntent.getStringExtra("YuHuiQuanMoney");
        mYuHuiQuanTextOne = mIntent.getStringExtra("YuHuiQuanProcessOneText");
        mYuHuiQuanTextTwo = mIntent.getStringExtra("YuHuiQuanProcessTwoText");
        mYuHuiQuanTextThree = mIntent.getStringExtra("YuHuiQuanProcessThreeText");
        mYuHuiQuanTextFour = mIntent.getStringExtra("YuHuiQuanProcessFourText");
        mYuHuiQuanTextFive = mIntent.getStringExtra("YuHuiQuanProcessFiveText");
        mYuHuiQuanImageOneUrl = mIntent.getStringExtra("YuHuiQuanProcessOneImageUrl");
        mYuHuiQuanImageTwoUrl = mIntent.getStringExtra("YuHuiQuanProcessTwoImageUrl");
        mYuHuiQuanImageThreeUrl = mIntent.getStringExtra("YuHuiQuanProcessThreeImageUrl");
        mYuHuiQuanImageFourUrl = mIntent.getStringExtra("YuHuiQuanProcessFourImageUrl");
        mYuHuiQuanImageFiveUrl = mIntent.getStringExtra("YuHuiQuanProcessFiveImageUrl");
    }

    private void setView() {
        //显示任务图片
        imageLoader = new ImageLoader();
        imageLoader.displayUrlImage(mYuHuiQuanImageUrl, mYuHuiQuanImageView, activity);
        mYuHuiQuanImageView.setScaleType(ScaleType.FIT_XY);
        mYuHuiQuanNameTextView.setText(mYuHuiQuanName);//显示任务名称
        // mYuHuiQuanIncomeTextView.setText(mYuHuiQuanIncome);//显示任务收益
        mYuHuiQuanTypeTextView.setText(mYuHuiQuanType);//显示任务类型
        mYuHuiQuanDateTextView.setText(mYuHuiQuanDate);//显示任务时间
        mYuHuiQuanIncomeTextView.setText(mYuHuiQuanIncome);

    }

    //获取返回键点击事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

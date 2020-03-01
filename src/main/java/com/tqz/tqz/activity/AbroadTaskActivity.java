package com.tqz.tqz.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tqz.tqz.R;
import com.tqz.tqz.adapter.FragmentAdapter;
import com.tqz.tqz.fragment.AbroadAppFragment;
import com.tqz.tqz.fragment.AbroadTaskFragment;
import com.tqz.tqz.update.UpdateApp;
import com.tqz.tqz.util.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

public class AbroadTaskActivity extends FragmentActivity {
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    private ViewPager mVp;

    private AbroadAppFragment mAbroadAppFragment;
    private AbroadTaskFragment mAbroadTaskFragment;
    //viewPager当前选中项
    private int currentIndex;
    //屏幕宽度
    private int screenWidth;
    //引导线
    private ImageView mTabLineIv;
    private ImageButton mBackBtn;
    private TextView mAbroadAppTextView;
    private TextView mAbroadTaskTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abroad);
        findById();
        //获取屏幕宽度
        initTabLineWidth();
        init();
        setListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.title_blue);//通知栏所需颜色
        UpdateApp updateApp = new UpdateApp();
        updateApp.cheakApp(this, false);
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
    public void setListener() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAbroadTaskTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVp.setCurrentItem(0);
            }
        });
        mAbroadAppTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVp.setCurrentItem(1);
            }
        });
    }

    public void findById() {
        mVp = (ViewPager) findViewById(R.id.vp_abroad_task);
        mTabLineIv = (ImageView) findViewById(R.id.iv_abroad_tab_line);
        mBackBtn = (ImageButton) findViewById(R.id.btn_abroad_back);
        mAbroadAppTextView = (TextView)findViewById(R.id.abroad_app_title_tv);
        mAbroadTaskTextView = (TextView)findViewById(R.id.abroad_task_title_tv);

    }

    public void init() {
        mAbroadTaskFragment = new AbroadTaskFragment();
        mAbroadAppFragment = new AbroadAppFragment();
        mFragmentList.add(mAbroadTaskFragment);
        mFragmentList.add(mAbroadAppFragment);
        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        mVp.setAdapter(mFragmentAdapter);
        mVp.setCurrentItem(0);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * @param position  当前位置
             * @param positionOffset  当前页面偏移百分比
             * @param positionOffsetPixels 当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - positionOffset)
                            * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2));
                }
                mTabLineIv.setLayoutParams(lp);
            }

            //页面被选中执行
            @Override
            public void onPageSelected(int position) {
            }

            /**
             * @param state 滑动中的状态 有三种状态(0，1，2)  1：正在滑动，2：滑动完毕，0：什么都没做
             */
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 设置滑动条的宽度为屏幕的1/2(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / 2;
        mTabLineIv.setLayoutParams(lp);
    }

    //获取返回键点击事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //销毁fragment和activity
            //mAbroadTaskFragment.onDestroy();
            //mAbroadAppFragment.onDestroy();
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

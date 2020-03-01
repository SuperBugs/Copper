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
import com.tqz.tqz.fragment.ChinaAppFragment;
import com.tqz.tqz.fragment.ChinaTaskFragment;
import com.tqz.tqz.util.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

public class ChinaTaskActivity extends FragmentActivity {
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    private ViewPager mVp;
    private TextView mAppTv;
    private TextView mTaskTv;
    private ImageButton mBackBtn;

    private ChinaAppFragment mChinaAppFragment;
    private ChinaTaskFragment mChinaTaskFragment;
    //viewPager当前选中项
    private int currentIndex;
    //屏幕宽度
    private int screenWidth;
    //引导线
    private ImageView mTabLineIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_china);
        findById();
        //获取屏幕宽度
        initTabLineWidth();
        setListener();
        init();
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
    private void setListener() {
        mAppTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVp.setCurrentItem(1);
            }
        });
        mTaskTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVp.setCurrentItem(0);
            }
        });
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findById() {
        mVp = (ViewPager) findViewById(R.id.vp_china_task);
        mTabLineIv=(ImageView)findViewById(R.id.iv_china_tab_line);
        mTaskTv=(TextView)findViewById(R.id.china_task_tv);
        mAppTv=(TextView)findViewById(R.id.china_app_tv);
        mBackBtn=(ImageButton)findViewById(R.id.china_back_btn);
    }

    public void init() {
        mChinaTaskFragment = new ChinaTaskFragment();
        mChinaAppFragment = new ChinaAppFragment();
        mFragmentList.add(mChinaTaskFragment);
        mFragmentList.add(mChinaAppFragment);
        mFragmentAdapter =new FragmentAdapter(this.getSupportFragmentManager(),mFragmentList);
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
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode== android.view.KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            //销毁fragment和activity
            //mChinaTaskFragment.onDestroy();
            //mChinaAppFragment.onDestroy();
            this.finish();
            return true;

        }
        return super.onKeyDown(keyCode,event);
    }

}

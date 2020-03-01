package com.tqz.tqz.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.tqz.tqz.R;
import com.tqz.tqz.activity.ViewpagerDetilesActivity;
import com.tqz.tqz.common.CommonFunction;

import java.util.ArrayList;
import java.util.HashMap;

public class MyViewPager extends ViewPager {
    PointF downP = new PointF();//触摸时按下的点
    PointF curP = new PointF();//触摸时当前的点
    OnSingleTouchListener onSingleTouchListener;
    private Context mContext;
    private boolean mIsMove;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    public MyViewPager(Context context) {
        super(context);

    }

    //禁止viewpager拦截触摸事件，让图片点击事件生效
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        curP.x = ev.getX();
        curP.y = ev.getY();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //记录按下时候的坐标
            downP.x = ev.getX();
            downP.y = ev.getY();
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            mIsMove = true;
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            //如果点击viewpager没有移动，则说明点击了viewpager，则打开相应的activity
            /*
            if (mIsMove != true) {
                CommonFunction commonFunction = new CommonFunction();
                ArrayList<HashMap<String, String>> data = (ArrayList<HashMap<String, String>>) commonFunction.getDate("MainViewPagerDate", mContext);
                if (data != null) {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap = data.get(this.getCurrentItem() - 1);
                    Intent intent = new Intent(mContext, ViewpagerDetilesActivity.class);
                    intent.putExtra("TaskImageUrl", hashMap.get("TaskImageUrl"));
                    intent.putExtra("TaskName", hashMap.get("TaskName"));
                    intent.putExtra("TaskIncome", hashMap.get("TaskIncome"));
                    intent.putExtra("TaskType", hashMap.get("TaskType"));
                    intent.putExtra("TaskDate", hashMap.get("TaskDate"));
                    intent.putExtra("TaskProcess", hashMap.get("TaskProcess"));
                    intent.putExtra("TaskAddressUrl", hashMap.get("TaskAddressUrl"));
                    intent.putExtra("TaskVideoUrl", hashMap.get("TaskVideoUrl"));
                    intent.putExtra("TaskShareNumber", hashMap.get("TaskShareNumber"));
                    intent.putExtra("TaskMoney", hashMap.get("TaskMoney"));
                    mContext.startActivity(intent);
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

            }
            */
            if (downP.x == curP.x && downP.y == curP.y) {

                CommonFunction commonFunction = new CommonFunction();
                ArrayList<HashMap<String, String>> data = (ArrayList<HashMap<String, String>>) commonFunction.getDate("MainViewPagerDate", mContext);
                if (data != null) {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    if(0==this.getCurrentItem()){
                        hashMap=data.get(0);
                    }else {
                        hashMap = data.get(this.getCurrentItem()-1);
                    }
                    Intent intent = new Intent(mContext, ViewpagerDetilesActivity.class);
                    intent.putExtra("TaskImageUrl", hashMap.get("TaskImageUrl"));
                    intent.putExtra("TaskName", hashMap.get("TaskName"));
                    intent.putExtra("TaskIncome", hashMap.get("TaskIncome"));
                    intent.putExtra("TaskType", hashMap.get("TaskType"));
                    intent.putExtra("TaskDate", hashMap.get("TaskDate"));
                    intent.putExtra("TaskAddressUrl", hashMap.get("TaskAddressUrl"));
                    intent.putExtra("TaskShareNumber", hashMap.get("TaskShareNumber"));
                    intent.putExtra("TaskMoney", hashMap.get("TaskMoney"));

                    intent.putExtra("AppProcessOneImageUrl", hashMap.get("AppProcessOneImageUrl"));
                    intent.putExtra("AppProcessOneText", hashMap.get("AppProcessOneText"));
                    intent.putExtra("AppProcessTwoImageUrl", hashMap.get("AppProcessTwoImageUrl"));
                    intent.putExtra("AppProcessTwoText", hashMap.get("AppProcessTwoText"));
                    intent.putExtra("AppProcessThreeImageUrl", hashMap.get("AppProcessThreeImageUrl"));
                    intent.putExtra("AppProcessThreeText", hashMap.get("AppProcessThreeText"));
                    intent.putExtra("AppProcessFourImageUrl", hashMap.get("AppProcessFourImageUrl"));
                    intent.putExtra("AppProcessFourText", hashMap.get("AppProcessFourText"));
                    intent.putExtra("AppProcessFiveImageUrl", hashMap.get("AppProcessFiveImageUrl"));
                    intent.putExtra("AppProcessFiveText", hashMap.get("AppProcessFiveText"));
                    mContext.startActivity(intent);
                    getParent().requestDisallowInterceptTouchEvent(true);
                }else{
                    Toast toastTwo = Toast.makeText(mContext, mContext.getString(R.string.toast_data_loading), Toast.LENGTH_SHORT);
                    toastTwo.show();
                }
                onSingleTouch();
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
            mIsMove = false;
        }
        return super.onTouchEvent(ev);
    }

    public void onSingleTouch() {
        if (onSingleTouchListener != null) {
            onSingleTouchListener.onSingleTouch();
        }
    }

    @Override
    public void removeView(View view) {
        //super.removeView(view);
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch();
    }

    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }
}

package com.tqz.tqz.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;
import com.tqz.tqz.R;
import com.tqz.tqz.common.CommonFunction;
import com.tqz.tqz.post.DiskCache;

import static android.view.MotionEvent.ACTION_UP;

public class SildingMenu extends HorizontalScrollView {

    private LinearLayout mLayout;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mScreenWidth;
    //dp
    private int mMenuRightPading = 120;
    private boolean once;
    //菜单是否打开
    public static boolean is_left_mueu_open;
    int mMenuWidth;
    //滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;
    private Context context;
    //阴影图像
    private ImageView shapeView;
    //主界面内容
    private ViewGroup mainContent;
    //文件缓存
    private DiskCache diskCache;
    //用户头像图片
    private Bitmap bitmap;
    //用户头像按钮
    //private ImageButton imageButton;
    private CommonFunction commonFunction;

    /**
     * 未使用自定义属性时调用
     *
     * @param context
     * @param attrs
     */
    public SildingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        //测量并获取屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        //把dp转化为px
        mMenuRightPading = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, context.getResources().getDisplayMetrics());
        commonFunction = new CommonFunction();
    }

    public void setShapeView(Context context, ViewGroup viewGroup) {
        this.context = context;
        this.mainContent = viewGroup;
        shapeView = new ImageView(context);
        shapeView.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams params = new LinearLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        shapeView.setLayoutParams(params);
        shapeView.setImageResource(R.drawable.bg_shape);
        //设置imageview透明度
        shapeView.setAlpha(0.5f);
    }

    public void setUserHead(Context context, ImageButton imageButton) {
        this.context = context;
        //this.imageButton = imageButton;
        diskCache = new DiskCache();
    }


    @Override
    //决定自定义ViewGroup内部View(子view)的宽和高,以及自己的宽和高
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (!once) {
            mLayout = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mLayout.getChildAt(0);
            mContent = (ViewGroup) mLayout.getChildAt(1);

            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPading;
            mContent.getLayoutParams().width = mScreenWidth;

            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    //决定子view的位置
    /**
     * 通过设置偏移量，将menu隐藏
     */
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case ACTION_UP:
                //scrollX表示隐藏在左边的宽度
                int scrollX = getScrollX();
                if (is_left_mueu_open) {
                    if (scrollX >= mMenuWidth / 15) {
                        this.smoothScrollTo(mMenuWidth, 0);
                        is_left_mueu_open = false;
                        //为主界面移除阴影图像
                        mainContent.removeView(shapeView);
                    } else {
                        this.smoothScrollTo(0, 0);
                    }
                } else {
                    if (scrollX <= mMenuWidth / 1.2) {
                        this.smoothScrollTo(0, 0);
                        is_left_mueu_open = true;
//                        //添加圆形头像
//                        if (UserFragment.mUserHead != null) {
//                            bitmap = commonFunction.makeCirculaImage(UserFragment.mUserHead);
//                            imageButton.setImageBitmap(bitmap);
//                        } else {
//                            imageButton.setImageBitmap(commonFunction.makeCirculaImage(BitmapFactory
//                                    .decodeResource(context.getResources(), R.drawable.default_user_head)));
//                        }
                        //为主界面添加阴影图像
                        mainContent.addView(shapeView);
                    } else {
                        this.smoothScrollTo(mMenuWidth, 0);
                    }
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    public void openLeftMenu() {
        if (is_left_mueu_open) return;
        this.smoothScrollTo(0, 0);
        is_left_mueu_open = true;

//        //添加圆形头像
//        if (UserFragment.mUserHead != null) {
//            bitmap = commonFunction.makeCirculaImage(UserFragment.mUserHead);
//            imageButton.setImageBitmap(bitmap);
//        } else {
//            imageButton.setImageBitmap(commonFunction.makeCirculaImage(BitmapFactory
//                    .decodeResource(context.getResources(), R.drawable.default_user_head)));
//        }
        //为主界面添加阴影图像
        mainContent.addView(shapeView);
    }

    public void closeLeftMenu() {

        if (!is_left_mueu_open) return;
        this.smoothScrollTo(mMenuWidth, 0);
        is_left_mueu_open = false;
        //为主界面移除阴影图像
        mainContent.removeView(shapeView);
    }

    /**
     * 切换菜单
     */
    public void toggle() {
        if (is_left_mueu_open) {
            closeLeftMenu();
        } else {
            openLeftMenu();
        }
    }

    //滚动发生时
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / mMenuWidth;
        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale);
    }
}




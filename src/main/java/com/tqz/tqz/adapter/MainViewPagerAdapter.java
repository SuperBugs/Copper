package com.tqz.tqz.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tqz.tqz.post.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;


public class MainViewPagerAdapter extends PagerAdapter {
    private Activity activity;
    //viewPager的数据
    private ArrayList<HashMap<String, String>> date;
    public static int mViewPagerPostion;
    HashMap<String, String> hashMap;

    public MainViewPagerAdapter(Activity activity, ArrayList<HashMap<String, String>> date) {
        this.date = date;
        this.activity = activity;
    }

    /**
     * 页面总数
     * @return
     */
    @Override
    public int getCount() {
        if (date == null) {
            return 7;
        }
        return 7;
    }

    //判断要显示的view与返回的object是否相等
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        object = null;
    }

    /**
     * 定义每个pager要显示的内容
     *
     * @param container view的容器，其实就是viewPager自身
     * @param position  相应的图片
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(activity);

        if (date == null) {
//            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.loading_2bi1);
//            imageView.setImageBitmap(bitmap);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            container.addView(imageView);
            return imageView;
        } else {
            if(position==0){
                position=5;
            }
            if(position==6){
                position=1;
            }
            mViewPagerPostion = position-1;

            hashMap = new HashMap<String, String>();
            hashMap = date.get(mViewPagerPostion);
            //使用imagerLoader对象显示图片的方法，而且双缓存图片
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.displayUrlImage(hashMap.get("TaskImageUrl"), imageView, activity);
            //设置ImageView填充父容器
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }

    }

    public void changeView(ArrayList<HashMap<String, String>> viewPagerDate) {
        this.date = viewPagerDate;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

package com.tqz.tqz.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tqz.tqz.R;
import com.tqz.tqz.post.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

//为mainFragmentListView自定义的baseAdapter
public class MainListBaseAdapter extends BaseAdapter {
    private Activity activity;
    //listview的数据
    private ArrayList<HashMap<String, String>> date;
    private static LayoutInflater inflater = null;

    HashMap<String, String> hashMap;
    private int Items;

    private class ViewHolder {
        public TextView taskName;
        public TextView taskIncome;
        public ImageView taskImage;

        public ViewHolder(View viewRoot) {
            taskName = (TextView) viewRoot.findViewById(R.id.tv_maintask_name);
            taskIncome = (TextView) viewRoot.findViewById(R.id.tv_maintask_income);
            taskImage = (ImageView) viewRoot.findViewById(R.id.iv_maintask_image);
        }

    }

    public MainListBaseAdapter(Activity activity, ArrayList<HashMap<String, String>> date,
                               int defaultItemCount) {
        this.activity = activity;
        this.date = date;
        this.Items = defaultItemCount;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return Items;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.main_list_view, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        if ((convertView != null) && convertView.getTag() instanceof ViewHolder) {
            if (date == null) {
                return convertView;
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();


            if (position > date.size()) {
                return null;
            }
            hashMap = new HashMap<String, String>();
            hashMap = date.get(position);
            if (!hashMap.get("TaskName").equals("")) {
                //设置listview相关的值
                holder.taskName.setText(hashMap.get("TaskName"));
                holder.taskIncome.setText(hashMap.get("TaskIncome"));
                //使用imagerLoader对象显示图片的方法，而且双缓存图片
                holder.taskImage.setTag(hashMap.get("TaskImageUrl"));
                //图片加载器
                ImageLoader imageLoader = new ImageLoader();

                imageLoader.displayRoundImage(hashMap.get("TaskImageUrl"), holder.taskImage, activity);
            }
        }
        return convertView;
    }

    public void addItems() {
        if (!(this.Items + 5 > date.size())) {
            this.Items = Items + 5;
            this.notifyDataSetChanged();
        }
    }

    public void changeView(ArrayList<HashMap<String, String>> newDate) {
        this.date = newDate;
    }
}

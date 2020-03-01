package com.tqz.tqz.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.tqz.tqz.Interface.ChangeListViewItemsCount;
import com.tqz.tqz.R;

public class MainLoadListView extends ListView implements AbsListView.OnScrollListener {
    // 底部加载提示布局
    private View footer;
    private View mMianTop;
    //总的Item数量
    private int totalItemCount;
    //可见Item的数量
    private int lastVisibleItem;
    //加载状态
    private boolean isLoading;
    public int maxItems=10;
    //定义一个
    ChangeListViewItemsCount changeListViewItemsCount;

    public MainLoadListView(Context context) {
        super(context);
        initView(context);
    }

    public MainLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MainLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    public void setMaxItems(int maxItems){
        this.maxItems=maxItems;
    }
    //添加底部加载提示布局到listView
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.footer_layout, null);
        mMianTop=inflater.inflate(R.layout.main_content_top,null);
        footer.findViewById(R.id.list_footer).setVisibility(View.GONE);
        footer.findViewById(R.id.list_over).setVisibility(View.GONE);
        this.setHeaderDividersEnabled(false);
        this.addHeaderView(mMianTop);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.totalItemCount = totalItemCount;
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //滚动到最后一项Item且滚动停止
        if (totalItemCount == lastVisibleItem && scrollState == SCROLL_STATE_IDLE) {
            //定义最多加载项数
            if (totalItemCount < maxItems && !isLoading) {
                footer.findViewById(R.id.list_footer).setVisibility(View.VISIBLE);
                //调用接口的实现方法
                changeListViewItemsCount.changeListViewItemsCount();
            } else {
                isLoading = false;
                //显示加载完毕
                footer.findViewById(R.id.list_footer).setVisibility(View.GONE);
                footer.findViewById(R.id.list_over).setVisibility(View.VISIBLE);
            }
        }
    }

    //设置此接口的实现类的实现方法
    public void setInterface(ChangeListViewItemsCount changeListViewItemsCount) {
        this.changeListViewItemsCount = changeListViewItemsCount;
    }

}

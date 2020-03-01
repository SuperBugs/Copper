package com.tqz.tqz.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tqz.tqz.Interface.ChangeListViewItemsCount;
import com.tqz.tqz.R;
import com.tqz.tqz.activity.YuiHuiQuanDetailesActivity;
import com.tqz.tqz.adapter.YuHuiQuanAdapter;
import com.tqz.tqz.common.CommonFunction;
import com.tqz.tqz.post.YuiHuiQuanListViewDate;
import com.tqz.tqz.view.LoadListView;
import com.tqz.tqz.view.SildingMenu;

import java.util.ArrayList;
import java.util.HashMap;

public class GouWuQuanFragment extends Fragment implements ChangeListViewItemsCount, SwipeRefreshLayout.OnRefreshListener {
    private int mType;
    private LoadListView listView;
    private Activity activity;
    private ArrayList<HashMap<String, String>> listItems;
    //MainListView的mainListViewAdapter
    private YuHuiQuanAdapter mYuiHuiQuanAdapter;
    private CommonFunction commonFunction;
    //初始化listview数据
    public static ArrayList<HashMap<String, String>> listViewdate;
    //获取listview数据的对象
    private YuiHuiQuanListViewDate yuiHuiQuanListViewDate;
    private Handler changeListViewHandler;
    private String mYuHuiQuanType;
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.fragment_yui_hui_quan, container, false);
        activity = getActivity();
        listView = (LoadListView) vi.findViewById(R.id.yuihuiquan_list_view);
        mSwipeLayout = (SwipeRefreshLayout) vi.findViewById(R.id.swipe_container);
        initHandler();
        initObj();
        getData();
        initListView();
        getInternetDate();
        setListener();
        setRefresh();
        return vi;
    }
    private void setRefresh() {
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setDistanceToTriggerSync(300);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_bright, android.R.color.holo_blue_bright, android.R.color.holo_blue_bright);
    }
    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                if (listViewdate == null) {
                    Toast toast = Toast.makeText(activity, getString(R.string.
                            toast_data_loading), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    hashMap = listViewdate.get(position);
                    Intent intent = new Intent(activity, YuiHuiQuanDetailesActivity.class);
                    intent.putExtra("YuHuiQuanImageUrl", hashMap.get("YuHuiQuanImageUrl"));
                    intent.putExtra("YuHuiQuanName", hashMap.get("YuHuiQuanName"));
                    intent.putExtra("YuHuiQuanIncome", hashMap.get("YuHuiQuanIncome"));
                    intent.putExtra("YuHuiQuanType", hashMap.get("YuHuiQuanType"));
                    intent.putExtra("YuHuiQuanDate", hashMap.get("YuHuiQuanDate"));
                    intent.putExtra("YuHuiQuanMoney", hashMap.get("YuHuiQuanMoney"));
                    intent.putExtra("YuHuiQuanAddressUrl", hashMap.get("YuHuiQuanAddressUrl"));
                    intent.putExtra("YuHuiQuanProcessOneImageUrl", hashMap.get("YuHuiQuanProcessOneImageUrl"));
                    intent.putExtra("YuHuiQuanProcessOneText", hashMap.get("YuHuiQuanProcessOneText"));
                    intent.putExtra("YuHuiQuanProcessTwoImageUrl", hashMap.get("YuHuiQuanProcessTwoImageUrl"));
                    intent.putExtra("YuHuiQuanProcessTwoText", hashMap.get("YuHuiQuanProcessTwoText"));
                    intent.putExtra("YuHuiQuanProcessThreeImageUrl", hashMap.get("YuHuiQuanProcessThreeImageUrl"));
                    intent.putExtra("YuHuiQuanProcessThreeText", hashMap.get("YuHuiQuanProcessThreeText"));
                    intent.putExtra("YuHuiQuanProcessFourImageUrl", hashMap.get("YuHuiQuanProcessFourImageUrl"));
                    intent.putExtra("YuHuiQuanProcessFourText", hashMap.get("YuHuiQuanProcessFourText"));
                    intent.putExtra("YuHuiQuanProcessFiveImageUrl", hashMap.get("YuHuiQuanProcessFiveImageUrl"));
                    intent.putExtra("YuHuiQuanProcessFiveText", hashMap.get("YuHuiQuanProcessFiveText"));
                    if (!hashMap.get("YuHuiQuanName").equals("")) {
                        activity.startActivity(intent);
                    }
                }

            }
        });
    }

    private void initObj() {
        yuiHuiQuanListViewDate = new YuiHuiQuanListViewDate();
    }

    private void getInternetDate() {
        //判断是否有网络连接，有则获取数据
        commonFunction = new CommonFunction();
        //将本地保存的ListView数据传递给ListBaseAdapter
        listViewdate = (ArrayList<HashMap<String, String>>) commonFunction.
                getDate(mYuHuiQuanType, activity);
        if (listViewdate != null) {
            Message message2 = new Message();
            message2.what = 1;
            changeListViewHandler.sendMessage(message2);
        }
        if (commonFunction.IsInternet(activity)) {
            //开启子线程获取listView网络数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    listViewdate = yuiHuiQuanListViewDate.getDate(mType);
                    if (listViewdate == null) {
                        //发送数据获取失败信息，系统错误，通知用户
                        Message message = new Message();
                        message.what = 3;
                        changeListViewHandler.sendMessage(message);
                        //将本地保存的ListView数据传递给ListBaseAdapter
                        listViewdate = (ArrayList<HashMap<String, String>>) commonFunction.
                                getDate(mYuHuiQuanType, activity);
                    } else {
                        mYuiHuiQuanAdapter = new YuHuiQuanAdapter(activity, listViewdate, 7);
                        //保存数据
                        commonFunction.putDate(mYuHuiQuanType, listViewdate, activity);
                    }
                    //更新listView
                    Message message = new Message();
                    message.what = 1;
                    changeListViewHandler.sendMessage(message);
                }
            }).start();
        } else {
            Message message = new Message();
            message.what = 2;
            changeListViewHandler.sendMessage(message);
        }
    }

    private void getData() {
        mType = 4;
        mYuHuiQuanType = "GouWuQuanData";
    }

    //初始化listView
    public void initListView() {
        //设置接口方法
        listView.setInterface(this);
        listItems = new ArrayList<HashMap<String, String>>();
        listItems = null;
        //创建一个自定义的baseAdapter
        mYuiHuiQuanAdapter = new YuHuiQuanAdapter(activity, listItems, 7);
        listView.setAdapter(mYuiHuiQuanAdapter);
        //为mainListView的item设置点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //第positison被点击时调用此方法
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (SildingMenu.is_left_mueu_open) {
                    switch (position) {
                        case 0:
                            break;
                    }
                }
            }
        });
    }

    //listView实现的接口方法
    @Override
    public void changeListViewItemsCount() {
        //延时1秒加载更多Items
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mYuiHuiQuanAdapter.addItems();
            }
        }, 1000);
    }

    private void initHandler() {
        //接受listView数据获取状态，1为成功获得数据更新ListView，2为失败提示用户网络问题
        changeListViewHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mSwipeLayout.setRefreshing(false);
                switch (msg.what) {
                    case 1:
                        mYuiHuiQuanAdapter = new YuHuiQuanAdapter(activity, listViewdate, 7);
                        if (listViewdate != null) {
                            listView.setMaxItems(listViewdate.size());//设置要显示的Items数量
                            listView.setAdapter(mYuiHuiQuanAdapter);
                        }
                        break;
                    case 2:
                        Toast toast = Toast.makeText(activity, getString(R.string.
                                toast_internet_wrong), Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    case 3:
                        Toast toast2 = Toast.makeText(activity, getString(R.string.
                                toast_system_wrong), Toast.LENGTH_SHORT);
                        toast2.show();
                }
            }
        };
    }

    /*
    * 监听器SwipeRefreshLayout.OnRefreshListener中的方法，当下拉刷新后触发
    */
    public void onRefresh() {
        getInternetDate();
    }


}

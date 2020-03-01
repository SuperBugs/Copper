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
import com.tqz.tqz.activity.AppDetailesActivity;
import com.tqz.tqz.adapter.MainListBaseAdapter;
import com.tqz.tqz.common.CommonFunction;
import com.tqz.tqz.post.MainListViewDate;
import com.tqz.tqz.view.LoadListView;
import com.tqz.tqz.view.SildingMenu;

import java.util.ArrayList;
import java.util.HashMap;

public class NewAppFragment extends Fragment implements ChangeListViewItemsCount,SwipeRefreshLayout.OnRefreshListener {
    private LoadListView listView;
    private View vi;
    private Activity activity;
    //MainListView的mainListViewAdapter
    private MainListBaseAdapter mainListViewAdapter;
    private CommonFunction commonFunction;
    //初始化listview数据
    public static ArrayList<HashMap<String, String>> listViewdate;
    //获取listview数据的对象
    private MainListViewDate listHashMapDate;
    private Handler changeListViewHandler;
    private SwipeRefreshLayout mSwipeLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this.getActivity();
        //创建对象
        initObj();

    }

    private void initObj() {
        listHashMapDate = new MainListViewDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //获取fragment要设置的布局界面
        vi = inflater.inflate(R.layout.f_new_task, container, false);
        initHandler();
        findById();
        setRefresh();
        //初始化listview
        initListView();
        //获取网络数据
        getInternetDate();
        setListener();
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
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
                    Intent intent = new Intent(activity, AppDetailesActivity.class);
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
                    if (!hashMap.get("TaskName").equals("")) {
                        activity.startActivity(intent);
                    }
                }
            }
        });

    }

    private void initHandler() {
        //接受listView数据获取状态，1为成功获得数据更新ListView，2为失败提示用户网络问题
        changeListViewHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mSwipeLayout.setRefreshing(false);
                switch (msg.what) {
                    case 1:
                        mainListViewAdapter = new MainListBaseAdapter(activity, listViewdate, 10);
                        listView.setMaxItems(listViewdate.size());//设置要显示的Items数量
                        listView.setAdapter(mainListViewAdapter);
                        break;
                    case 2:
                        Toast toast = Toast.makeText(activity, getString(R.string.
                                toast_system_wrong), Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                }
            }
        };
    }

    private void findById() {
        mSwipeLayout = (SwipeRefreshLayout) vi.findViewById(R.id.swipe_container);
        listView = (LoadListView) vi.findViewById(R.id.new_list_view);
    }

    //初始化listView
    public void initListView() {
        //设置接口方法
        listView.setInterface(this);
        ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
        listItems = null;
        //创建一个自定义的baseAdapter
        mainListViewAdapter = new MainListBaseAdapter(activity, listItems, 10);
        listView.setAdapter(mainListViewAdapter);
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

    private void getInternetDate() {
        //判断是否有网络连接，有则获取数据
        commonFunction = new CommonFunction();
        //将本地保存的ListView数据传递给ListBaseAdapter
        listViewdate = (ArrayList<HashMap<String, String>>) commonFunction.
                getDate("NewAppListViewData", activity);
        if (listViewdate != null) {
            mainListViewAdapter.changeView(listViewdate);
            mainListViewAdapter.notifyDataSetChanged();
        }
        if (commonFunction.IsInternet(activity)) {
            //开启子线程获取listView网络数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    listViewdate = listHashMapDate.getDate();
                    if (listViewdate == null) {
                        //发送数据获取失败信息，系统错误，通知用户
                        Message message = new Message();
                        message.what = 2;
                        changeListViewHandler.sendMessage(message);
                        //将本地保存的ListView数据传递给ListBaseAdapter
                        listViewdate = (ArrayList<HashMap<String, String>>) commonFunction.
                                getDate("NewAppListViewData", activity);
                        if (listViewdate != null) {
                            mainListViewAdapter = new MainListBaseAdapter(activity, listViewdate, 10);
                        }
                    } else {
                        mainListViewAdapter = new MainListBaseAdapter(activity, listViewdate, 10);
                        //保存数据
                        commonFunction.putDate("NewAppListViewData", listViewdate, activity);
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

    //listView实现的接口方法
    @Override
    public void changeListViewItemsCount() {
        //延时1秒加载更多Items
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //调用MainListBaseAdapter的addItems方法加载更多项数
                mainListViewAdapter.addItems();
            }
        }, 1000);
    }
    /*
    * 监听器SwipeRefreshLayout.OnRefreshListener中的方法，当下拉刷新后触发
    */
    public void onRefresh() {
        getInternetDate();
    }
}


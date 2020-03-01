package com.tqz.tqz.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tqz.tqz.Interface.ChangeListViewItemsCount;
import com.tqz.tqz.R;
import com.tqz.tqz.activity.AbroadTaskActivity;
import com.tqz.tqz.activity.AppDetailesActivity;
import com.tqz.tqz.activity.ChinaTaskActivity;
import com.tqz.tqz.activity.NewTaskActivity;
import com.tqz.tqz.activity.YuiHuiQuanActivity;
import com.tqz.tqz.adapter.MainListBaseAdapter;
import com.tqz.tqz.adapter.MainViewPagerAdapter;
import com.tqz.tqz.broadcast.NetWork;
import com.tqz.tqz.common.CommonFunction;
import com.tqz.tqz.post.MainListViewDate;
import com.tqz.tqz.post.MainViewPagerDate;
import com.tqz.tqz.view.MainLoadListView;
import com.tqz.tqz.view.SildingMenu;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 内容：listView，viewpager的滑动广告条
 * ListView实现方式：继承ListView自定义控件LoadListView，在fragment布局中使用,
 * 传入一个无数据初始化的自定义的MainListBaseAdapter，有网络时开启子线程获取网络数据，将数据写入先前的
 * 自定义adapter，并发送一个Handler消息，主线程收到消息后调用notifyDataSetChanged()方法
 * 刷新listView数据(使用Handler是因为子线程无法更新UI)
 * ViewPager实现方式:继承ViewPager自定义控件MyViewPager，在fragment布局中使用，
 * 传入一个无数据的的自定义的MainViewPagerAdapter，有网络时开启子线程获取网络数据，将数据写入先前的
 * 自定义adapter，发送Handler消息，主线程收到消息后调用notifyDataSetChanged()方法
 * 刷新ViewPager数据(使用Handler是因为子线程无法更新UI)
 */
public class MainFragment extends Fragment implements ChangeListViewItemsCount, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainFragment";
    //当前fragment所处的activity
    private Activity activity;
    //MainListView的mainListViewAdapter
    private MainListBaseAdapter mainListViewAdapter;
    //MainListView的mainListViewAdapter
    private MainViewPagerAdapter mainViewPagerAdapter;
    //网络广播器
    private NetWork netReceiver;
    //获取listview数据的对象
    private MainListViewDate listHashMapDate;
    //初始化listview数据
    public static ArrayList<HashMap<String, String>> listViewdate;
    //viewPager的数据
    private MainViewPagerDate viewPagerHashMapDate;
    //初始化viewPager数据
    private static ArrayList<HashMap<String, String>> viewPagerDate;
    //自定义的ListView
    private MainLoadListView list;
    private Intent yuiHuiIntent;
    //广告条
    private ViewPager viewPager;
    //广告引导小点布局
    private LinearLayout pointGroup;
    private ImageView mHongBaoIv;
    private ImageView mLiCaiIv;
    private ImageView mLiuLiangIv;
    private ImageView mHuaFeiIv;
    private ImageView mWaiMaiIv;
    private ImageView mGouWuIv;
    private ImageView mDaCheIv;
    private ImageView mDianYinIv;
    private ImageView mChinaIv;
    private ImageView mAbroadIv;
    private ImageView mNewIv;
    private Handler changeListViewHandler;
    private Handler changeViewPagerHandler;
    public static Handler changeViewPagerPageHandler;
    private CommonFunction commonFunction;
    View mainView;
    private Thread mThreadViewPager;
    private Thread mThreadListView;
    //当前viewPager的位置
    private int postion;
    //上一个小点的位置
    private int lastPosition = 2;
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this.getActivity();
        //创建对象
        initObj();
        //让viewPager自动滑动
        makeViewPagerAutoSlid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //获取fragment要设置的布局界面
        mainView = inflater.inflate(R.layout.f_main_layout, container, false);
        initHandler();
        //获取控件
        findById();
        //初始化ListView
        initListView();
        //初始化viewPager
        initViewPager();
        //加载所需的网络数据
        getInternetDate();
        initListener();
        setRefresh();
        setView();
        //mSwipeLayout.post(new Runnable() {
        //    @Override
        //    public void run() {
        //        mSwipeLayout.setRefreshing(true);
         //   }
        //});
        return mainView;
    }

    private void setRefresh() {
        mSwipeLayout.setOnRefreshListener(this);
//        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
//        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(300);// 设置手指在屏幕下拉多少距离会触发下拉刷新
//        mSwipeLayout.setProgressBackgroundColor(R.color.red); // 设定下拉圆圈的背景
//        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_bright, android.R.color.holo_blue_bright, android.R.color.holo_blue_bright);
        //设置下拉刷新监听
    }

    private void setView() {
        mLiuLiangIv.setImageBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.liuliang));
        mHuaFeiIv.setImageBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.huafei));
        mWaiMaiIv.setImageBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.waimai));
        mGouWuIv.setImageBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.gouwu));
        mDaCheIv.setImageBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.dache));
        mDianYinIv.setImageBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.dianyin));
        mLiCaiIv.setImageBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.licai));
        mHongBaoIv.setImageBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.hongbao));
    }

    private void initListener() {
        mChinaIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChinaTaskActivity.class);
                startActivity(intent);
            }
        });
        mAbroadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AbroadTaskActivity.class);
                startActivity(intent);
            }
        });
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        mNewIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewTaskActivity.class);
                startActivity(intent);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                if (listViewdate == null) {
                    Toast toast = Toast.makeText(activity, getString(R.string.
                            toast_data_loading), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    hashMap = listViewdate.get(position - 1);
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
                    activity.startActivity(intent);
                }
            }
        });
        yuiHuiIntent = new Intent(activity, YuiHuiQuanActivity.class);
        mLiuLiangIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuiHuiIntent.putExtra("Type", "1");
                activity.startActivity(yuiHuiIntent);
            }
        });
        mHuaFeiIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuiHuiIntent.putExtra("Type", "2");
                activity.startActivity(yuiHuiIntent);
            }
        });
        mWaiMaiIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuiHuiIntent.putExtra("Type", "3");
                activity.startActivity(yuiHuiIntent);
            }
        });
        mGouWuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuiHuiIntent.putExtra("Type", "4");
                activity.startActivity(yuiHuiIntent);
            }
        });
        mDaCheIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuiHuiIntent.putExtra("Type", "5");
                activity.startActivity(yuiHuiIntent);
            }
        });
        mDianYinIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuiHuiIntent.putExtra("Type", "6");
                activity.startActivity(yuiHuiIntent);
            }
        });
        mLiCaiIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,ChinaTaskActivity.class);
                startActivity(intent);
            }
        });
        mHongBaoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,ChinaTaskActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getInternetDate() {

        //判断是否有网络连接，有则获取数据
        commonFunction = new CommonFunction();
        //将本地保存的ListView数据传递给ListBaseAdapter
        //将本地保存的ViewPager数据传递给viewPagerAdapter
        listViewdate = (ArrayList<HashMap<String, String>>) commonFunction.
                getDate("MainListViewDate", activity);
        if (listViewdate != null) {
            mainListViewAdapter = new MainListBaseAdapter(activity, listViewdate, 5);
            list.setMaxItems(listViewdate.size());//设置要显示的Items数量
            list.setAdapter(mainListViewAdapter);
        }
        viewPagerDate = (ArrayList<HashMap<String, String>>) commonFunction.
                getDate("MainViewPagerDate", activity);
        if (viewPagerDate != null) {
            mainViewPagerAdapter = new MainViewPagerAdapter(activity, listViewdate);
            Message message2 = new Message();
            message2.what = 1;
            changeViewPagerHandler.sendMessage(message2);
        }
        if (commonFunction.IsInternet(activity)) {
            //开启子线程获取listView网络数据
            mThreadListView = new Thread(new Runnable() {
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
                                getDate("MainListViewDate", activity);
                        if (listViewdate != null) {
                            mainListViewAdapter = new MainListBaseAdapter(activity, listViewdate, 5);
                        }
                    } else {
                        mainListViewAdapter = new MainListBaseAdapter(activity, listViewdate, 5);
                        //保存数据
                        commonFunction.putDate("MainListViewDate", listViewdate, activity);
                    }
                    //更新listView
                    Message message = new Message();
                    message.what = 1;
                    changeListViewHandler.sendMessage(message);
                }
            });
            mThreadListView.start();
            //开启子线程获取ViewPager网络数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    viewPagerDate = viewPagerHashMapDate.getDate(activity);
                    if (viewPagerDate != null) {
                        //将数据传递给viewPagerAdapter
                        mainViewPagerAdapter = new MainViewPagerAdapter(activity, viewPagerDate);
                        //保存数据
                        commonFunction.putDate("MainViewPagerDate", viewPagerDate, activity);
                    } else {
                        //将本地保存的ViewPager数据传递给viewPagerAdapter
                        viewPagerDate = (ArrayList<HashMap<String, String>>) commonFunction.
                                getDate("MainViewPagerDate", activity);
                        if (viewPagerDate != null) {
                            mainViewPagerAdapter = new MainViewPagerAdapter(activity, viewPagerDate);
                        }
                    }
                    Message message4 = new Message();
                    message4.what = 1;
                    changeViewPagerHandler.sendMessage(message4);
                }
            }).start();

        } else {

            //发送数据获网络失败信息，通知用户
            Message message = new Message();
            message.what = 2;
            changeViewPagerHandler.sendMessage(message);

        }
    }


    private void findById() {
        mSwipeLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_container);
        viewPager = (ViewPager) mainView.findViewById(R.id.view_pager_main);
        list = (MainLoadListView) mainView.findViewById(R.id.main_list_view);
        pointGroup = (LinearLayout) mainView.findViewById(R.id.point_group);
        mLiuLiangIv = (ImageView) mainView.findViewById(R.id.liuliangquan_IB);
        mHuaFeiIv = (ImageView) mainView.findViewById(R.id.huafeiquan_IB);
        mWaiMaiIv = (ImageView) mainView.findViewById(R.id.waimaiquan_IB);
        mGouWuIv = (ImageView) mainView.findViewById(R.id.gouwuquan_IB);
        mDaCheIv = (ImageView) mainView.findViewById(R.id.dache_IB);
        mDianYinIv = (ImageView) mainView.findViewById(R.id.dianying_IB);
        mLiCaiIv = (ImageView) mainView.findViewById(R.id.licai_IB);
        mHongBaoIv = (ImageView) mainView.findViewById(R.id.hongbao_IB);
        mAbroadIv = (ImageView) mainView.findViewById(R.id.main_btn_three);
        mChinaIv = (ImageView) mainView.findViewById(R.id.main_btn_one);
        mNewIv = (ImageView) mainView.findViewById(R.id.main_btn_two);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(activity, getString(R.string.
                        toast_system_wrong), Toast.LENGTH_SHORT);
                toast.show();
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
                        mainListViewAdapter = new MainListBaseAdapter(activity, listViewdate, 5);
                        if (listViewdate != null) {
                            list.setMaxItems(listViewdate.size());//设置要显示的Items数量
                        }
                        list.setAdapter(mainListViewAdapter);
                        mainListViewAdapter.notifyDataSetChanged();
                        mSwipeLayout.setRefreshing(false);
                        break;
                    case 2:
                        Toast toast = Toast.makeText(activity, getString(R.string.
                                toast_internet_wrong), Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                }
            }
        };
        //接受viewPager数据获取状态，1为成功获得数据更新viewPager，2为失败提示用户网络问题
        changeViewPagerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mSwipeLayout.setRefreshing(false);
                switch (msg.what) {
                    case 1:
                        mainViewPagerAdapter = new MainViewPagerAdapter(activity, viewPagerDate);
                        viewPager.setAdapter(mainViewPagerAdapter);
                        break;
                    case 2:
                        Toast myToasthave = Toast.makeText(activity, getString(R.string.
                                toast_internet_wrong), Toast.LENGTH_SHORT);
                        myToasthave.show();
                        break;
                }
            }
        };

    }

    //初始化ViewPager
    private void initViewPager() {
        ArrayList<HashMap<String, String>> viewPagerItems = new ArrayList<HashMap<String, String>>();
        viewPagerItems = null;
        //创建一个自定义的ViewPagerAdapter
        mainViewPagerAdapter = new MainViewPagerAdapter(activity, viewPagerItems);
        viewPager.setAdapter(mainViewPagerAdapter);
        //添加小点
        for (int i = 0; i < 5; i++) {
            ImageView point = new ImageView(activity);
            point.setBackgroundResource(R.drawable.point_bg);
            //设置圆点填充布局
            LinearLayout.LayoutParams params = new LinearLayout.
                    LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //设置左右边距为5像素
            params.rightMargin = 10;
            params.leftMargin = 10;
            point.setLayoutParams(params);
            point.setEnabled(false);
            pointGroup.addView(point);
        }
        pointGroup.getChildAt(2).setEnabled(true);
        //为viewPager设置监听事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滑动时调用
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //页面切换后调用
            @Override
            public void onPageSelected(int position) {
                postion = position;
                //改变原点状态
                if (0 < position && position < 6) {
                    pointGroup.getChildAt(position-1).setEnabled(true);
                    pointGroup.getChildAt(lastPosition).setEnabled(false);
                    //保存当前圆点状态
                    lastPosition = position-1;
                }
                if(position==0&&lastPosition==0){
                    pointGroup.getChildAt(0).setEnabled(false);
                    lastPosition=4;
                    viewPager.setCurrentItem(5);
                    pointGroup.getChildAt(4).setEnabled(true);
                }

            }

            //滑动时调用
            @Override
            public void onPageScrollStateChanged(int state) {
                if (postion == 6) {
                    viewPager.setCurrentItem(1, false);
                } else if (postion == 0) {
                    viewPager.setCurrentItem(6, false);
                }
            }
        });
        //设置viewPager的7个界面不会被销毁
        viewPager.setOffscreenPageLimit(7);
        //设置当前是第几张图片
        viewPager.setCurrentItem(4, true);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    //初始化listView
    public void initListView() {
        //设置接口方法
        list.setInterface(this);
        ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
        listItems = null;
        //创建一个自定义的baseAdapter
        mainListViewAdapter = new MainListBaseAdapter(activity, listItems, 5);
        list.setAdapter(mainListViewAdapter);
        //为mainListView的item设置点击事件
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                //调用MainListBaseAdapter的addItems方法加载更多项数
                mainListViewAdapter.addItems();
            }
        }, 700);
    }

    //网络监听广播
    private void initReceiver() {
        netReceiver = new NetWork() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getIntExtra(NetWork.NET_TYPE, 0) == 4) {
                    //无网络链接，执行：提醒用户网络不可用
                    Message message = new Message();
                    message.what = 2;
                    changeViewPagerHandler.sendMessage(message);
                } else {
                    //4表示有网络，执行：
                    //获取ListView数据，刷新ListView
                    mainListViewAdapter = new MainListBaseAdapter(activity, listHashMapDate.getDate(), 5);
                    mainListViewAdapter.notifyDataSetChanged();
                    //获取ViewPager数据，刷新ViewPager
                    mainViewPagerAdapter = new MainViewPagerAdapter(activity, viewPagerHashMapDate.getDate(activity));
                    mainViewPagerAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void initObj() {
        listHashMapDate = new MainListViewDate();
        viewPagerHashMapDate = new MainViewPagerDate();
    }

    private void makeViewPagerAutoSlid() {
        //定时循环Handler，让广告条自动滑动
        changeViewPagerPageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                        //五秒后发送Handler消息
                        changeViewPagerPageHandler.sendEmptyMessageDelayed(0, 4000);
                        break;
                }
            }
        };
        //循环播放开始
        changeViewPagerPageHandler.sendEmptyMessageDelayed(0, 4000);
    }

    /*
     * 监听器SwipeRefreshLayout.OnRefreshListener中的方法，当下拉刷新后触发
     */
    public void onRefresh() {
        getInternetDate();
    }

}

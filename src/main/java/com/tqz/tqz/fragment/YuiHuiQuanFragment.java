package com.tqz.tqz.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tqz.tqz.R;
import com.tqz.tqz.adapter.YuiHuiQuanFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class YuiHuiQuanFragment extends Fragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.f_yuihuiquan_layout, container, false);
        mTabLayout = (TabLayout) vi.findViewById(R.id.tabs);
        mViewPager = (ViewPager) vi.findViewById(R.id.viewpager);
        initViewPager();
        return vi;
    }

    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("流量");
        titles.add("话费");
        titles.add("外卖");
        titles.add("购物");
        titles.add("打车");
        titles.add("电影");
        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LiuLianQuanFragment());
        fragments.add(new HuaFeiQuanFragment());
        fragments.add(new WaiMaiQuanFragment());
        fragments.add(new GouWuQuanFragment());
        fragments.add(new DaCheQuanFragment());
        fragments.add(new DianYingQuanFragment());
        YuiHuiQuanFragmentAdapter mFragmentAdapteradapter =
                new YuiHuiQuanFragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mFragmentAdapteradapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        //给TabLayout设置适配器
        mTabLayout.setTabsFromPagerAdapter(mFragmentAdapteradapter);
    }
}

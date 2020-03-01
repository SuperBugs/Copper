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

public class MakeMoneyFragment extends Fragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.f_makemoney_layout, container, false);
        mTabLayout = (TabLayout) vi.findViewById(R.id.make_money_tabs);
        mViewPager = (ViewPager) vi.findViewById(R.id.make_money_viewpager);
        initViewPager();
        return vi;
    }

    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("国内应用");
        titles.add("国内活动");
        titles.add("国外应用");
        titles.add("国外活动");

        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ChinaAppFragment());
        fragments.add(new ChinaTaskFragment());
        fragments.add(new AbroadAppFragment());
        fragments.add(new AbroadTaskFragment());
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

package com.tqz.tqz.post;


import android.app.Activity;

import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.httputil.MyHttpUrlConnectionGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//从服务器获取mainViewPagerView要展示的数据
public class MainViewPagerDate {
    private ArrayList<HashMap<String, String>> MainViewPagerData;
    private ImageLoader imageLoader = new ImageLoader();
    private Activity activity;
    public ArrayList<HashMap<String, String>> getDate(Activity activity) {
        this.activity = activity;
        //注入缓存实现
        imageLoader = new ImageLoader();
        requestDate();
        return MainViewPagerData;

    }

    private void requestDate() {
        CommonConfig config = new CommonConfig();
        String MainListViewAddress = config.getUrlMainViewPagerDateFunction();
        //向服务器发送请求
        String response = MyHttpUrlConnectionGet.sendHttpRequest(MainListViewAddress);
        if (response == null) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonList = jsonObject.getJSONArray("MainViewpagerData");
            ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < jsonList.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("TaskImageUrl", jsonList.getJSONObject(i).getString("imageurl"));
                map.put("TaskName", jsonList.getJSONObject(i).getString("name"));
                map.put("TaskIncome", jsonList.getJSONObject(i).getString("income"));
                map.put("TaskType", jsonList.getJSONObject(i).getString("type"));
                map.put("TaskDate", jsonList.getJSONObject(i).getString("date"));
                map.put("TaskAddressUrl", jsonList.getJSONObject(i).getString("addressurl"));
                map.put("TaskMoney", jsonList.getJSONObject(i).getString("money"));
                map.put("TaskShareNumber", jsonList.getJSONObject(i).getString("sharenumber"));

                map.put("AppProcessOneImageUrl", jsonList.getJSONObject(i).getString("imageoneurl"));
                map.put("AppProcessOneText", jsonList.getJSONObject(i).getString("stepone"));
                map.put("AppProcessTwoImageUrl", jsonList.getJSONObject(i).getString("imagetwourl"));
                map.put("AppProcessTwoText", jsonList.getJSONObject(i).getString("steptwo"));
                map.put("AppProcessThreeImageUrl", jsonList.getJSONObject(i).getString("imagethreeurl"));
                map.put("AppProcessThreeText", jsonList.getJSONObject(i).getString("stepthree"));
                map.put("AppProcessFourImageUrl", jsonList.getJSONObject(i).getString("imagefoururl"));
                map.put("AppProcessFourText", jsonList.getJSONObject(i).getString("stepfour"));
                map.put("AppProcessFiveImageUrl", jsonList.getJSONObject(i).getString("imagefiveurl"));
                map.put("AppProcessFiveText", jsonList.getJSONObject(i).getString("stepfive"));
                listItems.add(map);
            }
            MainViewPagerData = listItems;
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}

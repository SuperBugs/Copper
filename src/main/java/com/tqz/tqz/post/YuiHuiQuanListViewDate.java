package com.tqz.tqz.post;


import android.util.Log;

import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.httputil.MyHttpUrlConnectionGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//从服务器获取mainFragmentListView要展示的数据
public class YuiHuiQuanListViewDate {
    ArrayList<HashMap<String, String>> date = null;
    private String address;
    //服务器优惠券数据类型
    private int type = 1;
    private int mProcessCount = 0;
    private static final String TAG = "YuiHuiQuanListViewDate";
    public ArrayList<HashMap<String, String>> getDate(int type) {
        this.type = type;
        requestDate();
        return date;
    }

    private void requestDate() {

        /**
         *拼装ChainListViewAddress：http://url/index.php?c=LiuLiangQuanData&f=get
         */
        CommonConfig config = new CommonConfig();
        switch (type) {
            case 1:
                address = config.getUrlLiuLiangQuanDataFunction();
                break;
            case 2:
                address = config.getUrlHuaFeiQuanDataFunction();
                break;
            case 3:
                address = config.getUrlWaiMaiQuanDataFunction();
                break;
            case 4:
                address = config.getUrlGouWuQuanDataFunction();
                break;
            case 5:
                address = config.getUrlDaCheQuanDataFunction();
                break;
            case 6:
                address = config.getUrlDianYingQuanDataFunction();
                break;
            case 7:
                address = config.getUrlLiCaiQuanDataFunction();
                break;
            case 8:
                address = config.getUrlHongBaoQuanDataFunction();
                break;
        }
        //向服务器发送请求
        String response = MyHttpUrlConnectionGet.sendHttpRequest(address);
        Log.d(TAG, "链接"+response);
        if (response == null) {
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonList = jsonObject.getJSONArray("QuanData");
                ArrayList<HashMap<String, String>> mlistItems = new ArrayList<HashMap<String, String>>();
                for (int i = 0; i < jsonList.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("YuHuiQuanImageUrl", jsonList.getJSONObject(i).getString("imageurl"));
                    map.put("YuHuiQuanName", jsonList.getJSONObject(i).getString("name"));
                    map.put("YuHuiQuanIncome", jsonList.getJSONObject(i).getString("income"));
                    map.put("YuHuiQuanType", jsonList.getJSONObject(i).getString("type"));
                    map.put("YuHuiQuanDate", jsonList.getJSONObject(i).getString("date"));
                    map.put("YuHuiQuanAddressUrl", jsonList.getJSONObject(i).getString("addressurl"));
                    map.put("YuHuiQuanMoney", jsonList.getJSONObject(i).getString("money"));
                    map.put("YuHuiQuanProcessOneImageUrl", jsonList.getJSONObject(i).getString("imageoneurl"));
                    map.put("YuHuiQuanProcessOneText", jsonList.getJSONObject(i).getString("stepone"));
                    map.put("YuHuiQuanProcessTwoImageUrl", jsonList.getJSONObject(i).getString("imagetwourl"));
                    map.put("YuHuiQuanProcessTwoText", jsonList.getJSONObject(i).getString("steptwo"));
                    map.put("YuHuiQuanProcessThreeImageUrl", jsonList.getJSONObject(i).getString("imagethreeurl"));
                    map.put("YuHuiQuanProcessThreeText", jsonList.getJSONObject(i).getString("stepthree"));
                    map.put("YuHuiQuanProcessFourImageUrl", jsonList.getJSONObject(i).getString("imagefoururl"));
                    map.put("YuHuiQuanProcessFourText", jsonList.getJSONObject(i).getString("stepfour"));
                    map.put("YuHuiQuanProcessFiveImageUrl", jsonList.getJSONObject(i).getString("imagefiveurl"));
                    map.put("YuHuiQuanProcessFiveText", jsonList.getJSONObject(i).getString("stepfive"));
                    mlistItems.add(map);
                }
                date = mlistItems;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}

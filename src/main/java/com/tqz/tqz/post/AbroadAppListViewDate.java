package com.tqz.tqz.post;


import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.httputil.MyHttpUrlConnectionGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//从服务器获取mainFragmentListView要展示的数据
public class AbroadAppListViewDate {
    ArrayList<HashMap<String, String>> date = null;
    public ArrayList<HashMap<String, String>> getDate() {
        requestDate();
        return date;
    }

    private void requestDate() {
        /**
         *拼装ChainListViewAddress：http://url/index.php?c=AbroadAppListData&f=get
         */
        CommonConfig config = new CommonConfig();
        String AbroadAppListViewAddress = config.getUrlAbroadAppListViewDateFunction();
        //向服务器发送请求
        String response = MyHttpUrlConnectionGet.sendHttpRequest(AbroadAppListViewAddress);
        if (response == null) {
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonList = jsonObject.getJSONArray("AbroadAppListViewData");
                ArrayList<HashMap<String, String>> mlistItems = new ArrayList<HashMap<String, String>>();
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
                    mlistItems.add(map);
                }
                date = mlistItems;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}

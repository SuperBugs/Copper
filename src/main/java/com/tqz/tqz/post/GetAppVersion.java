package com.tqz.tqz.post;


import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.httputil.MyHttpUrlConnectionGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

//从服务器获取mainFragmentListView要展示的数据
public class GetAppVersion {
    private HashMap<String,String> serviceData;

    public HashMap<String,String> getData() {
        get();
        return serviceData;
    }

    private void get() {
        /**
         * 获取服务器上软件数据
         * 拼装http://url/index.php?c=App&f=getVersion
         */
        CommonConfig config = new CommonConfig();
        String shareAddress = config.getUrlAppVersionFunction();
        //向服务器发送请求
        String response = MyHttpUrlConnectionGet.sendHttpRequest(shareAddress);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonList = jsonObject.getJSONArray("result");
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("AppVersion", jsonList.getJSONObject(0).getString("appversion"));
            map.put("AppUrl", jsonList.getJSONObject(0).getString("appurl"));
            map.put("AppForceUpdate", jsonList.getJSONObject(0).getString("forceupdate"));
            map.put("AppNewVersionDetailes", jsonList.getJSONObject(0).getString("detailes"));
            serviceData=map;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

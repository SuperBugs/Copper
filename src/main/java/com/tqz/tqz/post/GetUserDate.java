package com.tqz.tqz.post;


import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.httputil.MyHttpUrlConnectionGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//从服务器获取用户数据
public class GetUserDate {
    private ArrayList<HashMap<String, String>> date = null;
    private String userPhoneNumber;
    private String userPassword;
    public ArrayList<HashMap<String, String>> getDate(String userPhoneNumber, String password) {
        this.userPhoneNumber = userPhoneNumber;
        this.userPassword = password;
        requestDate();
        return date;
    }

    private void requestDate() {

        CommonConfig config = new CommonConfig();
        String userDateUrl = config.getUrlgetUserDateFunction() + config.getUrlInsert()
                + config.getUrlgetUserDateParam1()
                + userPhoneNumber + config.getUrlInsert()
                + config.getUrlgetUserDateParam2()
                + userPassword;
        /**
         * 获取用户数据
         * url：http://url/index.php?c=MainListData&f=get&password=1234567&account=123456789
         * 返回范例：{"userInformation":[{"id":"74","name":"小明","account":"12345678901",
         * "password":"123456789","income":"1000","head":"http://192.168.169
         * .100/think/www/Image/MainListView/1.jpg"}]}
         */
        //向服务器发送请求
        String response = MyHttpUrlConnectionGet.sendHttpRequest(userDateUrl);
        if (response == null) {
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonList = jsonObject.getJSONArray("userInformation");
                ArrayList<HashMap<String, String>> mlistItems = new ArrayList<HashMap<String, String>>();
                for (int i = 0; i < jsonList.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("UserNickName", jsonList.getJSONObject(i).getString("name"));
                    map.put("UserIncome", jsonList.getJSONObject(i).getString("income"));
                    map.put("UserHeadUrl", jsonList.getJSONObject(i).getString("head"));
                    map.put("UserAccount", jsonList.getJSONObject(i).getString("account"));
                    map.put("UserExperience", jsonList.getJSONObject(i).getString("experience"));
                    map.put("UserPassword", jsonList.getJSONObject(i).getString("password"));
                    mlistItems.add(map);
                }
                date = mlistItems;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}

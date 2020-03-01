package com.tqz.tqz.post;

import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.httputil.MyHttpUrlConnectionGet;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PostShareData {
    private String mName;
    private String mAddress;
    private String mShareNumber;
    private String mIntroduce;
    private boolean result;
    public boolean postShareData(String name, String address, String introduce, String shareNumber) {
        mName = name;
        mAddress = address;
        mShareNumber = shareNumber;
        mIntroduce = introduce;
        try {
            //对中文进行编码
            mAddress = URLEncoder.encode(address,"utf-8");
            mIntroduce =URLEncoder.encode(introduce,"utf-8");
            mName = URLEncoder.encode(name,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post();
        return result;
    }

    private void post() {
        /**
         * 提交用户分享
         * url:http://url/index.php?c=FeedBack&f=shareData&name=百度钱包&address=www.baidu.com&
         * shareNumber=1245&introduce=一个赚钱任务
         */
        CommonConfig config = new CommonConfig();
        String shareAddress =
                config.getUrlShareDataFunction() +
                        config.getUrlInsert() +
                        config.getUrlShareDataParam1() +
                        mName +
                        config.getUrlInsert() +
                        config.getUrlShareDataParam2() +
                        mAddress +
                        config.getUrlInsert() +
                        config.getUrlShareDataParam3() +
                        mShareNumber +
                        config.getUrlInsert() +
                        config.getUrlShareDataParam4() +
                        mIntroduce;
        //向服务器发送请求
        String response = MyHttpUrlConnectionGet.sendHttpRequest(shareAddress);
        try {
            JSONObject json = new JSONObject(response);
            String res = json.getString("result");
            if (res.equals("true")) {
                result = true;
            } else {
                result = false;
            }

        } catch (Exception e) {
            result = false;
            e.printStackTrace();

        }

    }
}

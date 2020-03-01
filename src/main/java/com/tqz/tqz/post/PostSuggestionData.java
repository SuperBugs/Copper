package com.tqz.tqz.post;

import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.httputil.MyHttpUrlConnectionGet;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PostSuggestionData {
    private String mContent;
    private String mContact;
    private String mAccount;
    private boolean result;

    public boolean postSuggestionData(String content, String contact, String account) {
        try {
            //对中文进行编码
            mContact = URLEncoder.encode(contact,"utf-8");
            mContent = URLEncoder.encode(content,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mAccount = account;
        post();
        return result;
    }

    private void post() {
        /**
         * 提交用户建议
         * url:http://url/index.php?c=FeedBack&f=suggestion&account=123&content=厉害&
         * contact=123
         */
        CommonConfig config = new CommonConfig();
        String shareAddress =
                config.getUrlSuggestionFunction() +
                        config.getUrlInsert() +
                        config.getUrlSuggestionParam1() +
                        mAccount +
                        config.getUrlInsert() +
                        config.getUrlSuggestionParam2() +
                        mContent +
                        config.getUrlInsert() +
                        config.getUrlSuggestionParam3() +
                        mContact;
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

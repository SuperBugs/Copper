package com.tqz.tqz.post;

import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.httputil.MyHttpUrlConnectionGet;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UserChangeNickName {
    private boolean result;
    private String mNewNickName;
    private String mAccount;
    public boolean changeNickName(String NewNickName, String Account) {
        mAccount = Account;
        try {
            mNewNickName= URLEncoder.encode(NewNickName,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post();
        return result;
    }

    private void post() {
        CommonConfig config = new CommonConfig();
        String registerAddress =
                config.getUrlUserChangeNickNameFunction() +
                        config.getUrlInsert() +
                        config.getUrlUserChangeNickNameAccountParam1() +
                        mAccount +
                        config.getUrlInsert() +
                        config.getUrlUserChangeNewNickNameParam2() +
                        mNewNickName;
        //向服务器发送请求
        String response = MyHttpUrlConnectionGet.sendHttpRequest(registerAddress);
        try {
            JSONObject json = new JSONObject(response);
            String res = json.getString("result");
            //true 修改成功，false 修改失败
            if (res.equals("true")) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

    }

}

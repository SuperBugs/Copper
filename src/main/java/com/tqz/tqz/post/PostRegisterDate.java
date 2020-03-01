package com.tqz.tqz.post;

import android.util.Log;

import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.httputil.MyHttpUrlConnectionGet;

import org.json.JSONObject;

public class PostRegisterDate {
    private String userPhoneNumber;
    private String userPassword;
    private int register_state;
    private static final String TAG = "PostRegisterDate";
    /**
     * @param userPhoneNumber 用户手机号
     * @param userPassword    用户密码
     * @return
     */
    public int postRegisterDate(String userPhoneNumber, String userPassword) {
        this.userPhoneNumber = userPhoneNumber;
        this.userPassword = userPassword;
        post();
        return register_state;
    }

    private void post() {

        CommonConfig config=new CommonConfig();
        String registerAddress =
                config.getUrlRegisterFunction() +
                        config.getUrlInsert() +
                        config.getUrlRegisterParam1() +
                        userPhoneNumber +
                        config.getUrlInsert() +
                        config.getUrlRegisterParam2() +
                        userPassword;
        Log.d(TAG, "超军"+registerAddress);
        //向服务器发送请求
        String response = MyHttpUrlConnectionGet.sendHttpRequest(registerAddress);
        try {
            JSONObject json = new JSONObject(response);
            String result = json.getString("result");

            //设置注册结果0为注册失败1为注册成功2为已经注册
            if (result.equals("true")) {
                register_state = 1;
            } else {
                if (result.equals("have")){
                    register_state=2;
                }else{
                    register_state = 0;
                }
            }
        } catch (Exception e) {
            register_state = 0;
            e.printStackTrace();

        }

    }


}

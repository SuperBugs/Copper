package com.tqz.tqz.post;


import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.common.Crypt;
import com.tqz.tqz.httputil.MyHttpUrlConnectionGet;

import org.json.JSONObject;

public class ChangeUserPassword {
    private String userPhoneNumber;
    private String newPassword;
    private int change_state;

    /**
     * @param userPhoneNumber 用户手机号
     * @param newPassword     用户新密码
     * @return
     */
    public int postUserDate(String userPhoneNumber, String newPassword) {
        this.userPhoneNumber = Crypt.encrypt(userPhoneNumber);
        this.newPassword = Crypt.encrypt(newPassword);
        post();
        return change_state;
    }

    private void post() {

        CommonConfig config=new CommonConfig();
        String registerAddress =
                config.getUrlChangeUserDateFunction() +
                        config.getUrlInsert() +
                        config.getUrlChangeParam1() +
                        userPhoneNumber +
                        config.getUrlInsert() +
                        config.getUrlChangeParam2() +
                        newPassword;
        //向服务器发送请求
        String response = MyHttpUrlConnectionGet.sendHttpRequest(registerAddress);
        try {
            JSONObject json = new JSONObject(response);
            String result = json.getString("result");
            //设置改变密码结果0为失败1为成功2为账户不存在
            //设置注册结果
            if (result.equals("true")) {
                change_state = 1;
            } else if (result.equals("null")) {
                change_state = 2;
            } else if (result.equals("false")) {
                change_state = 0;
            }
        } catch (Exception e) {
            change_state = 0;
            e.printStackTrace();

        }

    }
}

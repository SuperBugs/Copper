package com.tqz.tqz.post;


import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.common.Crypt;
import com.tqz.tqz.httputil.MyHttpUrlConnectionGet;

import org.json.JSONObject;


public class CheakLoginDate {
    private String userAccount;
    private String userPassword;
    private int loginState;
    public int getloginResult(String userAccount, String userPassword) {
        //信息加密
        this.userAccount = Crypt.encrypt(userAccount);
        this.userPassword=Crypt.encrypt(userPassword);
        cheakLoginDate();
        return loginState;
    }

    private void cheakLoginDate() {

        CommonConfig config=new CommonConfig();
        String loginAddress =
                config.getUrlLoginFunction()+
                config.getUrlInsert() +
                config.getUrlLoginParam1() +
                userAccount +
                config.getUrlInsert() +
                config.getUrlLoginParam2() +
                userPassword;
        //向服务器发送请求
        String response = MyHttpUrlConnectionGet.sendHttpRequest(loginAddress);
        try {
            JSONObject json = new JSONObject(response);
            String result = Crypt.decrypt(json.getString("result"));
            //设置注册结果
            if (result.equals("true")) {
                loginState = 1;
            } else {
                if (result.equals("null")){
                    loginState=2;
                }else{
                    loginState = 0;
                }
            }
        } catch (Exception e) {
            loginState=0;
            e.printStackTrace();

        }

    }

}

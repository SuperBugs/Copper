package com.tqz.tqz.post;

import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.httputil.MyHttpUrlConnectionGet;

import org.json.JSONObject;

public class PostExperienceAndIncomeData {
    private String income;
    private String experience;
    private String account;
    private Boolean result = false;
    public boolean postData(String income, String experience, String account) {
        this.income = income;
        this.experience = experience;
        this.account = account;
        post();
        return result;
    }

    private void post() {

        CommonConfig config = new CommonConfig();
        String url =
                config.getUrlAddExperienceFunction() +
                        config.getUrlInsert() +
                        config.getUrlAddExperienceParam1() +
                        account +
                        config.getUrlInsert() +
                        config.getUrlAddExperienceParam2() +
                        income +
                        config.getUrlInsert() +
                        config.getUrlAddExperienceParam3() +
                        experience;
        //向服务器发送请求
        String response = MyHttpUrlConnectionGet.sendHttpRequest(url);
        try {
            JSONObject json = new JSONObject(response);
            String res = json.getString("result");

            //设置注册结果0为注册失败1为注册成功2为已经注册
            if (res.equals("true")) {
                result=true;
            } else {
                result=false;
            }
        } catch (Exception e) {
            result=false;
            e.printStackTrace();

        }

    }


}

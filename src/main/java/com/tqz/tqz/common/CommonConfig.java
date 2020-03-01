package com.tqz.tqz.common;

public class CommonConfig {

    private int AppVersion = 5;
    //应用缓存目录
    private String cacheDir = "/sdcard/Android/data/com.tqz.tqz/";
    //private String cacheDir = "/Android/data/com.tqz";
    /**
     * 网络url配置信息
     */
    //本地服务器真机
    //private  String HostAddressStart = "http://192.168.169.100/think/www/index.php/Home/";
    //真机
    private String HostAddressStart = "http://123.207.255.187/index.php?";
    //新版app服务器地址
    private String HostNewAppAddressStart = "http://123.207.248.92/index.php?";
    //虚拟机
    //private String HostAddressStart = "http://10.0.2.2/think/www/index.php/Home/";

    private String urlInsert = "&";
    /**
     * 获取服务器上软件版本
     * 拼装http://url/index.php?c=App&f=getVersion
     */
    private String urlAppVersionFunction = HostAddressStart + "c=App&f=getVersion";
    /**
     * 注册账户
     * 拼装http://url/index.php?c=User&f=register&account=用户账号&password=用户密码
     */
    private String urlRegisterFunction = HostAddressStart + "c=User&f=register";
    private String urlRegisterParam1 = "account=";
    private String urlRegisterParam2 = "password=";

    /**
     * 登陆账户
     * url：http://url/index.php?c=User&f=login&account=用户账号&password=用户密码
     */
    private String urlLoginFunction = HostAddressStart + "c=User&f=login";
    private String urlLoginParam1 = "account=";
    private String urlLoginParam2 = "password=";

    /**
     * 更改账户密码:
     * url：http://url/index.php?c=User&f=changePassword&newPassword=123456789&account=1234567
     */
    private String urlChangeUserDateFunction = HostAddressStart + "c=User&f=changePassword";
    private String urlChangeParam1 = "account=";
    private String urlChangeParam2 = "newPassword=";
    /**
     * 获取用户数据
     * url：http://url/index.php?c=MainListData&f=get&password=1234567&account=123456789
     * 返回范例：{"userInformation":[{"id":"74","name":"小明","account":"12345678901",
     * "password":"123456789","income":"1000","head":"http://192.168.169
     * .100/think/www/Image/MainListView/1.jpg"}]}
     */
    private String urlgetUserDateFunction = HostAddressStart + "c=User&f=getUserData";
    private String urlgetUserDateParam1 = "account=";
    private String urlgetUserDateParam2 = "password=";
    /**
     * 改变用户昵称
     * url:http://url/index.php?c=User&f=getUserData&account=123&nickName=name
     */
    private String urlUserChangeNickNameFunction = HostAddressStart + "c=User&f=changeNickName";
    private String urlUserChangeNickNameAccountParam1 = "account=";
    private String urlUserChangeNewNickNameParam2 = "name=";
    /**
     * 获取首页ListView数据:
     * url：http://url/index.php?c=MainListData&f=get
     */
    private String urlMainListViewDateFunction = HostAddressStart + "c=MainListData&f=get";
    /**
     * 获取ChainTaskListView数据:
     * url：http://url/index.php?c=ChinaTaskListData&f=get
     */
    private String urlChinaTaskListViewDateFunction = HostAddressStart + "c=ChinaTaskListData&f=get";
    /**
     * 获取ChainAppListView数据:
     * url：http://url/index.php?c=ChinaAppListData&f=get
     */
    private String urlChinaAppListViewDateFunction = HostAddressStart + "c=ChinaAppListData&f=get";
    /**
     * 获取NewAppListView数据:
     * url：http://url/index.php?c=NewAppListData&f=get
     */
    private String urlNewAppListViewDateFunction = HostAddressStart + "c=NewAppListData&f=get";
    /**
     * 获取NewTaskListView数据:
     * url：http://url/index.php?c=NewTaskListData&f=get
     */
    private String urlNewTaskListViewDateFunction = HostAddressStart + "c=NewTaskListData&f=get";
    /**
     * 获取AbroadTaskListView数据:
     * url：http://url/index.php?c=AbroadTaskListData&f=get
     */
    private String urlAbroadTaskListViewDateFunction = HostAddressStart + "c=AbroadTaskListData&f=get";
    /**
     * 获取AbroadAppListView数据:
     * url：http://url/index.php?c=AbroadAppListData&f=get
     */
    private String urlAbroadAppListViewDateFunction = HostAddressStart + "c=AbroadAppListData&f=get";
    /**
     * 获取首页ViewPager数据
     * url：http://localhost/think/www/index.php/
     * Home/MainViewPager/get
     */
    private String urlMainViewPagerDateFunction = HostAddressStart + "c=MainViewpagerData&f=get";
    /**
     * 上传用户头像
     * url：http://localhost/think/www/index.php/
     * Home/SetUserHeadImage/set
     */
    private String urlPostUserHeadImageFunction = HostAddressStart + "c=User&f=savaUserHead";

    /**
     * 提交用户分享
     * url:http://url/index.php?c=FeedBack&f=shareData&name=百度钱包&address=www.baidu.com&
     * shareNumber=1245&introduce=一个赚钱任务
     */
    private String urlShareDataFunction = HostAddressStart + "c=FeedBack&f=shareData";
    private String urlShareDataParam1 = "name=";
    private String urlShareDataParam2 = "address=";
    private String urlShareDataParam3 = "shareNumber=";
    private String urlShareDataParam4 = "introduce=";
    /**
     * 提交用户建议
     * url:http://url/index.php?c=FeedBack&f=suggestion&account=123&content=厉害&
     * contact=123
     */
    private String urlSuggestionFunction = HostAddressStart + "c=FeedBack&f=suggestion";
    private String urlSuggestionParam1 = "account=";
    private String urlSuggestionParam2 = "content=";
    private String urlSuggestionParam3 = "contact=";
    /**
     * 添加用户经验和历史赚金
     * 拼装http://url/index.php?c=User&f=addExperience&account=用户账号&money=收益&
     * experience=增加的经验
     */
    private String urlAddExperienceFunction = HostAddressStart + "c=User&f=addExperience";
    private String urlAddExperienceParam1 = "account=";
    private String urlAddExperienceParam2 = "money=";
    private String urlAddExperienceParam3 = "experience=";

    /**
     * 获取流量券数据:
     * url：http://url/index.php?c=LiuLiangQuanData&f=get
     */
    private String urlLiuLiangQuanDataFunction = HostAddressStart + "c=LiuLiangQuanData&f=get";
    /**
     * 获取话费券数据:
     * url：http://url/index.php?c=HuaFeiQuanData&f=get
     */
    private String urlHuaFeiQuanDataFunction = HostAddressStart + "c=HuaFeiQuanData&f=get";
    /**
     * 获取外卖券数据:
     * url：http://url/index.php?c=WaiMaiQuanData&f=get
     */
    private String urlWaiMaiQuanDataFunction = HostAddressStart + "c=WaiMaiQuanData&f=get";
    /**
     * 获取购物券数据:
     * url：http://url/index.php?c=HuaFeiQuanData&f=get
     */
    private String urlGouWuQuanDataFunction = HostAddressStart + "c=GouWuQuanData&f=get";
    /**
     * 获取打车券数据:
     * url：http://url/index.php?c=DaCheQuanData&f=get
     */
    private String urlDaCheQuanDataFunction = HostAddressStart + "c=DaCheQuanData&f=get";
    /**
     * 获取电影券数据:
     * url：http://url/index.php?c=DianYingQuanData&f=get
     */
    private String urlDianYingQuanDataFunction = HostAddressStart + "c=DianYingQuanData&f=get";
    /**
     * 获取券数据:
     * url：http://url/index.php?c=LiCaiQuanData&f=get
     */
    private String urlLiCaiQuanDataFunction = HostAddressStart + "c=LiCaiQuanData&f=get";
    /**
     * 获取红包券数据:
     * url：http://url/index.php?c=HuaFeiQuanData&f=get
     */
    private String urlHongBaoQuanDataFunction = HostAddressStart + "c=HongBaoQuanData&f=get";

    public String getUrlDaCheQuanDataFunction() {
        return urlDaCheQuanDataFunction;
    }

    public String getUrlDianYingQuanDataFunction() {
        return urlDianYingQuanDataFunction;
    }

    public String getUrlLiCaiQuanDataFunction() {
        return urlLiCaiQuanDataFunction;
    }

    public String getUrlHongBaoQuanDataFunction() {
        return urlHongBaoQuanDataFunction;
    }

    public String getUrlGouWuQuanDataFunction() {
        return urlGouWuQuanDataFunction;
    }

    public String getUrlWaiMaiQuanDataFunction() {
        return urlWaiMaiQuanDataFunction;
    }

    public String getUrlHuaFeiQuanDataFunction() {
        return urlHuaFeiQuanDataFunction;
    }

    public String getUrlLiuLiangQuanDataFunction() {
        return urlLiuLiangQuanDataFunction;
    }

    public String getCacheDir() {
        return cacheDir;
    }

    public String getHostAddressStart() {
        return HostAddressStart;
    }

    public String getUrlAbroadAppListViewDateFunction() {
        return urlAbroadAppListViewDateFunction;
    }

    public String getUrlAbroadTaskListViewDateFunction() {
        return urlAbroadTaskListViewDateFunction;
    }

    public String getUrlUserChangeNewNickNameParam2() {
        return urlUserChangeNewNickNameParam2;
    }

    public String getUrlUserChangeNickNameAccountParam1() {
        return urlUserChangeNickNameAccountParam1;
    }

    public String getUrlUserChangeNickNameFunction() {
        return urlUserChangeNickNameFunction;
    }

    public String getUrlChinaTaskListViewDateFunction() {
        return urlChinaTaskListViewDateFunction;
    }

    public String getUrlChinaAppListViewDateFunction() {
        return urlChinaAppListViewDateFunction;
    }

    public String getUrlChangeParam1() {
        return urlChangeParam1;
    }

    public String getUrlChangeParam2() {
        return urlChangeParam2;
    }

    public String getUrlChangeUserDateFunction() {
        return urlChangeUserDateFunction;
    }

    public String getUrlInsert() {
        return urlInsert;
    }

    public String getUrlLoginFunction() {
        return urlLoginFunction;
    }

    public String getUrlLoginParam1() {
        return urlLoginParam1;
    }

    public String getUrlLoginParam2() {
        return urlLoginParam2;
    }

    public String getUrlMainListViewDateFunction() {
        return urlMainListViewDateFunction;
    }

    public String getUrlRegisterFunction() {
        return urlRegisterFunction;
    }

    public String getUrlRegisterParam1() {
        return urlRegisterParam1;
    }

    public String getUrlRegisterParam2() {
        return urlRegisterParam2;
    }

    public String getUrlgetUserDateFunction() {
        return urlgetUserDateFunction;
    }

    public String getUrlgetUserDateParam1() {
        return urlgetUserDateParam1;
    }

    public String getUrlgetUserDateParam2() {
        return urlgetUserDateParam2;
    }

    public String getUrlMainViewPagerDateFunction() {
        return urlMainViewPagerDateFunction;
    }

    public String getUrlPostUserHeadImageFunction() {
        return urlPostUserHeadImageFunction;
    }

    public String getUrlShareDataFunction() {
        return urlShareDataFunction;
    }

    public String getUrlShareDataParam1() {
        return urlShareDataParam1;
    }

    public String getUrlShareDataParam2() {
        return urlShareDataParam2;
    }

    public String getUrlShareDataParam3() {
        return urlShareDataParam3;
    }

    public String getUrlShareDataParam4() {
        return urlShareDataParam4;
    }

    public String getUrlSuggestionFunction() {
        return urlSuggestionFunction;
    }

    public String getUrlSuggestionParam1() {
        return urlSuggestionParam1;
    }

    public String getUrlSuggestionParam2() {
        return urlSuggestionParam2;
    }

    public String getUrlSuggestionParam3() {
        return urlSuggestionParam3;
    }

    public String getUrlNewAppListViewDateFunction() {
        return urlNewAppListViewDateFunction;
    }

    public int getAppVersion() {
        return AppVersion;
    }

    public String getUrlAppVersionFunction() {
        return urlAppVersionFunction;
    }

    public String getUrlNewTaskListViewDateFunction() {
        return urlNewTaskListViewDateFunction;
    }

    public String getHostNewAppAddressStart() {
        return HostNewAppAddressStart;
    }

    public String getUrlAddExperienceFunction() {
        return urlAddExperienceFunction;
    }

    public String getUrlAddExperienceParam1() {
        return urlAddExperienceParam1;
    }

    public String getUrlAddExperienceParam2() {
        return urlAddExperienceParam2;
    }

    public String getUrlAddExperienceParam3() {
        return urlAddExperienceParam3;
    }
}


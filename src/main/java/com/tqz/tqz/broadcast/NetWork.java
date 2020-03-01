package com.tqz.tqz.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWork extends BroadcastReceiver {
    public static final String NET_CHANGE = "net_change";
    //标记当前网络状态，0为无，1为移动网络，3为wifi网络，4为网络可用
    public static final String NET_TYPE = "net_type";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //移动数据
        NetworkInfo netWorkInfo = connectivityManager.
                getActiveNetworkInfo();

        if (netWorkInfo== null) {
            //网络都不可用
            Intent intent0 = new Intent(NET_CHANGE);
            intent0.putExtra(NET_TYPE, 0);
            context.sendBroadcast(intent0);
            return;
        }

        //移动网络
        if (netWorkInfo.getTypeName() == "MOBILE") {
            Intent intent1 = new Intent(NET_CHANGE);
            intent1.putExtra(NET_TYPE, 2);
            context.sendBroadcast(intent1);
            return;
        }
        //wifi网络
        if (netWorkInfo.getTypeName() == "WIFI") {
            Intent intent3 = new Intent(NET_CHANGE);
            intent3.putExtra(NET_TYPE, 3);
            context.sendBroadcast(intent3);
            return;
        }
        //有网络
        if (netWorkInfo!= null) {
            Intent intent4 = new Intent(NET_CHANGE);
            intent4.putExtra(NET_TYPE, 4);
            context.sendBroadcast(intent4);
            return;
        }
    }
}

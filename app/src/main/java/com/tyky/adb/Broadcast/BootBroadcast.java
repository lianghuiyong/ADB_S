package com.tyky.adb.Broadcast;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.tyky.adb.Service.MyServiceConn;

public class BootBroadcast extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {


        Log.d("BootUP", "==============BootUP==============");

        //获取开机启动
        SharedPreferences autoBootUP = context.getSharedPreferences("user_BootUP", Context.MODE_PRIVATE);  //开机启动记录
        Boolean str_BootUP = autoBootUP.getBoolean("BootUP", false);
        Log.d("BootUP", "ADB服务开机启动："+String.valueOf(str_BootUP));
        MyServiceConn myServiceConn = new MyServiceConn();

        if (str_BootUP){
            Intent intentADB = new Intent();
            intentADB.setComponent(new ComponentName("com.tyky.adb","com.tyky.adb.Service.ADBService"));
            //context.startService(intentADB);
            context.bindService(intent, myServiceConn, Context.BIND_AUTO_CREATE);
        }
    }

}

package com.tyky.adb.Broadcast;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootBroadcast extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BootUP", "==============BootUP==============");
        //获取开机启动
        SharedPreferences autoBootUP = context.getSharedPreferences("user_BootUP", Context.MODE_PRIVATE);  //开机启动记录
        Boolean str_BootUP = autoBootUP.getBoolean("BootUP", false);

        if (str_BootUP){
            Intent intentADB = new Intent();
            intentADB.setComponent(new ComponentName("com.tyky.adb","com.tyky.adb.Service.ADBService"));
            context.startService(intentADB);
        }
    }

}

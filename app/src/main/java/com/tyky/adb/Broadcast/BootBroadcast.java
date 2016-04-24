package com.tyky.adb.Broadcast;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.tyky.adb.MainApplication;

public class BootBroadcast extends BroadcastReceiver {

    private Boolean str_BootUP = null;
    @Override
    public void onReceive(Context context, Intent intent) {


        str_BootUP = MainApplication.autoBootUP.getBoolean("BootUP",false);
        if (str_BootUP){
            Intent intentADB = new Intent();
            intentADB.setComponent(new ComponentName("com.tyky.adb","com.tyky.adb.Service.ADBService"));
            context.startService(intentADB);
        }
    }
}

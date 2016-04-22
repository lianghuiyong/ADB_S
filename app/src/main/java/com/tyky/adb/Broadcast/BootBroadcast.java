package com.tyky.adb.Broadcast;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class BootBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            //Intent intentADB = new Intent();
            //intentADB.setComponent(new ComponentName("com.tyky.adb","com.tyky.adb.Service.ADBService"));
            //context.startService(intentADB);
    }
}

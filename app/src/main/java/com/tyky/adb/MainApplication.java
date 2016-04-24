package com.tyky.adb;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by lianghuiyong on 2016/4/24.
 */
public class MainApplication extends Application {
    public static SharedPreferences autoBootUP = null;  //开机启动记录

    @Override
    public void onCreate() {
        super.onCreate();
        autoBootUP =getSharedPreferences("user_BootUP",MODE_PRIVATE);
    }
}

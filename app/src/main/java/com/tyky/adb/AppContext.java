package com.tyky.adb;

import android.app.Application;

import java.net.Socket;

/**
 * Created by lenovo on 2016/4/25.
 */
public class AppContext extends Application{

    /*ADB通信的客户端（单一通信）*/
    public static Socket socket = null;
    public static volatile boolean isRun = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

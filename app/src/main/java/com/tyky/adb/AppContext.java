package com.tyky.adb;

import android.app.Application;

/**
 * Created by lenovo on 2016/4/25.
 */
public class AppContext extends Application{

    public static IADBAidlInterface iadbAidlInterface = null;       //Service 服务接口

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

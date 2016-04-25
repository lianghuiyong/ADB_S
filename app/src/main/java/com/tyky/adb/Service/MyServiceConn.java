package com.tyky.adb.Service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.tyky.adb.AppContext;

/**
 * Created by lenovo on 2016/4/25.
 */
public class MyServiceConn implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        AppContext.iadbAidlInterface = com.tyky.adb.IADBAidlInterface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        AppContext.iadbAidlInterface = null;
    }
}

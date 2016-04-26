package com.tyky.adb.IO;

import android.util.Log;

import com.tyky.adb.AppContext;
import com.tyky.adb.Model.ADBRecvJson;

import de.greenrobot.event.EventBus;

/**
 * Created by lenovo on 2016/4/26.
 */
public class RecvDataThread extends Thread {

    private String TAG = "RecvDataThread";


    @Override
    public void run() {
        super.run();
        while (AppContext.isRun){
            SocketIO socketIO = new SocketIO();
            String strRecv =socketIO.recvJData(AppContext.socket);
            if (strRecv == null){
                Log.d( TAG, "客户端已断开");
                AppContext.isRun = false;
                EventBus.getDefault().post(getJsonStr(" "));
            }else {

                /*处理接收到的字符*/
                EventBus.getDefault().post(getJsonStr(strRecv));
                Log.d( TAG, strRecv);
            }
        }
        Log.d(TAG,"结束线程");
    }

    /*
    * 获取接收的json字符串
    * */
    private ADBRecvJson getJsonStr(String strJson){
        ADBRecvJson recvJson = null;
        if (null != strJson){
            recvJson = new ADBRecvJson();
            recvJson.setStrJson(strJson);
        }
        return recvJson;
    }
}

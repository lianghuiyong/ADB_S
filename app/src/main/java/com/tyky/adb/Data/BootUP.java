package com.tyky.adb.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by lenovo on 2016/4/26.
 */
public class BootUP {

    //提交service开机启动开关
    public static void setBootUP(Context context,boolean BootUP) throws RemoteException {
        SharedPreferences autoBootUP =context.getSharedPreferences("user_BootUP", Context.MODE_PRIVATE);
        SharedPreferences.Editor bootUPEditor = autoBootUP.edit();
        bootUPEditor.putBoolean("BootUP",BootUP);
        bootUPEditor.apply();
        Log.d("BootUP", "开机启动["+String.valueOf(BootUP)+"]");
    }

    //获取开机自启动设置
    public static boolean getBootUP(Context context) throws RemoteException {
        SharedPreferences autoBootUP =context.getSharedPreferences("user_BootUP", Context.MODE_PRIVATE);
        return autoBootUP.getBoolean("BootUP",false);
    }
}

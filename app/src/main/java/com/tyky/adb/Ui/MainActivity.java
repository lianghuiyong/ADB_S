package com.tyky.adb.Ui;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tyky.adb.AppContext;
import com.tyky.adb.R;
import com.tyky.adb.Service.MyServiceConn;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public volatile boolean isRun = false;
    Button btn_StatADB = null;
    Button btn_StopADB = null;
    Button btn_bindADB = null;
    Button btn_unbindADB = null;
    Button btn_sendData = null;
    Button btn_BootUP= null;
    Intent intent = null;
    MyServiceConn myServiceConn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_StatADB = (Button) findViewById(R.id.startADB);
        btn_StopADB = (Button) findViewById(R.id.stopADB);
        btn_bindADB = (Button) findViewById(R.id.bindADB);
        btn_unbindADB = (Button) findViewById(R.id.unbindADB);
        btn_sendData = (Button) findViewById(R.id.sendData);
        btn_BootUP = (Button) findViewById(R.id.BootUP);

        btn_StatADB.setOnClickListener(this);
        btn_StopADB.setOnClickListener(this);
        btn_bindADB.setOnClickListener(this);
        btn_unbindADB.setOnClickListener(this);
        btn_sendData.setOnClickListener(this);
        btn_BootUP.setOnClickListener(this);

        intent = new Intent();
        intent.setComponent(new ComponentName("com.tyky.adb","com.tyky.adb.Service.ADBService"));
    }

    @Override
    protected void onStart() {
        super.onStart();

        btn_StopADB.setEnabled(false);
        btn_unbindADB.setEnabled(false);

        myServiceConn = new MyServiceConn();

        //初始化自启动开关
        //if(AppContext.iadbAidlInterface != null){
        if (isServiceRunning("com.tyky.adb.Service.ADBService")){
            btn_BootUP.setClickable(true);
            try {
                if (AppContext.iadbAidlInterface.getBootUP()){
                    btn_BootUP.setEnabled(true);
                    btn_BootUP.setText("ADB服务自启动(开)");
                }else{
                    btn_BootUP.setEnabled(false);
                    btn_BootUP.setText("ADB服务自启动(关)");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else {
            btn_BootUP.setText("请先 绑定ADB服务");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startADB:
                startService(intent);
                btn_StatADB.setEnabled(false);
                btn_StopADB.setEnabled(true);
                break;
            case R.id.stopADB:
                stopService(intent);
                btn_StatADB.setEnabled(true);
                btn_StopADB.setEnabled(false);
                break;

            case R.id.bindADB:
                bindService(intent, myServiceConn, Context.BIND_AUTO_CREATE);
                btn_bindADB.setEnabled(false);
                btn_unbindADB.setEnabled(true);
                break;

            case R.id.unbindADB:
                isRun = false;
                unbindService(myServiceConn);
                btn_bindADB.setEnabled(true);
                btn_unbindADB.setEnabled(false);
                break;

            case R.id.sendData:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("New photo","IMG_20160410_000155.jpg");
                    AppContext.iadbAidlInterface.sendJData(jsonObject.toString());
                } catch (JSONException | RemoteException e) {
                    e.printStackTrace();
                }


                break;

            case R.id.BootUP:
                //初始化自启动开关
                try {
                    if (AppContext.iadbAidlInterface.getBootUP()){
                        AppContext.iadbAidlInterface.setBootUP(false);
                        btn_BootUP.setText("ADB服务自启动(关)");
                    }else{
                        AppContext.iadbAidlInterface.setBootUP(true);
                        btn_BootUP.setText("ADB服务自启动(开)");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    //判断服务是否启动
    private boolean isServiceRunning(String className)
    {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfoList = manager.getRunningServices(100);
        if(runningServiceInfoList == null)
            return false;
        for(int i = 0; i < runningServiceInfoList.size(); i++)
        {
            if(runningServiceInfoList.get(i).service.getClassName().equals(className))
                return true;
        }
        return false;
    }

}


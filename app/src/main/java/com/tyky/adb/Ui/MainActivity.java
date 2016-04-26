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
import android.widget.TextView;

import com.tyky.adb.AppContext;
import com.tyky.adb.Data.BootUP;
import com.tyky.adb.IO.SocketIO;
import com.tyky.adb.Model.ADBRecvJson;
import com.tyky.adb.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_StatADB = null;
    Button btn_StopADB = null;
    Button btn_sendData = null;
    Button btn_BootUP= null;
    TextView textView = null;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        btn_StatADB = (Button) findViewById(R.id.startADB);
        btn_StopADB = (Button) findViewById(R.id.stopADB);
        btn_sendData = (Button) findViewById(R.id.sendData);
        btn_BootUP = (Button) findViewById(R.id.BootUP);
        textView = (TextView) findViewById(R.id.textView);

        btn_StatADB.setOnClickListener(this);
        btn_StopADB.setOnClickListener(this);
        btn_sendData.setOnClickListener(this);
        btn_BootUP.setOnClickListener(this);

        intent = new Intent();
        intent.setComponent(new ComponentName("com.tyky.adb","com.tyky.adb.Service.ADBService"));
    }

    @Override
    protected void onStart() {
        super.onStart();

        //初始化启动服务器按钮
        if (isServiceRunning("com.tyky.adb.Service.ADBService")){
            btn_StatADB.setEnabled(false);
        }else {
            btn_StopADB.setEnabled(false);
            btn_sendData.setEnabled(false);
        }

        //初始化自启动开关
        try {
            if (BootUP.getBootUP(this)){
                btn_BootUP.setText("通信服务自启动[开]");
            }else{
                btn_BootUP.setText("通信服务自启动[关]");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startADB:
                startService(intent);
                btn_StatADB.setEnabled(false);
                btn_StopADB.setEnabled(true);
                btn_sendData.setEnabled(true);
                break;

            case R.id.stopADB:
                stopService(intent);
                btn_StatADB.setEnabled(true);
                btn_StopADB.setEnabled(false);
                btn_sendData.setEnabled(false);
                break;

            case R.id.sendData:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("NewPhoto","IMG_20160410_000155.jpg");
                    SocketIO socketIO  = new SocketIO();
                    if(!socketIO.sendJData(AppContext.socket, jsonObject.toString())){
                        AppContext.socket = null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.BootUP:
                try {
                    if (BootUP.getBootUP(this)){
                        BootUP.setBootUP(this,false);
                        btn_BootUP.setText("通信服务自启动[关]");
                    }else {
                        BootUP.setBootUP(this,true);
                        btn_BootUP.setText("通信服务自启动[开]");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    //EventBus更新UI
    public void onEventMainThread(ADBRecvJson recvJson){
        textView.setText(recvJson.getStrJson());
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


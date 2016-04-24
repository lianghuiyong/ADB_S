package com.tyky.adb.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.tyky.adb.IADBAidlInterface;
import com.tyky.adb.MainApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ADBService extends Service{

    private String TAG = "ADBService";
    final int SERVER_PORT = 17786;
    volatile boolean isRun = false;
    private BufferedWriter date_Out;
    private BufferedReader date_In;


    private DataBinder dataBinder = null;

    private ServerSocket serverSocket = null;
    private Socket client;

    @Override
    public IBinder onBind(Intent intent) {
        dataBinder = new DataBinder();
        return dataBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"service开始");
        this.createServer(SERVER_PORT);
    }

    //线程一，监听客户端的链接
    public void createServer(final int Port){
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("127.0.0.1", Port));
            Log.e(TAG,"绑定服务");

            ExecutorService weatherPool = Executors.newSingleThreadExecutor();  //采用线程池单一线程方式，防止被杀死
            weatherPool.execute(new Runnable() {
                @Override
                public void run() {
                    isRun = true;
                    while (isRun){
                        try {
                            Log.d(TAG,"serverSocket.accept()");
                            client = serverSocket.accept();
                            date_In = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            date_Out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                            Log.d(TAG,"client is connect :"+client);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        try {
            dataBinder = null;                //binder置空
            serverSocket.close();             //关闭服务器socket
            Log.d(TAG,"解绑adb service");
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    class DataBinder extends IADBAidlInterface.Stub{

        private SharedPreferences.Editor bootUPEditor = null;

        @Override
        //提交service开机启动开关
        public void setBootUP(boolean BootUP) throws RemoteException {
            bootUPEditor = MainApplication.autoBootUP.edit();
            bootUPEditor.putBoolean("BootUP",BootUP);
            bootUPEditor.apply();
        }

        @Override
        public void sendJData(String strJ) throws RemoteException {
            try {
                if(client != null){
                    date_Out.write(strJ);
                    date_Out.flush();
                    Log.d(TAG,"send ["+client+"] :"+strJ);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String recvJData() throws RemoteException {
            return null;
        }
    }
}

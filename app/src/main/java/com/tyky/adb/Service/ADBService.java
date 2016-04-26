package com.tyky.adb.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.tyky.adb.AppContext;
import com.tyky.adb.IO.RecvDataThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ADBService extends Service{

    private String TAG = "ADBService";
    private final int SERVER_PORT = 17786;
    private volatile boolean isRun = false;
    private ServerSocket serverSocket = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"service启动");
        createServer(SERVER_PORT);
    }

    //线程一，监听客户端的链接
    public void createServer(final int Port){
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("127.0.0.1", Port));
            Log.e(TAG,"绑定服务");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ExecutorService weatherPool = Executors.newSingleThreadExecutor();  //采用线程池单一线程方式，防止被杀死
        weatherPool.execute(new Runnable() {
            @Override
            public void run() {
                isRun = true;
                while (isRun) try {
                    AppContext.socket = serverSocket.accept();
                    Log.d(TAG, "client is connect :" + AppContext.socket);

                    /*创建一个线程接收*/
                    AppContext.isRun = true;
                    new RecvDataThread().start();
                } catch (IOException e) {
                    //e.printStackTrace();
                    //置空客户端
                    AppContext.socket = null;
                    Log.d(TAG, "服务器关闭");
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        try {
            isRun = false;
            serverSocket.close();             //关闭服务器socket
            Log.d(TAG,"解绑adb service");
        } catch (IOException e) {
            //e.printStackTrace();
        }
        super.onDestroy();
    }
}

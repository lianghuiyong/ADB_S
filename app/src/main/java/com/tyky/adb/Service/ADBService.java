package com.tyky.adb.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ADBService extends Service {

    private String TAG = "ADBService";
    int SERVER_PORT = 19986;
    volatile boolean isRun = false;
    private BufferedInputStream in;
    private BufferedOutputStream out;

    private ServerSocket serverSocket = null;
    private Socket client;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        isRun = true;
        ADB_Server(SERVER_PORT);
    }


    public void ADB_Server(final int Port){
        ExecutorService weatherPool = Executors.newSingleThreadExecutor();  //采用线程池单一线程方式，防止被杀死
        weatherPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建服务器Socket
                    serverSocket = new ServerSocket();
                    serverSocket.bind(new InetSocketAddress("127.0.0.1", Port));
                    client = serverSocket.accept();

                    if(client != null){
                        out = new BufferedOutputStream(client.getOutputStream());
                        in = new BufferedInputStream(client.getInputStream());
                        Log.e(TAG,"客户端已连接");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    void sendStr(String str){

    }

    String recvStr(){
        String str = null;

        return str;
    }


    @Override
    public void onDestroy() {
        isRun = false;
        super.onDestroy();
    }
}

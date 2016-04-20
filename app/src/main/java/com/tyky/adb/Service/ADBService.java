package com.tyky.adb.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.tyky.adb.Utils.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ADBService extends Service {

    private String TAG = "ADBService";
    final int SERVER_PORT = 19986;
    volatile boolean isRun = false;
    private DataOutputStream date_Out;
    private DataInputStream data_In;

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
                    Log.e(TAG,"客户端已开启");
                    //创建服务器Socket
                    serverSocket = new ServerSocket();
                    serverSocket.bind(new InetSocketAddress("127.0.0.1", Port));
                    client = serverSocket.accept();

                    if(client != null){
                        data_In = new DataInputStream(client.getInputStream());
                        date_Out = new DataOutputStream(client.getOutputStream());
                        Log.e(TAG,"客户端已连接");

                        //图片位置
                        String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/IMG_20160410_000155.jpg";
                        Log.e(TAG,path);
                        sendImg(date_Out,path);

                        //发送数据
                        isRun = true;
                        //while (isRun){
                            sendImg(date_Out,path);
                            Thread.sleep(3000);
                        //}
                    }else {//断开连接
                        Log.e(TAG,"断开连接");
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //以byte数组 发送图片给客户端
    void sendImg(DataOutputStream out,String path) throws IOException {

        //获取图片file
        FileInputStream file = new FileInputStream(path);
        int imgSize = file.available(); //图片的长度

        //传过去的byte数组的总长
        //byte[] btLength = DataUtils.intToByte(imgSize+4+4);

        //打flag 1:字符串 2：图片
        byte[] btFlag = DataUtils.intToByte(2);

        //将图片file转成byte数组
        byte[] btImg = new byte[imgSize];
        file.read(btImg);
        file.close();

        Log.e(TAG,"btImg.length = "+btImg.length);

        //拼接要发送的byte数组
        byte[] sendData = DataUtils.byteMerger(btFlag,btImg);

        //发送byte给客户端
        //out.writeInt(imgSize);
        out.write(sendData);
        out.flush();
    }

    //接收客户端发来的图片
    void recvimg(DataInputStream in, Socket client){

    }

    //
    void sendStr(String str){

    }

    String recvStr(){
        String str = null;

        return str;
    }


    @Override
    public void onDestroy() {
        //关闭
        try {
            isRun = false;
            serverSocket.close();
            Log.e(TAG,"关闭服务器");
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}

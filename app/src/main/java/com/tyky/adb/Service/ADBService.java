package com.tyky.adb.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ADBService extends Service {

    private String TAG = "ADBService";
    final int SERVER_PORT = 17786;
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
            //创建服务器Socket
            try {
                serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress("127.0.0.1", Port));
                Log.e(TAG,"服务器已开启");
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (isRun) {
                try {
                    client = serverSocket.accept();
                    data_In = new DataInputStream(client.getInputStream());
                    date_Out = new DataOutputStream(client.getOutputStream());
                    Log.e(TAG, "客户端已连接");

                    //图片位置
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/IMG_20160410_000155.jpg";
                    Log.e(TAG, path);
                    sendImg(date_Out, path);

                    //发送数据
                    isRun = true;
                    //while (isRun){
                    sendImg(date_Out, path);
                    Thread.sleep(3000);
                    //}
                }catch (IOException | InterruptedException e) {
                    //e.printStackTrace();
                }
            }
                }
        });
    }

    //以byte数组 发送图片给客户端
    void sendImg(DataOutputStream out,String path) throws IOException{

        //获取图片file
        File fileImg = new File(path);
        FileInputStream fileIn =new FileInputStream(fileImg);
        Log.d(TAG,"1：获取图片file");

        //1：传送标签1：字符串 2：图片
        out.writeInt(2);
        out.flush();
        Log.d(TAG,"1：传送标签1：字符串 2：图片");


        //2：图片名
        out.writeUTF(fileImg.getName());
        out.flush();
        Log.d(TAG," 2：图片名 ");


        //3：图片长度
        out.writeLong(fileImg.length());
        out.flush();
        Log.d(TAG,"3：图片长度 = "+fileImg.length());


        //4：图片
        //byte[] sendBytes =new byte[1024];
        //int length =0;
        /*while((length = fileIn.read(sendBytes,0, sendBytes.length)) >0){
            out.write(sendBytes,0, length);
            out.flush();
        }*/

        int length = (int) fileImg.length();
        byte[] sendBytes =new byte[length];
        if(fileIn.read(sendBytes,0, length)>0){
            out.write(sendBytes);
            out.flush();
        }

        //5:结束的EOF
        out.writeUTF("EOF");
        out.flush();

        Log.e(TAG,"传输完成+++++");

        /*
        //打flag 1:字符串 2：图片
        byte[] btFlag = DataUtils.intToByte(2);


        Log.e(TAG,"btLength = "+imgSize+4+4);

        //拼接要发送的byte数组
        byte[] sendData = DataUtils.byteMerger(btLength,btFlag);
        sendData = DataUtils.byteMerger(sendData,btImg);

*/


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
        try {
            isRun = false;
            serverSocket.close();
            Log.e(TAG,"关闭服务器");
        } catch (IOException e) {
            //e.printStackTrace();
        }
        super.onDestroy();
    }
}

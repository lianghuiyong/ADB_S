package com.tyky.adb.IO;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by lenovo on 2016/4/26.
 */
public class SocketIO {
    private String TAG = "SocketIO";

    public boolean sendJData(Socket socket, String strData){
        boolean ret;
        try {
            if(socket != null && socket.isConnected()){
                BufferedWriter data_Out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                data_Out.write(strData);
                data_Out.flush();
                //Log.d(TAG,"send ["+socket+"] :"+strData);
                ret = true;
            }else {
                Log.d(TAG,"无客户端");
                ret = false;
            }
        } catch (IOException e) {
            Log.d(TAG,"客户端已断开");
            ret = false;
        }
        return ret;
    }

    //返回null时，客户端断开连接
    public String recvJData(Socket socket){
        String ret = null;
        try {
            if (socket != null){
                BufferedReader data_In = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                char[] tempBuffer = new char[400];
                int numReadBytes = data_In.read(tempBuffer,0,tempBuffer.length);
                if (numReadBytes != -1){
                    ret = new String(tempBuffer, 0, numReadBytes);
                }
            }else {
                Log.d(TAG,"无客户端");
            }
        } catch (IOException e) {
            //e.printStackTrace();
            Log.d(TAG,"客户端已断开");
        }
        return ret;
    }
}

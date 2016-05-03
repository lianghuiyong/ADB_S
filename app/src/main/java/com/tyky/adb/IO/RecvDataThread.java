package com.tyky.adb.IO;

import android.content.Context;
import android.util.Log;

import com.tyky.adb.AppContext;
import com.tyky.adb.Model.ADBRecvJson;

import org.json.JSONException;
import org.json.JSONObject;

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
                JSONObject retJson = new JSONObject();
                /*处理接收到的字符*/
                EventBus.getDefault().post(getJsonStr(strRecv));
                try {
                    JSONObject jsonObject = new JSONObject(strRecv);
                    switch (jsonObject.get("Fun").toString()){
                        case "CARD":
                            retJson.put("Result", "success");
                            retJson.put("ImgPath", "/mnt/internal_sd/Terminal/371327199001065757371327199001065757/371327199001065757.jpg");
                            retJson.put("name", "张小飞");
                            retJson.put("sex","男");
                            retJson.put("nation","汉");
                            retJson.put("birth", "19960221");
                            retJson.put("address", "河北涿县西北二十里");
                            retJson.put("cardId", "336644199602218456");
                            retJson.put("department", "河北公安局");
                            retJson.put("effectDate", "20150513");
                            retJson.put("expireDate", "20250513");
                            socketIO.sendJData(AppContext.socket,retJson.toString());
                            break;

                        //指纹
                        case "FINGER":
                            retJson.put("Result", "success");
                            retJson.put("ImgPath", "/mnt/internal_sd/Terminal/371327199001065757371327199001065757/371327199001065757.jpg");
                            socketIO.sendJData(AppContext.socket,retJson.toString());
                            break;

                        //小证照
                        case "RECOGNIZE":
                            retJson.put("Result", "success");
                            retJson.put("ImgPath", "/mnt/internal_sd/Terminal/371327199001065757371327199001065757/371327199001065757.jpg");
                            socketIO.sendJData(AppContext.socket,retJson.toString());
                            break;

                        //人像
                        case "AVATAR":
                            retJson.put("Result", "success");
                            retJson.put("ImgPath", "/mnt/internal_sd/Terminal/371327199001065757371327199001065757/371327199001065757.jpg");
                            socketIO.sendJData(AppContext.socket,retJson.toString());
                            break;

                        //A4文件批文
                        case "A4":
                            retJson.put("Result", "success");
                            retJson.put("ImgPath", "/mnt/internal_sd/Terminal/371327199001065757371327199001065757/371327199001065757.jpg");
                            socketIO.sendJData(AppContext.socket,retJson.toString());
                            break;

                        //材料
                        case "MATTER":
                            retJson.put("Result", "success");
                            retJson.put("ImgPath", "/mnt/internal_sd/Terminal/371327199001065757371327199001065757/371327199001065757.jpg");
                            socketIO.sendJData(AppContext.socket,retJson.toString());
                            break;
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                }
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

// IADBAidlInterface.aidl
package com.tyky.adb;

// Declare any non-default types here with import statements

interface IADBAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    //开机自启动开关
    void setBootUP(boolean BootUP);
    boolean getBootUP();

    //JSON收发
    void sendJData(String strJ);
    String recvJData();
}

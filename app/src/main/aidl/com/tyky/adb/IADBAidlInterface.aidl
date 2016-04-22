// IADBAidlInterface.aidl
package com.tyky.adb;

// Declare any non-default types here with import statements

interface IADBAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    void sendJData(String strJ);
    String recvJData();
}

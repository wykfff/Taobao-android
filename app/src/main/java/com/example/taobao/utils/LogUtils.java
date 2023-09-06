package com.example.taobao.utils;

import android.util.Log;

public class LogUtils {
    private static int currentLev =4;

    private static final int debugLev = 4;
    private static final int infoLev = 3;
    private static final int warningLev = 2;
    private static final int errorLev = 1;

    public static void d(Object object,String log){
        if (currentLev >= debugLev) {
            Log.d(object.getClass().getSimpleName(), log);
        }
    }


    public static void i(Object object,String log){
        if (currentLev >= infoLev) {
            Log.i(object.getClass().getSimpleName(), log);
        }
    }

    public static void w(Object object,String log){
        if (currentLev >= warningLev) {
            Log.w(object.getClass().getSimpleName(), log);
        }
    }

    public static void e(Object object,String log){
        if (currentLev >= errorLev) {
            Log.e(object.getClass().getSimpleName(), log);
        }
    }



}

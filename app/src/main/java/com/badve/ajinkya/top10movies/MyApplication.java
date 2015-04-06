package com.badve.ajinkya.top10movies;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ajinkya on 25-03-2015.
 */
public class MyApplication extends Application {
    public static final String API_KEY_ROTTEN_TOMATOS = "yh4767jwr72h7f6ea9vvtvus";
    private static  MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
    }
    public static MyApplication getIntance(){
        return sInstance;
    }
    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}

package com.example.priyanka.ghmc.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Created by Priyanka on 23/09/16.
 */

public class MyApplication extends MultiDexApplication {
    private static MyApplication sInstance;
    public void onCreate(){
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getInstance(){
        return sInstance;
    }
    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }


}

package com.example.priyanka.SmartCitizen.utils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Priyanka on 23/09/16.
 */

public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    public static VolleySingleton getInstance() {
        if (sInstance == null) {
            sInstance = new VolleySingleton();
        }
        return sInstance;

    }
    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
}


package com.example.priyanka.SmartCitizen.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Priyanka on 21/04/16.
 */
public class AppPreferences {


    private static final String APP_SHARED_PREFS = "com.example.priyanka";
    private static AppPreferences instance = null;


    /**
     * Saving data in shared preferences which will store life time of
     * Application
     */
    public static void saveValue(String key, String value, Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setBooleanValue(String key, boolean value, Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void saveValue(String key, long value, Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void saveValue(String key, int value, Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getValue(String key, Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static boolean getBooleanValue(String key, Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static long getLongValue(String key, Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        return sp.getLong(key, Constants.DEFAULT_LONG_VALUE);
    }

    public static int getIntValue(String key, Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        return sp.getInt(key, Constants.DEFAULT_INT_VALUE);
    }

    public static boolean getDefaultBooleanTrue(String key, Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        return sp.getBoolean(key, true);
    }

    public static void deletePref(Context context) {
        String email = AppPreferences.getValue(Constants.EMAIL_ID, context);
        SharedPreferences sp = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        AppPreferences.saveValue(Constants.EMAIL_ID, email, context);
    }

    public static void setApplicationInBackground(Boolean status, Context context) {
        setBooleanValue(Constants.APP_STATUS, status, context);
    }

    public static boolean isApplicationInBackground(Context context) {
        return getBooleanValue(Constants.APP_STATUS, context);
    }

    public static boolean saveArray(String photoList, Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.PHOTO_ARRAY, photoList);
        return editor.commit();
    }

    public static ArrayList<Long> loadArray(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        String data = sp.getString(Constants.PHOTO_ARRAY,"");
        return new Gson().<ArrayList>fromJson(data,new TypeToken<ArrayList<Long>>() {}.getType());
    }
}

package com.example.priyanka.SmartCitizen.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.keeptraxinc.utils.logger.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by sahul on 9/22/16.
 */

public class AppUtils {

    private Context context;
    private final String LOG_TAG = getClass().getSimpleName();

    public static boolean isInternetAccessible(Context context) {
        boolean isDataAccessible = false;
        try {
            isDataAccessible = new isDataAccessible().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return isDataAccessible;
    }


    private static class isDataAccessible extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Logger.error("isInternetAccessible", "Couldn't check internet connection", e);
            }
            return false;
        }
    }

    public static void showNoInternetAccessibleAlert(Context context) {
        if (!AppPreferences.isApplicationInBackground(context)) {
            Toast.makeText(context, " Internet is not Accessible" , Toast.LENGTH_SHORT).show();
        }
    }


    public static void showWiFiSettingsAlert(Context context) {
        if (!AppPreferences.isApplicationInBackground(context)) {
            Toast.makeText(context , " Please connect to internet" , Toast.LENGTH_SHORT).show();
        }
    }
}

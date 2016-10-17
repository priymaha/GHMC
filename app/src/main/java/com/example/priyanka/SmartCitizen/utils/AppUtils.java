package com.example.priyanka.SmartCitizen.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.priyanka.SmartCitizen.activity.LoginActivity;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;
import com.keeptraxinc.utils.logger.Logger;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static String getFormattedDate(String isoDate, String format) {
        Date date = null;
        try {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'");
            date = dt.parse(isoDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // *** same for the format String below
        SimpleDateFormat dt1 = new SimpleDateFormat(format);
        return dt1.format(date);

    }
    public static void logoutFromApp(final Context context) {
        final ProgressDialog dialog = ProgressDialog.show(context, "", "Please wait");
        KeepTrax keepTrax = KeepTraxImpl.getInstance(context, UrlBuilder.getUrl(context), UrlBuilder.getApiKey(context));
        keepTrax.logout(true, new VoidCallback() {
            @Override
            public void onSuccess() {
                AppPreferences.deletePref(context);
                navigateToLogin(context);
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable t) {
                AppPreferences.deletePref(context);
                navigateToLogin(context);
                dialog.dismiss();
            }
        });
    }
    private static void navigateToLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}

package com.example.priyanka.SmartCitizen.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.example.priyanka.SmartCitizen.R;
import com.example.priyanka.SmartCitizen.callback.LoadEventCallback;
import com.example.priyanka.SmartCitizen.controller.EntryController;
import com.example.priyanka.SmartCitizen.utils.Globals;
import com.example.priyanka.SmartCitizen.utils.UrlBuilder;
import com.keeptraxinc.cachemanager.dao.User;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;
import com.keeptraxinc.utils.logger.Logger;

public class SplashActivity extends AppCompatActivity implements LoadEventCallback {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();
    Boolean mBackPress = false;
    private EntryController mEntryController;
    //q7nt8q5b

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            getSupportActionBar().hide();
        } catch (NullPointerException npe) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        new CountDownTimer(2000, 100) {
            @Override
            public void onFinish() {
                if (!mBackPress) {
                    String url = UrlBuilder.getUrl(SplashActivity.this);
                    KeepTrax keepTrax = KeepTraxImpl.getInstance(SplashActivity.this, url, UrlBuilder.getApiKey(SplashActivity.this));
                    final User user = keepTrax.getUser();
                    try {
                        Logger.debug(SplashActivity.this, LOG_TAG, "versionName " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    if (user != null) {
                        gotoHome();
                    } else {
                        gotoLogin();
                    }
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
            }
        }.start();
    }

    private void gotoLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void gotoHome() {
        mEntryController = new EntryController(this);
        mEntryController.init(this);

    }


    @Override
    public void onBackPressed() {
        mBackPress = true;
        finish();
    }

    @Override
    public void loadListOfShows() {
        if (Globals.dialog != null && Globals.dialog.isShowing())
            Globals.dialog.dismiss();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

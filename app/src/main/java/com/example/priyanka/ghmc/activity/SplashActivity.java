package com.example.priyanka.ghmc.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.priyanka.ghmc.R;

public class SplashActivity extends AppCompatActivity {
    Boolean mBackPress = false;

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
                    gotoLogin();
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

    @Override
    public void onBackPressed() {
        mBackPress = true;
        finish();
    }

}

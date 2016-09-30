package com.example.priyanka.ghmc.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.priyanka.ghmc.viewbinder.activity.GrievancePostActivityVB;
import com.example.priyanka.ghmc.viewbinder.activity.GrievanceStatusActivityVB;

/**
 * Created by Priyanka on 30/09/16.
 */

public class GrievanceStatusActivity extends BaseActivity {
    private GrievanceStatusActivityVB viewBinder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewBinder();
    }

    private void initViewBinder() {
        viewBinder = new GrievanceStatusActivityVB(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewBinder.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewBinder.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewBinder.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewBinder.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewBinder.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewBinder = null;
    }
}


package com.example.priyanka.ghmc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.example.priyanka.ghmc.viewbinder.activity.GrievancePostActivityVB;


public class GrievancePostActivity extends BaseActivity {
    private GrievancePostActivityVB viewBinder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewBinder();
    }

    private void initViewBinder() {
        viewBinder = new GrievancePostActivityVB(this);
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

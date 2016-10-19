package com.example.priyanka.SmartCitizen.activity;

import android.os.Bundle;

import com.example.priyanka.SmartCitizen.viewbinder.activity.GrievanceDetailActivityVB;

public class GrievanceDetailActivity extends BaseActivity {
    private GrievanceDetailActivityVB viewBinder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewBinder();
    }

    private void initViewBinder() {
        viewBinder = new GrievanceDetailActivityVB(this);
    }


}

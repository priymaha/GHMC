package com.example.priyanka.SmartCitizen.activity;

import android.os.Bundle;

import com.example.priyanka.SmartCitizen.viewbinder.activity.EventDetailActivityVB;

public class EventDetailActivity extends BaseActivity {
    private EventDetailActivityVB viewBinder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewBinder();
    }

    private void initViewBinder() {
        viewBinder = new EventDetailActivityVB(this);
    }


}

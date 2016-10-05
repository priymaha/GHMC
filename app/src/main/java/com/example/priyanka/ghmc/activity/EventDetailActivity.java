package com.example.priyanka.ghmc.activity;

import android.os.Bundle;

import com.example.priyanka.ghmc.viewbinder.activity.EventDetailActivityVB;

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

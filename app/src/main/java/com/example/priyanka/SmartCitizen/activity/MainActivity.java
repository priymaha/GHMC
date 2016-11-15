package com.example.priyanka.SmartCitizen.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.priyanka.SmartCitizen.viewbinder.activity.MainActivityVB;

public class MainActivity extends BaseActivity {

    private MainActivityVB viewBinder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewBinder();
    }

    private void initViewBinder() {
        viewBinder = new MainActivityVB(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        viewBinder.handleOptionsSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu item) {
        viewBinder.onCreateOptionsMenu(item);
        return super.onCreateOptionsMenu(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewBinder.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewBinder.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewBinder = null;
    }

}


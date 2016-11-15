package com.example.priyanka.SmartCitizen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.example.priyanka.SmartCitizen.viewbinder.activity.HomeActivityVB;

public class HomeActivity extends BaseActivity {
    private HomeActivityVB viewBinder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewBinder();
    }

    private void initViewBinder() {
        viewBinder = new HomeActivityVB(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        viewBinder.handleOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        viewBinder.handleOptionsSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        viewBinder.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

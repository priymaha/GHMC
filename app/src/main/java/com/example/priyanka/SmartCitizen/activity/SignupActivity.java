package com.example.priyanka.SmartCitizen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.priyanka.SmartCitizen.viewbinder.activity.SignupActivityVB;

/**
 * Created by sahul on 9/20/16.
 */

public class SignupActivity extends BaseActivity{
    private SignupActivityVB viewBinder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewBinder();
    }

    private void initViewBinder() {
        viewBinder = new SignupActivityVB(this);
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
}

package com.example.priyanka.SmartCitizen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.priyanka.SmartCitizen.viewbinder.activity.GrievanceStatusActivityVB;

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

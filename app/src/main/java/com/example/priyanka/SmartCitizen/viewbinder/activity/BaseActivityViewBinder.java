
package com.example.priyanka.SmartCitizen.viewbinder.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyanka.SmartCitizen.R;


public abstract class BaseActivityViewBinder implements ActivityViewBinder {

    public AppCompatActivity activity;
    public Context context;
    public View contentView;
    public Resources resources;
    public Bundle savedInstanceState;

    public BaseActivityViewBinder(AppCompatActivity activity) {

        this.activity = activity;
        context = activity;
        resources = activity.getResources();
        init();
    }

    public BaseActivityViewBinder(AppCompatActivity activity, boolean isLoggedIn) {

        this.activity = activity;
        context = activity;
        resources = activity.getResources();
        init();

    }

    public BaseActivityViewBinder(AppCompatActivity activity, Bundle savedInstanceState) {

        this.activity = activity;
        context = activity;
        resources = activity.getResources();
        this.savedInstanceState = savedInstanceState;
        init();

    }

    @Override
    public void init() {

        initContentView();
        initViews();
        initBackgroundColor();
        initViewListeners();
        onInitFinish();
    }

    public void showShortToast(String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    public void showLongToast(String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }

    public void setText(TextView textView, String value) {
        if (textView != null) {
            if (value != null) {
                String newValue = value.replace("null", "");
                textView.setText(newValue);
            } else {
                textView.setText("");
            }
        }
    }

    public Intent getIntent() {
        if (activity != null) {
            return activity.getIntent();
        }
        return null;
    }

    public void finishActivity() {
        if (activity != null) {
            activity.finish();
        }
    }

    private ProgressDialog getProgressDialogInternal() {

        return new ProgressDialog(context);

    }

    public ProgressDialog getProgressDialog() {

        ProgressDialog progressDialog = getProgressDialogInternal();
        progressDialog.setMessage(context.getString(R.string.please_wait));
        return progressDialog;

    }

    protected ProgressDialog getProgressDialog(String message) {

        ProgressDialog progressDialog = getProgressDialogInternal();
        progressDialog.setMessage(message);
        return progressDialog;

    }

    /*protected void setColor(final View view, Setting setting) {
        //wanted not adding null check, because view should not be null here

        if (setting != null) {
            view.setBackgroundColor(Color.parseColor(setting.getValue()));
        }
    }

    protected void setColor(final ViewGroup viewGroup, Setting setting) {
        if (setting != null) {
            viewGroup.setBackgroundColor(Color.parseColor(setting.getValue()));
        }
    }*/

    protected void setColor(final View view, int color) {
        //wanted not adding null check, because view should not be null here
        if (color != 0) {
            view.setBackgroundColor(color);
        }
    }

    protected void setColor(final ViewGroup viewGroup, int color) {
        if (color != 0) {
            viewGroup.setBackgroundColor(color);
        }
    }

   /* public void onEvent(SearchModel eventModel){

    }*/

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStop() {

    }

    public void onStart() {

    }

    public void onDestroy() {

    }

}
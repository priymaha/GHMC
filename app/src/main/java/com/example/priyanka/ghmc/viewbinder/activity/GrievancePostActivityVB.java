

package com.example.priyanka.ghmc.viewbinder.activity;

import android.content.Intent;;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;


import com.example.priyanka.ghmc.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class GrievancePostActivityVB extends BaseActivityViewBinder {


    public GrievancePostActivityVB(AppCompatActivity activity) {
        super(activity);
    }


    @Override
    public void initContentView() {
        LayoutInflater inflater = activity.getLayoutInflater();
        contentView = inflater.inflate(R.layout.activity_grievance_post, null);
        if (contentView != null) {
            activity.setContentView(contentView);
        }
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initBackgroundColor() {

    }

    @Override
    public void initViewListeners() {

    }

    @Override
    public void onInitFinish() {

    }



    @Override
    public boolean handleOptionsSelected(int itemId) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {

    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();


    }


}

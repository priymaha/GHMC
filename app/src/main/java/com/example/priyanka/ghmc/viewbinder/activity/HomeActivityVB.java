package com.example.priyanka.ghmc.viewbinder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.priyanka.ghmc.R;
import com.example.priyanka.ghmc.activity.GrievancePostActivity;

/**
 * Created by sahul on 9/21/16.
 */

public class HomeActivityVB extends BaseActivityViewBinder {

    private ImageView mGrievanceIV;
    private ImageView mVolunteerIV;
    private ImageView mPointsIV;

    public  HomeActivityVB(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void initContentView() {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LayoutInflater inflater = activity.getLayoutInflater();
        contentView = inflater.inflate(R.layout.activity_home, null);
        if (contentView != null) {
            activity.setContentView(contentView);
        }
    }
    @Override
    public void initViews() {
        mGrievanceIV = (ImageView) contentView.findViewById(R.id.home_grievance_iv);
        mVolunteerIV = (ImageView) contentView.findViewById(R.id.home_volunteer_iv);
        mPointsIV = (ImageView) contentView.findViewById(R.id.home_points_iv);
    }

    @Override
    public void initBackgroundColor() {

    }

    @Override
    public void initViewListeners() {
        if (mGrievanceIV != null) {
           mGrievanceIV.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   gotoGrievance();
               }
           });
        }

        if (mVolunteerIV != null) {
            mVolunteerIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //gotoVolunteer();
                    Toast.makeText(activity,"gotoVolunteer",Toast.LENGTH_LONG).show();
                }
            });
        }

        if (mPointsIV != null) {
            mPointsIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity,"Participate in the social activities to earn points",Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    public void gotoGrievance(){
        Intent intent = new Intent(activity, GrievancePostActivity.class);
        activity.startActivity(intent);
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

    }
}

package com.example.priyanka.ghmc.viewbinder.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyanka.ghmc.R;
import com.example.priyanka.ghmc.activity.GrievancePostActivity;
import com.example.priyanka.ghmc.activity.GrievanceStatusActivity;
import com.example.priyanka.ghmc.activity.LoginActivity;
import com.example.priyanka.ghmc.utils.AppPreferences;
import com.example.priyanka.ghmc.utils.Constants;
import com.example.priyanka.ghmc.utils.UrlBuilder;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;
import com.strongloop.android.loopback.callbacks.VoidCallback;

/**
 * Created by sahul on 9/21/16.
 */

public class HomeActivityVB extends BaseActivityViewBinder {

    private TextView mGrievanceTV;
    private TextView mVolunteerTV;
    private TextView mPointsTV;
    private  String mUserName;

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
        mGrievanceTV = (TextView) contentView.findViewById(R.id.home_grievance_tv);
        mVolunteerTV = (TextView) contentView.findViewById(R.id.home_volunteer_tv);
        mPointsTV = (TextView) contentView.findViewById(R.id.home_points_tv);
    }

    @Override
    public void initBackgroundColor() {

    }

    @Override
    public void initViewListeners() {
        if (mGrievanceTV != null) {
            mGrievanceTV.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   gotoGrievance();
               }
           });
        }

        if (mVolunteerTV != null) {
            mVolunteerTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //gotoVolunteer();
                    Toast.makeText(activity,"gotoVolunteer",Toast.LENGTH_LONG).show();
                }
            });
        }

        if (mPointsTV != null) {
            mPointsTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity,"Participate in the social activities to earn points",Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    public void gotoGrievance(){
        Intent intent = new Intent(activity, GrievanceStatusActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onInitFinish() {
        /*mUserName = AppPreferences.getValue(Constants.USER_NAME, context);
        showShortToast(mUserName);*/
    }

    @Override
    public boolean handleOptionsSelected(int itemId) {
        switch (itemId) {
            case R.id.about:
                Toast.makeText(activity,"version 1.0",Toast.LENGTH_LONG).show();
                return true;
            case R.id.logout:
                logoutFromApp();
                return true;
            default:
                return true;
        }
    }

    public void logoutFromApp(){
        /*KeepTrax keepTrax = KeepTraxImpl.getInstance(context, UrlBuilder.getUrl(context), UrlBuilder.getApiKey(context));
        keepTrax.logout(true, new VoidCallback() {
            @Override
            public void onSuccess() {
                AppPreferences.deletePref(activity);
                navigateToLogin(context);
            }

            @Override
            public void onError(Throwable t) {
                AppPreferences.deletePref(activity);
                navigateToLogin(context);
            }
        });*/
        AppPreferences.deletePref(activity);
        navigateToLogin(context);
    }

    private static void navigateToLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    public boolean handleOptionsMenu(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {

    }

    @Override
    public void onBackPressed() {

    }

}

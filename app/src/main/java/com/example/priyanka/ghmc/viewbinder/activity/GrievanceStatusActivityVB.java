package com.example.priyanka.ghmc.viewbinder.activity;

import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.priyanka.ghmc.R;
import com.example.priyanka.ghmc.activity.GrievancePostActivity;
import com.example.priyanka.ghmc.activity.GrievanceStatusActivity;
import com.example.priyanka.ghmc.adapter.GrievanceStatusFragmentAdapter;

/**
 * Created by Priyanka on 30/09/16.
 */

public class GrievanceStatusActivityVB extends BaseActivityViewBinder implements ActionBar.TabListener {
    private ViewPager viewPager;
    private ActionBar actionBar;
    public GrievanceStatusActivityVB(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void initContentView() {
        LayoutInflater inflater = activity.getLayoutInflater();
        contentView = inflater.inflate(R.layout.activity_grievance_status, null);
        if (contentView != null) {
            activity.setContentView(contentView);
        }
        activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        activity.getSupportActionBar().setCustomView(R.layout.status_layout);
//        activity.getSupportActionBar().setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ico_cam));
        activity.getSupportActionBar().getCustomView().findViewById(R.id.customIv).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent (activity, GrievancePostActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) contentView.findViewById(R.id.pager);
        createTabs();
        setViewPagerAdapter();

    }

    @Override
    public void initBackgroundColor() {

    }

    @Override
    public void initViewListeners() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onInitFinish() {

    }

    private void createTabs() {
        actionBar = activity.getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab openTab = actionBar.newTab();
        openTab.setText ("Open ");
        openTab.setTabListener(this);

        ActionBar.Tab closedTab = actionBar.newTab();
        closedTab.setText ("Closed ");
        closedTab.setTabListener(this);

        actionBar.addTab(openTab);
        actionBar.addTab(closedTab);
        activity.getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#1779DC")));
    }

    private void setViewPagerAdapter() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        viewPager.setAdapter(new GrievanceStatusFragmentAdapter(fragmentManager));
    }

    @Override
    public boolean handleOptionsSelected(int itemId) {
        switch (itemId) {
            case R.id.action_grievance:
                Toast.makeText(activity,"version 1.0",Toast.LENGTH_LONG).show();
                return true;

            default:
                return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {

    }
    public boolean handleOptionsMenu(Menu menu) {
       /* MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.add_grievance, menu);
        return true;*/
        return false;
    }
    @Override
    public void onBackPressed() {

    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
    public void toolbarClick(){
        Toast.makeText(activity,"hello",Toast.LENGTH_LONG).show();

    }
}

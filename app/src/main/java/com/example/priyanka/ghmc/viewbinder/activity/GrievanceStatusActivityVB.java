package com.example.priyanka.ghmc.viewbinder.activity;

import android.app.FragmentTransaction;
import android.content.Intent;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.example.priyanka.ghmc.R;
import com.example.priyanka.ghmc.adapter.GrievanceStatusFragmentAdapter;

/**
 * Created by Priyanka on 30/09/16.
 */

public class GrievanceStatusActivityVB extends BaseActivityViewBinder implements ActionBar.TabListener {
    ViewPager viewPager = null;
    ActionBar actionBar;
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
       /* try {
            activity.getSupportActionBar().hide();
        } catch (NullPointerException npe) {

        }*/

    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) contentView.findViewById(R.id.pager);

    }

    @Override
    public void initBackgroundColor() {

    }

    @Override
    public void initViewListeners() {

    }

    @Override
    public void onInitFinish() {
        createTabs();
        setViewPagerAdapter();
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
    }

    private void setViewPagerAdapter() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        viewPager.setAdapter(new GrievanceStatusFragmentAdapter(fragmentManager));
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


    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}

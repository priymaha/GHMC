package com.example.priyanka.SmartCitizen.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.priyanka.SmartCitizen.fragments.GrievanceStatusFragment;

/**
 * Created by Priyanka on 30/09/16.
 */

public class GrievanceStatusFragmentAdapter extends FragmentPagerAdapter {
    public GrievanceStatusFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return GrievanceStatusFragment.newInstance();
            case 1:
                return GrievanceStatusFragment.newInstance();
            default:
                return GrievanceStatusFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
/* Commenting code for scrollable tabs*/
   /* @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Open";
            case 1:
                return "Closed";
            default:
                return "Open";
        }
    }*/
}

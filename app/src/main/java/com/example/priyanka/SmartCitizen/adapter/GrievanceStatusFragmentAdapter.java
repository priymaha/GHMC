package com.example.priyanka.SmartCitizen.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.priyanka.SmartCitizen.fragments.GrievanceClosedStatusFragment;
import com.example.priyanka.SmartCitizen.fragments.GrievanceOpenStatusFragment;

/**
 * Created by Priyanka on 30/09/16.
 */

public class GrievanceStatusFragmentAdapter extends FragmentPagerAdapter {
    public static int int_items = 2 ;
    public GrievanceStatusFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return GrievanceOpenStatusFragment.newInstance();
            case 1:
                return GrievanceClosedStatusFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Open";
            case 1:
                return "Closed";
        }
        return null;
    }
}

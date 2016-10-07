

package com.example.priyanka.SmartCitizen.viewbinder.activity;

import android.content.Intent;

/**
 * This class has all the viewbinder methods to be implemented in all the screens
 */
public interface ActivityViewBinder {

    void init();

    void initContentView();

    void initViews();

    /**
     * Initialise the custom background color of views/viewgroup
     */
    void initBackgroundColor();


    void initViewListeners();

    /**
     * This method indicates the all the init operations are finished
     * Post init operations can be added in this method
     */
    void onInitFinish();

    boolean handleOptionsSelected(int itemId);

    void onResume();

    void onActivityResult(int requestCode, int responseCode, Intent data);

    void onBackPressed();

}
 
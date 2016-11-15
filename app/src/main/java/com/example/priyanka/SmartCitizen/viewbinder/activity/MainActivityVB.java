package com.example.priyanka.SmartCitizen.viewbinder.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.priyanka.SmartCitizen.R;
import com.example.priyanka.SmartCitizen.activity.GrievancePostActivity;
import com.example.priyanka.SmartCitizen.activity.MainActivity;
import com.example.priyanka.SmartCitizen.fragments.GrievanceStatusFragment;
import com.example.priyanka.SmartCitizen.fragments.RewardsFragment;
import com.example.priyanka.SmartCitizen.utils.AppPreferences;
import com.example.priyanka.SmartCitizen.utils.AppUtils;
import com.example.priyanka.SmartCitizen.utils.InstallVerifier;
import com.example.priyanka.SmartCitizen.utils.PermissionsHelper;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.utils.helper.NetworkInfo;
import com.keeptraxinc.utils.logger.Logger;

/**
 * Created by Priyanka on 15/11/16.
 */

public class MainActivityVB extends BaseActivityViewBinder {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    PermissionsHelper permissionsHelper;
    LocationManager locationManager;
    boolean gps_enabled;
    boolean isLocationEnabled = false;
    private Boolean newInstallation;
    private Boolean mAlreadyEnabled = false;
    private boolean checkStoragePermission;
    private ProgressDialog dialog;
    private KeepTrax keepTrax;
    private View parentLayout;
    private Toolbar toolbar;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    public MainActivityVB(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void initContentView() {
        LayoutInflater inflater = activity.getLayoutInflater();
        contentView = inflater.inflate(R.layout.activity_main, null);
        if (contentView != null) {
            activity.setContentView(contentView);
        }
    }

    @Override
    public void initViews() {
        parentLayout = contentView.findViewById(R.id.parentLayout);
        toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) contentView.findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) contentView.findViewById(R.id.drawer);
    }

    @Override
    public void initBackgroundColor() {

    }

    @Override
    public void initViewListeners() {

    }

    @Override
    public void onInitFinish() {


        activity.setSupportActionBar(toolbar);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = activity.getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new GrievanceStatusFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */
        mUserLearnedDrawer = AppPreferences.getBooleanValue(KEY_USER_LEARNED_DRAWER, activity);
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();


                if (menuItem.getItemId() == R.id.nav_item_rewards) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new RewardsFragment()).commit();

                }

                if (menuItem.getItemId() == R.id.nav_item_grievance) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new GrievanceStatusFragment()).commit();
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    AppPreferences.setBooleanValue(KEY_USER_LEARNED_DRAWER, false, activity);
                }
                activity.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                activity.invalidateOptionsMenu();
            }

        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }

    @Override
    public boolean handleOptionsSelected(int itemId) {
        switch (itemId) {
            case R.id.action_grievance:
                Intent intent = new Intent (activity, GrievancePostActivity.class);
                activity.startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkInfo.isNetworkAvailable(activity)) {
            if (AppUtils.isInternetAccessible(activity)) {
                checkStoragePermission = true;
                checkGPS();
            } else {
                AppUtils.showNoInternetAccessibleAlert(activity);
            }
        } else {
            AppUtils.showWiFiSettingsAlert(activity);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {

    }

    @Override
    public void onBackPressed() {

    }

    private void checkGPS() {
        if (locationManager == null)
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gps_enabled) {
            if (checkStoragePermission) {
                PermissionsHelper helper = new PermissionsHelper(activity);
                if (helper.isReadStorageAllowed()) {
                    checkRequiredPermissions();
                } else {
                    helper.requestReadStoragePermission();
                }
            } else {
                checkRequiredPermissions();
            }
        } else {
            if (isLocationEnabled)
                return;
            isLocationEnabled = true;
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage(activity.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setCancelable(false);
            dialog.setPositiveButton(activity.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivityForResult(myIntent, 0);
                    isLocationEnabled = false;
                }
            });
            dialog.show();
        }
    }


    private void checkRequiredPermissions() {
        checkNewInstallProcess();
        permissionsHelper = new PermissionsHelper(activity);
        if (permissionsHelper.isLocationAllowed()) {

            if (!mAlreadyEnabled) {
                onPrerequisitesDone();
            }
            mAlreadyEnabled = true;

        } else {
            if (newInstallation) {
                //as new installation, can request permission, need not check rationale
                newInstallation = false;
                mAlreadyEnabled = false;
                permissionsHelper.requestLocationPermission();
            } else {
                //location permission is not available, check if we can request permission
                boolean showRequest = permissionsHelper.shouldShowRequestRationale(Manifest.permission.ACCESS_FINE_LOCATION);
                if (showRequest) {
                    //user has denied before
                    //already asked before, do not ask now, show snackbar
                    mAlreadyEnabled = false;
                    showLocationPermissionSnackBar();
                } else {
                    //user have selected never ask again, thus do not ask but show info on snackbar
                    mAlreadyEnabled = false;
                    showSettingsSnackBar();
                }
            }
        }
    }

    /**
     * This method checks if this app open instance is a new installation
     * or a normal app open
     */
    private void checkNewInstallProcess() {

        //check if this is a new install
        if (InstallVerifier.isUpdate(activity)) {
            Logger.debug(activity, LOG_TAG, "Re-Installation, thus restart SE");
            newInstallation = true;
        } else {
            //make explicitely false, because after its true,
            //the subsequent HomeActivity calls should be treated this false
            newInstallation = false;
            Logger.debug(activity, LOG_TAG, "This is not install/update");
        }
    }

    public void onPrerequisitesDone() {
    }

    public void showLocationPermissionSnackBar() {

        Snackbar snackbar = Snackbar
                .make(parentLayout, R.string.home_snackbar_location, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_action_allow, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            PermissionsHelper permissionHelper = new PermissionsHelper(activity);
                            if (permissionHelper.shouldShowRequestRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                permissionHelper.requestLocationPermission();
                            } else {
                                showSettingsSnackBar();
                            }
                        }
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.YELLOW);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackbar.show();
    }

    public void showSettingsSnackBar() {
        Snackbar
                .make(parentLayout, R.string.home_snackbar_settings, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_action_settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //open application settings screen
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", activity.getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }
                        }
                ).show();
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case PermissionsHelper.REQUEST_ACCESS_FINE_LOC:
                handleLocationPermissionsResult(grantResults);
                break;
            default:
                break;
        }
    }

    /**
     * if the permission is allowed, to do
     * if permisison is not allowed, ask calendar permission and start tracking
     *
     * @param grantResults
     */
    private void handleLocationPermissionsResult(int[] grantResults) {

        if (PermissionsHelper.isAllowed(grantResults)) {

        } else {
            //location permission is not allowed, show snack bar
            showLocationPermissionSnackBar();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.add_grievance, menu);
        return true;
    }

}

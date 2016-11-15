package com.example.priyanka.SmartCitizen.activity;

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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.priyanka.SmartCitizen.R;
import com.example.priyanka.SmartCitizen.fragments.GrievanceStatusFragment;
import com.example.priyanka.SmartCitizen.fragments.RewardsFragment;
import com.example.priyanka.SmartCitizen.utils.AppPreferences;
import com.example.priyanka.SmartCitizen.utils.AppUtils;
import com.example.priyanka.SmartCitizen.utils.InstallVerifier;
import com.example.priyanka.SmartCitizen.utils.PermissionsHelper;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.utils.helper.NetworkInfo;
import com.keeptraxinc.utils.logger.Logger;

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentLayout = findViewById(R.id.parentLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        setSupportActionBar(toolbar);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new GrievanceStatusFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */
        mUserLearnedDrawer = AppPreferences.getBooleanValue(KEY_USER_LEARNED_DRAWER, this);
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

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    AppPreferences.setBooleanValue(KEY_USER_LEARNED_DRAWER, false, MainActivity.this);
                }
               invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }


    @Override
    public void onResume() {
        super.onResume();
        if (NetworkInfo.isNetworkAvailable(this)) {
            if (AppUtils.isInternetAccessible(this)) {
                checkStoragePermission = true;
                checkGPS();
            } else {
                AppUtils.showNoInternetAccessibleAlert(this);
            }
        } else {
            AppUtils.showWiFiSettingsAlert(this);
        }
    }

    private void checkGPS() {
        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gps_enabled) {
            if (checkStoragePermission) {
                PermissionsHelper helper = new PermissionsHelper(this);
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setCancelable(false);
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(myIntent, 0);
                    isLocationEnabled = false;
                }
            });
            dialog.show();
        }
    }


    private void checkRequiredPermissions() {
        checkNewInstallProcess();
        permissionsHelper = new PermissionsHelper(this);
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
        if (InstallVerifier.isUpdate(this)) {
            Logger.debug(this, LOG_TAG, "Re-Installation, thus restart SE");
            newInstallation = true;
        } else {
            //make explicitely false, because after its true,
            //the subsequent HomeActivity calls should be treated this false
            newInstallation = false;
            Logger.debug(this, LOG_TAG, "This is not install/update");
        }
    }

    public void onPrerequisitesDone() {
       /* getKeepTraxInstance();
        keepTrax.start();*/
      /*  showShortToast("onPrerequisitesDone");
        mGrievanceTV.setAlpha(1f);
        mGrievanceTV.setEnabled(true);*/
       /* getKeepTraxInstance();
        Globals.getEvents(this,getWhereClause());*/

    }

    public void showLocationPermissionSnackBar() {

        Snackbar snackbar = Snackbar
                .make(parentLayout, R.string.home_snackbar_location, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_action_allow, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            PermissionsHelper permissionHelper = new PermissionsHelper(MainActivity.this);
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
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_grievance, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_grievance:
                Intent intent = new Intent (this, GrievancePostActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


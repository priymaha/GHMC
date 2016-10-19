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
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyanka.SmartCitizen.R;
import com.example.priyanka.SmartCitizen.activity.GrievanceStatusActivity;
import com.example.priyanka.SmartCitizen.activity.LoginActivity;
import com.example.priyanka.SmartCitizen.utils.AppPreferences;
import com.example.priyanka.SmartCitizen.utils.AppUtils;
import com.example.priyanka.SmartCitizen.utils.Constants;
import com.example.priyanka.SmartCitizen.utils.Globals;
import com.example.priyanka.SmartCitizen.utils.InstallVerifier;
import com.example.priyanka.SmartCitizen.utils.PermissionsHelper;
import com.example.priyanka.SmartCitizen.utils.UrlBuilder;
import com.keeptraxinc.cachemanager.dao.EventDao;
import com.keeptraxinc.cachemanager.query.WhereClause;
import com.keeptraxinc.cachemanager.query.WhereSimple;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;
import com.keeptraxinc.utils.helper.DateUtils;
import com.keeptraxinc.utils.helper.NetworkInfo;
import com.keeptraxinc.utils.logger.Logger;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sahul on 9/21/16.
 */

public class HomeActivityVB extends BaseActivityViewBinder {

    private static final String LOG_TAG = HomeActivityVB.class.getSimpleName();
    PermissionsHelper permissionsHelper;
    LocationManager locationManager;
    boolean gps_enabled;
    boolean isLocationEnabled = false;
    private TextView mGrievanceTV;
    private TextView mVolunteerTV;
    private TextView mPointsTV;
    private String mUserName;
    private Boolean newInstallation;
    private Boolean mAlreadyEnabled = false;
    private boolean checkStoragePermission;
    private ProgressDialog dialog;
    private KeepTrax keepTrax;


    public HomeActivityVB(AppCompatActivity activity) {
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

        activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        activity.getSupportActionBar().setCustomView(R.layout.status_layout);
        activity.getSupportActionBar().getCustomView().findViewById(R.id.customIv).setVisibility(View.GONE);
        TextView mTitleTV = (TextView) activity.getSupportActionBar().getCustomView().findViewById(R.id.ic_actionbar_title);
        mTitleTV.setText("Smart Citizen");
        mTitleTV.setPadding(10,0,0,0);
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
                    getKeepTraxInstance();
                    Globals.getEvents(activity,getWhereClause());

                    gotoGrievance();
                }
            });
        }

        if (mVolunteerTV != null) {
            mVolunteerTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //gotoVolunteer();
                    Toast.makeText(activity, "gotoVolunteer", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (mPointsTV != null) {
            mPointsTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "Participate in the social activities to earn points", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void gotoGrievance() {
        Intent intent = new Intent(activity, GrievanceStatusActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onInitFinish() {
        mGrievanceTV.setAlpha(0.3f);
        mGrievanceTV.setEnabled(false);
    }

    @Override
    public boolean handleOptionsSelected(int itemId) {
        switch (itemId) {
            case R.id.about:
                Toast.makeText(activity, "version 1.0", Toast.LENGTH_LONG).show();
                return true;
            case R.id.logout:
                AppUtils.logoutFromApp(activity);
                return true;
            default:
                return true;
        }
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

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkInfo.isNetworkAvailable(context)) {
            if (AppUtils.isInternetAccessible(activity)) {
                checkStoragePermission = true;
                checkGPS();
            } else {
                AppUtils.showNoInternetAccessibleAlert(context);
            }
        } else {
            AppUtils.showWiFiSettingsAlert(context);
        }
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
        if (InstallVerifier.isUpdate(context)) {
            Logger.debug(context, LOG_TAG, "Re-Installation, thus restart SE");
            newInstallation = true;
        } else {
            //make explicitely false, because after its true,
            //the subsequent HomeActivity calls should be treated this false
            newInstallation = false;
            Logger.debug(context, LOG_TAG, "This is not install/update");
        }
    }

    public void onPrerequisitesDone() {
       /* getKeepTraxInstance();
        keepTrax.start();*/
        showShortToast("onPrerequisitesDone");
        mGrievanceTV.setAlpha(1f);
        mGrievanceTV.setEnabled(true);

    }

    private WhereClause getWhereClause() {
        WhereClause wc = WhereSimple.le(EventDao.Properties.Start.name, DateUtils.getISOTime(System.currentTimeMillis()))
                        .and(WhereSimple.eq(EventDao.Properties.UserId.name, keepTrax.getUser().getId()))
                        .and(WhereSimple.eq(EventDao.Properties.Status.name, Constants.CREATED));
        return wc;
    }

    private void getKeepTraxInstance() {
        if (keepTrax != null) {
            return;
        }
        keepTrax = KeepTraxImpl.getInstance(activity, UrlBuilder.getUrl(activity), UrlBuilder.getApiKey(activity));
    }

    public void showLocationPermissionSnackBar() {

        Snackbar snackbar = Snackbar
                .make(contentView, R.string.home_snackbar_location, Snackbar.LENGTH_LONG)
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
                .make(contentView, R.string.home_snackbar_settings, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_action_settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //open application settings screen
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", context.getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
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


}

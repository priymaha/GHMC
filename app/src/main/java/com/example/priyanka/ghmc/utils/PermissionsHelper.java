/**
 * Copyright (C) 2016 KeepTrax, Inc. All Rights Reserved.
 * <p>
 * NOTICE:  All information contained herein is, and remains the property of
 * KeepTrax Incorporated.  The intellectual and technical concepts contained
 * herein are proprietary to KeepTrax Incorporated and may be covered by U.S.
 * and Foreign Patents, patents in process, and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction of this
 * material as a whole or in part is strictly forbidden unless prior written
 * permission is obtained from KeepTrax Incorporated.
 */
package com.example.priyanka.ghmc.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionsHelper {

    public static final int REQUEST_READ_CONTACTS = 101;
    public static final int REQUEST_CAMERA = 102;
    public static final int REQUEST_ACCESS_FINE_LOC = 103;
    public static final int REQUEST_READ_CALENDER = 105;
    public static final int REQUEST_READ_PHONE_STATE = 106;
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 107;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 108;

    private Activity activity;

    public PermissionsHelper(Activity activity) {
        this.activity = activity;
    }

    public static boolean isAllowed(int[] results) {
        return results != null
                && results.length > 0
                && results[0] == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Called when the user is performing an action which requires a permission grant
     * Checks if we need to ask the permission
     * check only if the SDK version >= 23(marshmallow)
     * also check if we have asked this user before
     * else show a snackbar and take to settings on its click
     *
     * @return
     */
    private boolean isPermissionAvailable(String permission) {

        //check if permission is already available
//if available, then need not ask
//else ask permission from the user
// 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
// checking the build version since Context.checkSelfPermission(...) is only available
// in Marshmallow
// 2) Always check for permission (even if permission has already been granted)
// since the user can revoke permissions at any time through Settings
//permission is already available
//permission not available
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * To help find the situations where you need to provide extra explanation, the system provides the Activity.shouldShowRequest(String) method.
     * This method returns true if the app has requested this permission previously and the user denied the request.
     * That indicates that you should probably explain to the user why you need the permission.
     * If the user turned down the permission request in the past and chose the Don't ask again option in the permission request system dialog, this method returns false.
     * The method also returns false if the device policy prohibits the app from having that permission.
     *
     * @param permission
     * @return
     */
    public boolean shouldShowRequestRationale(String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.

            if (activity.shouldShowRequestPermissionRationale(permission)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
                // show in snackbar
                return true;
            }
        }
        return false;
    }

    private void requestPermission(String permission, int requestCode) {

        ActivityCompat.requestPermissions(activity,
                new String[]{permission},
                requestCode);
    }

    public boolean isLocationAllowed() {
        return isPermissionAvailable(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public void requestLocationPermission() {

        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                REQUEST_ACCESS_FINE_LOC);
    }

    public boolean isCalendarAllowed() {
        return isPermissionAvailable(Manifest.permission.READ_CALENDAR);
    }

    public void requestCalendarPermission() {

        requestPermission(Manifest.permission.READ_CALENDAR,
                REQUEST_READ_CALENDER);
    }

    public boolean isContactsAllowed() {
        return isPermissionAvailable(Manifest.permission.READ_CONTACTS);
    }

    public void requestContactsPermission() {

        requestPermission(Manifest.permission.READ_CONTACTS,
                REQUEST_READ_CONTACTS);
    }

    public boolean isStorageAllowed() {
        return isPermissionAvailable(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public boolean isReadStorageAllowed() {
        return isPermissionAvailable(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public void requestReadStoragePermission() {

        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                REQUEST_READ_EXTERNAL_STORAGE);
    }

    public void requestStoragePermission() {

        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                REQUEST_WRITE_EXTERNAL_STORAGE);
    }

}

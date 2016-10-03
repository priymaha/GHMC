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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.keeptraxinc.pinmanager.util.SdkPreferences;
import com.keeptraxinc.utils.logger.Logger;

import java.util.Date;

/**
 * Created by sahul on 09/28/16.
 */
public class InstallVerifier {

    private static final String LOG_TAG = InstallVerifier.class.getSimpleName();

    public static boolean isUpdate(Context context) {

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo("com.example.priyanka.ghmc", PackageManager.GET_PERMISSIONS);

            Date installTime = new Date(packageInfo.firstInstallTime);
            Logger.debug(context, LOG_TAG, "Installed: " + installTime.toString());

            long lastUpdatedTime = packageInfo.lastUpdateTime; //Epoch Time
            Date updateTime = new Date(lastUpdatedTime);
            Logger.debug(context, LOG_TAG, "Updated: " + updateTime.toString());
            long savedInstallTime = SdkPreferences.getAppInstalledTime(context);
            if (lastUpdatedTime != 0) {
                if (lastUpdatedTime != savedInstallTime) {
                    SdkPreferences.setAppInstallTime(context, lastUpdatedTime);
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }
}

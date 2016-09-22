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

import com.keeptraxinc.cachemanager.CachePreferences;

/**
 * Created by sahul on 9/22/16.
 */
public class UrlBuilder {

    private static String SERVERURL = "http://sci.keeptraxapp.com/api/v4/";
    private static String APIKEY = "OWJlMjFjODlmNTgwZTZjNjNjNDdkMTRkZTkzZmJkYmE6Y2IyNjM0ODEzZWE0NGMxMjJjYzJiOGUwMmM5NDEyMDk1YzA5Y2MwZg==";

    public static String getUrl(Context context) {
        String baseurl = CachePreferences.getBaseUrl(context);
        return baseurl != null ? baseurl : SERVERURL;
    }

    public static String getApiKey(Context context) {
        String apiKey = CachePreferences.getApiKey(context);
        return apiKey != null ? apiKey : APIKEY;
    }
}

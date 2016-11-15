/**
 * Copyright (C) 2016 KeepTrax, Inc. All Rights Reserved.
 * <p/>
 * NOTICE:  All information contained herein is, and remains the property of
 * KeepTrax Incorporated.  The intellectual and technical concepts contained
 * herein are proprietary to KeepTrax Incorporated and may be covered by U.S.
 * and Foreign Patents, patents in process, and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction of this
 * material as a whole or in part is strictly forbidden unless prior written
 * permission is obtained from KeepTrax Incorporated.
 */

package com.example.priyanka.SmartCitizen.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.example.priyanka.SmartCitizen.callback.LoadEventCallback;
import com.example.priyanka.SmartCitizen.callback.ViewCallback;
import com.example.priyanka.SmartCitizen.utils.Globals;
import com.example.priyanka.SmartCitizen.utils.UrlBuilder;
import com.keeptraxinc.cachemanager.dao.EventDao;
import com.keeptraxinc.cachemanager.query.WhereClause;
import com.keeptraxinc.cachemanager.query.WhereSimple;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;

import com.keeptraxinc.utils.helper.DateUtils;

/**
 * Created by Priyanka on 04/04/16.
 */
public class EntryController  {
    private static final int ALERT_REQUEST_CODE = 198;
    private final String LOG_TAG = getClass().getSimpleName();
    private Activity activity;
    private KeepTrax keepTrax;

    private LoadEventCallback mLoadEventCallback;


    public EntryController(Activity activity) {
        this.activity = activity;

    }

    public void init(final LoadEventCallback mLoadEventCallback) {
        keepTrax = KeepTraxImpl.getInstance(activity, UrlBuilder.getUrl(activity), UrlBuilder.getApiKey(activity));
        this.mLoadEventCallback = mLoadEventCallback;
        getKeepTraxInstance();
        Globals.getEvents(activity,getWhereClause(),this.mLoadEventCallback);
    }

    private WhereClause getWhereClause() {
        WhereClause wc = WhereSimple.le(EventDao.Properties.Start.name, DateUtils.getISOTime(System.currentTimeMillis()))
               /* .and(WhereSimple.eq(EventDao.Properties.UserId.name, keepTrax.getUser().getId()))
                .and(WhereSimple.eq(EventDao.Properties.Status.name, Constants.CREATED))*/;
        return wc;
    }

    private void getKeepTraxInstance() {
        if (keepTrax != null) {
            return;
        }
        keepTrax = KeepTraxImpl.getInstance(activity, UrlBuilder.getUrl(activity), UrlBuilder.getApiKey(activity));
    }




}

package com.example.priyanka.SmartCitizen.utils;

import android.content.Context;

import com.example.priyanka.SmartCitizen.model.DataModel;
import com.example.priyanka.SmartCitizen.model.GrievanceStatusModel;
import com.keeptraxinc.cachemanager.PageToken;
import com.keeptraxinc.cachemanager.dao.Enterprise;
import com.keeptraxinc.cachemanager.dao.Event;
import com.keeptraxinc.cachemanager.dao.EventDao;
import com.keeptraxinc.cachemanager.query.ListCallback;
import com.keeptraxinc.cachemanager.query.WhereClause;
import com.keeptraxinc.cachemanager.query.WhereSimple;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;
import com.keeptraxinc.utils.helper.DateUtils;
import com.strongloop.android.loopback.callbacks.ObjectCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Priyanka on 12/10/16.
 */

public class Globals {
    public static List<Event> allGrievance = new ArrayList <Event> ();
    public static List<DataModel> allSampleData;

    public static GrievanceStatusModel dummyGrievanceStatusModel = new GrievanceStatusModel();
    private static KeepTrax keepTrax;

    public static void populateAdapterDataSet() {
        int size = Globals.allGrievance.size();
        int j;
        if (size > 0) {
            allSampleData.clear();
            for (int i = 0; i < size; i++) {
                DataModel dm = new DataModel();
                dm.setHeaderTitle(AppUtils.getFormattedDate(Globals.allGrievance.get(i).getStart(), Constants.HEADER_FORMAT).toUpperCase());
                ArrayList<GrievanceStatusModel> singleItem = new ArrayList<>();
                for (j = i; j < size; j++) {
                    if (AppUtils.getFormattedDate(Globals.allGrievance.get(i).getStart(), Constants.HEADER_FORMAT).toUpperCase().equals(AppUtils.getFormattedDate(Globals.allGrievance.get(j).getStart(), Constants.HEADER_FORMAT).toUpperCase())) {
                        getEventExtras(Globals.allGrievance.get(j));
                        GrievanceStatusModel grievanceStatusModel = new GrievanceStatusModel();
                        grievanceStatusModel.time = (AppUtils.getFormattedDate(Globals.allGrievance.get(j).getStart(), Constants.TIME_FORMAT));
                        grievanceStatusModel.type = dummyGrievanceStatusModel.type;
                        grievanceStatusModel.title = Globals.allGrievance.get(j).getName();
                        grievanceStatusModel.members = dummyGrievanceStatusModel.members;
                        singleItem.add(grievanceStatusModel);
                    } else {
                        break;
                    }
                }
                i = j;
                dm.setAllItemsInSection(singleItem);
                allSampleData.add(dm);
            }
        }
    }

    private static void getEventExtras(Event event) {

        if (event != null && event.getExtensions() != null && !event.getExtensions().isEmpty()) {
            Map<String, Object> map = (Map<String, Object>) event.getExtensions();
            try {
                if (map.containsKey(Constants.GRIEVANCE_TYPE)) {
                    dummyGrievanceStatusModel.type = (String) map.get(Constants.GRIEVANCE_TYPE);
                }
                if (map.containsKey(Constants.MEMBERS_GOING)) {
                    dummyGrievanceStatusModel.members = (String) map.get(Constants.MEMBERS_GOING);
                }

            } catch (Exception e) {
            }
        }
    }

    public static void getEnterpriseEvents(final Enterprise enterprise, WhereClause whereClause) {
        Globals.allGrievance.clear();
        enterprise.getEvents(whereClause, null, null, new ListCallback<Event>() {
            @Override
            public void onSuccess(PageToken pageToken, List<Event> list) {
                if (list != null && !list.isEmpty()) {
                    Globals.allGrievance.addAll(list);
                } else if (list != null && list.isEmpty()) {
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    public static void getEvents(Context context, final WhereClause whereClause) {
        getKeepTraxInstance(context);
        if (keepTrax.getUser() != null) {

            keepTrax.getUser().getEnterprise(new ObjectCallback<Enterprise>() {
                @Override
                public void onSuccess(Enterprise enterprise) {
                    if (enterprise != null) {
                        getEnterpriseEvents(enterprise,whereClause);
                    } else {

                    }
                }

                @Override
                public void onError(Throwable t) {

                }
            });
        }
    }
    private static void getKeepTraxInstance(Context context) {
        if (keepTrax != null) {
            return;
        }
        keepTrax = KeepTraxImpl.getInstance(context, UrlBuilder.getUrl(context), UrlBuilder.getApiKey(context));
    }

}

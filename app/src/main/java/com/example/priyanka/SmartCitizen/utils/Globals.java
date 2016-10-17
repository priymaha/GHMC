package com.example.priyanka.SmartCitizen.utils;

import android.app.ProgressDialog;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Priyanka on 12/10/16.
 */

public class Globals {
    public static List<Event> allGrievance = new ArrayList <Event> ();
    public static List<DataModel> allOpenSampleData = new ArrayList<>();
    public static List<DataModel> allClosedSampleData=new ArrayList<>();

    public static GrievanceStatusModel dummyGrievanceStatusModel = new GrievanceStatusModel();
    private static KeepTrax keepTrax;
    private static EventBus bus = EventBus.getDefault();
    public static ProgressDialog dialog;

    public static void populateAdapterDataSet(String status) {

        int size = Globals.allGrievance.size();
        int j;
        if (size > 0) {

            for (int i = 0; i < size; i++) {


                ArrayList<GrievanceStatusModel> singleOpenItem = new ArrayList<>();
                ArrayList<GrievanceStatusModel> singleClosedItem = new ArrayList<>();
                for (j = i; j < size; j++) {
                    if (AppUtils.getFormattedDate(Globals.allGrievance.get(i).getStart(), Constants.HEADER_FORMAT).toUpperCase().equals(AppUtils.getFormattedDate(Globals.allGrievance.get(j).getStart(), Constants.HEADER_FORMAT).toUpperCase()) &&
                            status.equals(Globals.allGrievance.get(j).getStatus())) {
                        getEventExtras(Globals.allGrievance.get(j));
                        GrievanceStatusModel grievanceStatusModel = new GrievanceStatusModel();
                        grievanceStatusModel.time = (AppUtils.getFormattedDate(Globals.allGrievance.get(j).getStart(), Constants.TIME_FORMAT));
                        grievanceStatusModel.type = dummyGrievanceStatusModel.type;
                        grievanceStatusModel.title = Globals.allGrievance.get(j).getName();
                        grievanceStatusModel.members = dummyGrievanceStatusModel.members;
                        grievanceStatusModel.status = Globals.allGrievance.get(j).getStatus();
                        if (status.equals(Constants.CREATED)) {
                            singleOpenItem.add(grievanceStatusModel);
                        }else if (status.equals(Constants.STARTED)){
                            singleClosedItem.add(grievanceStatusModel);
                        }
                    } else {
                        break;
                    }
                }
                if (singleOpenItem.size() >0) {
                    DataModel dmOpen = new DataModel();
                    dmOpen.setHeaderTitle(AppUtils.getFormattedDate(Globals.allGrievance.get(i).getStart(), Constants.HEADER_FORMAT).toUpperCase());
                    dmOpen.setAllItemsInSection(singleOpenItem);
                    allOpenSampleData.add(dmOpen);
                }
                if (singleClosedItem.size() >0) {
                    DataModel dmClosed = new DataModel();
                    dmClosed.setHeaderTitle(AppUtils.getFormattedDate(Globals.allGrievance.get(i).getStart(), Constants.HEADER_FORMAT).toUpperCase());
                    dmClosed.setAllItemsInSection(singleOpenItem);
                    allClosedSampleData.add(dmClosed);
                }
                i=j;
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

    public static void getEnterpriseEvents(final Enterprise enterprise, WhereClause whereClause, final Context context) {
        Globals.allGrievance.clear();
        dialog = ProgressDialog.show(context, "", "Please wait");
        enterprise.getEvents(whereClause, null, null, new ListCallback<Event>() {
            @Override
            public void onSuccess(PageToken pageToken, List<Event> list) {
                if (list != null && !list.isEmpty()){
                    Globals.allGrievance.addAll(list);

                }else if (list != null && list.isEmpty()) {

                }
                bus.post(Constants.FETCHED);
            }
            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    public static void getEvents(final Context context, final WhereClause whereClause) {
        getKeepTraxInstance(context);
        if (keepTrax.getUser() != null) {
            keepTrax.getUser().getEnterprise(new ObjectCallback<Enterprise>() {
                @Override
                public void onSuccess(Enterprise enterprise) {
                    if (enterprise != null) {
                        getEnterpriseEvents(enterprise,whereClause,context);
                    } else {

                    }
                }

                @Override
                public void onError(Throwable t) {
                    AppUtils.logoutFromApp(context);
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

package com.example.priyanka.SmartCitizen.utils;

import com.example.priyanka.SmartCitizen.model.DataModel;
import com.example.priyanka.SmartCitizen.model.GrievanceStatusModel;
import com.keeptraxinc.cachemanager.dao.Event;

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

    public static void populateAdapterDataSet() {
        int size = Globals.allGrievance.size();
        int j;
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                DataModel dm = new DataModel();
                dm.setHeaderTitle(AppUtils.getFormattedDate(Globals.allGrievance.get(i).getStart(), Constants.HEADER_FORMAT).toUpperCase());
                ArrayList<GrievanceStatusModel> singleItem = new ArrayList<>();
                for (j = i; j < size; j++) {
                    if (Globals.allGrievance.get(i).getStart().equals(Globals.allGrievance.get(j).getStart())) {
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
}

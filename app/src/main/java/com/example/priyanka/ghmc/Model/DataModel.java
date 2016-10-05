package com.example.priyanka.ghmc.model;

/**
 * Created by Priyanka on 03/10/16.
 */

import java.util.ArrayList;


public class DataModel {



    private String headerTitle;
    private ArrayList<GrievanceStatusModel> allItemsInSection;


    public DataModel() {

    }
    public DataModel(String headerTitle, ArrayList<GrievanceStatusModel> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }



    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<GrievanceStatusModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<GrievanceStatusModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}

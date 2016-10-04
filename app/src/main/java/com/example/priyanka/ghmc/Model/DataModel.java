package com.example.priyanka.ghmc.Model;

/**
 * Created by Priyanka on 03/10/16.
 */

import java.util.ArrayList;


public class DataModel {



    private String headerTitle;
    private ArrayList<String> allItemsInSection;


    public DataModel() {

    }
    public DataModel(String headerTitle, ArrayList<String> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }



    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<String> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<String> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}

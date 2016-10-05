package com.example.priyanka.ghmc.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.priyanka.ghmc.Model.DataModel;
import com.example.priyanka.ghmc.Model.GrievanceStatusModel;
import com.example.priyanka.ghmc.R;
import com.example.priyanka.ghmc.adapter.RecyclerViewSectionAdapter;
import com.example.priyanka.ghmc.utils.DividerItemDecoration;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priyanka on 30/09/16.
 */

public class GrievanceStatusFragment extends Fragment {
    private RecyclerView recyclerView;
    List<DataModel> allSampleData;
    public static GrievanceStatusFragment newInstance() {
        GrievanceStatusFragment fragment = new GrievanceStatusFragment();

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout =  inflater.inflate (R.layout.fragment_grievance_status,container,false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.my_recycler_view);
        return layout;
    }
    @Override
    public void onResume(){
        super.onResume();
        allSampleData = new ArrayList<DataModel>();
        populateSampleData();
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));


        RecyclerViewSectionAdapter adapter = new RecyclerViewSectionAdapter(allSampleData);


        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);


     /*   GridLayoutManager manager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.grid_span_2));


        GridLayoutManager manager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.grid_span_3));*/

        recyclerView.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void populateSampleData() {

        for (int i = 1; i <= 10; i++) {

            DataModel dm = new DataModel();

            dm.setHeaderTitle("Section " + i);

            ArrayList<GrievanceStatusModel> singleItem = new ArrayList<>();
            for (int j = 1; j <= 4; j++) {
                GrievanceStatusModel grievanceStatusModel = new GrievanceStatusModel();
                grievanceStatusModel.time = "00:00 AM";
                grievanceStatusModel.type = "Environment";
                grievanceStatusModel.title="Save Nature";
                grievanceStatusModel.members ="10";
                singleItem.add(grievanceStatusModel);
            }

            dm.setAllItemsInSection(singleItem);

            allSampleData.add(dm);

        }
    }
}

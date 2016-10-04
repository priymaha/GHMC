package com.example.priyanka.ghmc.activity;

/**
 * Created by Priyanka on 03/10/16.
 */


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.priyanka.ghmc.Model.DataModel;
import com.example.priyanka.ghmc.R;
import com.example.priyanka.ghmc.adapter.RecyclerViewSectionAdapter;
import com.example.priyanka.ghmc.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<DataModel> allSampleData;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        allSampleData = new ArrayList<DataModel>();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("Section RecyclerView");

        }


        populateSampleData();


        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
        my_recycler_view.addItemDecoration(
                new DividerItemDecoration(this));


        RecyclerViewSectionAdapter adapter = new RecyclerViewSectionAdapter(allSampleData);


        GridLayoutManager manager = new GridLayoutManager(this, 1);


     /*   GridLayoutManager manager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.grid_span_2));


        GridLayoutManager manager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.grid_span_3));*/

        my_recycler_view.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        my_recycler_view.setAdapter(adapter);


    }

    private void populateSampleData() {

        for (int i = 1; i <= 10; i++) {

            DataModel dm = new DataModel();

            dm.setHeaderTitle("Section " + i);

            ArrayList<String> singleItem = new ArrayList<>();
            for (int j = 1; j <= 4; j++) {

                singleItem.add("Item " + j);
            }

            dm.setAllItemsInSection(singleItem);

            allSampleData.add(dm);

        }
    }
}


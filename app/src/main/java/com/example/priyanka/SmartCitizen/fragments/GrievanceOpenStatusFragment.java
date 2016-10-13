package com.example.priyanka.SmartCitizen.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.priyanka.SmartCitizen.R;
import com.example.priyanka.SmartCitizen.activity.EventDetailActivity;
import com.example.priyanka.SmartCitizen.adapter.RecyclerViewSectionAdapter;
import com.example.priyanka.SmartCitizen.listener.ClickListener;
import com.example.priyanka.SmartCitizen.model.DataModel;
import com.example.priyanka.SmartCitizen.model.GrievanceStatusModel;
import com.example.priyanka.SmartCitizen.utils.DividerItemDecoration;
import com.example.priyanka.SmartCitizen.utils.Globals;
import com.example.priyanka.SmartCitizen.utils.UrlBuilder;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priyanka on 30/09/16.
 */

public class GrievanceOpenStatusFragment extends Fragment implements ClickListener {
    private RecyclerView recyclerView;
    private List<DataModel> allSampleData;
    private KeepTrax keepTrax;
    private GrievanceStatusModel dummyGrievanceStatusModel = new GrievanceStatusModel();

    public static GrievanceOpenStatusFragment newInstance() {
        GrievanceOpenStatusFragment fragment = new GrievanceOpenStatusFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_grievance_status, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.my_recycler_view);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialization();
    }

    @Override
    public void onResume() {
        super.onResume();
        Globals.populateAdapterDataSet();
        if (Globals.allSampleData.size() > 0) {
            loadAdapter();
        }

    }

    private void initialization() {
        Globals.allSampleData = new ArrayList<DataModel>();
        keepTrax = KeepTraxImpl.getInstance(getActivity(), UrlBuilder.getUrl(getActivity()), UrlBuilder.getApiKey(getActivity()));
    }

    private void loadAdapter() {
        RecyclerViewSectionAdapter adapter = new RecyclerViewSectionAdapter(Globals.allSampleData);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void itemClicked(View view, int position) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        getActivity().startActivity(intent);
    }
}

package com.example.priyanka.SmartCitizen.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priyanka.SmartCitizen.R;
import com.example.priyanka.SmartCitizen.activity.EventDetailActivity;
import com.example.priyanka.SmartCitizen.adapter.RecyclerViewSectionAdapter;
import com.example.priyanka.SmartCitizen.listener.ClickListener;
import com.example.priyanka.SmartCitizen.model.DataModel;
import com.example.priyanka.SmartCitizen.model.GrievanceStatusModel;
import com.example.priyanka.SmartCitizen.utils.AppPreferences;
import com.example.priyanka.SmartCitizen.utils.Constants;
import com.example.priyanka.SmartCitizen.utils.DividerItemDecoration;
import com.example.priyanka.SmartCitizen.utils.Globals;
import com.example.priyanka.SmartCitizen.utils.UrlBuilder;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priyanka on 30/09/16.
 */

public class GrievanceClosedStatusFragment extends Fragment implements ClickListener {
    private RecyclerView recyclerView;
    private TextView mEventAvailableTV;
    private KeepTrax keepTrax;
    private static EventBus bus = EventBus.getDefault();

    private GrievanceStatusModel dummyGrievanceStatusModel = new GrievanceStatusModel();


    public static GrievanceClosedStatusFragment newInstance() {
        GrievanceClosedStatusFragment fragment = new GrievanceClosedStatusFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_grievance_status, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.my_recycler_view);
        mEventAvailableTV = (TextView) layout.findViewById(R.id.grievance_noevents_tv);
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

//        mEventAvailableTV.setVisibility(View.VISIBLE);
      /*  if (AppPreferences.getBooleanValue(Constants.NO_SHOWS, getActivity())) {
            mEventAvailableTV.setVisibility(View.VISIBLE);
        } else {
            mEventAvailableTV.setVisibility(View.GONE);
           *//* Globals.populateAdapterDataSet();
            if (Globals.allSampleData.size() > 0) {
                loadAdapter();
            }*//*
        }*/
    }

    private void initialization() {
        Globals.allClosedSampleData.clear();
        keepTrax = KeepTraxImpl.getInstance(getActivity(), UrlBuilder.getUrl(getActivity()), UrlBuilder.getApiKey(getActivity()));
    }

    private void loadAdapter() {
        RecyclerViewSectionAdapter adapter = new RecyclerViewSectionAdapter(Globals.allClosedSampleData);
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
    @Subscribe
    public void onEvent(String fetched) {
//
        if (fetched.equals(Constants.FETCHED)){
            if (Globals.dialog!=null && Globals.dialog.isShowing())
                Globals.dialog.dismiss();
            Globals.populateAdapterDataSet(Constants.STARTED);
            if (Globals.allClosedSampleData.size() > 0) {
                mEventAvailableTV.setVisibility(View.GONE);
                loadAdapter();
            } else{
                mEventAvailableTV.setVisibility(View.VISIBLE);
            }
        }else{

        }

    }
    @Override
    public void onStart() {
        super.onStart();
        try {
            bus.register(this);
        } catch (Exception e) {
            //if already registered
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            bus.unregister(this);
        } catch (Exception e) {
            //if already registered
        }
    }
}

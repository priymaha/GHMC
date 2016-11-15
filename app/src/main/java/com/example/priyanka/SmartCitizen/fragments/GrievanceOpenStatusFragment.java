package com.example.priyanka.SmartCitizen.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.priyanka.SmartCitizen.R;
import com.example.priyanka.SmartCitizen.activity.GrievanceDetailActivity;
import com.example.priyanka.SmartCitizen.adapter.RecyclerViewSectionAdapter;
import com.example.priyanka.SmartCitizen.listener.ClickListener;
import com.example.priyanka.SmartCitizen.model.DataModel;
import com.example.priyanka.SmartCitizen.model.GrievanceStatusModel;
import com.example.priyanka.SmartCitizen.utils.Constants;
import com.example.priyanka.SmartCitizen.utils.DividerItemDecoration;
import com.example.priyanka.SmartCitizen.utils.Globals;
import com.example.priyanka.SmartCitizen.utils.UrlBuilder;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Priyanka on 30/09/16.
 */

public class GrievanceOpenStatusFragment extends Fragment implements ClickListener {
    private static EventBus bus = EventBus.getDefault();
    private RecyclerView recyclerView;
    private TextView mEventAvailableTV;
    private List<DataModel> allSampleData;
    private KeepTrax keepTrax;
    private GrievanceStatusModel dummyGrievanceStatusModel = new GrievanceStatusModel();
    private List<DataModel> openEvents;

    public static GrievanceOpenStatusFragment newInstance() {
        GrievanceOpenStatusFragment fragment = new GrievanceOpenStatusFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_grievance_status, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.my_recycler_view);
        mEventAvailableTV = (TextView) layout.findViewById(R.id.grievance_noevents_tv);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.grievances));
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

        Globals.populateAdapterDataSet(Constants.CREATED);
        if (!Globals.allOpenSampleData.isEmpty()) {
            mEventAvailableTV.setVisibility(View.GONE);
            loadAdapter();
        } else {
            mEventAvailableTV.setVisibility(View.VISIBLE);
        }
    }

    private void initialization() {
        Globals.allOpenSampleData.clear();
        keepTrax = KeepTraxImpl.getInstance(getActivity(), UrlBuilder.getUrl(getActivity()), UrlBuilder.getApiKey(getActivity()));
    }

    private void loadAdapter() {
        RecyclerViewSectionAdapter adapter = new RecyclerViewSectionAdapter(Globals.allOpenSampleData);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void itemClicked(View view, int absolutePosition, int relativePosition) {
        Intent intent = new Intent(getActivity(), GrievanceDetailActivity.class);
        intent.putExtra(Constants.EVENT_ID, Globals.allOpenSampleData.get(absolutePosition).getAllItemsInSection().get(relativePosition).eventId);
        getActivity().startActivity(intent);
    }

    @Subscribe
    public void onEvent(String fetched) {
//
        if (fetched.equals(Constants.FETCHED)) {
            if (Globals.dialog != null && Globals.dialog.isShowing())
                Globals.dialog.dismiss();
            Globals.populateAdapterDataSet(Constants.CREATED);
            if (!Globals.allOpenSampleData.isEmpty()) {
                mEventAvailableTV.setVisibility(View.GONE);
                loadAdapter();
            } else {
                mEventAvailableTV.setVisibility(View.VISIBLE);
            }
        } else {

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

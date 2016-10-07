package com.example.priyanka.ghmc.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.priyanka.ghmc.R;
import com.example.priyanka.ghmc.activity.EventDetailActivity;
import com.example.priyanka.ghmc.adapter.RecyclerViewSectionAdapter;
import com.example.priyanka.ghmc.listener.ClickListener;
import com.example.priyanka.ghmc.model.DataModel;
import com.example.priyanka.ghmc.model.GrievanceStatusModel;
import com.example.priyanka.ghmc.utils.Constants;
import com.example.priyanka.ghmc.utils.DividerItemDecoration;
import com.example.priyanka.ghmc.utils.UrlBuilder;
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

/**
 * Created by Priyanka on 30/09/16.
 */

public class GrievanceStatusFragment extends Fragment implements ClickListener {
    private RecyclerView recyclerView;
    private List<DataModel> allSampleData;
    private KeepTrax keepTrax;

    public static GrievanceStatusFragment newInstance() {
        GrievanceStatusFragment fragment = new GrievanceStatusFragment();

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
//        populateSampleData(list);

    }

    @Override
    public void onResume() {
        super.onResume();
        getEvents();
    }

    private void initialization() {
        allSampleData = new ArrayList<DataModel>();
        keepTrax = KeepTraxImpl.getInstance(getActivity(), UrlBuilder.getUrl(getActivity()), UrlBuilder.getApiKey(getActivity()));
    }

    private void loadAdapter() {
        RecyclerViewSectionAdapter adapter = new RecyclerViewSectionAdapter(allSampleData);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void getEnterpriseEvents(final Enterprise enterprise) {
        WhereClause wc = WhereSimple.le(EventDao.Properties.Start.name, DateUtils.getISOTime(System.currentTimeMillis()))
                .and(WhereSimple.ge(EventDao.Properties.End.name, DateUtils.getISOTime(System.currentTimeMillis()))

                        .and(WhereSimple.ne(EventDao.Properties.Status.name, Constants.EVENT_STATUS_COMPLETED)));
        enterprise.getEvents(wc, null, null, new ListCallback<Event>() {
            @Override
            public void onSuccess(PageToken pageToken, List<Event> list) {
                if (list != null && !list.isEmpty()) {
                    populateSampleData (list);

                } else {


                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    private void getEvents() {
        if (keepTrax.getUser() != null) {

            keepTrax.getUser().getEnterprise(new ObjectCallback<Enterprise>() {
                @Override
                public void onSuccess(Enterprise enterprise) {
                    if (enterprise != null) {
                        getEnterpriseEvents(enterprise);
                    } else {

                    }
                }

                @Override
                public void onError(Throwable t) {

                }
            });
        }
    }

    private void populateSampleData(List<Event> list) {




        int size = list.size();
        for (int i = 0; i <= size; i++) {

            DataModel dm = new DataModel();

            dm.setHeaderTitle(list.get(i).getStart());
            dm.setHeaderTitle("THURSDAY, OCTOBER " + i);

            ArrayList<GrievanceStatusModel> singleItem = new ArrayList<>();
            for (int j = 1; j <= 2; j++) {
                GrievanceStatusModel grievanceStatusModel = new GrievanceStatusModel();

                grievanceStatusModel.time = "10:00 AM";
                grievanceStatusModel.type = "Environment & Clean Hyderabad";
                grievanceStatusModel.title="Clean Hussain Sagar";
                grievanceStatusModel.members="243 members going";

                singleItem.add(grievanceStatusModel);
            }

            dm.setAllItemsInSection(singleItem);

            allSampleData.add(dm);

        }
        loadAdapter();
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        getActivity().startActivity(intent);
    }
}

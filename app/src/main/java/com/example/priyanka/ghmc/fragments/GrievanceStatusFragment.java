package com.example.priyanka.ghmc.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.priyanka.ghmc.R;

import org.json.JSONObject;

/**
 * Created by Priyanka on 30/09/16.
 */

public class GrievanceStatusFragment extends Fragment {
    public static GrievanceStatusFragment newInstance() {
        GrievanceStatusFragment fragment = new GrievanceStatusFragment();

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_grievance_status,container,false);
    }
}

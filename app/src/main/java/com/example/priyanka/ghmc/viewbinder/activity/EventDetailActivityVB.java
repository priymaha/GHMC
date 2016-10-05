package com.example.priyanka.ghmc.viewbinder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.priyanka.ghmc.R;
import com.example.priyanka.ghmc.activity.EventDetailActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

/**
 * Created by sahul on 10/5/16.
 */

public class EventDetailActivityVB extends BaseActivityViewBinder implements OnMapReadyCallback {

    private GoogleMap mMap;
    ImageView mEventPic;

    public  EventDetailActivityVB(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void initContentView() {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LayoutInflater inflater = activity.getLayoutInflater();
        contentView = inflater.inflate(R.layout.activity_event_detail, null);
        if (contentView != null) {
            activity.setContentView(contentView);
        }
    }

    @Override
    public void initViews() {
        mEventPic = (ImageView) contentView.findViewById(R.id.detail_pic_iv);

        SupportMapFragment mapFragment = (SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        try {
            activity.getSupportActionBar().hide();
        } catch (NullPointerException npe) {

        }
    }

    @Override
    public void initBackgroundColor() {

    }

    @Override
    public void initViewListeners() {

    }

    @Override
    public void onInitFinish() {
        Picasso.with(activity)
                .load("http://cdn.deccanchronicle.com/sites/default/files/28HUSSAIN%20SAGAR%20(23).jpg")
                .resize(1035,500)
                .into(mEventPic);
    }

    @Override
    public boolean handleOptionsSelected(int itemId) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sagar = new LatLng(17.4239, 78.4738);
        mMap.addMarker(new MarkerOptions().position(sagar).title("Marker in sagar"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sagar, 14.3f));
    }
}

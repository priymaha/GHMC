package com.example.priyanka.SmartCitizen.viewbinder.activity;

import android.content.Intent;
import android.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.priyanka.SmartCitizen.R;
import com.example.priyanka.SmartCitizen.utils.AppPreferences;
import com.example.priyanka.SmartCitizen.utils.Constants;
import com.example.priyanka.SmartCitizen.utils.UrlBuilder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.keeptraxinc.cachemanager.PageToken;
import com.keeptraxinc.cachemanager.dao.Document;
import com.keeptraxinc.cachemanager.dao.DocumentDao;
import com.keeptraxinc.cachemanager.dao.Event;
import com.keeptraxinc.cachemanager.dao.EventDao;
import com.keeptraxinc.cachemanager.query.ListCallback;
import com.keeptraxinc.cachemanager.query.OrderClause;
import com.keeptraxinc.cachemanager.query.WhereClause;
import com.keeptraxinc.cachemanager.query.WhereSimple;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by sahul on 10/5/16.
 */

public class GrievanceDetailActivityVB extends BaseActivityViewBinder implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageView mEventPic;
    private long eventId;
    private ExifInterface exif;
    private Toolbar toolbar;


    public GrievanceDetailActivityVB(AppCompatActivity activity) {
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
        toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);

        activity.getSupportActionBar().setTitle(activity.getResources().getString(R.string.detail_grievance));
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
        getIntentData();
        if (eventId != Constants.DEFAULT_LONG_VALUE) {
            getEvent();
        } else {
            Toast.makeText(activity, "No event id", Toast.LENGTH_LONG).show();
        }
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

    private void getAppPreferences() {
        eventId = AppPreferences.getLongValue(Constants.EVENT_ID, context);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sagar = new LatLng(17.4239, 78.4738);
        mMap.addMarker(new MarkerOptions().position(sagar).title("Marker in sagar"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sagar, 14.3f));
    }

    private void getEvent() {
        KeepTrax keepTrax = KeepTraxImpl.getInstance(activity, UrlBuilder.getUrl(context), UrlBuilder.getApiKey(context));
        WhereClause wc = WhereSimple.eq(EventDao.Properties.CId.name, eventId);
        keepTrax.getOneModel(Event.NAME, wc, null, new ObjectCallback<Event>() {
            @Override
            public void onSuccess(Event currentEvent) {

                getEventPhotos(currentEvent);
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void getEventPhotos(Event event) {

        OrderClause oc = new OrderClause(DocumentDao.Properties.Time.name, OrderClause.Direction.ASCENDING);
        WhereClause wc = WhereSimple.eq(DocumentDao.Properties.Type.name, Constants.IMAGE_JPEG);
        event.getDocuments(wc, oc, null, new ListCallback<Document>() {
            @Override
            public void onSuccess(PageToken pageToken, List<Document> photoList) {
                if (photoList != null && !photoList.isEmpty()) {
                 setPhoto(photoList.get(0));
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EVENT_ID)) {
            eventId = intent.getLongExtra(Constants.EVENT_ID, Constants.DEFAULT_LONG_VALUE);
        }
    }

    private void setPhoto(final Document photo) {
        if (photo != null) {
            File t = new File(context.getExternalFilesDir(null) + "/" + Constants.IMAGE_DIRECTORY_NAME + photo.getName());
            if (t.exists()) {
                try {
                    exif = new ExifInterface(context.getExternalFilesDir(null) + "/" + Constants.IMAGE_DIRECTORY_NAME + photo.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Glide.with(context)
                        .load(context.getExternalFilesDir(null) + "/" + Constants.IMAGE_DIRECTORY_NAME + photo.getName())
                        .skipMemoryCache(true)
                        .into(mEventPic);
            } else {
                photo.download(new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        try {
                            exif = new ExifInterface(context.getExternalFilesDir(null) + "/" + Constants.IMAGE_DIRECTORY_NAME + photo.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Glide.with(context)
                                .load(context.getExternalFilesDir(null) + "/" + Constants.IMAGE_DIRECTORY_NAME + photo.getName())
                                .skipMemoryCache(true)
                                .into(mEventPic);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });

            }
        }


    }

}

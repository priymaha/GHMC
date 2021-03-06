package com.example.priyanka.SmartCitizen.viewbinder.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.priyanka.SmartCitizen.R;
import com.example.priyanka.SmartCitizen.utils.AppUtils;
import com.example.priyanka.SmartCitizen.utils.CameraHelper;
import com.example.priyanka.SmartCitizen.utils.Constants;
import com.example.priyanka.SmartCitizen.utils.UIValidator;
import com.example.priyanka.SmartCitizen.utils.UrlBuilder;
import com.example.priyanka.SmartCitizen.utils.VolleySingleton;
import com.example.priyanka.SmartCitizen.view.roundedimageview.RoundedImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.keeptraxinc.cachemanager.PageToken;
import com.keeptraxinc.cachemanager.dao.Document;
import com.keeptraxinc.cachemanager.dao.Enterprise;
import com.keeptraxinc.cachemanager.dao.Event;
import com.keeptraxinc.cachemanager.dao.EventDao;
import com.keeptraxinc.cachemanager.query.ListCallback;
import com.keeptraxinc.cachemanager.query.WhereClause;
import com.keeptraxinc.cachemanager.query.WhereSimple;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;
import com.keeptraxinc.utils.helper.DateUtils;
import com.keeptraxinc.utils.helper.NetworkInfo;
import com.keeptraxinc.utils.logger.Logger;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.priyanka.SmartCitizen.utils.ChangeOrientationUtils.changeOrientation;

public class GrievancePostActivityVB extends BaseActivityViewBinder implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String LOG_TAG = GrievancePostActivityVB.class.getSimpleName();
    private static KeepTrax keepTrax;
    protected GoogleApiClient mGoogleApiClient;
    private RequestQueue requestQueue;
    private Uri fileUri = null;
    private ExifInterface exif;
    private RelativeLayout rlParent;
    private RoundedImageView bitmap_image;
    private LinearLayout llDetails;
    private FrameLayout rl_photogrid;
    private RelativeLayout rl_photogrid_bitmap;
    private Button cancel, submit;
    private Location mLastLocation;
    private Document photo;
    private EditText grievanceTitle, grievanceDescription;
    private Spinner grievanceType;
    private Spinner grievanceStatus;
    private ProgressDialog dialog;
    private Toolbar toolbar;


    public GrievancePostActivityVB(AppCompatActivity activity) {
        super(activity);
    }


    @Override
    public void initContentView() {
        LayoutInflater inflater = activity.getLayoutInflater();
        contentView = inflater.inflate(R.layout.activity_grievance_post, null);
        if (contentView != null) {
            activity.setContentView(contentView);
        }
        toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);

       activity.getSupportActionBar().setTitle(activity.getResources().getString(R.string.report_grievance));
    }

    @Override
    public void initViews() {
        rlParent = (RelativeLayout) contentView.findViewById(R.id.rl_photogrid_parent);
        grievanceTitle = (EditText) contentView.findViewById(R.id.grievanceTitle);
        grievanceDescription = (EditText) contentView.findViewById(R.id.feedbackBody);
        grievanceType = (Spinner) contentView.findViewById(R.id.feedbackType);
        grievanceStatus = (Spinner) contentView.findViewById(R.id.grievance_status);
        rl_photogrid_bitmap = (RelativeLayout) contentView.findViewById(R.id.rl_photogrid_bitmap);
        rl_photogrid = (FrameLayout) contentView.findViewById(R.id.rl_photogrid);
        llDetails = (LinearLayout) contentView.findViewById(R.id.ll_bottom);
        bitmap_image = (RoundedImageView) contentView.findViewById(R.id.bitmap_image);
        cancel = (Button) llDetails.findViewById(R.id.cancel);
        submit = (Button) llDetails.findViewById(R.id.submit);
    }

    @Override
    public void initBackgroundColor() {

    }

    @Override
    public void initViewListeners() {
        rl_photogrid.setOnClickListener(this);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onInitFinish() {
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
//        postEvent();
//        getPostedEvent();
        /* build google API client to use location services */
        buildGoogleApiClient();
        grievanceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#999999"));
                }
                ((TextView) adapterView.getChildAt(0)).setTextSize(18);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        grievanceStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#999999"));
                }
                ((TextView) adapterView.getChildAt(0)).setTextSize(18);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    private void postEvent() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("start", DateUtils.getISOTime(System.currentTimeMillis()));
        params.put("end", "2016-10-14T16:10:00.000Z");
        params.put("name", grievanceTitle.getText().toString());
        params.put("event", "123456");
        params.put("status", grievanceStatus.getSelectedItem().toString());
        params.put("enterpriseId", Constants.ENTERPRISE);
        getKeepTraxInstance(context);
        params.put("userId",keepTrax.getUser().getId());

        JSONObject jsonObj = new JSONObject(params);
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, "http://52.34.54.113/api/v4/events", jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(activity, response.toString(), Toast.LENGTH_LONG).show();
                        parseResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("X-Product-Key", "OWJlMjFjODlmNTgwZTZjNjNjNDdkMTRkZTkzZmJkYmE6MDc1YWFiMTUwZGNiNDljNTIyYTAxNTM0YTQ2MmVlMjkyYWVjNjkwYg==");
                params.put("Authorization", "3nOvf70quAatwZHo0Q6HAvqogMWHmt7jC4dUuZ739k8zcL6MfUPoJP4jaPr1yc1P");
                return params;
            }
        };
        requestQueue.add(jsonObjRequest);

    }

    private void parseResponse(JSONObject response) {
        String eventId = null;
        try {
            eventId = response.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getEvent(eventId);

    }

    private void getEvent(String eventId) {
        WhereClause wc = WhereSimple.eq(EventDao.Properties.Id.name, eventId);
        getEvents(context, wc);
    }

    private void addDocument(Event currentEvent) {
        currentEvent.addDocument(photo, null);
    }

    private void getPostedEvent() {
        StringRequest request = new StringRequest(Request.Method.GET, "http://sci.keeptraxapp.com/api/v4/events/57e4d4363cc64a4d3e681b13",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(activity, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("X-Product-Key", "OWJlMjFjODlmNTgwZTZjNjNjNDdkMTRkZTkzZmJkYmE6MDc1YWFiMTUwZGNiNDljNTIyYTAxNTM0YTQ2MmVlMjkyYWVjNjkwYg==");
                params.put("Authorization", "yvK96sVX3dnafQEeh2B8K2OdIVS2MZaO5XNr1l8GlqHjj2WDueCBy2sfI7mQAhAA");
                return params;
            }
        };


        requestQueue.add(request);
    }


    @Override
    public boolean handleOptionsSelected(int itemId) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
//                photoAfterTime = new Date().getTime();
                previewCapturedImage();
//                CameraHelper.deleteLatestImageFromCam(context, photoBeforeTime, photoAfterTime);
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = CameraHelper.getOutputMediaFileUri(context);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        activity.startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /*
   * Display image from a path to ImageView
   */
    private void previewCapturedImage() {
        createPhoto();

    }


    private void createPhoto() {
        KeepTrax keepTrax = KeepTraxImpl.getInstance(activity, UrlBuilder.getUrl(context), UrlBuilder.getApiKey(context));
        photo = (Document) keepTrax.createModel(Document.NAME);
        final String[] path = fileUri.getPath().split("/");
        photo.setLocalUrl(path[path.length - 2] + "/" + path[path.length - 1]);   // Setting the local path in photo model
        photo.setTime(DateUtils.getISOTime(System.currentTimeMillis()));
        String name = path[path.length - 1];
        photo.setName(name);
        photo.setType(Constants.IMAGE_JPEG);


        try {
            exif = new ExifInterface(fileUri.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.debug(null, LOG_TAG, "ORIENTATION OF PHOTO " + exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0));
        changeOrientation(fileUri.getPath());
        savePhoto(photo);

    }

    private void savePhoto(Document photo) {
        photo.save(new VoidCallback() {
            @Override
            public void onSuccess() {
                rl_photogrid_bitmap.setVisibility(View.VISIBLE);
                rlParent.setVisibility(View.GONE);
                Glide.with(context)
                        .load(fileUri.getPath())
                        .asBitmap()
                        .skipMemoryCache(true)
                        .centerCrop()
                        .into(bitmap_image);
            }

            @Override
            public void onError(Throwable t) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        if (view == rl_photogrid) {
//            Log.e ("time ",DateUtils.getISOTime(System.currentTimeMillis()));
            captureImage();
        } else if (view == cancel) {

        } else if (view == submit) {
            submitClicked();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(LOG_TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /*
    * Called by Google Play services if the connection to GoogleApiClient drops because of an
    * error.
    */
    public void onDisconnected() {
        Log.i(LOG_TAG, "Disconnected");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(LOG_TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    private void setEventExtras(Event event) {
        JSONObject jsonObject = null;
        try {
            if (event.getExtensions() != null && !event.getExtensions().isEmpty()) {
                jsonObject = new JSONObject(event.getExtensions());

                    jsonObject.put(Constants.GRIEVANCE_TYPE, grievanceType.getSelectedItem().toString());
                    jsonObject.put(Constants.GRIEVANCE_DESCRIPTION, grievanceDescription.getText().toString());
                if (mLastLocation != null) {
                    jsonObject.put(Constants.LATITUDE, String.valueOf(mLastLocation.getLatitude()));
                    jsonObject.put(Constants.LONGITUDE, String.valueOf(mLastLocation.getLongitude()));
                }
            } else {
                jsonObject = new JSONObject();
                jsonObject.put(Constants.GRIEVANCE_TYPE, grievanceType.getSelectedItem().toString());
                jsonObject.put(Constants.GRIEVANCE_DESCRIPTION, grievanceDescription.getText().toString());
                if (mLastLocation != null) {
                    jsonObject.put(Constants.LATITUDE, String.valueOf(mLastLocation.getLatitude()));
                    jsonObject.put(Constants.LONGITUDE, String.valueOf(mLastLocation.getLongitude()));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event.setExtensions(JsonUtil.fromJson(jsonObject));
        event.setExtras(jsonObject.toString());
    }

    public void submitClicked() {
        if (validData()) {

            if (NetworkInfo.isNetworkAvailable(context)) {
                if (AppUtils.isInternetAccessible(activity)) {
                    dialog = ProgressDialog.show(activity, "", "Please wait...");
                    postEvent();
                } else {
                    AppUtils.showNoInternetAccessibleAlert(context);
                }
            } else {
                AppUtils.showWiFiSettingsAlert(context);
            }

        }
    }

    private boolean validData() {
        return !UIValidator.isError(context, grievanceTitle, grievanceType, grievanceStatus, grievanceDescription, bitmap_image);
    }

    public void getEnterpriseEvents(final Enterprise enterprise, WhereClause whereClause) {
        enterprise.getEvents(whereClause, null, null, new ListCallback<Event>() {
            @Override
            public void onSuccess(PageToken pageToken, final List<Event> list) {
                if (list != null && !list.isEmpty()) {
                    setEventExtras(list.get(0));
                    list.get(0).save(new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            list.get(0).linkUser(keepTrax.getUser(), null);
                            addDocument(list.get(0));
                            dialog.dismiss();
                            finishActivity();
                        }

                        @Override
                        public void onError(Throwable t) {
                            dialog.dismiss();
                        }
                    });

                } else if (list != null && list.isEmpty()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dialog.dismiss();
            }
        });
    }

    public void getEvents(Context context, final WhereClause whereClause) {
        getKeepTraxInstance(context);
        if (keepTrax.getUser() != null) {

            keepTrax.getUser().getEnterprise(new ObjectCallback<Enterprise>() {
                @Override
                public void onSuccess(Enterprise enterprise) {
                    if (enterprise != null) {
                        getEnterpriseEvents(enterprise, whereClause);
                    } else {

                    }
                }

                @Override
                public void onError(Throwable t) {

                }
            });
        }
    }

    private void getKeepTraxInstance(Context context) {
        if (keepTrax != null) {
            return;
        }
        keepTrax = KeepTraxImpl.getInstance(context, UrlBuilder.getUrl(context), UrlBuilder.getApiKey(context));
    }
}

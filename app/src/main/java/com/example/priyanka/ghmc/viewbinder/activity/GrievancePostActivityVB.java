package com.example.priyanka.ghmc.viewbinder.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.priyanka.ghmc.R;
import com.example.priyanka.ghmc.utils.CameraHelper;
import com.example.priyanka.ghmc.utils.Constants;
import com.example.priyanka.ghmc.utils.UrlBuilder;
import com.example.priyanka.ghmc.utils.VolleySingleton;
import com.keeptraxinc.cachemanager.dao.Document;
import com.keeptraxinc.sdk.KeepTrax;
import com.keeptraxinc.sdk.impl.KeepTraxImpl;
import com.keeptraxinc.utils.helper.DateUtils;
import com.keeptraxinc.utils.logger.Logger;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.priyanka.ghmc.utils.ChangeOrientationUtils.changeOrientation;

public class GrievancePostActivityVB extends BaseActivityViewBinder implements View.OnClickListener {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String LOG_TAG = GrievancePostActivityVB.class.getSimpleName();
    private RequestQueue requestQueue;
    private Uri fileUri = null;
    private ExifInterface exif;
    private RelativeLayout rlParent;
    private ImageView bitmap_image;
    private LinearLayout llDetails;
    private FrameLayout rl_photogrid;
    private RelativeLayout rl_photogrid_bitmap;
    private Button cancel, submit;

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
        try {
            activity.getSupportActionBar().hide();
        } catch (NullPointerException npe) {

        }
    }

    @Override
    public void initViews() {
        rlParent = (RelativeLayout) contentView.findViewById(R.id.rl_photogrid_parent);
        rl_photogrid_bitmap = (RelativeLayout) contentView.findViewById(R.id.rl_photogrid_bitmap);
        rl_photogrid = (FrameLayout) contentView.findViewById(R.id.rl_photogrid);
        llDetails = (LinearLayout) contentView.findViewById(R.id.ll_bottom);
        bitmap_image = (ImageView) contentView.findViewById(R.id.bitmap_image);
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
        postEvent();
        getEvent();
    }

    private void postEvent() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("start", "2016-09-23T00:10:00.000Z");
        params.put("end", "2016-09-23T14:10:00.000Z");
        params.put("name", "P_V_1");
        params.put("event", "123456");
        params.put("imageUrl", "https://pbs.twimg.com/profile_images/2326463999/smartride_logo_facebook-03.jpg");
        params.put("status", "Approved");
        params.put("retailer", "Kroger - Atlanta");
        params.put("store", "426");
        params.put("region", "SUWANEE");
        params.put("enterpriseId", "57c3d95ec9738d252654b331");
        params.put("userId", "57c7fbf13cc64a4d3e68143a");
        JSONObject jsonObj = new JSONObject(params);
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, "http://sci.keeptraxapp.com/api/v4/events", jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(activity, response.toString(), Toast.LENGTH_LONG).show();
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
                params.put("Authorization", "v951tVFjntBaB1vb28x1czCBBhFqH12o5m6YHaCZG00IDHChWZPW6fZkJH0hW1gy");
                return params;
            }
        };
        requestQueue.add(jsonObjRequest);

    }

    private void getEvent() {
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
                params.put("Authorization", "v951tVFjntBaB1vb28x1czCBBhFqH12o5m6YHaCZG00IDHChWZPW6fZkJH0hW1gy");
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


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();


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

    /*private void updatePhoto() throws JSONException {
        String[] path = fileUri.getPath().split("/");
        String name = path[path.length - 1];
        if (!name.equals(photo.getName())) {
            File imageFile = new File(context.getExternalFilesDir(null) + "/" + photo.getLocalUrl());
            if (imageFile.exists()) {
                imageFile.delete();
            }

        }
        photo.setLocalUrl(path[path.length - 2] + "/" + path[path.length - 1]);   // Setting the local path in photo model
        photo.setTime(DateUtils.getISOTime(System.currentTimeMillis()));
        photo.setName(name);
        photo.setType(Constants.IMAGE_JPEG);

        try {
            exif = new ExifInterface(fileUri.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.debug(null, LOG_TAG, "ORIENTATION OF PHOTO " + exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0));
        changeOrientation(fileUri.getPath());
        savePhoto();


    }*/

    private void createPhoto() {
        KeepTrax keepTrax = KeepTraxImpl.getInstance(activity, UrlBuilder.getUrl(context), UrlBuilder.getApiKey(context));
        final Document photo = (Document) keepTrax.createModel(Document.NAME);
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
                        .skipMemoryCache(true)
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
            captureImage();
        } else if (view == cancel) {

        }else if (view == submit){
            postEvent();
        }

    }
}

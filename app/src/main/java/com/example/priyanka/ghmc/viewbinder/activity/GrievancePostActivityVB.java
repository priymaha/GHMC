package com.example.priyanka.ghmc.viewbinder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.priyanka.ghmc.R;
import com.example.priyanka.ghmc.utils.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

;

public class GrievancePostActivityVB extends BaseActivityViewBinder {
    private RequestQueue requestQueue;


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

    }

    @Override
    public void initBackgroundColor() {

    }

    @Override
    public void initViewListeners() {

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
    public void onActivityResult(int requestCode, int responseCode, Intent data) {

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


}

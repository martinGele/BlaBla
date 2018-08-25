package com.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.adapters.LocationsAdapter;
import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.LocationInterface;
import com.interfaces.NavigationItemChangedListener;
import com.interfaces.PhoneInterface;
import com.interfaces.RefreshListner;
import com.responsemodels.AvailableOfferResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class FindFragment extends Fragment implements RefreshListner {

    ArrayList<AvailableOfferResponse.OperatingHour> operatingHourArrayList;
    ArrayList<AvailableOfferResponse.OperatingHour.Restaurant> restaurantArrayList;
    private static AlertDialog.Builder alertDialogBuilder;
    EditText locationSearchEditText;
    TextView locationEmptyTextView;
    ScrollView layout_recycle;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    View view;
    private double latitude = 0.0, longitude = 0.0;

    private Tracker mTracker;
    private String SCREEN_NAME = "Find Fragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.info_location, container, false);

/*
        request for a runtime permissions
         */
        requestPermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                }, 1);
        initView(view);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    refreshView();
                } else {
                    refreshView();
                }
                return;
            }


        }
    }

    private void initView(View view) {
        setTracker();
        ((NavigationItemChangedListener) getActivity()).onItemChanged(AppConstants.ITEM_FIND);


        locationSearchEditText = view.findViewById(R.id.location_search_edittext);
        locationEmptyTextView = view.findViewById(R.id.location_empty_text);
        layout_recycle = view.findViewById(R.id.layout_recycle);

        SetTextWatcherForAmountEditView(locationSearchEditText);

        locationSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("wetzel", "actionId:" + actionId + "#" + EditorInfo.IME_ACTION_DONE);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (Engine.isNetworkAvailable(getActivity())) {
                        //isUserSearchLocations = true;
                        fetchLocationFromServer(locationSearchEditText
                                .getText().toString().trim());
                    } else
                        Dialogs.showMsgDialog("",
                                AppConstants.ERRORNETWORKCONNECTION, getActivity());
                }

                return false;
            }
        });


        recyclerView = view.findViewById(R.id.location_linear_parentview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Fonts.fontRobotoCodensedBoldTextView(locationSearchEditText, 16,
                AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedRegularTextView(locationEmptyTextView, 16,
                AppConstants.COLORBLUE, getActivity().getAssets());


    }

    private void refreshView() {
        if (Engine.CheckEnableGPS(getActivity())) {

            try {
                RotiHomeActivity.startGPS();


                latitude = RotiHomeActivity.getGetLatLongObj().getLatitude();
                longitude = RotiHomeActivity.getGetLatLongObj().getLongitude();
            } catch (Exception e) {

            }
        }


        Log.d("le6end4", "latlng:" + latitude + ", " + longitude);
        if (Engine.isNetworkAvailable(getActivity())) {
            fetchLocationFromServer("");
        } else
            Dialogs.showMsgDialog("",
                    AppConstants.ERRORNETWORKCONNECTION, getActivity());

    }

    /*
    get all the locations
     */

    private void fetchLocationFromServer(String search) {

        RotiHomeActivity.getProgressDialog().show();

        String txtSearch = "";
        try {
            txtSearch = URLEncoder.encode(search, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String url = getResources().getString(R.string.BASE_URL) + "/restaurants/nearby?" + "appkey=" + getResources().getString(R.string.APPKEY) + "&lat="
                + latitude
                + "&lng="
                + longitude
                + "&locale=en&distance=0&search="
                + txtSearch;

        Log.d("Fetch", url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Locations", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        AvailableOfferResponse.OperatingHour.Restaurant.Restaurants restaurant = Gson.getGson().
                                fromJson(response.toString(), AvailableOfferResponse.OperatingHour.Restaurant.Restaurants.class);

                        if (restaurant.getRestaurants().isEmpty()) {

                            /*
                            set the recycle view to not be visible if there are no results
                             */
                            layout_recycle.setVisibility(View.GONE);
                            locationEmptyTextView.setVisibility(View.VISIBLE);
                            Dialogs.showMsgDialog("", getResources().getString(R.string.NO_LOC_FOUND_txt), getActivity());

                        } else {
                            /*
                            set the recycle view to  be visible if there are some results
                             */

                            layout_recycle.setVisibility(View.VISIBLE);
                            locationEmptyTextView.setVisibility(View.GONE);
                        }

                        adapter = new LocationsAdapter(restaurant.getRestaurants(), (LocationInterface) getActivity(), (PhoneInterface) getActivity());
                        recyclerView.setAdapter(adapter);

                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("le6end4", "Error: " + error.getMessage());
                Log.d("le6end4", "fetchOrderSiteFromServer Error: " + error.getMessage());
                String json = null;

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            json = Dialogs.trimMessage(json, "message");
                            if (json != null)
                                Dialogs.showMsgDialog("", json, ((Activity) getContext()));
                            break;

                        case 401:

                            HomeFragment.getInstance().logoutAccount();
                            json = new String(response.data);
                            json = Dialogs.trimMessage(json, "message");
                            if (json != null)
                                Dialogs.showMsgDialog("", json, ((Activity) getContext()));
                            break;
                        default:
                            Dialogs.showMsgDialog("", json, ((Activity) getContext()));

                            break;
                    }
                }

            }
        });

        // Adding request to request queue
        VolleyControllerAndTracker.getInstance().

                addToRequestQueue(jsonObjReq, "");


    }


    private void SetTextWatcherForAmountEditView(final EditText tmpEditText) {
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    tmpEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    //Assign your image again to the view, otherwise it will always be gone even if the text is 0 again.
                    tmpEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_search, 0, 0, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }

    @Override
    public void notifyRefresh(String className) {
        if (className.equalsIgnoreCase("FindFragment"))
            refreshView();

    }


    private void closeSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(locationSearchEditText.getWindowToken(), 0);
    }


    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RotiHomeActivity.stopGPS();

    }

    @Override
    public void onPause() {
        super.onPause();
//        RotiHomeActivity.stopGPS();
    }
}

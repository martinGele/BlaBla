package com.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adapters.FavoriteStoreAdapter;
import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.FavoriteStoreInterface;
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

import static com.fragments.AddCurrencyFragment.headerTxt;

public class FavoriteStoreFragment extends Fragment implements View.OnClickListener, RefreshListner {

    ArrayList<AvailableOfferResponse.OperatingHour> operatingHourArrayList;
    ArrayList<AvailableOfferResponse.OperatingHour.Restaurant> restaurantArrayList;
    View view;
    TextView please_select_favorite_store;
    EditText locationSearchEditText;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private Tracker mTracker;
    private String SCREEN_NAME = "Favorite Store Fragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.favorite_store, container, false);

/*
        request for a runtime permissions
         */
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                }, 1);
        initView(view);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getActivity(), "Permission denied to read access fine location", Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }

    private void initView(View view) {
        setTracker();
        please_select_favorite_store = view.findViewById(R.id.please_select_favorite_store);

        locationSearchEditText = view.findViewById(R.id.location_search_edittext1);
        headerTxt = view.findViewById(R.id.text_header);


        SetTextWatcherForAmountEditView(locationSearchEditText);

        locationSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("wetzel", "actionId:" + actionId + "#" + EditorInfo.IME_ACTION_DONE);
                if (actionId == EditorInfo.IME_ACTION_DONE) {
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

        recyclerView = view.findViewById(R.id.favorit_store_linear_parentview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        Fonts.fontRobotoCodensedBoldTextView(locationSearchEditText, 16,
                AppConstants.COLORORANGE, getActivity().getAssets());


        Fonts.fontFinkHeavyRegularTextView(please_select_favorite_store,
                18, AppConstants.COLORBLUE, getActivity().getAssets());

        Fonts.fontFinkHeavyRegularTextView(headerTxt,
                (int) (getActivity().getResources().getDimension(R.dimen.header_font_size) /
                        getActivity().getResources().getDisplayMetrics().density),
                AppConstants.COLORWHITE, getActivity().getAssets());

        refreshView();
    }

    private void refreshView() {

        if (Engine.CheckEnableGPS(getActivity())) {
            Log.d("le6end4", "latlng:" + RotiHomeActivity.getGetLatLongObj().getLatitude() + ", " + RotiHomeActivity.getGetLatLongObj().getLatitude());
            if (Engine.isNetworkAvailable(getActivity())) {
                fetchLocationFromServer("");
            } else
                Dialogs.showMsgDialog("",
                        AppConstants.ERRORNETWORKCONNECTION, getActivity());
        } else
            Dialogs.showMsgDialog("", AppConstants.ERRORLOCATIONSERVICES, getActivity());
    }


    @Override
    public void onClick(View v) {

    }

    private void fetchLocationFromServer(String search) {

        RotiHomeActivity.getProgressDialog().show();

        String txtSearch = "";
        try {
            txtSearch = URLEncoder.encode(search, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = getResources().getString(R.string.BASE_URL) + "/restaurants/nearby?" + "appkey=" + getResources().getString(R.string.APPKEY) + "&lat="
                + RotiHomeActivity.getGetLatLongObj().getLatitude()
                + "&lng="
                + RotiHomeActivity.getGetLatLongObj().getLongitude()
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

                        AvailableOfferResponse.OperatingHour.Restaurant restaurantLocation = Gson.getGson().
                                fromJson(response.toString(), AvailableOfferResponse.OperatingHour.Restaurant.class);


                        adapter = new FavoriteStoreAdapter(restaurant.getRestaurants(), (FavoriteStoreInterface) getActivity());
                        adapter.notifyDataSetChanged();
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

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }

}

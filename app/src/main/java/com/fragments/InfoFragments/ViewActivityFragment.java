package com.fragments.InfoFragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.adapters.ActivityDetailViewAdapter;
import com.adapters.ActivityRedeemedAdapter;
import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fragments.HomeFragment;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.RefreshListner;
import com.responsemodels.LastTransactionResponse;
import com.responsemodels.RewardClaimResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewActivityFragment extends Fragment implements View.OnClickListener, RefreshListner {

    public static View view;
    Button activityText;
    Button claimedText;

    boolean isActivityOpen;
    boolean isRewardOpen;
    public LayoutInflater mInflater;


    private Tracker mTracker;
    private String SCREEN_NAME = "View Activity Fragment";


    public List<LastTransactionResponse.Receipt> listRewardActivityBean;
    public List<LastTransactionResponse> transactionResponses;

    public List<RewardClaimResponse> listRewardClaimBean;
    public List<RewardClaimResponse.Activity> activities;
    public List<RewardClaimResponse.Activity.Reward> rewards;

    ScrollView viewActivityScroll;
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    LinearLayout layout_recycle;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.viewactivity, container, false);
        inintView(view);
        return view;
    }

    private void inintView(View view) {

        setTracker();

        activityText = view.findViewById(R.id.viewactivity_text_activity);
        claimedText = view.findViewById(R.id.viewactivity_text_claimed);
        layout_recycle = view.findViewById(R.id.layout_recycle);
        activityText.setOnClickListener(this);
        claimedText.setOnClickListener(this);


        viewActivityScroll = view.findViewById(R.id.viewactivity_scroll);
        recyclerView = view.findViewById(R.id.view_activity_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        Fonts.fontFinkHeavyRegularTextView(activityText, 16,
                AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(claimedText, 16,
                AppConstants.COLORWHITE, getActivity().getAssets());

        setDefaultPressed(0);

        refreshView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.viewactivity_text_activity:
                setDefaultPressed(0);

                fetchViewActivityServer();
                break;
            case R.id.viewactivity_text_claimed:
                setDefaultPressed(1);

                fetchClaimedViewFromServer();
                break;
        }

    }

    private void setDefaultPressed(int choose) {
        //activityText.setPressed(false);
        //claimedText.setPressed(false);

        isActivityOpen = false;
        isRewardOpen = false;

        Fonts.fontFinkHeavyRegularTextView(activityText, 16,
                AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(claimedText, 16,
                AppConstants.COLORWHITE, getActivity().getAssets());

//        mParntReward.removeAllViews();

        if (choose == 0) {
            isActivityOpen = true;

            if (listRewardActivityBean != null
                    && listRewardActivityBean.size() > 0) {
                // createActivityDetailView(listRewardActivityBean);
            }

            //activityText.setPressed(true);
            activityText.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.btn_check_left_white));
            claimedText.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.btn_check_right_transparent));

            Fonts.fontFinkHeavyRegularTextView(activityText, 16,
                    AppConstants.COLORBG, getActivity().getAssets());
        } else {
            isRewardOpen = true;

            if (listRewardClaimBean != null
                    && listRewardClaimBean.size() > 0) {
                // createClaimDetailView(listRewardClaimBean);
            }

            //claimedText.setPressed(true);
            activityText.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.btn_check_left_transparent));
            claimedText.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.btn_check_right_white));

            Fonts.fontFinkHeavyRegularTextView(claimedText, 16,
                    AppConstants.COLORBG, getActivity().getAssets());
        }
    }

    @Override
    public void notifyRefresh(String className) {
        // TODO Auto-generated method stub
        if (className.equalsIgnoreCase("showViewActivityPage"))
            refreshView();
    }

    private void refreshView() {
        // Create Font for every textview and buttons;
        if (Engine.isNetworkAvailable(getActivity())) {
            if (RotiHomeActivity.checkIfLogin()) {
                fetchViewActivityServer();
            } else {
                // mHomePage.showLoginOptionPage(false);
            }
        } else
            AppConstants.showMsgDialog("",
                    AppConstants.ERRORNETWORKCONNECTION, getActivity());
    }

    /*
    get all the rewards dedicated to the user
     */

    private void fetchViewActivityServer() {

        RotiHomeActivity.getProgressDialog().show();
        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");

        String url = getResources().getString(R.string.BASE_URL) + "/user/activity?" + "auth_token=" + authToken + "&appkey=" + getResources().getString(R.string.APPKEY) + "&locale="
                + getResources().getString(R.string.LOCALE_HEADER_VALUE);
        Log.d("Fetch", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Fetch Image From Server", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        LastTransactionResponse.Receipt receipt = Gson.getGson().fromJson(response.toString(), LastTransactionResponse.Receipt.class);
                        LastTransactionResponse.Receipt.RewardActivityResponse rewardActivityResponse = Gson.getGson().fromJson(response.toString(), LastTransactionResponse.Receipt.RewardActivityResponse.class);

                        listRewardActivityBean = new ArrayList<LastTransactionResponse.Receipt>();
                        transactionResponses = new ArrayList<LastTransactionResponse>();


                        if (!rewardActivityResponse.getStatus()) {

                            layout_recycle.setVisibility(View.GONE);

                        }


                        if (rewardActivityResponse.getStatus()) {

                            layout_recycle.setVisibility(View.VISIBLE);

                            for (int i = 0; i < rewardActivityResponse.getReceipts().size(); i++) {

                                String activityType = rewardActivityResponse.getReceipts().get(i).getActivityType();


                                LastTransactionResponse.Receipt rewardActivityBean = new LastTransactionResponse.Receipt();
                                LastTransactionResponse.Receipt.RewardActivityResponse rewardActivityBean1 = new LastTransactionResponse.Receipt.RewardActivityResponse();

                                LastTransactionResponse lastTransactionResponse = new LastTransactionResponse();

                                rewardActivityBean.setType(rewardActivityResponse.getReceipts().get(i).getActivityType());
                                Log.d("Type", rewardActivityResponse.getReceipts().get(i).getActivityType());

                                if (activityType.equals("payments")) {
                                    rewardActivityBean.setId(rewardActivityResponse.getReceipts().get(i).getId());

                                    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");

                                    try {
                                        Date dateObj = curFormater.parse(rewardActivityResponse.getReceipts().get(i).getCreatedAt());
                                        SimpleDateFormat postFormater = new SimpleDateFormat("MM/dd/yyyy");
                                        String newDateStr = postFormater.format(dateObj);
                                        rewardActivityBean.setCreatedAt(newDateStr);

                                        Log.d("New date", newDateStr);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    rewardActivityBean.setAmount(rewardActivityResponse.getReceipts().get(i).getAmount());

                                    rewardActivityBean.setAddress(rewardActivityResponse.getReceipts().get(i).getRestaurant());


                                } else {
                                    rewardActivityBean.setId(rewardActivityResponse.getReceipts().get(i).getId());

                                    String jsonStr = String.valueOf(rewardActivityResponse.getReceipts().get(i).getLastTransaction());

                                    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");

                                    try {
                                        Date dateObj = curFormater.parse(rewardActivityResponse.getReceipts().get(i).getLastTransaction().getCreatedAt());
                                        Log.d("Date1", dateObj.toString());
                                        SimpleDateFormat postFormater = new SimpleDateFormat("MM/dd/yyyy");
                                        String newDateStr = postFormater.format(dateObj);
                                        rewardActivityBean.setCreatedAt(newDateStr);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
//                                }
                                    }

                                    lastTransactionResponse.setStatus(rewardActivityResponse.getReceipts().get(i).getLastTransaction().getStatus());
                                    lastTransactionResponse.setTotalPointsEarned(rewardActivityResponse.getReceipts().get(i).getLastTransaction().getTotalPointsEarned());
                                    Log.d("points1", rewardActivityResponse.getReceipts().get(i).getLastTransaction().getTotalPointsEarned());
                                    Log.d("status1", String.valueOf(rewardActivityResponse.getReceipts().get(i).getLastTransaction().getStatus()));
                                    lastTransactionResponse.setAdminId(rewardActivityResponse.getReceipts().get(i).getLastTransaction().getAdminId());


                                    listRewardActivityBean.add(rewardActivityBean);
                                    transactionResponses.add(lastTransactionResponse);


                                }
                            }

                            adapter = new ActivityDetailViewAdapter(listRewardActivityBean, transactionResponses);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                        }

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

    private void fetchClaimedViewFromServer() {
        RotiHomeActivity.getProgressDialog().show();
        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");

        String url = getResources().getString(R.string.BASE_URL) + "/rewards/activity?" + "auth_token=" + authToken + "&appkey=" + getResources().getString(R.string.APPKEY);
        Log.d("Fetch", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("fetchClaimedView", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();


                        listRewardClaimBean = new ArrayList<RewardClaimResponse>();
                        rewards = new ArrayList<RewardClaimResponse.Activity.Reward>();
                        activities = new ArrayList<RewardClaimResponse.Activity>();

                        RewardClaimResponse.Activity.Reward reward = new RewardClaimResponse.Activity.Reward();
                        RewardClaimResponse.Activity activity = new RewardClaimResponse.Activity();

                        /*
                        this is the serilized object
                         */
                        RewardClaimResponse rewardClaimResponse = Gson.getGson().fromJson(response.toString(), RewardClaimResponse.class);


                        if (!rewardClaimResponse.getStatus()) {

                            layout_recycle.setVisibility(View.GONE);

                        }


                        if (rewardClaimResponse.getStatus()) {

                            for (int i = 0; i < rewardClaimResponse.getActivities().size(); i++) {

                                String jsonStr = String.valueOf(rewardClaimResponse.getActivities().get(i).getReward());
                                reward.setId(rewardClaimResponse.getActivities().get(i).getReward().getId());

                                SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");

                                try {
                                    Date dateObj = curFormater.parse(rewardClaimResponse.getActivities().get(i).getReward().getCreatedAt());

                                    SimpleDateFormat postFormater = new SimpleDateFormat("MM/dd/yyyy");
                                    String newDateStr = postFormater.format(dateObj);
                                    reward.setCreatedAt(newDateStr);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                reward.setName(rewardClaimResponse.getActivities().get(i).getReward().getName());

                                reward.setPoints(rewardClaimResponse.getActivities().get(i).getReward().getPoints());

                                reward.setChainId(rewardClaimResponse.getActivities().get(i).getReward().getChainId());

                                Log.d("Name", rewardClaimResponse.getActivities().get(i).getReward().getName());


                                try {
                                    Date dateObj = curFormater.parse(rewardClaimResponse.getActivities().get(i).getClaimDate());

								/*SimpleDateFormat postFormater = new SimpleDateFormat(
										"MMM d, yyyy");*/
                                    SimpleDateFormat postFormater = new SimpleDateFormat("MM/dd/yyyy");

                                    String newDateStr = postFormater.format(dateObj);

                                    activity.setClaimDate(newDateStr);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                rewards.add(reward);
                                activities.add(activity);


                            }
                        }
                        adapter = new ActivityRedeemedAdapter(rewards, activities);
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
        VolleyControllerAndTracker.getInstance().addToRequestQueue(jsonObjReq, "");


    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }


}

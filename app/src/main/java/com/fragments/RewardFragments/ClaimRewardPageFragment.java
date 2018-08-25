package com.fragments.RewardFragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fragments.HomeFragment;
import com.fragments.LoginOptionFragment;
import com.fragments.PostSignUpEmailFragment;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.RefreshListner;
import com.postmodels.FetchRewardValuesModel;
import com.responsemodels.Balance;
import com.responsemodels.EnjoyRewardResponse;
import com.responsemodels.FetchRewardResponse;
import com.responsemodels.WarnResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class ClaimRewardPageFragment extends Fragment implements View.OnClickListener, RefreshListner {


    private Balance.MyGoodieRewardsResponse.Reward reward;
    private boolean statusRedeem = false;
    private TextView timeredeemTV, bottomDesc, rewardName;
    private ImageView rewardsImage;
    Button back_btn, claim_btn;
    String rewardLocateid = "";
    private Tracker mTracker;
    private String SCREEN_NAME = "Claim Reward Page Fragment";
    public double latitude = 0.0, longitude = 0.0;



    View view;

    public ClaimRewardPageFragment() {

    }

    @SuppressLint("ValidFragment")
    public ClaimRewardPageFragment(Balance.MyGoodieRewardsResponse.Reward reward) {
        this.reward = reward;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Engine.CheckEnableGPS(getActivity())) {

            try {
                RotiHomeActivity.startGPS();


                latitude = RotiHomeActivity.getGetLatLongObj().getLatitude();
                longitude = RotiHomeActivity.getGetLatLongObj().getLongitude();
            } catch (Exception e) {

            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.reward_claim, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {

        setTracker();

        bottomDesc = view.findViewById(R.id.reward_claim_text_redeemtitle);
        back_btn = view.findViewById(R.id.back_btn);

        back_btn.setOnClickListener(this);

        claim_btn = view.findViewById(R.id.reward_claim_image_reedem_btn);
        claim_btn.setOnClickListener(this);
        rewardsImage = view.findViewById(R.id.rewardsImage);
        rewardName = view.findViewById(R.id.rewardName);
        rewardName.setText(reward.getName());

        bottomDesc.setText(reward.getFineprint());

        Fonts.fontFinkHeavyRegularTextView(rewardName, 30, AppConstants.COLORBLUE, getActivity().getAssets());

        if (reward.getFineprint().length() > 20) {
            Fonts.fontRobotoCodensedRegularTextView(bottomDesc, 10, AppConstants.COLORBLUE, getActivity().getAssets());

        } else {
            Fonts.fontRobotoCodensedRegularTextView(bottomDesc, 15, AppConstants.COLORBLUE, getActivity().getAssets());
        }
        Fonts.fontRobotoCodensedBoldTextView(back_btn, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(claim_btn, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        if (!reward.getImageUrl().equals("")) {

            DownloadImageTask(reward.getImageUrl());
        }
        refreshView();

    }

    private void DownloadImageTask(String imageUrl) {
        RotiHomeActivity.getProgressDialog().show();
        ImageRequest request = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {

                        RotiHomeActivity.getProgressDialog().dismiss();
                        rewardsImage.setImageBitmap(bitmap);


                        Log.d("Bitmap", bitmap.toString());


                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        RotiHomeActivity.getProgressDialog().dismiss();
                    }
                });
// Access the RequestQueue through your singleton class.
        VolleyControllerAndTracker.getInstance().addToRequestQueue(request, "");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_btn:
                //Todo handle back click
                break;
            case R.id.reward_claim_image_reedem_btn:
                if (Engine.isNetworkAvailable(getActivity())) {


                    fetchWarningValuesFromServer();
                } else {
                    clearBackStack();
                    Engine.getInstance().setSetNextPage(AppConstants.showRewardPageFragment);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                    transaction.addToBackStack(AppConstants.TAG_SIGN);
                    transaction.commit();

                }

                break;
        }

    }


    private void refreshView() {
        if (Engine.isNetworkAvailable(getActivity())) {

            if (RotiHomeActivity.checkIfLogin()) {
                fetchRewardAddressFromServer();
            } else {

                if (RotiHomeActivity.checkIfemailId()) {

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    String email = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINID, "");

                    String password = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINPAS, "");

                    Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                    getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                    transaction.addToBackStack(AppConstants.TAG_SIGN);
                    transaction.commit();
                } else {
                    clearBackStack();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    Engine.getInstance().setSetNextPage(AppConstants.showRewardPageFragment);
                    transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                    transaction.addToBackStack(AppConstants.TAG_SIGN);
                    transaction.commit();
                }

            }
        }
    }


    @Override
    public void notifyRefresh(String className) {

        if (className.equalsIgnoreCase("ClaimRewardPageFragment")) {
            refreshView();
        }

    }

    /*
    volley call
     */
    private void fetchRewardAddressFromServer() {

        RotiHomeActivity.getProgressDialog().show();
        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");
        String url = getResources().getString(R.string.BASE_URL) + "/rewards/locate?" + "auth_token=" + authToken + "&appkey=" + getResources().getString(R.string.APPKEY) +
                "&lat="
                + latitude
                + "&lng="
                + longitude;

        Log.d("Fetch", url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Fetch Reward Address", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        FetchRewardResponse fetchRewardRespons = Gson.getGson().fromJson(response.toString(), FetchRewardResponse.class);


                        if (fetchRewardRespons.getStatus()) {

                            for (int i = 0; i < fetchRewardRespons.getRestaurants().size(); i++) {
                                String address = fetchRewardRespons.getRestaurants().get(i).getAddress();

                                rewardLocateid = String.valueOf(fetchRewardRespons.getRestaurants().get(i).getId());

                                Log.d("rewardLocaton", rewardLocateid);
                            }


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
        VolleyControllerAndTracker.getInstance().addToRequestQueue(jsonObjReq, "");
    }
     /*
     volley call
     */

    private void fetchWarningValuesFromServer() {
        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");
        RotiHomeActivity.getProgressDialog().show();

        FetchRewardValuesModel fetchRewardResponse = new FetchRewardValuesModel(getResources().getString(R.string.APPKEY), authToken, String.valueOf(reward.getId()),
                latitude + "", longitude + "", rewardLocateid, "true");
        String json = Gson.getGson().toJson(fetchRewardResponse);


        String submitUrl = getResources().getString(R.string.BASE_URL) + "/rewards/claim";

        JsonObjectRequest request_json = null;
        try {

            request_json = new JsonObjectRequest(Request.Method.POST, submitUrl, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Warn response", response.toString());
                            RotiHomeActivity.getProgressDialog().dismiss();

                            WarnResponse warnResponse = Gson.getGson().fromJson(response.toString(), WarnResponse.class);

                            String warn_tile = "";
                            String warn_body = "";
                            if (warnResponse.getWarnTile().equalsIgnoreCase("Are You sure?")) {
                                warn_tile = warnResponse.getWarnTile();
                                warn_body = warnResponse.getWarnBody();

                                ClaimRewardAsyncTask();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    VolleyLog.e("Error: ", error.getMessage());

                    RotiHomeActivity.getProgressDialog().dismiss();


                    VolleyLog.d("le6end4", "Error: " + error.getMessage());
                    Log.d("le6end4", "fetchOrderSiteFromServer Error: " + error.getMessage());
                    String json = null;

                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        switch (response.statusCode) {
                            case 401:
                                json = new String(response.data);
                                json = Dialogs.trimMessage(json, "message");
                                if (json != null)
                                    Dialogs.showMsgDialog("", json, (getActivity()));
                                break;
                            default:
                                Dialogs.showMsgDialog("", AppConstants.BLANKMESSAGEREPLACEMENT, (Activity) getContext());
                                break;
                        }
                    }

                }
            });

            int socketTimeout = 15000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request_json.setRetryPolicy(policy);


        } catch (
                org.json.JSONException e)

        {
            e.printStackTrace();
        }
        /*
         add the request object to the queue to be executed
         */
        VolleyControllerAndTracker.getInstance().

                addToRequestQueue(request_json);


    }

    private void ClaimRewardAsyncTask() {


        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");

        FetchRewardValuesModel fetchRewardResponse = new FetchRewardValuesModel(getResources().getString(R.string.APPKEY), authToken, String.valueOf(reward.getId()),
                latitude+ "", longitude+ "", rewardLocateid);
        String json = Gson.getGson().toJson(fetchRewardResponse);


        RotiHomeActivity.getProgressDialog().show();
        String submitUrl = getResources().getString(R.string.BASE_URL) + "/rewards/claim";

        JsonObjectRequest request_json = null;
        try {

            request_json = new JsonObjectRequest(Request.Method.POST, submitUrl, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Warn response 1", response.toString());
                            RotiHomeActivity.getProgressDialog().dismiss();


                            EnjoyRewardResponse enjoyRewardResponse = Gson.getGson().fromJson(response.toString(), EnjoyRewardResponse.class);

                            String reward_timer = "";
                            if (enjoyRewardResponse.getStatus()) {

                                try {

                                    Engine.getInstance().setSetNextPage(AppConstants.showRewardPageFragment);
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.home_linear_container, new RewardRedeemedPageFragment(reward, enjoyRewardResponse.getRewardCode()), AppConstants.TAG_REWARD);
                                    transaction.addToBackStack(AppConstants.TAG_REWARD);
                                    transaction.commit();


                                    reward_timer = String.valueOf(enjoyRewardResponse.getRewardTimer());
                                    try {
                                        startTime = Integer.parseInt(reward_timer);// 30;//
                                        startTime -= 1;
                                        startTime = startTime * 1000;
                                        formatTime(startTime);
                                        createTimeRemainingView();
                                        if (!timerHasStarted) {
                                            countDownTimer.start();
                                            timerHasStarted = true;
                                        } else {
                                            countDownTimer.cancel();
                                            timerHasStarted = false;
                                        }
                                    } catch (Exception e) {
                                    }
                                } catch (Exception e) {
                                }
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    VolleyLog.e("Error: ", error.getMessage());
                    if (RotiHomeActivity.getProgressDialog() != null && RotiHomeActivity.getProgressDialog().isShowing())
                        RotiHomeActivity.getProgressDialog().dismiss();


                    VolleyLog.d("le6end4", "Error: " + error.getMessage());
                    Log.d("le6end4", "fetchOrderSiteFromServer Error: " + error.getMessage());
                    String json = null;

                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        switch (response.statusCode) {
                            case 401:
                                json = new String(response.data);
                                json = Dialogs.trimMessage(json, "message");
                                if (json != null)
                                    Dialogs.showMsgDialog("", json, (getActivity()));
                                break;
                            default:
                                Dialogs.showMsgDialog("", AppConstants.BLANKMESSAGEREPLACEMENT, (Activity) getContext());
                                break;
                        }
                    }

                }
            });

            int socketTimeout = 15000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request_json.setRetryPolicy(policy);


        } catch (
                org.json.JSONException e)

        {
            e.printStackTrace();
        }
        /*
         add the request object to the queue to be executed
         */
        VolleyControllerAndTracker.getInstance().

                addToRequestQueue(request_json);


    }

    private void formatTime(long time) {
        long min = time / 60000;
        long sec = time - min * 60000;
        long showTime = sec / 1000;
        String delim = ":";
        if (showTime < 10)
            delim = ":0";
        if (min < 1 && showTime < 1)
            showTime = 0;
        // timeremainingvalueTV.setText(min + delim + showTime);
    }

    private void createTimeRemainingView() {

        countDownTimer = new RedeemCountDownTimer(startTime, interval);

    }

    private boolean timerHasStarted = false;
    private RedeemCountDownTimer countDownTimer;
    private long startTime = 60000;
    private final long interval = 1000;
    private long timeElapsed = 0;

    public class RedeemCountDownTimer extends CountDownTimer {
        public RedeemCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            countDownTimer.cancel();
            timerHasStarted = false;
            countDownTimer = null;

        }

        @Override
        public void onTick(long millisUntilFinished) {
            startTime = startTime - interval;// millisUntilFinished;
            timeElapsed = startTime;// millisUntilFinished;
            formatTime(timeElapsed);
        }
    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }


    public void clearBackStack() {
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            android.support.v4.app.FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);


            getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        }

    }

}

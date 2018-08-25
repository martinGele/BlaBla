package com.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adapters.RewardsAdapter;
import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fragments.RewardFragments.ClaimRewardPageGiftFragment;
import com.fragments.RewardFragments.PromoPageFragment;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.NavigationItemChangedListener;
import com.interfaces.RefreshListner;
import com.interfaces.RewardInterface;
import com.responsemodels.Balance;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.util.Dialogs.trimMessage;

public class RewardsFragment extends Fragment implements View.OnClickListener, RefreshListner {


    TextView scrollText;
    TextView refreshBtn;
    LinearLayout mParntReward;
    public ArrayList<Balance> listMyGoodieRewardsBean;
    public ArrayList<Balance.MyGoodieRewardsResponse.Reward> listMyGoodieRewardsBean2;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public double mTotalPoint = 0;
    private TextView rewardpointsvalueTextView, titleTextView, ExpireText;


    private Tracker mTracker;
    private String SCREEN_NAME = "Rewards Fragment";

    View view;

    @Override
    public void onResume() {
        super.onResume();
        refreshView();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.enjoy_mygoodies, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {
        ((NavigationItemChangedListener) getActivity()).onItemChanged(AppConstants.ITEM_REWARDS);

        setTracker();


        scrollText = view.findViewById(R.id.scrollText);
        refreshBtn = view.findViewById(R.id.refreshBtn);
        titleTextView = view.findViewById(R.id.enjoy_mygodie_reward_list_text_title);

        scrollText.setVisibility(View.GONE);
        refreshBtn.setVisibility(View.GONE);
        refreshBtn.setOnClickListener(this);


        TextView gotPromoMenu = view.findViewById(R.id.gotPromoText);
        RelativeLayout gotPromoBtn = view.findViewById(R.id.gotPromoBtn);
        recyclerView = view.findViewById(R.id.enjoy_mygoodies_linear_reward_parent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        gotPromoBtn.setOnClickListener(this);

        Fonts.fontFinkHeavyRegularTextView(scrollText, 18,
                AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(refreshBtn, 18,
                AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(gotPromoMenu, 18,
                AppConstants.COLORWHITE, getActivity().getAssets());
        refreshBtn.setVisibility(View.VISIBLE);


        refreshView();

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (v.getId()) {

            case R.id.refreshBtn:
                refreshBtn.setVisibility(View.GONE);
                refreshView();
                break;
            case R.id.gotPromoBtn:
                transaction.replace(R.id.home_linear_container, new PromoPageFragment(), AppConstants.TAG_REWARD);
                transaction.addToBackStack(AppConstants.TAG_REWARD);
                transaction.commit();


                break;
        }
    }

    private void refreshView() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        try {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }

        if (Engine.isNetworkAvailable(getActivity())) {
            if (RotiHomeActivity.checkIfLogin()) {
                fetchRewardsFromServer();
            } else {


                if (RotiHomeActivity.checkIfemailId()) {

                    String email = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINID, "");

                    String password = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINPAS, "");

                    Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                    getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                    transaction.addToBackStack(AppConstants.TAG_SIGN);
                    transaction.commit();
                } else {


                    clearBackStack();
                    Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                    transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                    transaction.addToBackStack(AppConstants.TAG_SIGN);
                    transaction.commit();
                }


            }
        } else
            AppConstants.showMsgDialog("",
                    AppConstants.ERRORNETWORKCONNECTION, getActivity());
    }

    public  void fetchRewardsFromServer() {
        String authToken = RotiHomeActivity.getPreference().getString(
                AppConstants.PREFAUTH_TOKEN, "");


        String url = getResources().getString(R.string.BASE_URL) + "/rewards?" + "auth_token=" + authToken + "&appkey="
                + getResources().getString(R.string.APPKEY) + "&locale="
                + getResources().getString(R.string.LOCALE_HEADER_VALUE);
        RotiHomeActivity.getProgressDialog().show();
        final JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        RotiHomeActivity.getProgressDialog().dismiss();
                        Log.d("REwARDS", response.toString());

                        Balance.MyGoodieRewardsResponse balance = Gson.getGson().fromJson(response.toString(), Balance.MyGoodieRewardsResponse.class);

                        if (balance.getStatus()) {

                            adapter = new RewardsAdapter(balance.getRewards(), (RewardInterface) getActivity());

                            recyclerView.setAdapter(adapter);


                            scrollText.setVisibility(View.VISIBLE);
                            scrollText.setText(R.string.purchase_six_items);

                            refreshBtn.setVisibility(View.GONE);

                        }
                    }
                }, new Response.ErrorListener()

        {
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
                            json = trimMessage(json, "message");
                            if (json != null)
                                Dialogs.showMsgDialog("", json, (getContext()));
                            break;

                        case 501:
                            json = new String(response.data);
                            json = trimMessage(json, "message");
                            if (json != null)
                                Dialogs.showMsgDialog("", json, (getContext()));
                            break;
                        case 500:
                            json = new String(response.data);
                            json = trimMessage(json, "message");
                            if (json != null)
                                Dialogs.showMsgDialog("", json, (getContext()));
                            break;
                        default:

                            Dialogs.showMsgDialog("", String.valueOf(AppConstants.BLANKMESSAGEREPLACEMENT), getContext());


                    }
                }

            }
        });

        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request_json.setRetryPolicy(policy);


        /*
         add the request object to the queue to be executed
         */
        VolleyControllerAndTracker.getInstance().

                addToRequestQueue(request_json);

    }



    @Override
    public void notifyRefresh(String className) {
        if (className.equalsIgnoreCase("RewardsFragment"))
            refreshView();
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

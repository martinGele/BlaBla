package com.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.RefreshListner;
import com.responsemodels.GetGiftCardResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;


public class AddCurrencyFragment extends Fragment implements View.OnClickListener, RefreshListner {

    public String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");

    public static TextView currentBalanceAmount, headerTxt, currentBalanceTitle, addCurrencyText;
    public static Button btnCreditCard, btnGiftCard;
    public static String giftCard;
    private Tracker mTracker;
    private String SCREEN_NAME = "Add Currency Fragment";





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.info_addcurrency, container, false);

        initView(view);
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {

            case R.id.btn_credit_card:
                if (Engine.isNetworkAvailable(getActivity())) {


                    Engine.getInstance().setSetNextPage(AppConstants.showAddCurrencyFragment);
                    clearBackStack();
                    transaction.replace(R.id.home_linear_container, new AddBalanceCreditCardFragment(), AppConstants.ITEM_HOME);
                    transaction.addToBackStack(AppConstants.ITEM_HOME);
                    transaction.commit();

                } else
                    AppConstants.showMsgDialog("",
                            AppConstants.CONNECTION_FAILURE, getActivity());


                break;
            case R.id.btn_gift_card:

                Engine.getInstance().setSetNextPage(AppConstants.showAddCurrencyFragment);
                clearBackStack();
                transaction.replace(R.id.home_linear_container, new AddBalanceGiftCardFragment(giftCard), AppConstants.ITEM_HOME);
                transaction.addToBackStack(AppConstants.ITEM_HOME);
                transaction.commit();

                break;
        }
    }

    private void initView(View view) {
        setTracker();
        /*
        get all the views
         */
        headerTxt = view.findViewById(R.id.text_header);
        btnCreditCard = view.findViewById(R.id.btn_credit_card);
        btnGiftCard = view.findViewById(R.id.btn_gift_card);
        currentBalanceTitle = view.findViewById(R.id.current_balance_title);
        currentBalanceAmount = view.findViewById(R.id.current_balance_amount);
        addCurrencyText = view.findViewById(R.id.add_currency_text);
        /*
        setup all the text colors
         */
        Fonts.fontFinkHeavyRegularTextView(headerTxt, (int) (getActivity().getResources().getDimension(R.dimen.header_font_size) / getActivity().getResources().getDisplayMetrics().density), AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(currentBalanceTitle, 18, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(currentBalanceAmount, 40, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(addCurrencyText, 18, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(btnCreditCard, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(btnGiftCard, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        /*
        set them click listiners
         */
        btnCreditCard.setOnClickListener(this);
        btnGiftCard.setOnClickListener(this);


        refreshView();

    }

    private void refreshView() {

        if (Engine.isNetworkAvailable(getActivity())) {
            getGiftCardDetail();
        } else
            AppConstants.showMsgDialog("",
                    AppConstants.ERRORNETWORKCONNECTION, getActivity());
    }
    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }

    /*
    volley call to catch the gift card details
     */
    public void getGiftCardDetail() {

        RotiHomeActivity.getProgressDialog().show();

        String url = getResources().getString(R.string.BASE_URL_GIFTCARD) + "/card/?" + "appkey=" + getResources().getString(R.string.APPKEY) + "&auth_token=" + authToken;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Get Gift Card Detail", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        GetGiftCardResponse giftCardResponse = Gson.getGson().fromJson(response.toString(), GetGiftCardResponse.class);

                        try {

                            if (giftCardResponse.getStatus()) {
                                giftCard = giftCardResponse.getCardNumber();
                                String balance = String.valueOf(giftCardResponse.getBalance());
                                currentBalanceAmount.setText("$" + String.format("%.2f", Float.valueOf(balance)));


                                Log.d("Bal", String.valueOf(giftCard));
                            } else {
                                createDefaultGiftCard();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppConstants.showMsgDialog("",
                                    AppConstants.BLANKMESSAGEREPLACEMENT,
                                    getContext());
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

    private void createDefaultGiftCard() {

        RotiHomeActivity.getProgressDialog().show();

        String url = getResources().getString(R.string.BASE_URL_GIFTCARD) + "/card/?" + "appkey=" + getResources().getString(R.string.APPKEY) + "&auth_token=" + authToken + "&amount=0";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Get Gift Card Detail", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        GetGiftCardResponse giftCardResponse = Gson.getGson().fromJson(response.toString(), GetGiftCardResponse.class);


                        if (giftCardResponse.getStatus()) {

                            SharedPreferences.Editor prefsEditor = null;
                            prefsEditor = RotiHomeActivity.getPreferenceEditor();
                            String giftCard = "";
                            try {
                                if (giftCardResponse.getCardNumber() != null) {
                                    giftCard = giftCardResponse.getCardNumber();
                                    prefsEditor.putString(AppConstants.PREFGIFT_CARD, giftCard);
                                    Log.d("giftCard", giftCard);
                                }
                            } catch (Exception e) {
                            }
                            prefsEditor.commit();

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

    @Override
    public void notifyRefresh(String className) {

        refreshView();
    }

    public void clearBackStack() {
        android.support.v4.app.FragmentManager fragmentManag = getFragmentManager();
        if (fragmentManag.getBackStackEntryCount() > 0) {


        }

    }

    public void clearBackStackSign() {

        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack(AppConstants.TAG_SIGN, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


}

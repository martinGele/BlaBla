package com.payment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
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
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.util.Dialogs.trimMessage;


public class ShowCardSummaryPageFragment extends Fragment implements View.OnClickListener {

    String cardNum, expiration;
    TextView pageHeader, cardNumberLabel, cardNumberValue, expiringLabel, expiringValue, securedbyLabel;
    Button updateBtn, deleteBtn;
    View view;
    private static AlertDialog.Builder alertDialogBuilder;


    @SuppressLint("ValidFragment")
    public ShowCardSummaryPageFragment(String cardNum, String expiration) {
        this.cardNum = cardNum;
        this.expiration = expiration;
    }

    public ShowCardSummaryPageFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.card_summary, container, false);


        initView(view);
        return view;
    }


    private void initView(View view) {


        /*
        get all the views
         */
        pageHeader = view.findViewById(R.id.page_header_text);
        cardNumberLabel = view.findViewById(R.id.cardNumberLabel);
        cardNumberValue = view.findViewById(R.id.cardNumberValue);
        expiringLabel = view.findViewById(R.id.expiringLabel);
        expiringValue = view.findViewById(R.id.expiringValue);
        securedbyLabel = view.findViewById(R.id.txt_secured_by);
        updateBtn = view.findViewById(R.id.btn_update);
        deleteBtn = view.findViewById(R.id.btn_delete);
        /*
        set click listiners to the buttons
         */
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        /*
        set all the fonts the colors
         */
        Fonts.fontFinkHeavyRegularTextView(pageHeader,
                (int) (getActivity().getResources().getDimension(R.dimen.header_font_size)
                        / getActivity().getResources().getDisplayMetrics().density),
                AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedRegularTextView(cardNumberLabel, 15, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedRegularTextView(expiringLabel, 15, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedRegularTextView(cardNumberValue, 15, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedRegularTextView(expiringValue, 15, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedRegularTextView(securedbyLabel, 15, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(updateBtn, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(deleteBtn, 18, AppConstants.COLORWHITE, getActivity().getAssets());


        String tmpCardNumber = cardNum.replaceAll("(?=\\d{5})\\d", "x");
        tmpCardNumber = tmpCardNumber.replaceAll("(.{4})(?!$)", "$1 ");
        cardNumberValue.setText(tmpCardNumber);


        Log.d("wetzel", "cardNumber:" + cardNum);
        Log.d("wetzel", "expirationDate:" + expiration);

        expiringValue.setText(expiration);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_update:
                if (Engine.isNetworkAvailable(getActivity())) {
                    showMsgDialog("", "Delete existing credit card to update?", getActivity());
                } else {
                    AppConstants.showMsgDialog("", AppConstants.CONNECTION_FAILURE, getActivity());
                }
                break;

            case R.id.btn_delete:

                if (Engine.isNetworkAvailable(getActivity())) {
                    showMsgDialog("", "Delete existing credit card?", getActivity());
                } else {
                    AppConstants.showMsgDialog("", AppConstants.CONNECTION_FAILURE, getActivity());
                }
                break;
        }

    }

    private void showMsgDialog(String title, final String message, Context context) {
        try {
            if (alertDialogBuilder == null) {
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(message).setCancelable(false)
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alertDialogBuilder = null;

                                dialog.cancel();

                                postDeletePaymentDetail();

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialogBuilder = null;
                        dialog.cancel();

                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                alertDialogBuilder.create();
                alertDialogBuilder.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postDeletePaymentDetail() {

        RotiHomeActivity.getProgressDialog().show();
        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");

        String url = getResources().getString(R.string.BASE_URL_MOBILEPAY) + "/user/method";

        JsonObjectRequest request_json = null;
        request_json = new JsonObjectRequest(Request.Method.DELETE, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        RotiHomeActivity.getProgressDialog().dismiss();

                        Log.d("DELETE PAY", response.toString());
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();


                        clearBackStack();
                        Engine.getInstance().setSetNextPage(AppConstants.showManagePaymentFragment);
                        transaction.replace(R.id.home_linear_container, new ManagePaymentFragment(), AppConstants.TAG_PAYMENT);
                        transaction.addToBackStack(AppConstants.TAG_PAYMENT);
                        transaction.commit();


//                        if (Engine.getInstance().getSetNextPage().length() > 0) {
//                            ((OpenFragmetListener) getActivity()).startFragment(Engine.getInstance().getSetNextPage());
//                            Engine.getInstance().setSetNextPage("");
//                        }
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

                            Dialogs.showMsgDialog("", String.valueOf(AppConstants.BLANKMESSAGEREPLACEMENT), getActivity());


                    }
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appkey", getResources().getString(R.string.APPKEY));
                params.put("auth_token", authToken);

                return params;

            }
        };

        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request_json.setRetryPolicy(policy);


        /*
         add the request object to the queue to be executed
         */
        VolleyControllerAndTracker.getInstance().addToRequestQueue(request_json);

    }

    public void clearBackStack() {
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            android.support.v4.app.FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);

            getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_CARD_SUMMARY, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        }

    }

}

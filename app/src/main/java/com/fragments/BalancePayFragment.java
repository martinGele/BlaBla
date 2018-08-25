package com.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.analytics.Tracker;
import com.interfaces.OpenFragmetListener;
import com.postmodels.SubmitPaymentAddBalanceModel;
import com.responsemodels.GetGiftCardResponse;
import com.responsemodels.SubmitPaymentResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.util.Dialogs.trimMessage;

@SuppressLint("ValidFragment")
public class BalancePayFragment extends Fragment implements View.OnClickListener {


    private Tracker mTracker;
    private String SCREEN_NAME = "Balance Pay Fragment";
    static AlertDialog.Builder alertDialogBuilder;



    @SuppressLint("ValidFragment")
    public BalancePayFragment(String thisCardNumber, String giftCardAmounntPay) {
        this.thisCardNumber1 = thisCardNumber;
        this.giftCardAmountPay = giftCardAmounntPay;

    }


    Button btnPay;


    String thisCardNumber1, giftCardAmountPay;
    String giftCardNumber;


    TextView headerTxt, payTitle, payAmountTitle, payAmountValue, payWithTitle, payWithValue;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_balance_pay, container, false);

        initView(view);
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initView(View view) {
        setTracker();

        headerTxt = view.findViewById(R.id.text_header);
        btnPay = view.findViewById(R.id.btn_pay);
        payTitle = view.findViewById(R.id.pay_title);
        payAmountTitle = view.findViewById(R.id.pay_amount_title);
        payAmountValue = view.findViewById(R.id.pay_amount);
        payWithTitle = view.findViewById(R.id.pay_with_title);
        payWithValue = view.findViewById(R.id.pay_with);

        Fonts.fontFinkHeavyRegularTextView(headerTxt,
                (int) (getActivity().getResources().getDimension(R.dimen.header_font_size) /
                        getActivity().getResources().getDisplayMetrics().density),
                AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(payTitle, 16, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(payAmountTitle, 18, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(payAmountValue, 30, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(payWithTitle, 18, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(payWithValue, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(btnPay, 18, AppConstants.COLORWHITE, getActivity().getAssets());


        payAmountValue.setText("$" + String.format("%.2f", Float.valueOf(giftCardAmountPay)));
        payWithValue.setText("CARD ENDING IN ..." + thisCardNumber1.substring(thisCardNumber1.length() - 4));

        Log.d("Card", thisCardNumber1);


        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog inputCvvDialog = new Dialog(getActivity());
                inputCvvDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.input_cvv_dialog
                        , null);

                TextView infoText = (TextView) dialogView.findViewById(R.id.text_cvv_info);
                final EditText editCvv = (EditText) dialogView.findViewById(R.id.edit_cvv);
                Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
                final Button btnEnter = (Button) dialogView.findViewById(R.id.btn_enter);

                editCvv.setText("");
                btnEnter.setEnabled(false);

                Fonts.fontFinkHeavyRegularTextView(infoText, 14,
                        AppConstants.COLORWHITE, getActivity().getAssets());
                Fonts.fontRobotoCodensedBoldTextView(editCvv, 16,
                        AppConstants.COLORORANGE, getActivity().getAssets());
                Fonts.fontRobotoCodensedBoldTextView(btnCancel, 18,
                        AppConstants.COLORWHITE, getActivity().getAssets());
                Fonts.fontRobotoCodensedBoldTextView(btnEnter, 18,
                        AppConstants.COLORWHITE, getActivity().getAssets());

                editCvv.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (filterLongEnough(editCvv)) {
                            try {
                                btnEnter.setEnabled(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            btnEnter.setEnabled(false);
                        }
                    }

                    private boolean filterLongEnough(EditText tmpEditText) {
                        return tmpEditText.getText().toString().trim().length() >= 3;
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {

                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editCvv.getWindowToken(), 0);

                        } catch (Exception e) {
                        }

                        inputCvvDialog.dismiss();
                    }
                });

                btnEnter.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {

                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editCvv.getWindowToken(), 0);

                        } catch (Exception e) {
                        }

                        inputCvvDialog.dismiss();
                        try {
                            getValueFromDialog(editCvv.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                inputCvvDialog.setCancelable(false);
                inputCvvDialog.setContentView(dialogView);
                inputCvvDialog.show();
            }
        });

        refreshView();

    }

    private void getValueFromDialog(String cvvValue) throws JSONException {
        if (Engine.isNetworkAvailable(getActivity())) {
            if (thisCardNumber1 != null) {
                if (cvvValue.length() >= 3) {
//                    String[] params = {String.valueOf(thisAmount), cvvValue};
                    /*
                    with model we pass the value to be pay with the credit card
                     */
                    SubmitPaymentAddBalanceModel submitPaymentAddBalanceModel = new SubmitPaymentAddBalanceModel(giftCardAmountPay, cvvValue);
                    String model = Gson.getGson().toJson(submitPaymentAddBalanceModel);
                    submitPayment(model);
                } else {
                    Dialogs.showMsgDialog("", "Cvv not valid!",
                            getActivity());
                }
            } else {
                AppConstants.showMsgDialog("", "Amount not valid!",
                        getActivity());
            }
        } else {
            AppConstants.showMsgDialog("",
                    AppConstants.CONNECTION_FAILURE, getActivity());
        }
    }


    public void refreshView() {
        if (Engine.isNetworkAvailable(getActivity())) {
            getGiftCardDetail();
        } else
            AppConstants.showMsgDialog("",
                    AppConstants.ERRORNETWORKCONNECTION, getActivity());
    }

    /*
    volley call to submit payment
     */
    private void submitPayment(String json) throws JSONException {

        final String authToken = RotiHomeActivity.getPreference().getString(
                AppConstants.PREFAUTH_TOKEN, "");

        RotiHomeActivity.getProgressDialog().show();

        String url = getResources().getString(R.string.BASE_URL_MOBILEPAY) + "/user/app/payment";

        JsonObjectRequest request_json = null;
        request_json = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(json),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("PAYRESPONSE", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();
                        SubmitPaymentResponse submitPaymentResponse = Gson.getGson().fromJson(response.toString(), SubmitPaymentResponse.class);

                        if (submitPaymentResponse.getCode() == 0) {

                            AppConstants.showMsgDialog("", submitPaymentResponse.getMessage(), (getContext()));
                            executeDepositGC();
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

    /*
    place the amount to the default gift card
     */

    private void executeDepositGC() {

        final String authToken = RotiHomeActivity.getPreference().getString(
                AppConstants.PREFAUTH_TOKEN, "");

        String url = getResources().getString(R.string.BASE_URL_GIFTCARD) + "/card/deposit?" + "cardNumber=" + giftCardNumber + "&appkey=" + getResources().getString(R.string.APPKEY)
                + "&auth_token=" + authToken + "&amount=" + giftCardAmountPay;

        JsonObjectRequest request_json = null;
        request_json = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("execute Deposit GC", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        GetGiftCardResponse giftCardResponse = Gson.getGson().fromJson(response.toString(), GetGiftCardResponse.class);

                        if (giftCardResponse.getStatus()) {



                            if (alertDialogBuilder == null) {
                                alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                alertDialogBuilder.setMessage(giftCardResponse.getMessage()).setCancelable(false).setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int id) {
                                                alertDialogBuilder = null;
                                                dialog.cancel();


                                                if (Engine.getInstance().getSetNextPage().length() > 0) {
                                                    ((OpenFragmetListener) getActivity()).startFragment(Engine.getInstance().getSetNextPage());
                                                    Engine.getInstance().setSetNextPage("");
                                                }
                                            }
                                        });
                                AlertDialog alert = alertDialogBuilder.create();
                                alert.show();


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

    /*
    volley call to get gift card detail
     */

    public void getGiftCardDetail() {
        final String authToken = RotiHomeActivity.getPreference().getString(
                AppConstants.PREFAUTH_TOKEN, "");


        RotiHomeActivity.getProgressDialog().show();
        String url = getResources().getString(R.string.BASE_URL_GIFTCARD) + "/card/?" + "appkey=" + getResources().getString(R.string.APPKEY) + "&auth_token=" + authToken;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Client Token", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();


                        GetGiftCardResponse giftCardResponse = Gson.getGson().fromJson(response.toString(), GetGiftCardResponse.class);

                        try {

                            if (giftCardResponse.getStatus()) {
                                giftCardNumber = giftCardResponse.getCardNumber();
                            } else {
                                String message = giftCardResponse.getMessage();
                                if (!message.equals(""))
                                    AppConstants.showMsgDialog("", message, getActivity());
                                else
                                    AppConstants.showMsgDialog("", AppConstants.BLANKMESSAGEREPLACEMENT, getActivity());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AppConstants.showMsgDialog("",
                                    AppConstants.BLANKMESSAGEREPLACEMENT,
                                    getActivity());
                        }


                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                RotiHomeActivity.getProgressDialog().dismiss();

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
                        case 500:
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appkey", getResources().getString(R.string.APPKEY));
                params.put("auth_token", authToken);

                return params;
            }
        };

        // Adding request to request queue
        VolleyControllerAndTracker.getInstance().addToRequestQueue(jsonObjReq, "");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_pay:


                break;


        }
    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }
}

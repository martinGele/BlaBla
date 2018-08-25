package com.fragments.SendGiftCardFragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.OpenFragmetListener;
import com.interfaces.RefreshListner;
import com.payment.ManagePaymentFragment;
import com.postmodels.SubmitPaymentAddBalanceModel;
import com.responsemodels.GetClientTokenResponse;
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

public class GiftCardSendDetail extends Fragment implements View.OnClickListener, RefreshListner {

    View view;

    String mToName;
    String mToEmail;
    String mFromName;
    String mCustomMessage;
    String mImageUrl;
    int thisAmount;
    String thisCardNumber;
    String giftCardNumber;
    LinearLayout btnCurrentCreditCard;
    Button btnAddNewCreditCard;
    TextView textCurrentCreditCard, payTitle, payAmountTitle, payAmountValue;
    private ImageView giftcardDesignImage;


    private Tracker mTracker;
    private String SCREEN_NAME = "Gift Card Send Detail Fragment";

    public GiftCardSendDetail() {

    }

    @SuppressLint("ValidFragment")
    public GiftCardSendDetail(String mToName, String mToEmail, String mFromName, String mCustomMessage, int thisAmount, String imageUrl) {
        this.mToName = mToName;
        this.mToEmail = mToEmail;
        this.mFromName = mFromName;
        this.mCustomMessage = mCustomMessage;
        this.thisAmount = thisAmount;
        this.mImageUrl = imageUrl;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.snap_pay_send, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {


        setTracker();
        btnCurrentCreditCard = view.findViewById(R.id.btn_current_creditcard);
        /*
        set the visibility of the card holder to be gone before the api call
         */
        btnCurrentCreditCard.setVisibility(View.GONE);
        btnAddNewCreditCard = view.findViewById(R.id.btn_addnew_creditcard);
        textCurrentCreditCard = view.findViewById(R.id.text_current_creditcard_send);
        payTitle = view.findViewById(R.id.confirm_pay_title);
        payAmountTitle = view.findViewById(R.id.confirm_pay_amount_title);
        payAmountValue = view.findViewById(R.id.confirm_pay_amount);

        giftcardDesignImage = view.findViewById(R.id.giftcard_design_image);

        Fonts.fontFinkHeavyRegularTextView(payTitle, 16, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(payAmountTitle, 18, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(payAmountValue, 30, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(textCurrentCreditCard, 16, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(btnAddNewCreditCard, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        payAmountValue.setText("$"
                + String.format("%.2f", Float.valueOf(thisAmount)));

        btnCurrentCreditCard.setOnClickListener(this);
        btnAddNewCreditCard.setOnClickListener(this);

        refreshView();

        DownloadImageTask(mImageUrl);

    }

    private void refreshView() {
        if (Engine.isNetworkAvailable(getActivity())) {
            getClientToken();
        } else {
            AppConstants.showMsgDialog("", AppConstants.ERRORNETWORKCONNECTION,
                    getActivity());
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_current_creditcard:
                final Dialog inputCvvDialog = new Dialog(getActivity());
                inputCvvDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

                view = getActivity().getLayoutInflater().inflate(R.layout.input_cvv_dialog
                        , null);

                TextView infoText = (TextView) view.findViewById(R.id.text_cvv_info);
                final EditText editCvv = (EditText) view.findViewById(R.id.edit_cvv);
                Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
                final Button btnEnter = (Button) view.findViewById(R.id.btn_enter);

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
                        getValueFromDialog(editCvv.getText().toString());
                    }
                });

                inputCvvDialog.setCancelable(false);
                inputCvvDialog.setContentView(view);
                inputCvvDialog.show();

                break;

            case R.id.btn_addnew_creditcard:

                clearBackStack();
                Engine.getInstance().setSetNextPage(AppConstants.giftCardSendDetail);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.home_linear_container, new ManagePaymentFragment(), AppConstants.TAG_PAYMENT);
                transaction.addToBackStack(AppConstants.TAG_PAYMENT);
                transaction.commit();


                break;


        }
    }

    public void getValueFromDialog(String cvvValue) {
        if (Engine.isNetworkAvailable(getActivity())) {
            if (thisAmount > 0) {
                if (cvvValue.length() >= 3) {
//                    String[] params = {String.valueOf(thisAmount), cvvValue};
//                    submitPayment(params);


                    SubmitPaymentAddBalanceModel submitPaymentAddBalanceModel = new SubmitPaymentAddBalanceModel(String.valueOf(thisAmount), cvvValue);
                    String model = Gson.getGson().toJson(submitPaymentAddBalanceModel);
                    try {
                        submitPayment(model);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AppConstants.showMsgDialog("", "Cvv not valid!",
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


    @Override
    public void notifyRefresh(String className) {

        refreshView();
    }


    public void getClientToken() {

        RotiHomeActivity.getProgressDialog().show();
        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");
        String url = getResources().getString(R.string.BASE_URL_MOBILEPAY) + "/user/method";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Client Token", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        GetClientTokenResponse getClientTokenResponse = Gson.getGson().fromJson(response.toString(), GetClientTokenResponse.class);


                        try {

                            int code = getClientTokenResponse.getCode();
                            if (getClientTokenResponse.getStatus()) {
                                String cardNumber = getClientTokenResponse.getPaymentMethodId();

                                textCurrentCreditCard.setText("PAY WITH SAVED CARD - " + cardNumber.substring(cardNumber.length() - 4));
                                btnCurrentCreditCard.setTag(cardNumber);
                                btnCurrentCreditCard.setVisibility(View.VISIBLE);
                            } else {
                                btnCurrentCreditCard.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                            btnCurrentCreditCard.setVisibility(View.GONE);
                            Dialogs.showMsgDialog("", AppConstants.BLANKMESSAGEREPLACEMENT,
                                    getActivity());
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

    private void submitPayment(String json) throws JSONException {

        final String authToken = RotiHomeActivity.getPreference().getString(
                AppConstants.PREFAUTH_TOKEN, "");

        RotiHomeActivity.getProgressDialog().show();

        String url = getResources().getString(R.string.BASE_URL_MOBILEPAY) + "/user/app/payment";
        Log.d("Pay", url);

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
                            executeSendEmailGC();
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

    private void executeSendEmailGC() {
        final String authToken = RotiHomeActivity.getPreference().getString(
                AppConstants.PREFAUTH_TOKEN, "");

        RotiHomeActivity.getProgressDialog().show();

        mToName = mToEmail.replace(" ", "%20");
        mCustomMessage = mCustomMessage.replace(" ", "%20");
        mToName = mToName.replace(" ", "%20");

        String url = getResources().getString(R.string.BASE_URL_GIFTCARD) + "/card/email?" + "&email=" + mToEmail + "&toName=" + mToName
                + "&fromName=" + mFromName + "&amount="
                + thisAmount + "&imageUrl=" + mImageUrl
                + "&message=" + mCustomMessage;

        Log.d("Pay", url);

        JsonObjectRequest request_json = null;
        request_json = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("execute send email", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();
                        SubmitPaymentResponse submitPaymentResponse = Gson.getGson().fromJson(response.toString(), SubmitPaymentResponse.class);

                        if (submitPaymentResponse.getCode() == 0) {


                            //  Dialogs.showMsgDialog("", submitPaymentResponse.getMessage(), (getContext()));


                            AlertDialog.Builder alertDialogBuilder;

                            alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setMessage(submitPaymentResponse.getMessage()).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
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

    private void DownloadImageTask(final String appImageUrl) {


        RotiHomeActivity.getProgressDialog().show();
        ImageRequest request = new ImageRequest(appImageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        RotiHomeActivity.getProgressDialog().dismiss();
                        giftcardDesignImage.setImageBitmap(bitmap);

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

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }


    public void clearBackStack() {
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            android.support.v4.app.FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);

            getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_PAYMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        }

    }

}

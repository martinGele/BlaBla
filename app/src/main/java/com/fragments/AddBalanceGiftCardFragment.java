package com.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import com.responsemodels.GetGiftCardBalanceResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.util.Utility;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.util.Dialogs.trimMessage;

@SuppressLint("ValidFragment")
public class AddBalanceGiftCardFragment extends Fragment implements View.OnClickListener {

    private EditText eGiftCardCode, confirmCode;
    private TextView eGiftCardCodeHint, confirmCodeHint, addBalanceText;

    private Tracker mTracker;
    private String SCREEN_NAME = "Add Balance Fragment";

    public String authToken = RotiHomeActivity.getPreference().getString(
            AppConstants.PREFAUTH_TOKEN, "");
    Button submitBtn;

    String myGiftCard;
    View view;
    String giftCard;

    @SuppressLint("ValidFragment")
    public AddBalanceGiftCardFragment(String giftCard) {
        this.giftCard = giftCard;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_addbalance_giftcard, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {


        setTracker();
        submitBtn = view.findViewById(R.id.btn_submit);
        eGiftCardCode = view.findViewById(R.id.addbalance_edit_egiftcardcode);
        confirmCode = view.findViewById(R.id.addbalance_edit_confirm);
        eGiftCardCodeHint = view.findViewById(R.id.addbalance_edit_egiftcardcode_hint);
        confirmCodeHint = view.findViewById(R.id.addbalance_edit_confirm_hint);
        addBalanceText = view.findViewById(R.id.add_balance_text);
        Fonts.fontRobotoCodensedBoldTextView(eGiftCardCode,
                16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(confirmCode,
                16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(eGiftCardCodeHint,
                16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(confirmCodeHint,
                16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(addBalanceText,
                18, AppConstants.COLORBLUE, getActivity().getAssets());

        Fonts.fontRobotoCodensedBoldTextView(submitBtn, 18,
                AppConstants.COLORWHITE, getActivity().getAssets());

        SetTextWatcherForAmountEditView(eGiftCardCode);
        SetTextWatcherForAmountEditView(confirmCode);

        submitBtn.setOnClickListener(this);
        submitBtn.setEnabled(false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_submit:
                try {


                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {

                    e.printStackTrace();
                }

                if (Engine.isNetworkAvailable(getActivity())) {
                    fetchGetInputCardBalance();
                } else
                    AppConstants.showMsgDialog("",
                            AppConstants.CONNECTION_FAILURE, getActivity());

                break;
        }
    }


    public void fetchGetInputCardBalance() {

        RotiHomeActivity.getProgressDialog().show();
        String inputCard = eGiftCardCode.getText().toString();

        String url = getResources().getString(R.string.BASE_URL_GIFTCARD) + "/card/" + inputCard + "?";
        //String url = getResources().getString(R.string.BASE_URL_GIFTCARD) +"?"+ "/card/?";// + inputCard;
        Log.d("GIFt", url);

        RotiHomeActivity.getProgressDialog().show();
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        RotiHomeActivity.getProgressDialog().dismiss();
                        Log.d("GetInputCardBalance", response.toString());


                        GetGiftCardBalanceResponse getClientTokenResponse = Gson.getGson().fromJson(response.toString(), GetGiftCardBalanceResponse.class);

                        try {


                            if (getClientTokenResponse.getStatus()) {
                                String balance = String.valueOf(getClientTokenResponse.getBalance());

                                String inputCard = eGiftCardCode.getText().toString();

                                String message = getClientTokenResponse.getMessage();

                                AppConstants.showMsgDialog("", message, getActivity());

                                fetchGetTransferBalance(inputCard, balance);
                            } else {
                                String message = getClientTokenResponse.getMessage();
                                Dialogs.showMsgDialog("", message, getActivity());

                            }
                        } catch (Exception e) {
                            Dialogs.showMsgDialog("", getClientTokenResponse.getMessage(), getActivity());


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

    private void fetchGetTransferBalance(String card_from, String balance) {
        RotiHomeActivity.getProgressDialog().show();

        final String authToken = RotiHomeActivity.getPreference().getString(
                AppConstants.PREFAUTH_TOKEN, "");

        String url = getResources().getString(R.string.BASE_URL_GIFTCARD) + "/card/transfer" + "?appkey=" + getResources().getString(R.string.APPKEY) + "&auth_token=" + authToken
                + "&card1=" + card_from + "&card2=" + giftCard + "&amount=" + balance;

        Log.d("URLGIFT", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("FetchGetTransferBalance", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        GetGiftCardBalanceResponse giftCardResponse = Gson.getGson().fromJson(response.toString(), GetGiftCardBalanceResponse.class);

                        if (giftCardResponse.getStatus()) {

                            String message = giftCardResponse.getMessage();

                            Dialogs.showMsgDialog("", message, (getContext()));

                            if (Engine.getInstance().getSetNextPage().length() > 0) {
                                ((OpenFragmetListener) getActivity()).startFragment(Engine.getInstance().getSetNextPage());
                                Engine.getInstance().setSetNextPage("");
                            }

                        } else {
                            String messageBad = giftCardResponse.getMessage();
                            Dialogs.showMsgDialog("", messageBad, (getContext()));

                            clearBackStack();


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


    private void SetTextWatcherForAmountEditView(final EditText tmpEditText) {
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
/*				if (s.length() > 0){
					// no text entered. Center the hint text.
					tmpEditText.setGravity(Gravity.CENTER_VERTICAL);
				}else{
					// position the text type in the left top corner
					tmpEditText.setGravity(Gravity.LEFT | Gravity.TOP);
				}*/

                Utility.editTextHideShowHint(s.length(), tmpEditText, getActivity(), view);

                if (filterLongEnough(eGiftCardCode)
                        && isGiftcardAndConfirmSame(eGiftCardCode, confirmCode)) {
                    try {
                        submitBtn.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    submitBtn.setEnabled(false);
                }
            }

            private boolean filterLongEnough(EditText tmpEditText) {
                return tmpEditText.getText().toString().trim().length() > 0;
            }

            private boolean isGiftcardAndConfirmSame(EditText eGiftCardCode,
                                                     EditText confirmCode) {
                return eGiftCardCode.getText().toString()
                        .equals(confirmCode.getText().toString());
            }
        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }

    public void clearBackStack() {
        android.support.v4.app.FragmentManager fragmentManag = getFragmentManager();
        if (fragmentManag.getBackStackEntryCount() > 0) {


        }

    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }

}

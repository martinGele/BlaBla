package com.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.RefreshListner;
import com.payment.ManagePaymentFragment;
import com.responsemodels.GetClientTokenResponse;
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

import static com.util.AppConstants.alertDialogBuilder;

public class AddBalanceCreditCardFragment extends Fragment implements View.OnClickListener, RefreshListner {


    boolean isInfo = false;

    String cardNumber;

    EditText eOtherAmount, editHideFocus;
    TextView eOtherAmountHint, textCurrentCreditCard, addBalanceText;
    LinearLayout btnCurrentCreditCard;
    Button btnAddNewCreditCard;

    RadioButton rValue1, rValue2, rValue3, rValue4;

    private int VALUE1 = 15;
    private int VALUE2 = 25;
    private int VALUE3 = 35;
    private int VALUE4 = 45;

    int defaultValue = VALUE2;
    View view;
    private Tracker mTracker;
    private String SCREEN_NAME = "Add Balance Credit Card Fragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_addbalance_creditcard, container, false);

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


            case R.id.btn_current_creditcard:
                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            eOtherAmount.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }

                if (Engine.isNetworkAvailable(getActivity())) {
                    boolean isPass = false;
                    int tmpDefault = 0;
                    if (!eOtherAmount.getText().toString().equals("")) {
                        tmpDefault = Integer.valueOf(eOtherAmount.getText().toString());
                        if (tmpDefault >= 2 && tmpDefault <= 100) {
                            isPass = true;
                            defaultValue = tmpDefault;
                        }
                    } else
                        isPass = true;

                    if (isPass) {
                        Engine.getInstance().setSetNextPage(AppConstants.showHomeFragment);
                        transaction.replace(R.id.home_linear_container, new BalancePayFragment(String.valueOf(cardNumber), String.valueOf(defaultValue)));
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();


                    } else {
                        AppConstants.showMsgDialog("",
                                "Please enter a dollar amount that is between $2 and $100.", getActivity());
                    }
                } else {
                    AppConstants.showMsgDialog("",
                            AppConstants.CONNECTION_FAILURE, getActivity());
                }


                break;

            case R.id.btn_addnew_creditcard:
                Engine.getInstance().setSetNextPage(AppConstants.showAddCurrencyFragment);

                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            eOtherAmount.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }

                if (Engine.isNetworkAvailable(getActivity())) {
                    transaction.replace(R.id.home_linear_container, new ManagePaymentFragment(), AppConstants.TAG_PAYMENT);
                    transaction.addToBackStack(AppConstants.TAG_PAYMENT);
                    transaction.commit();


                } else {
                    AppConstants.showMsgDialog("",
                            AppConstants.CONNECTION_FAILURE, getActivity());
                }

                break;

        }


    }

    private void initView(View view) {


        setTracker();
        /*
        get all the views in the layout
         */
        btnCurrentCreditCard = view.findViewById(R.id.btn_current_creditcard);
        btnAddNewCreditCard = view.findViewById(R.id.btn_addnew_creditcard);
        textCurrentCreditCard = view.findViewById(R.id.text_current_creditcard);
        eOtherAmount = view.findViewById(R.id.addbalance_edit_eOtherAmount);
        eOtherAmountHint = view.findViewById(R.id.addbalance_edit_eOtherAmount_hint);
        addBalanceText = view.findViewById(R.id.add_balance_text);
        editHideFocus = view.findViewById(R.id.editHideFocus);

        rValue1 = view.findViewById(R.id.radio0);
        rValue2 = view.findViewById(R.id.radio1);
        rValue3 = view.findViewById(R.id.radio2);
        rValue4 = view.findViewById(R.id.radio3);

        /*
        setup click listiners
         */

        btnCurrentCreditCard.setOnClickListener(this);
        btnAddNewCreditCard.setOnClickListener(this);

        /*
        setup the views color and fonts
         */

        Fonts.fontRobotoCodensedBoldTextView(eOtherAmount, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(eOtherAmountHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(textCurrentCreditCard, 16, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(btnAddNewCreditCard, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(addBalanceText, 18, AppConstants.COLORBLUE, getActivity().getAssets());

        Fonts.fontRobotoCodensedBoldTextView(rValue1, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(rValue2, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(rValue3, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(rValue4, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        eOtherAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        eOtherAmount.setMaxLines(2);
        eOtherAmount.setEllipsize(TextUtils.TruncateAt.END);
        eOtherAmount.setHorizontallyScrolling(false);

        SetTextWatcherForAmountEditView(eOtherAmount);

        rValue1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    if (!eOtherAmount.getText().toString().equals(""))
                        eOtherAmount.setText("");

                    defaultValue = VALUE1;
                    rValue1.setTextColor(Color.parseColor("#e86724"));

                    rValue2.setChecked(false);
                    rValue3.setChecked(false);
                    rValue4.setChecked(false);

                    editHideFocus.requestFocus();
                    btnCurrentCreditCard.setEnabled(true);
                    closeSoftKeyBoard();
                } else {
                    rValue1.setTextColor(Color.WHITE);
                }
            }
        });

        rValue2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    if (!eOtherAmount.getText().toString().equals(""))
                        eOtherAmount.setText("");

                    defaultValue = VALUE2;
                    rValue2.setTextColor(Color.parseColor("#e86724"));

                    rValue1.setChecked(false);
                    rValue3.setChecked(false);
                    rValue4.setChecked(false);

                    editHideFocus.requestFocus();
                    btnCurrentCreditCard.setEnabled(true);
                    closeSoftKeyBoard();
                } else {
                    rValue2.setTextColor(Color.WHITE);
                }
            }
        });

        rValue3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    if (!eOtherAmount.getText().toString().equals(""))
                        eOtherAmount.setText("");

                    defaultValue = VALUE3;
                    rValue3.setTextColor(Color.parseColor("#e86724"));

                    rValue1.setChecked(false);
                    rValue2.setChecked(false);
                    rValue4.setChecked(false);

                    editHideFocus.requestFocus();
                    btnCurrentCreditCard.setEnabled(true);
                    closeSoftKeyBoard();
                } else {
                    rValue3.setTextColor(Color.WHITE);
                }
            }
        });

        rValue4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    if (!eOtherAmount.getText().toString().equals(""))
                        eOtherAmount.setText("");

                    eOtherAmount.clearFocus();
                    defaultValue = VALUE4;
                    rValue4.setTextColor(Color.parseColor("#e86724"));

                    rValue1.setChecked(false);
                    rValue2.setChecked(false);
                    rValue3.setChecked(false);

                    editHideFocus.requestFocus();
                    btnCurrentCreditCard.setEnabled(true);
                    closeSoftKeyBoard();
                } else {
                    rValue4.setTextColor(Color.WHITE);
                }
            }
        });

        refreshView();
    }

    private void refreshView() {

        if (eOtherAmount.getText().toString().equals("")) {
            rValue1.setChecked(false);
            rValue2.setChecked(true);
            rValue3.setChecked(false);
            rValue4.setChecked(false);
        } else {
            rValue1.setChecked(false);
            rValue2.setChecked(false);
            rValue3.setChecked(false);
            rValue4.setChecked(false);
        }

        if (Engine.isNetworkAvailable(getActivity())) {
            getClientToken();
        } else
            AppConstants.showMsgDialog("",
                    AppConstants.ERRORNETWORKCONNECTION, getActivity());
    }

    @Override
    public void notifyRefresh(String className) {
        refreshView();
    }

    /*
    get the client token
     */
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
                                cardNumber = getClientTokenResponse.getPaymentMethodId();

                                textCurrentCreditCard.setText("USE CARD ENDING IN " + cardNumber.substring(cardNumber.length() - 4));
                                btnCurrentCreditCard.setTag(cardNumber);
                                btnCurrentCreditCard.setVisibility(View.VISIBLE);
                            } else {
                                btnCurrentCreditCard.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                            btnCurrentCreditCard.setVisibility(View.GONE);
                            showMsgDialog("", AppConstants.BLANKMESSAGEREPLACEMENT,
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


    private static void showMsgDialog(String title, final String message,
                                      Context context) {
        try {
            if (alertDialogBuilder == null) {
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        alertDialogBuilder = null;
                                        dialog.cancel();


                                    }
                                });
                AlertDialog alert = alertDialogBuilder.create();
                if (title.equals("")) {
                    alert.setTitle(AppConstants.CONSTANTTITLEMESSAGE);
                    alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
                } else {
                    alert.setTitle(title);
                    alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
                }
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void SetTextWatcherForAmountEditView(final EditText tmpEditText) {
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (rValue1.isChecked())
                        rValue1.setChecked(false);
                    if (rValue2.isChecked())
                        rValue2.setChecked(false);
                    if (rValue3.isChecked())
                        rValue3.setChecked(false);
                    if (rValue4.isChecked())
                        rValue4.setChecked(false);
                }

                defaultValue = 0;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
				/*if (s.length() > 0){
					// no text entered. Center the hint text.
					tmpEditText.setGravity(Gravity.CENTER_VERTICAL);
				}else{
					// position the text type in the left top corner
					tmpEditText.setGravity(Gravity.LEFT | Gravity.TOP);
				}*/

                Utility.editTextHideShowHint(s.length(), tmpEditText, getActivity(), view);

                if (filterLongEnough(eOtherAmount)) {
                    try {
                        btnCurrentCreditCard.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    btnCurrentCreditCard.setEnabled(false);
                }
            }

            private boolean filterLongEnough(EditText tmpEditText) {
                return tmpEditText.getText().toString().trim().length() > 0;
            }

        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }

    private void closeSoftKeyBoard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    eOtherAmount.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }
}

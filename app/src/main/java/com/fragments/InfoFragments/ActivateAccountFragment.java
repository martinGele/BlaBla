package com.fragments.InfoFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
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
import com.postmodels.GetActivationLinkModel;
import com.responsemodels.ActivateAccountResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.util.Utility;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;


public class ActivateAccountFragment extends Fragment implements View.OnClickListener {

    View view;
    TextView headerTxt, emailEditHint;
    EditText EditEmail;
    Button submitBtn;
    static AlertDialog.Builder alertDialogBuilder;
    private Tracker mTracker;
    private String SCREEN_NAME = "Activate Account Fragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activate_account, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {
        setTracker();

        headerTxt = view.findViewById(R.id.text_header);
        EditEmail = view.findViewById(R.id.login_edit_email);
        emailEditHint = view.findViewById(R.id.login_edit_email_hint);
        submitBtn = view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);


        Fonts.fontFinkHeavyRegularTextView(headerTxt,
                (int) (getActivity().getResources().getDimension(R.dimen.header_font_size) /
                        getActivity().getResources().getDisplayMetrics().density),
                AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(EditEmail, 18,
                AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(emailEditHint, 18,
                AppConstants.COLORBG, getActivity().getAssets());

        Fonts.fontRobotoCodensedBoldTextView(submitBtn, 18,
                AppConstants.COLORWHITE, getActivity().getAssets());
        submitBtn.setEnabled(false);
        SetTextWatcherForAmountEditView(EditEmail);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.submitBtn:


                if (!isValidEmail(EditEmail)) {
                    Dialogs.showMsgDialog("", "Please enter a valid email address.", getActivity());
                } else {
                    hideSoftKeyboard();
                    String mEmailID = EditEmail.getText().toString();
                    GetActivationLinkModel getActivationLinkModel = new GetActivationLinkModel(getResources().getString(R.string.APPKEY), mEmailID);
                    String model = Gson.getGson().toJson(getActivationLinkModel);
                    submitEmailToServer(model);
                }

                break;
        }

    }

    private void submitEmailToServer(String json) {
        RotiHomeActivity.getProgressDialog().show();

        String submitUrl = getResources().getString(R.string.BASE_URL) + "/users/request_account_activation_link";

        JsonObjectRequest request_json = null;
        try {
            request_json = new JsonObjectRequest(Request.Method.POST, submitUrl, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("ActivationRes", response.toString());
                            RotiHomeActivity.getProgressDialog().dismiss();

                            ActivateAccountResponse activateAccountResponse = Gson.getGson().fromJson(response.toString(), ActivateAccountResponse.class);

                            if (activateAccountResponse.getStatus().equals("Success")) {

                                if (alertDialogBuilder == null) {
                                    alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setMessage(activateAccountResponse.getMessage()).setCancelable(false).setPositiveButton("Ok",
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
                            if (activateAccountResponse.getStatus().equals("Error")) {


                                Dialogs.showMsgDialog("", activateAccountResponse.getMessage(), (getActivity()));
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

    private void SetTextWatcherForAmountEditView(final EditText tmpEditText) {
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (tmpEditText.getId() == R.id.login_edit_email
                        || tmpEditText.getId() == R.id.login_edit_password) {
                    String result = s.toString().replaceAll(" ", "");
                    if (!s.toString().equals(result)) {
                        tmpEditText.setText(result);
                        tmpEditText.setSelection(result.length());
                        // alert the user
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                /*
                 * if (s.length() > 0){ // no text entered. Center the hint
                 * text. amountEditText.setGravity(Gravity.CENTER_VERTICAL);
                 * }else{ // position the text type in the left top corner
                 * amountEditText.setGravity(Gravity.LEFT | Gravity.TOP); }
                 */
                Utility.editTextHideShowHint(s.length(), tmpEditText, getActivity(), view);


                if (filterLongEnough(EditEmail)) {
                    try {
                        // loginBtn.setBackgroundResource(R.drawable.log_in_button);
                        submitBtn.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // loginBtn.setBackgroundResource(R.drawable.log_in_button_idle);
                    submitBtn.setEnabled(false);
                }
            }

            private boolean filterLongEnough(EditText amountEditText) {
                return amountEditText.getText().toString().trim().length() > 0;
            }

            private boolean filterLongEnoughPassword(EditText amountEditText) {
                return amountEditText.getText().toString().length() >= 6;
            }


        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }

    public boolean isValidEmail(EditText emailEditText) {
        return !TextUtils.isEmpty(emailEditText.getText())
                && android.util.Patterns.EMAIL_ADDRESS.matcher(
                emailEditText.getText()).matches();
    }

    public void hideSoftKeyboard() {
        try {

            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                    .getWindowToken(), 0);

        } catch (Exception e) {
        }
    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }

}

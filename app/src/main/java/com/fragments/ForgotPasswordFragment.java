package com.fragments;

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
import com.postmodels.ForgotPasswordModel;
import com.responsemodels.ForgotPasswordResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.util.Utility;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import static com.util.Dialogs.trimMessage;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {

    private Button resetpasswordBtn;
    EditText emailEdit;
    TextView emailEditHint, headerTxt;
    View view;

    private Tracker mTracker;
    private String SCREEN_NAME = "Forgot Password Fragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.info_forgetpassword, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {

        setTracker();
        /*

        get the views
         */

        headerTxt = view.findViewById(R.id.text_header);
        emailEdit = view.findViewById(R.id.forgetpassword_edit_email);
        emailEditHint = view.findViewById(R.id.forgetpassword_edit_email_hint);
        resetpasswordBtn = view.findViewById(R.id.forgetpassword_image_resetpassword);
       /*
       setup click listiners to the views
        */
        resetpasswordBtn.setOnClickListener(this);


        /*
        setup the views text colors
         */
        Fonts.fontFinkHeavyRegularTextView(headerTxt,
                (int) (getActivity().getResources().getDimension(R.dimen.header_font_size) /
                        getActivity().getResources().getDisplayMetrics().density),
                AppConstants.COLORWHITE, getActivity().getAssets());

        Fonts.fontRobotoCodensedBoldTextView(emailEdit, 16,
                AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(emailEditHint, 16,
                AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(resetpasswordBtn, 18,
                AppConstants.COLORWHITE, getActivity().getAssets());


        SetTextWatcherForAmountEditView(emailEdit);
        resetpasswordBtn.setEnabled(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.forgetpassword_image_resetpassword:
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String email = emailEdit.getText().toString();
                if (Engine.isNetworkAvailable(getActivity())) {
                    String[] param = new String[]{email};


                    ForgotPasswordModel forgotPasswordModel = new ForgotPasswordModel(email, AppConstants.REGISTERTYPE, getResources().getString(R.string.APPKEY));
                    String model = Gson.getGson().toJson(forgotPasswordModel);
                    Log.d("MOdel", model);

                    submitForgetPasswordToServer(model);
                } else {
                    AppConstants.showMsgDialog("",
                            AppConstants.ERRORNETWORKCONNECTION, getActivity());

                    break;


                }

        }
    }

    private void submitForgetPasswordToServer(String json) {

        RotiHomeActivity.getProgressDialog().show();

        String url = getResources().getString(R.string.BASE_URL) + "/user/forgot_password";

        JsonObjectRequest request_json = null;
        try {
            request_json = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            RotiHomeActivity.getProgressDialog().dismiss();

                            ForgotPasswordResponse forgotPasswordResponse = Gson.getGson().fromJson(response.toString(), ForgotPasswordResponse.class);


                            if (forgotPasswordResponse.getStatus()) {

                                String notice = forgotPasswordResponse.getNotice();

                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                alertDialog.setMessage(notice);
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                if (Engine.getInstance().getSetNextPage().length() > 0) {
                                                    ((OpenFragmetListener) getActivity()).startFragment(Engine.getInstance().getSetNextPage());
                                                    Engine.getInstance().setSetNextPage("");
                                                }
                                            }
                                        });
                                alertDialog.show();


                            } else if (!forgotPasswordResponse.getStatus()) {

                                String notice = forgotPasswordResponse.getNotice();

                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                alertDialog.setMessage(notice);
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                            }
                                        });
                                alertDialog.show();


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
                            default:

                                Dialogs.showMsgDialog("", String.valueOf(AppConstants.BLANKMESSAGEREPLACEMENT), getActivity());


                        }
                    }

                }
            });

            int socketTimeout = 15000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request_json.setRetryPolicy(policy);


        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        /*
         add the request object to the queue to be executed
         */
        VolleyControllerAndTracker.getInstance().addToRequestQueue(request_json);


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
				/*if (s.length() > 0){
					// no text entered. Center the hint text.
					tmpEditText.setGravity(Gravity.CENTER_VERTICAL);
				}else{
					// position the text type in the left top corner
					tmpEditText.setGravity(Gravity.LEFT | Gravity.TOP);
				}*/

                Utility.editTextHideShowHint(s.length(), tmpEditText, getActivity(), view);

                if (filterLongEnough(tmpEditText)) {
                    try {
//						resetpasswordBtn
//								.setBackgroundResource(R.drawable.submit_button);
                        resetpasswordBtn.setEnabled(true);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                } else {
//					resetpasswordBtn
//							.setBackgroundResource(R.drawable.submit_button_disable);
                    resetpasswordBtn.setEnabled(false);
                }
            }

            private boolean filterLongEnough(EditText tmpEditText) {
                return tmpEditText.getText().toString().trim().length() > 0;
            }

            public boolean isValidEmail(EditText emailEditText) {
                return !TextUtils.isEmpty(emailEditText.getText())
                        && android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText()).matches();
            }

        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }
}
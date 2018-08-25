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
import com.postmodels.ChangePasswordModel;
import com.responsemodels.SignInResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.util.Utility;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;


public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    View view;
    private EditText repwdEdit, oldpwdEdit, newpwdEdit;
    private TextView repwdEditHint, oldpwdEditHint, newpwdEditHint;
    private Button submitBtn;
    private Tracker mTracker;
    private String SCREEN_NAME = "Change Password Fragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_changepassword, container, false);
        inintView(view);
        return view;
    }

    private void inintView(View view) {

        setTracker();
        /*
        get the views
         */

        oldpwdEdit = view.findViewById(R.id.changepassword_edit_oldpassword);
        newpwdEdit = view.findViewById(R.id.changepassword_edit_newpassword);
        repwdEdit = view.findViewById(R.id.changepassword_edit_repeatpassword);
        oldpwdEditHint = view.findViewById(R.id.changepassword_edit_oldpassword_hint);
        newpwdEditHint = view.findViewById(R.id.changepassword_edit_newpassword_hint);
        repwdEditHint = view.findViewById(R.id.changepassword_edit_repeatpassword_hint);
        submitBtn = view.findViewById(R.id.changepassword_image_submit);

        /*
        setup all the fonts and colors
         */

        Fonts.fontRobotoCodensedBoldTextView(oldpwdEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(newpwdEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(repwdEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(oldpwdEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(newpwdEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(repwdEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(submitBtn, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        submitBtn.setEnabled(false);
        submitBtn.setOnClickListener(this);
        SetTextWatcherForAmountEditView(oldpwdEdit);
        SetTextWatcherForAmountEditView(newpwdEdit);
        SetTextWatcherForAmountEditView(repwdEdit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.changepassword_image_submit:

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(oldpwdEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }

                // if (!filterLongEnoughPassword())

                String oldpwd = oldpwdEdit.getText().toString();
                String newpwd = newpwdEdit.getText().toString();
                String repwd = repwdEdit.getText().toString();

                if (Engine.isNetworkAvailable(getActivity())) {
                    if (RotiHomeActivity.checkIfLogin()) {
                        String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");

                        ChangePasswordModel model = new ChangePasswordModel(getResources().getString(R.string.APPKEY), authToken, oldpwd, newpwd, repwd);
                        String postModel = Gson.getGson().toJson(model);

                        submitChangePaswordToServer(postModel);
                    }
                } else {
                    AppConstants.showMsgDialog("", AppConstants.ERRORNETWORKCONNECTION, getActivity());
                }
                break;
        }

    }

    private void submitChangePaswordToServer(String json) {

        Log.d("Json", json);

        String url = getResources().getString(R.string.BASE_URL) + "/user/update_password";
        RotiHomeActivity.getProgressDialog().show();
        JsonObjectRequest request_json = null;
        try {
            request_json = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            RotiHomeActivity.getProgressDialog().dismiss();
                            Log.d("Change pass", response.toString());


                            SignInResponse signInResponse = Gson.getGson().fromJson(response.toString(), SignInResponse.class);


                            if (signInResponse.getStatus()) {
                                AlertDialog.Builder alertDialogBuilder;

                                alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                alertDialogBuilder.setMessage(signInResponse.getNotice()).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                            } else if (signInResponse.getStatus() == false) {
                                AlertDialog.Builder alertDialogBuilder;

                                alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                alertDialogBuilder.setMessage(signInResponse.getNotice()).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();


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
                if (tmpEditText.getId() == R.id.changepassword_edit_oldpassword
                        || tmpEditText.getId() == R.id.changepassword_edit_newpassword
                        || tmpEditText.getId() == R.id.changepassword_edit_repeatpassword) {
                    String result = s.toString().replaceAll(" ", "");
                    if (!s.toString().equals(result)) {
                        tmpEditText.setText(result);
                        tmpEditText.setSelection(result.length());
                        // alert the user
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*
                 * if (s.length() > 0){ // no text entered. Center the hint text.
                 * tmpEditText.setGravity(Gravity.CENTER_VERTICAL); }else{ // position the text
                 * type in the left top corner tmpEditText.setGravity(Gravity.LEFT |
                 * Gravity.TOP); }
                 */

                Utility.editTextHideShowHint(s.length(), tmpEditText, getActivity(), view);

                if (filterLongEnough(oldpwdEdit) && filterLongEnough(newpwdEdit) && filterLongEnough(repwdEdit)) {
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
        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }

    private boolean filterLongEnoughPassword(EditText amountEditText) {
        return amountEditText.getText().toString().length() >= 6;
    }

    private boolean isNewPasswordAndConfirmSame(EditText newPassword, EditText confirmPassword) {
        return newPassword.getText().toString().equals(confirmPassword.getText().toString());
    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }
}

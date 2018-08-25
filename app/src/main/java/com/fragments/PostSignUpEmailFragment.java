package com.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.Tracker;
import com.postmodels.FbSignInModel;
import com.postmodels.GetActivationLinkModel;
import com.postmodels.SignInModel;
import com.pushnotifications.PushNotificationHelper;
import com.responsemodels.ActivateAccountResponse;
import com.responsemodels.SignInResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import static com.util.Dialogs.trimMessage;


@SuppressLint("ValidFragment")
public class PostSignUpEmailFragment extends Fragment implements View.OnClickListener {

    private String androidOS = Build.VERSION.RELEASE;
    private String model = Build.MODEL;
    private String manufacturer = Build.MANUFACTURER;
    static AlertDialog.Builder alertDialogBuilder;
    String regId = "";
    View view;
    TextView first_text, text_email, text_verify, notification_for_email, or_text, text_header, resend_verification, please_check_your_spam;
    Button sign_up_with_another_account, sign_in_with_another_account, sign_up_with_facebook;

    String email;
    String password;
    private Tracker mTracker;
    private String SCREEN_NAME = "Post Sign Up Fragment";

    @SuppressLint("ValidFragment")
    public PostSignUpEmailFragment(String email, String password) {
        this.email = email;
        this.email = email;
        this.password = password;
    }

    @SuppressLint("ValidFragment")
    public PostSignUpEmailFragment(String email) {
        this.email = email;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();

    }

    @Override
    public void onResume() {
        super.onResume();

        boolean fromFbSignUp = RotiHomeActivity.getPreference().getBoolean(AppConstants.FROMFBSIGNUP, false);


        if (fromFbSignUp == false && password != null) {
            signIn();
        } else {

            String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

            final PushNotificationHelper helper = new PushNotificationHelper(getActivity());
            String regId = helper.getRegID();


            FbSignInModel fbSignInModel = new FbSignInModel(email, android_id, "",
                    AppConstants.REGISTERTYPEFB, getResources().getString(R.string.APPKEY), AppConstants.DEVICE_TYPE,
                    Engine.getDeviceID(getActivity()), regId, manufacturer
                    + " " + model, androidOS);
            String modelPostRequest = Gson.getGson().toJson(fbSignInModel);

            signInFb(modelPostRequest);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.post_sign_up_verification, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {
        setTracker();

        first_text = view.findViewById(R.id.first_text);
        text_email = view.findViewById(R.id.text_email);


        text_verify = view.findViewById(R.id.text_verify);
        notification_for_email = view.findViewById(R.id.notification_for_email);
        or_text = view.findViewById(R.id.or_text);
        text_header = view.findViewById(R.id.text_header);
        please_check_your_spam = view.findViewById(R.id.please_check_your_spam);
        resend_verification = view.findViewById(R.id.resend_verification);
        resend_verification.setPaintFlags(resend_verification.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        resend_verification.setOnClickListener(this);


        sign_up_with_another_account = view.findViewById(R.id.sign_up_with_another_account);
        sign_in_with_another_account = view.findViewById(R.id.sign_in_with_another_account);
        sign_up_with_facebook = view.findViewById(R.id.sign_up_with_facebook);

        sign_in_with_another_account.setOnClickListener(this);
        sign_up_with_another_account.setOnClickListener(this);
        sign_up_with_facebook.setOnClickListener(this);
        notification_for_email.setOnClickListener(this);

        Fonts.fontFinkHeavyRegularTextView(text_header,
                (int) (getActivity().getResources().getDimension(R.dimen.header_font_size) /
                        getActivity().getResources().getDisplayMetrics().density),
                AppConstants.COLORWHITE, getActivity().getAssets());

        Fonts.fontRobotoCodensedBoldTextView(notification_for_email, 14, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(please_check_your_spam, 14, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(resend_verification, 14, AppConstants.COLORBLUE, getActivity().getAssets());

        Fonts.fontFinkHeavyRegularTextView(or_text, 17, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(first_text, 17, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(text_verify, 17, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(text_email, 17, AppConstants.COLORWHITE, getActivity().getAssets());


        text_email.setAllCaps(true);
        text_email.setText(email);


        Fonts.fontRobotoCodensedBoldTextView(sign_in_with_another_account, 17, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(sign_up_with_another_account, 17, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(sign_up_with_facebook, 17, AppConstants.COLORWHITE, getActivity().getAssets());


    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        SharedPreferences.Editor prefsEditor = RotiHomeActivity.getPreferenceEditor();

        switch (v.getId()) {

            case R.id.sign_up_with_another_account:
                prefsEditor.putString(AppConstants.PREFLOGINID, "");
                prefsEditor.putString(AppConstants.PREFLOGINPAS, "");
                prefsEditor.putBoolean(AppConstants.FROMFBSIGNUP, false);


                prefsEditor.commit();

                clearBackStack();
                transaction.replace(R.id.home_linear_container, new SignUpFragment(), AppConstants.TAG_SIGN);
                transaction.addToBackStack(AppConstants.TAG_SIGN);
                transaction.commit();

                break;
            case R.id.sign_in_with_another_account:
                prefsEditor.putString(AppConstants.PREFLOGINID, "");
                prefsEditor.putString(AppConstants.PREFLOGINPAS, "");
                prefsEditor.putBoolean(AppConstants.FROMFBSIGNUP, false);

                prefsEditor.commit();
                clearBackStack();
                transaction.replace(R.id.home_linear_container, new SignInFragment(), AppConstants.TAG_SIGN);
                transaction.addToBackStack(AppConstants.TAG_SIGN);
                transaction.commit();

                break;

            case R.id.sign_up_with_facebook:
                prefsEditor.putString(AppConstants.PREFLOGINID, "");
                prefsEditor.putString(AppConstants.PREFLOGINPAS, "");

                prefsEditor.commit();

                clearBackStack();
                transaction.replace(R.id.home_linear_container, new FbSignUpFragment(), AppConstants.TAG_SIGN);
                transaction.addToBackStack(AppConstants.TAG_SIGN);
                transaction.commit();

                break;

            case R.id.resend_verification:

                GetActivationLinkModel getActivationLinkModel = new GetActivationLinkModel(getResources().getString(R.string.APPKEY), email);
                String model = Gson.getGson().toJson(getActivationLinkModel);
                sendVerificationLinkToMail(model);

                break;
        }

    }
    /*
    volley call to verify the email
     */

    private void sendVerificationLinkToMail(String json) {
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

                                Dialogs.showMsgDialog("", activateAccountResponse.getMessage(), (getActivity()));


                            }
                            if (activateAccountResponse.getStatus().equals("Error")) {


//                                Dialogs.showMsgDialog("", activateAccountResponse.getMessage(), (getActivity()));
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


    /*
    volley call to sign in
     */
    public void signIn() {

        if (Engine.isNetworkAvailable(getActivity())) {

            String android_id = Settings.Secure.getString(
                    getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            final PushNotificationHelper helper = new PushNotificationHelper(getActivity());
            String regId = helper.getRegistrationId(getActivity());

            SignInModel signInModel = new SignInModel(email, password, android_id, AppConstants.DEVICE_TYPE,
                    AppConstants.REGISTERTYPE, getResources().getString(R.string.APPKEY), regId, Engine
                    .getDeviceID(getActivity()), manufacturer + " " + model, androidOS);


            String modelPostRequest = Gson.getGson().toJson(signInModel);

            String url = getResources().getString(R.string.BASE_URL) + "/user/login";
            RotiHomeActivity.getProgressDialog().show();
            JsonObjectRequest json = null;
            try {
                json = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(modelPostRequest),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("REsponse", response.toString());
                                RotiHomeActivity.getProgressDialog().dismiss();

                                ActivateAccountResponse activateAccountResponse = Gson.getGson().fromJson(response.toString(), ActivateAccountResponse.class);


                                SignInResponse signInResponse = Gson.getGson().fromJson(response.toString(), SignInResponse.class);

                                if (!signInResponse.getStatus()) {

//                                    Dialogs.showMsgDialog("", signInResponse.getNotice(), (getActivity()));

                                }

                                if (signInResponse.getStatus()) {
                                    String auth_token = signInResponse.getAuthToken();
                                    Log.d("auth", auth_token);

                                    SharedPreferences.Editor prefsEditor = RotiHomeActivity.getPreferenceEditor();
                                    prefsEditor.putString(AppConstants.PREFAUTH_TOKEN, auth_token);
//                                    Dialogs.showMsgDialog("", signInResponse.getNotice(), (getActivity()));

                                    Log.d("Email", email);

                                    prefsEditor.putString(AppConstants.PREFLOGINID,
                                            email);
                                    prefsEditor.putBoolean(
                                            AppConstants.PREFLOGOUTBUTTONTAG, false);
                                    prefsEditor.commit();


                                    clearBackStack();
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                                    transaction.replace(R.id.home_linear_container, new AccountSuccessfullyVerified(), AppConstants.TAG_SIGN);
                                    transaction.addToBackStack(AppConstants.TAG_SIGN);
                                    transaction.commit();



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
                json.setRetryPolicy(policy);


            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        /*
         add the request object to the queue to be executed
         */
            VolleyControllerAndTracker.getInstance().addToRequestQueue(json);


        }
    }

    /*
    sign in with Facebook
     */
    private void signInFb(String modelPostRequest) {

        String url = getResources().getString(R.string.BASE_URL) + "/user/login";

        Log.d("Post", modelPostRequest);
        RotiHomeActivity.getProgressDialog().show();
        JsonObjectRequest json = null;
        try {
            json = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(modelPostRequest),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("REsponse", response.toString());
                            RotiHomeActivity.getProgressDialog().dismiss();

                            SignInResponse signInResponse = Gson.getGson().fromJson(response.toString(), SignInResponse.class);


                            if (!signInResponse.getStatus()) {

//                                Dialogs.showMsgDialog("", signInResponse.getNotice(), (getActivity()));

                            }

                            if (signInResponse.getStatus()) {
                                String auth_token = signInResponse.getAuthToken();
                                Log.d("auth", auth_token);

                                SharedPreferences.Editor prefsEditor = RotiHomeActivity.getPreferenceEditor();
                                prefsEditor.putString(AppConstants.PREFAUTH_TOKEN, auth_token);
//                                    Dialogs.showMsgDialog("", signInResponse.getNotice(), (getActivity()));

                                Log.d("Email", email);

                                prefsEditor.putString(AppConstants.PREFLOGINID,
                                        email);
                                prefsEditor.putBoolean(
                                        AppConstants.PREFLOGOUTBUTTONTAG, false);
                                prefsEditor.commit();


                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                                transaction.replace(R.id.home_linear_container, new AccountSuccessfullyVerified(), AppConstants.TAG_SIGN);
                                transaction.addToBackStack(AppConstants.TAG_SIGN);
                                transaction.commit();
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
            json.setRetryPolicy(policy);


        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        /*
         add the request object to the queue to be executed
         */
        VolleyControllerAndTracker.getInstance().addToRequestQueue(json);


    }

    public void clearBackStack() {
        android.support.v4.app.FragmentManager fragmentManag = getFragmentManager();
        if (fragmentManag.getBackStackEntryCount() > 0) {
            fragmentManag.popBackStack(AppConstants.TAG_SIGN, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);


        }

    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }
}

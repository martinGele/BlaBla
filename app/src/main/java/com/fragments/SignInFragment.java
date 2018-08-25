package com.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.OpenFragmetListener;
import com.postmodels.FbSignInModel;
import com.postmodels.SignInModel;
import com.pushnotifications.PushNotificationHelper;
import com.responsemodels.SignInResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.util.Utility;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.util.Dialogs.trimMessage;

public class SignInFragment extends Fragment implements View.OnClickListener {

    public static EditText emailEdit, pwdEdit;
    public static TextView pwdEditHint, emailEditHint, pageTitle, forgetPasswordText;
    Button loginFbButton, loginBtn;
    View view;
    String regId = "";
    public String mEmailID = "";

    private String androidOS = Build.VERSION.RELEASE;
    private String model = Build.MODEL;
    private String manufacturer = Build.MANUFACTURER;
    static AlertDialog.Builder alertDialogBuilder;


    private Tracker mTracker;
    private String SCREEN_NAME = "Sign In Fragment";

    /*
    callback manager needed to pass the result of the facebook user get
     */
    private CallbackManager callbackManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_login, container, false);
        initView(view);
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                }, 1);
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_PHONE_STATE,
                }, 2);

        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getActivity(), "Permission denied to read access fine location", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private void initView(View view) {
        setTracker();
        /*
        get the callback manager, initiate by calling the create method
         */

        callbackManager = CallbackManager.Factory.create();


          /*
         get all the views
        */
        emailEdit = view.findViewById(R.id.login_edit_email);
        pwdEdit = view.findViewById(R.id.login_edit_password);
        emailEditHint = view.findViewById(R.id.login_edit_email_hint);
        pwdEditHint = view.findViewById(R.id.login_edit_password_hint);
        loginBtn = view.findViewById(R.id.loginBtn);
        loginFbButton = view.findViewById(R.id.login_fb_button);
        pageTitle = view.findViewById(R.id.pageTitleInfoLogin);
        forgetPasswordText = view.findViewById(R.id.login_text_forgetpassword);

        /*
        set all the text views their font and color
         */
        Fonts.fontFinkHeavyRegularTextView(pageTitle, 18,
                AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(emailEdit, 18,
                AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(pwdEdit, 18,
                AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(emailEditHint, 18,
                AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(pwdEditHint, 18,
                AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(forgetPasswordText, 18,
                AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(loginBtn, 18,
                AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(loginFbButton, 18,
                AppConstants.COLORWHITE, getActivity().getAssets());
        /*
        get the click events
         */
        loginBtn.setOnClickListener(this);
        loginFbButton.setOnClickListener(this);
        forgetPasswordText.setOnClickListener(this);


        SetTextWatcherForAmountEditView(emailEdit);
        SetTextWatcherForAmountEditView(pwdEdit);
        loginBtn.setEnabled(false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {

            case R.id.loginBtn:

                final PushNotificationHelper helper = new PushNotificationHelper(getActivity());
                String regId = helper.getRegistrationId(getActivity());

                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            emailEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }
                String mEmailID = emailEdit.getText().toString();
                String mPassword = pwdEdit.getText().toString();
                String android_id = Settings.Secure.getString(
                        getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                if (Engine.isNetworkAvailable(getActivity())) {


                    SignInModel signInModel = new SignInModel(mEmailID, mPassword, android_id, AppConstants.DEVICE_TYPE,
                            AppConstants.REGISTERTYPE, getResources().getString(R.string.APPKEY), regId, Engine
                            .getDeviceID(getActivity()), manufacturer + " " + model, androidOS);


                    String modelPostRequest = Gson.getGson().toJson(signInModel);
                    Log.d("signInModel", modelPostRequest);


                    submitLoginDetailsToServer(modelPostRequest, mEmailID);
                } else {
                    AppConstants.showMsgDialog("",
                            AppConstants.ERRORNETWORKCONNECTION, getActivity());
                }


                break;
            case R.id.login_fb_button:

                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            emailEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }
                String mrefer = "";
                String marketingOptin = "true";
                android_id = Settings.Secure.getString(
                        getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                SharedPreferences preferences = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("android_id", android_id);
                editor.putString("marketing_optin", marketingOptin);
                editor.commit();

                if (Engine.isNetworkAvailable(getActivity())) {
                    List<String> permissions = new ArrayList<String>();
                    permissions.add("email");
                    openActiveSession(getActivity(), permissions);


                }

                break;

            case R.id.login_text_forgetpassword:
                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            emailEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }
                Engine.getInstance().setSetNextPage(AppConstants.showSignInFragment);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.home_linear_container, new ForgotPasswordFragment(), AppConstants.ITEM_HOME);
                transaction.addToBackStack(AppConstants.ITEM_HOME);
                transaction.commit();


                break;
        }


    }

    private void openActiveSession(Activity activity, List<String> permissions) {
        AppEventsLogger logger = AppEventsLogger.newLogger(activity);
        logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);
        LoginManager.getInstance().logInWithReadPermissions(
                getActivity(),
                permissions);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                RotiHomeActivity.getProgressDialog().show();
                if (AccessToken.getCurrentAccessToken() != null) {
                    Bundle parameters = new Bundle();

                    parameters.putString("fields", "id, first_name, last_name ,name, email, gender, birthday");

                    GraphRequest gr = GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject json, GraphResponse response) {

                                    if (response.getError() != null) {
                                        System.out.println("ERROR " + response.getError().toString());
                                    } else {
                                        if (json != null) {
                                            Bundle bFacebookData = getFacebookData(json);
                                            final String email = bFacebookData.getString("email");
                                            final String firstName = bFacebookData.getString("first_name");

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
                                }

                            });
                    gr.setParameters(parameters);
                    gr.executeAsync();
                }
            }

            @Override
            public void onCancel() {
                RotiHomeActivity.getProgressDialog().dismiss();
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException error) {
                LoginManager.getInstance().logOut();
                RotiHomeActivity.getProgressDialog().dismiss();
            }
        });
    }


    private void submitLoginDetailsToServer(String json, final String mEmailID) {
        RotiHomeActivity.getProgressDialog().show();


        String submitUrl = getResources().getString(R.string.BASE_URL) + "/user/login";
        Log.d("URLS", submitUrl);
        Log.d("JSON", json);


        JsonObjectRequest request_json = null;
        try {
            request_json = new JsonObjectRequest(Request.Method.POST, submitUrl, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Sign in response", response.toString());
                            RotiHomeActivity.getProgressDialog().dismiss();

                            SignInResponse signInResponse = Gson.getGson().fromJson(response.toString(), SignInResponse.class);

                            if (!signInResponse.getStatus()) {

                                Dialogs.showMsgDialog("", signInResponse.getNotice(), getActivity());
                            }


                            if (!signInResponse.getStatus() && signInResponse.getNotice().equalsIgnoreCase("Account not validated.Please tap on the activation link sent to your email  and login from the app to continue using the app. Please reach out to customer support if you have received your activation link.")) {

                                String email = emailEdit.getText().toString();
                                String password = pwdEdit.getText().toString();
                                SharedPreferences.Editor prefsEditor = RotiHomeActivity.getPreferenceEditor();

                                prefsEditor.putString(AppConstants.PREFLOGINID, emailEdit.getText().toString());
                                prefsEditor.putString(AppConstants.PREFLOGINPAS, pwdEdit.getText().toString());
                                prefsEditor.putBoolean(AppConstants.PREFLOGOUTBUTTONTAG, false);
                                prefsEditor.putBoolean(AppConstants.FROMFBSIGNUP, false);

                                prefsEditor.commit();


                                clearBackStack();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                                transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                                transaction.addToBackStack(AppConstants.TAG_SIGN);
                                transaction.commit();


                            }


                            if (signInResponse.getStatus()) {
                                String auth_token = signInResponse.getAuthToken();
                                Log.d("auth", auth_token);

                                SharedPreferences.Editor prefsEditor = RotiHomeActivity.getPreferenceEditor();
                                prefsEditor.putString(AppConstants.PREFAUTH_TOKEN, auth_token);

                                Log.d("Email", mEmailID);

                                prefsEditor.putString(AppConstants.PREFLOGINID,
                                        mEmailID);
                                prefsEditor.putBoolean(
                                        AppConstants.PREFLOGOUTBUTTONTAG, false);
                                prefsEditor.commit();


                                /*
                                 * prefsEditor.putBoolean(
                                 * AppConstants.IS_IT_GIFT_FIRST_TIME, false);
                                 */

                                if (alertDialogBuilder == null) {
                                    alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setMessage(signInResponse.getNotice()).setCancelable(false).setPositiveButton("Ok",
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


    private void signInFb(String modelPostRequest) {

        String url = getResources().getString(R.string.BASE_URL_HTTPS) + "/user/login";

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
                                Dialogs.showMsgDialog("", signInResponse.getNotice(), getActivity());


                            }

                            if (signInResponse.getStatus()) {
                                String auth_token = signInResponse.getAuthToken();
                                Log.d("auth", auth_token);

                                SharedPreferences.Editor prefsEditor = RotiHomeActivity.getPreferenceEditor();
                                prefsEditor.putString(AppConstants.PREFAUTH_TOKEN, auth_token);

                                Log.d("Email", mEmailID);


                                prefsEditor.putString(AppConstants.PREFLOGINID,
                                        mEmailID);
                                prefsEditor.putBoolean(AppConstants.FROMFBSIGNUP, true);

                                prefsEditor.putBoolean(
                                        AppConstants.PREFLOGOUTBUTTONTAG, false);
                                prefsEditor.commit();


                                try {
                                    AlertDialog.Builder alertDialogBuilder;
                                    alertDialogBuilder = new AlertDialog.Builder(getContext());
                                    alertDialogBuilder.setMessage(signInResponse.getNotice()).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();

                                            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                                            fm.popBackStack();
                                            if (Engine.getInstance().getSetNextPage().length() > 0) {
                                                ((OpenFragmetListener) getActivity()).startFragment(Engine.getInstance().getSetNextPage());
                                                Engine.getInstance().setSetNextPage("");
                                            }

                                        }
                                    });
                                    AlertDialog alert = alertDialogBuilder.create();
                                    alert.show();


                                } catch (Exception e) {
                                    AppConstants.parseInput(response.toString(), getActivity(), "");
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
                            default:

                                Dialogs.showMsgDialog("", String.valueOf(AppConstants.BLANKMESSAGEREPLACEMENT), getActivity());


                        }
                    }

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();


                    return params;
                }
            };

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


                if (filterLongEnough(emailEdit) && filterLongEnough(pwdEdit)) {
                    try {
                        // loginBtn.setBackgroundResource(R.drawable.log_in_button);
                        loginBtn.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // loginBtn.setBackgroundResource(R.drawable.log_in_button_idle);
                    loginBtn.setEnabled(false);
                }
            }

            private boolean filterLongEnough(EditText amountEditText) {
                return amountEditText.getText().toString().trim().length() > 0;
            }

            private boolean filterLongEnoughPassword(EditText amountEditText) {
                return amountEditText.getText().toString().length() >= 6;
            }

            public boolean isValidEmail(EditText emailEditText) {
                return !TextUtils.isEmpty(emailEditText.getText())
                        && android.util.Patterns.EMAIL_ADDRESS.matcher(
                        emailEditText.getText()).matches();
            }
        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }

    /*
    get the facebook data and pass it to the post parameter
     */

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            bundle.putString("idFacebook", id);

            if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
            } else {
                final String errorMessage = getString(R.string.reward_acess_email_);
                Dialogs.showMsgDialog("", errorMessage, getActivity());
            }

            return bundle;
        } catch (JSONException e) {
            e.printStackTrace();
            LoginManager.getInstance().logOut();
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

    public void clearBackStack() {
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            android.support.v4.app.FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);

            getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        }

    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }


}
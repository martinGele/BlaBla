package com.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.postmodels.FacebookSignUpModel;
import com.pushnotifications.PushNotificationHelper;
import com.responsemodels.SignUpResposne;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.GetLatLongFromGPS;
import com.util.Gson;
import com.util.Utility;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.util.Dialogs.trimMessage;

public class FbSignUpFragment extends Fragment implements View.OnClickListener {

    private SharedPreferences mPreference;
    private SharedPreferences.Editor prefsEditor;
    boolean isInfo = false;

    public boolean doNotFinishAllActivities = false;
    public String FB_ACCESS_SERVICE = "SIGNIN";

    private String androidOS = Build.VERSION.RELEASE;
    private String model = Build.MODEL;
    private String manufacturer = Build.MANUFACTURER;
    public double latitude = 0.0, longitude = 0.0;


    Button signupBtn;
    private CallbackManager callbackManager;

    DatePicker date_picker;
    RelativeLayout datepickerclick;
    Integer dobYear, dobMonth, dobDate;
    EditText firstNameEdit, referEdit;
    TextView firstNameEditHint, favMenuText, referEditHint, textDob, optinText, spinnerPlaceholder;
    String favMenuString = "";
    ImageView locSpinnerBtn;
    RelativeLayout spinnerContainer;
    CheckBox marketingOptionCB;

    Calendar myCalendar = Calendar.getInstance();
    private GetLatLongFromGPS getLatLongObj;
    private Tracker mTracker;
    private String SCREEN_NAME = "Facebook Sign Up Fragment";


    String[] items;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();
        getLatLongObj = GetLatLongFromGPS.getinstance(getActivity());
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                }, 1);
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_PHONE_STATE,
                }, 2);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.info_fb_signin, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        setTracker();
        /*
        create callback manager for facebook
         */

        /*
        get the views ids and cast them
         */

        callbackManager = CallbackManager.Factory.create();

        firstNameEdit = view.findViewById(R.id.loginoption_fb_edittext_firstname);
        referEdit = view.findViewById(R.id.loginoption_fb_edittext_refercode);
        firstNameEditHint = view.findViewById(R.id.loginoption_fb_edittext_firstname_hint);
        referEditHint = view.findViewById(R.id.loginoption_fb_edittext_refercode_hint);
        favMenuText = view.findViewById(R.id.signup_fav_menu_text);
        spinnerPlaceholder = view.findViewById(R.id.signupspinnerPlaceholder);
        locSpinnerBtn = view.findViewById(R.id.locSpinnerBtn);
        spinnerContainer = view.findViewById(R.id.spinnerContainer);
        signupBtn = view.findViewById(R.id.loginoption_fb_image_facebook);
        datepickerclick = view.findViewById(R.id.layoutdob);
        textDob = view.findViewById(R.id.textviewdob);
        optinText = view.findViewById(R.id.optinText);
        marketingOptionCB = view.findViewById(R.id.marketingOptionCB);


        signupBtn.setEnabled(false);


        items = new String[16];
        items[0] = "Original Pretzel";
        items[1] = "Sinful Cinnamon Pretzel";
        items[2] = "Almond Crunch Pretzel";
        items[3] = "Cheese Meltdown Pretzel";
        items[4] = "Pepperoni Twist Pretzel";
        items[5] = "Jalaroni Pretzel";
        items[6] = "Grateful Garlic Pretzel";
        items[7] = "Jalapeno Cheese Melt Pretzel";
        items[8] = "Original Wetzel Bitz";
        items[9] = "Sinful Cinnamon Bitz";
        items[10] = "Almond Crunch Bitz";
        items[11] = "Pizza Bitz";
        items[12] = "Wetzel Dog";
        items[13] = "Cheese Wetzel Dog";
        items[14] = "Jalapeno Cheese Dog";
        items[15] = "Dog Bites";
        //items[16] = "FAV. MENU ITEM";


        /*
        setup click listiners on the views
         */
        firstNameEdit.setOnClickListener(this);
        referEdit.setOnClickListener(this);
        firstNameEditHint.setOnClickListener(this);
        referEditHint.setOnClickListener(this);
        favMenuText.setOnClickListener(this);
        spinnerContainer.setOnClickListener(this);
        locSpinnerBtn.setOnClickListener(this);
        spinnerPlaceholder.setOnClickListener(this);
        datepickerclick.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
        /*
        setup font and text size to the views
         */

        Fonts.fontRobotoCodensedBoldTextView(firstNameEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(firstNameEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(spinnerPlaceholder, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(referEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(referEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(textDob, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(favMenuText, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(optinText, 14, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(signupBtn, 18, AppConstants.COLORWHITE, getActivity().getAssets());


        SetTextWatcherForAmountEditView(firstNameEdit, view);
        SetTextWatcherForAmountEditView(referEdit, view);

        /*
        initialise  the integers
         */
        dobYear = 0;
        dobMonth = 0;
        dobDate = 0;

        /*
        setup the date picker dialog for the birthday
         */


    }

    @Override
    public void onClick(View v) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }

        };
        switch (v.getId()) {

            case R.id.layoutdob:
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();

                break;
            case R.id.loginoption_fb_image_facebook:

                if (Engine.isNetworkAvailable(getActivity())) {
                    if (getActivity() != null) {
                        List<String> permissions = new ArrayList<String>();
                        List<String> permissionNeeds = Arrays.asList("email");
//                        permissions.add("email");
                        openActiveSession(getActivity(), permissionNeeds);
                    } else {
                        Dialogs.showMsgDialog("", AppConstants.ERRORNETWORKCONNECTION, getActivity());
                    }
                    break;

                }
            case R.id.signup_fav_menu_text:
                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(firstNameEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }

                openDialog();


                break;

            case R.id.locSpinnerBtn:
                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            firstNameEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }
                openDialog();

                break;
        }


    }

    private void openActiveSession(Activity activity, List<String> permissions) {

        if (Engine.CheckEnableGPS(getActivity())) {

            try {
                RotiHomeActivity.startGPS();


                latitude = RotiHomeActivity.getGetLatLongObj().getLatitude();
                longitude = RotiHomeActivity.getGetLatLongObj().getLongitude();
            } catch (Exception e) {

            }
        }
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
                                            final String firstName = firstNameEdit.getText().toString();   // bFacebookData.getString("first_name");


                                            String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                                            String marketingOptin = "";


                                            if (marketingOptionCB.isChecked())
                                                marketingOptin = "true";
                                            else
                                                marketingOptin = "false";

                                            final PushNotificationHelper helper = new PushNotificationHelper(getActivity());
                                            String regId = helper.getRegistrationId(getActivity());
                                            String refer = referEdit.getText().toString();

                                            latitude = RotiHomeActivity.getGetLatLongObj().getLatitude();
                                            longitude = RotiHomeActivity.getGetLatLongObj().getLongitude();

                                            FacebookSignUpModel fbModel = new FacebookSignUpModel(email, firstName, android_id, dobDate, dobMonth, dobYear,
                                                    String.valueOf(latitude), String.valueOf(longitude)
                                                    , AppConstants.DEVICE_TYPE, AppConstants.REGISTERTYPEFB, getResources().getString(R.string.APPKEY), refer, Engine
                                                    .getDeviceID(getActivity()), regId, manufacturer + " " + model, androidOS, marketingOptin, favMenuString, "true");

                                            String modelPostRequest = Gson.getGson().toJson(fbModel);
                                            String url = getResources().getString(R.string.BASE_URL) + "/user/signup";

                                            submitSignUpToServerVolley(url, modelPostRequest, email);
//
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
                Log.d("Facebook1", "ERROR");
            }

            @Override
            public void onError(FacebookException error) {
                LoginManager.getInstance().logOut();
                RotiHomeActivity.getProgressDialog().dismiss();
                Log.d("Facebook1", "ERROR");

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("rht", "onActivityResult called requestCode " + requestCode + " resultCode " + resultCode);

        callbackManager.onActivityResult(requestCode, resultCode, data);
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
                bundle.putString("first_name", object.getString("first_name"));
            } else {
                final String errorMessage = getString(R.string.reward_acess_email_);
                Dialogs.showMsgDialog("", errorMessage, getActivity());
            }

            return bundle;
        } catch (JSONException e) {
            Log.d("Facebook", e.toString());
            LoginManager.getInstance().logOut();
        }
        return null;
    }


    private boolean filterLongEnough(EditText amountEditText) {
        return amountEditText.getText().toString().trim().length() > 0;
    }

    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    public void showconfirmDialog() {
        try {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
            alt_bld.setMessage("Please enter a valid 10-digit number")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();

                                }
                            });
            AlertDialog alert = alt_bld.create();
            alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetTextWatcherForAmountEditView(final EditText tmpEditText,
                                                 final View view) {
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Utility.editTextHideShowHint(s.length(), tmpEditText, getActivity(), view);

                if (filterLongEnough(firstNameEdit)
                        && !favMenuString.equals("")) {
                    try {
                        signupBtn.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    signupBtn.setEnabled(false);
                }
            }

            private boolean filterLongEnough(EditText tmpEditText) {
                return tmpEditText.getText().toString().trim().length() > 0;
            }
        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }


    private void updateDateLabel() {

        dobYear = myCalendar.get(Calendar.YEAR);
        dobMonth = myCalendar.get(Calendar.MONTH) + 1;
        dobDate = myCalendar.get(Calendar.DAY_OF_MONTH);

        Log.d("le6end4", dobYear + "-" + dobMonth + "-" + dobDate);

        String myFormat = "MMMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textDob.setText(sdf.format(myCalendar.getTime()));
        textDob.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void openDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = inflater.inflate(R.layout.list_dialog, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("FAV. MENU ITEM");
        ListView lv = convertView.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterGroup, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                favMenuString = items[position];
                if (filterLongEnough(firstNameEdit)) {
                    signupBtn.setEnabled(true);
                }
                favMenuText.setText(favMenuString);
                spinnerPlaceholder.setVisibility(View.GONE);

                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public GetLatLongFromGPS getGetLatLongObj() {
        return getLatLongObj;
    }

    public void submitSignUpToServerVolley(final String submitUrl, String json, String email) {

        Log.d("Json", json);

        Log.d("EMAILFACEBOOK", email);
        RotiHomeActivity.getProgressDialog().show();
        JsonObjectRequest request_json = null;
        try {
            request_json = new JsonObjectRequest(Request.Method.POST, submitUrl, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            RotiHomeActivity.getProgressDialog().dismiss();

                            SignUpResposne emailSignInResponse = Gson.getGson().fromJson(response.toString(), SignUpResposne.class);


                            if (!emailSignInResponse.getStatus()) {
                                Dialogs.showMsgDialog("", emailSignInResponse.getNotice(), getActivity());

                            }

                            if (emailSignInResponse.getNotice().equals("Welcome back")) {
                                try {
                                    AlertDialog.Builder alertDialogBuilder;
                                    alertDialogBuilder = new AlertDialog.Builder(getContext());
                                    alertDialogBuilder.setMessage(emailSignInResponse.getNotice()).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            clearBackStack();

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

                            try {
                                if (emailSignInResponse.getStatus()) {
                                    SharedPreferences.Editor prefsEditor = RotiHomeActivity.getPreferenceEditor();
                                    prefsEditor.putString(AppConstants.PREFLOGINID, email);

                                    prefsEditor.putBoolean(AppConstants.PREFLOGOUTBUTTONTAG, false);

                                    String auth_token = "";


                                    if (emailSignInResponse.getAuthToken() != null) {
                                        auth_token = emailSignInResponse.getAuthToken();
                                        prefsEditor.putString(AppConstants.PREFAUTH_TOKEN, auth_token);


                                        Log.d("auth_token", auth_token);
                                    }
                                    prefsEditor.commit();


                                    if (emailSignInResponse.getStatus() && !emailSignInResponse.getNotice().equalsIgnoreCase("welcome back")) {
                                        try {
                                            AlertDialog.Builder alertDialogBuilder;
                                            alertDialogBuilder = new AlertDialog.Builder(getContext());
                                            alertDialogBuilder.setMessage(emailSignInResponse.getNotice()).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    SharedPreferences.Editor prefsEditor = RotiHomeActivity.getPreferenceEditor();
                                                    prefsEditor.putBoolean(AppConstants.FROMFBSIGNUP, true);
                                                    prefsEditor.commit();


                                                    clearBackStack();
                                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                                                    transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email), AppConstants.TAG_SIGN);
                                                    transaction.addToBackStack(AppConstants.TAG_SIGN);
                                                    transaction.commit();


                                                }
                                            });
                                            AlertDialog alert = alertDialogBuilder.create();
                                            alert.show();


                                        } catch (Exception e) {
                                            AppConstants.parseInput(response.toString(), getActivity(), "");
                                        }
                                    }


//                                    if (Engine.getInstance().getSetNextPage().length() > 0) {
//                                        ((OpenFragmetListener) getActivity()).startFragment(Engine.getInstance().getSetNextPage());
//                                        Engine.getInstance().setSetNextPage("");
//                                    } else {
//
//                                    }

                                }


                            } catch (Exception e) {
                                AppConstants.parseInput(response.toString(), getActivity(), "");
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


    public void startGPS() {
        if (getLatLongObj != null)
            getLatLongObj.startGPS();
    }

    public void stopGPS() {
        if (getLatLongObj != null) { // PP
            getLatLongObj.stopLocationListening();
        }
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

            getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


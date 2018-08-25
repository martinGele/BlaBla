package com.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
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
import android.text.TextUtils;
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
import com.google.android.gms.analytics.Tracker;
import com.postmodels.EmailSignUpModel;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class SignUpFragment extends Fragment implements View.OnClickListener {


    private String androidOS = Build.VERSION.RELEASE;
    private String model = Build.MODEL;
    private String manufacturer = Build.MANUFACTURER;

    boolean isInfo = false;
    String mTabName;
    public static String favMenuString = "";
    EditText lastnameEdit, firstNameEdit, emailEdit, pwdEdit, confirmPwdEdit, editRetailer, referEdit;
    TextView lastnameEditHint;
    TextView favoriteStore;
    TextView optinText;
    TextView firstNameEditHint;
    TextView emailEditHint;
    TextView pwdEditHint;
    TextView confirmPwdEditHint;
    TextView mallEmployee;
    TextView editRetailerHint;
    TextView spinnerPlaceholder;
    TextView referEditHint;
    TextView favMenuText;
    ImageView locSpinnerBtn;
    DatePicker date_picker;
    RelativeLayout datepickerclick, spinnerContainer;
    TextView textDob;
    Integer dobYear;
    Integer dobMonth;
    Integer dobDate;
    Button signupBtn;
    CheckBox marketingOptionCB, mallEmployeeCB;
    Calendar myCalendar = Calendar.getInstance();
    View view;
    SimpleDateFormat sdf;
    public double latitude = 0.0, longitude = 0.0;

    DatePickerDialog.OnDateSetListener date;


    private Tracker mTracker;
    private String SCREEN_NAME = "Sign Up Fragment";

    String[] items;
    private GetLatLongFromGPS getLatLongObj;

    private static List<String> listfield = new ArrayList<String>();

    public static String name;
    public static String id;

    public SignUpFragment() {

    }


    public static void setName(String name) {
        SignUpFragment.name = name;
    }

    public static void setId(String id) {
        SignUpFragment.id = id;

    }

    public GetLatLongFromGPS getGetLatLongObj() {
        return getLatLongObj;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();
        getLatLongObj = GetLatLongFromGPS.getinstance(getActivity());
        listfield.clear();
        name = "";

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_signupemail, container, false);

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                }, 1);
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_PHONE_STATE,
                }, 2);


        initView(view);
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
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getActivity(), "Permission denied to read phone state", Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }


    private void initView(View view) {
        setTracker();

        /*
        initialize the views
         */
        firstNameEdit = view.findViewById(R.id.signup_edit_firstname);
        lastnameEdit = view.findViewById(R.id.signup_edit_lastname);
        emailEdit = view.findViewById(R.id.signup_edit_email);
        pwdEdit = view.findViewById(R.id.signup_edit_password);
        confirmPwdEdit = view.findViewById(R.id.signup_edit_confirm_password);
        editRetailer = view.findViewById(R.id.signup_edit_retailer);
        firstNameEditHint = view.findViewById(R.id.signup_edit_firstname_hint);
        lastnameEditHint = view.findViewById(R.id.signup_edit_lastname_hint);
        emailEditHint = view.findViewById(R.id.signup_edit_email_hint);
        pwdEditHint = view.findViewById(R.id.signup_edit_password_hint);
        confirmPwdEditHint = view.findViewById(R.id.signup_edit_confirm_password_hint);
        mallEmployee = view.findViewById(R.id.signup_edit_mallemployee_hint);
        editRetailerHint = view.findViewById(R.id.signup_edit_retailer_hints);
        favMenuText = view.findViewById(R.id.signup_fav_menu_text_signup);
        spinnerPlaceholder = view.findViewById(R.id.signupspinnerPlaceholder);
        locSpinnerBtn = view.findViewById(R.id.locSpinnerBtnSignUp);
        spinnerContainer = view.findViewById(R.id.spinnerContainer);
        marketingOptionCB = view.findViewById(R.id.marketingOptionCB);
        mallEmployeeCB = view.findViewById(R.id.sugnup_edit_mallemployee);
        datepickerclick = view.findViewById(R.id.layoutdob);
        textDob = view.findViewById(R.id.textviewdob);
        referEdit = view.findViewById(R.id.signup_edit_Refercodeoptional);
        referEditHint = view.findViewById(R.id.signup_edit_Refercodeoptional_hint);
        signupBtn = view.findViewById(R.id.signup_image_signup_s);
        optinText = view.findViewById(R.id.optinText);
        favoriteStore = view.findViewById(R.id.textViewFavoriteStore);
        /*
        setup click listiners
         */
        locSpinnerBtn.setOnClickListener(this);
        favMenuText.setOnClickListener(this);
        datepickerclick.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
        favoriteStore.setOnClickListener(this);

        /*
        dropdown spinner selection
         */
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

        /*
        setup font's and size of the textviews
         */

        Fonts.fontFinkHeavyRegularTextView(mallEmployee, 18, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(editRetailer, 16, AppConstants.COLORORANGE, getActivity().getAssets());

        Fonts.fontRobotoCodensedBoldTextView(firstNameEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(lastnameEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());

        Fonts.fontRobotoCodensedBoldTextView(emailEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(pwdEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(confirmPwdEdit, 16, AppConstants.COLORORANGE,
                getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(favMenuText, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(firstNameEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(lastnameEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());

        Fonts.fontRobotoCodensedBoldTextView(emailEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(editRetailerHint, 16, AppConstants.COLORBG, getActivity().getAssets());

        Fonts.fontRobotoCodensedBoldTextView(pwdEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(confirmPwdEditHint, 16, AppConstants.COLORBG,
                getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(spinnerPlaceholder, 16, AppConstants.COLORBG,
                getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(referEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(referEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(textDob, 16, AppConstants.COLORORANGE, getActivity().getAssets());

        Fonts.fontRobotoCodensedBoldTextView(signupBtn, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        Fonts.fontRobotoCodensedBoldTextView(optinText, 14, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(favoriteStore, 16, AppConstants.COLORBLUE, getActivity().getAssets());


        if (name != null && !name.isEmpty()) {
            favoriteStore.setText(name);
            favoriteStore.setGravity(Gravity.CENTER | Gravity.LEFT);
            Fonts.fontRobotoCodensedBoldTextView(favoriteStore, 16, AppConstants.COLORORANGE, getActivity().getAssets());


        } else {

        }


        SetTextWatcherForAmountEditView(editRetailer);

        SetTextWatcherForAmountEditView(firstNameEdit);
        SetTextWatcherForAmountEditView(lastnameEdit);
        SetTextWatcherForAmountEditView(emailEdit);
        SetTextWatcherForAmountEditView(pwdEdit);
        SetTextWatcherForAmountEditView(confirmPwdEdit);
        SetTextWatcherForAmountEditView(referEdit);

        /* signupBtn.setBackgroundResource(R.drawable.sign_up_button_idle); */
        signupBtn.setEnabled(false);

        /*
         * AppConstants.fontBaileySanITCStdBookBoldTextView(signupemailText, 25,
         * AppConstants.COLORWHITEFH, mHomePage.getAssets());
         */
        dobYear = 0;
        dobMonth = 0;
        dobDate = 0;

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }

        };

        if (listfield.size() > 0) {
            textDob.setText(listfield.get(0));
            dobDate = Integer.valueOf(listfield.get(1));
            dobMonth = Integer.valueOf(listfield.get(2));
            dobYear = Integer.valueOf(listfield.get(3));
            favMenuText.setText(listfield.get(4));
            spinnerPlaceholder.setVisibility(View.GONE);
            textDob.setGravity(Gravity.CENTER_VERTICAL);


            listfield.clear();

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.locSpinnerBtnSignUp:

                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(firstNameEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }

                openDialog();

                break;

            case R.id.signup_fav_menu_text_signup:

                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(firstNameEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }

                openDialog();

                break;

            case R.id.layoutdob:
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();


                break;

            case R.id.signup_image_signup_s:

                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(emailEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }

                boolean isPass = true;

                if (!isValidEmail(emailEdit)) {
                    isPass = false;
                    Dialogs.showMsgDialog("", "Please enter a valid email address.", getActivity());
                }

                if (isPass) {
                    String mFirstName = firstNameEdit.getText().toString();
                    String mLastName = lastnameEdit.getText().toString();
                    String email = emailEdit.getText().toString();
                    String mPassword = pwdEdit.getText().toString();
                    String mConfirmPassword = confirmPwdEdit.getText().toString();
                    String mrefer = referEdit.getText().toString();
                    String mRetailer = editRetailer.getText().toString();
                    String favoriteMenuItem = favMenuString;
                    String marketingOptin = "";
                    int mallEployeeOption;
//                    String favoriteStoreString= favoriteStore.getText().toString();

                    if (mPassword.equals(mConfirmPassword)) {

                        if (mallEmployeeCB.isChecked())
                            mallEployeeOption = 1;

                        else
                            mallEployeeOption = 0;

                        if (marketingOptionCB.isChecked())
                            marketingOptin = "true";
                        else
                            marketingOptin = "false";

                        if (Engine.isNetworkAvailable(getActivity())) {

                            showBackConfirmMsgDialog(getActivity().getString(R.string.signupheader) + "\"" + email + "\""
                                            + getActivity().getString(R.string.signupmessages)
                                            + getActivity().getString(R.string.signupattentionmessages),
                                    email, mPassword, mrefer, marketingOptin, mFirstName, mLastName, mRetailer, mallEployeeOption, favoriteMenuItem, id);
                        } else {
                            Dialogs.showMsgDialog("", AppConstants.ERRORNETWORKCONNECTION, getActivity());
                        }
                    } else {
                        confirmPwdEdit.requestFocus();
                        Dialogs.showMsgDialog("", "Password and Cofirm Password do not  match.", getActivity());
                    }
                }


                break;

            case R.id.textViewFavoriteStore:

                listfield.clear();
                listfield.add(textDob.getText().toString());
                listfield.add(String.valueOf(dobDate));
                listfield.add(String.valueOf(dobMonth));
                listfield.add(String.valueOf(dobYear));
                listfield.add(favMenuString);


                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.home_linear_container, new FavoriteStoreFragment(), AppConstants.TAG_SIGN);
                transaction.addToBackStack(AppConstants.TAG_SIGN);
                transaction.commit();
                break;
        }

    }

    private void showBackConfirmMsgDialog(String s, final String email, final String mPassword, final String mrefer, final String finalMarketingOptin, final String mFirstName, final String mLastName, final String mRetailer, final int mallEployeeOption, final String favoriteMenuItem,
                                          final String favoriteStoreString) {

        if (Engine.CheckEnableGPS(getActivity())) {

            try {
                RotiHomeActivity.startGPS();


                latitude = RotiHomeActivity.getGetLatLongObj().getLatitude();
                longitude = RotiHomeActivity.getGetLatLongObj().getLongitude();
            } catch (Exception e) {

            }
        }
        try {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
            alt_bld.setMessage(s).setCancelable(false)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                    if (Engine.isNetworkAvailable(getActivity())) {
                        String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);


                        final PushNotificationHelper helper = new PushNotificationHelper(getActivity());
                        String regId = helper.getRegistrationId(getActivity());
                        String url = getResources().getString(R.string.BASE_URL) + "/user/signup";
                        EmailSignUpModel emailSignUpModel = new EmailSignUpModel(email, mPassword, mFirstName, mLastName, android_id,
                                String.valueOf(latitude), String.valueOf(longitude), AppConstants.DEVICE_TYPE, AppConstants.REGISTERTYPE, getResources().getString(R.string.APPKEY),
                                mrefer, AppConstants.DEVICE_TYPE, regId, Engine.getDeviceID(getActivity()), androidOS, dobDate, dobMonth, dobYear, finalMarketingOptin, favoriteMenuItem, mallEployeeOption, mRetailer, favoriteStoreString, "true");

                        String modelPostRequest = Gson.getGson().toJson(emailSignUpModel);

                        Log.d("ModelS", modelPostRequest);
                        submitSignUpDetailsToServer(url, modelPostRequest);
                    } else {
                        Dialogs.showMsgDialog("", AppConstants.ERRORNETWORKCONNECTION, getActivity());
                    }
                }
            });
            AlertDialog alert = alt_bld.create();

            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void openDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list_dialog, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("FAV. MENU ITEM");
        ListView lv = (ListView) convertView.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterGroup, View view, int position, long id) {
                // TODO Auto-generated method stub
                favMenuString = items[position];
                if (filterLongEnoughName(firstNameEdit) && filterLongEnough(emailEdit)
                        && filterLongEnoughLastname(lastnameEdit) && filterLongEnoughPassword(pwdEdit)
                        && filterLongEnoughPassword(confirmPwdEdit) && !favMenuString.equals("")) {
                    signupBtn.setEnabled(true);
                }
                favMenuText.setText(favMenuString);
                spinnerPlaceholder.setVisibility(View.GONE);

                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private boolean filterLongEnoughName(EditText amountEditText) {
        return amountEditText.getText().toString().trim().length() > 1;
    }

    private boolean filterLongEnoughLastname(EditText amountEditText) {
        return amountEditText.getText().toString().trim().length() > 1;
    }

    private boolean filterLongEnough(EditText amountEditText) {
        return amountEditText.getText().toString().trim().length() > 0;
    }

    private boolean filterLongEnoughPassword(EditText amountEditText) {
        return amountEditText.getText().toString().length() >= 6;
    }

    public boolean isValidEmail(EditText emailEditText) {
        return !TextUtils.isEmpty(emailEditText.getText())
                && android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText()).matches();
    }

    private boolean isPasswordAndConfirmSame(EditText password, EditText confirmPassword) {
        return password.getText().toString().equals(confirmPassword.getText().toString());
    }

    /*
    setup text watcher for the edit texts, if all the edit texts are fulfilled correctly then sign up button will be enabled
     */

    private void SetTextWatcherForAmountEditView(final EditText tmpEditText) {
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (tmpEditText.getId() == R.id.signup_edit_email || tmpEditText.getId() == R.id.signup_edit_password
                        || tmpEditText.getId() == R.id.signup_edit_lastname
                        || tmpEditText.getId() == R.id.signup_edit_confirm_password) {
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
                 * this line of code hides the hint on the edit text when text is being placed
                 */
                Utility.editTextHideShowHint(s.length(), tmpEditText, getActivity(), view);

                if (filterLongEnoughName(firstNameEdit) && filterLongEnough(emailEdit)
                        && filterLongEnoughLastname(lastnameEdit) && filterLongEnoughPassword(pwdEdit)
                        && filterLongEnoughPassword(confirmPwdEdit) && !favMenuString.equals("")) {
                    try {
                        /*
                         * signupBtn .setBackgroundResource(R.drawable.sign_up_button);
                         */
                        signupBtn.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    /*
                     * signupBtn .setBackgroundResource(R.drawable.sign_up_button_idle);
                     */
                    signupBtn.setEnabled(false);
                }
            }

            private boolean filterLongEnough(EditText tmpEditText) {
                return tmpEditText.getText().toString().trim().length() > 0;
            }

            private boolean filterLongEnoughName(EditText tmpEditText) {
                return tmpEditText.getText().toString().trim().length() > 1;
            }

            private boolean filterLongEnoughLastname(EditText tmpEditText) {
                return tmpEditText.getText().toString().trim().length() > 1;
            }

            private boolean filterLongEnoughPhone(EditText tmpEditText) {
                return tmpEditText.getText().toString().trim().length() >= 12;
            }

            private boolean filterLongEnoughPassword(EditText amountEditText) {
                return amountEditText.getText().toString().length() >= 6;
            }
        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }


    private void updateDateLabel() {

        dobYear = myCalendar.get(Calendar.YEAR);
        dobMonth = myCalendar.get(Calendar.MONTH) + 1;
        dobDate = myCalendar.get(Calendar.DAY_OF_MONTH);

        Log.d("le6end4", dobYear + "-" + dobMonth + "-" + dobDate);

        String myFormat = "MMMM dd, yyyy"; // In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        textDob.setText(sdf.format(myCalendar.getTime()));
        textDob.setGravity(Gravity.CENTER_VERTICAL);


    }


    /*
    pass the volley sign up parameters to server
     */
    private void submitSignUpDetailsToServer(String submitUrl, String json) {

        RotiHomeActivity.getProgressDialog().show();
        JsonObjectRequest request_json = null;
        try {
            request_json = new JsonObjectRequest(Request.Method.POST, submitUrl, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            RotiHomeActivity.getProgressDialog().dismiss();

                            Log.d("SignUp", response.toString());

                            SignUpResposne emailSignInResponse = Gson.getGson().fromJson(response.toString(), SignUpResposne.class);


                            if (!emailSignInResponse.getStatus()) {

                                Dialogs.showMsgDialog("", emailSignInResponse.getNotice(), getActivity());
                            }

                            try {
                                if (emailSignInResponse.getStatus()) {
                                    SharedPreferences.Editor prefsEditor = RotiHomeActivity.getPreferenceEditor();
                                    prefsEditor.putString(AppConstants.PREFLOGINID, emailEdit.getText().toString());
                                    prefsEditor.putString(AppConstants.PREFLOGINPAS, pwdEdit.getText().toString());
                                    prefsEditor.putBoolean(AppConstants.PREFLOGOUTBUTTONTAG, false);
                                    prefsEditor.putBoolean(AppConstants.FROMFBSIGNUP, false);

                                    String auth_token = "";

                                    if (emailSignInResponse.getAuthToken() != null) {
                                        auth_token = emailSignInResponse.getAuthToken();
                                        prefsEditor.putString(AppConstants.PREFAUTH_TOKEN, auth_token);
                                        Log.d("auth_token", auth_token);
                                    }
                                    prefsEditor.commit();
                                }

                                if (emailSignInResponse.getStatus()) {
                                    try {
                                        AlertDialog.Builder alertDialogBuilder;
                                        alertDialogBuilder = new AlertDialog.Builder(getContext());
                                        alertDialogBuilder.setMessage(emailSignInResponse.getNotice()).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();

                                                String email = emailEdit.getText().toString();
                                                String password = pwdEdit.getText().toString();

                                                clearBackStack();
                                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                                                transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
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
                            } catch (Exception e) {
                                e.printStackTrace();
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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}

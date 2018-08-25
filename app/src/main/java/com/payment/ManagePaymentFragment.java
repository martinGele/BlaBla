package com.payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.interfaces.OpenFragmetListener;
import com.responsemodels.GetClientTokenResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.util.Utility;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.util.Dialogs.trimMessage;

public class ManagePaymentFragment extends Fragment implements View.OnClickListener, Handler.Callback {

    private static final int MONTHYEARDATESELECTOR_ID = 3;
    private int keyDel;
    private String a;
    private Button save_btn;
    boolean isGetClientToken;

    private TextView manage_payment_zipcode_hint, manage_payment_edit_city_hint,
            manage_payment_edit_state_hint, manage_payment_edit_card_type_hint, manage_payment_edit_address_hint, card_number_hint, name_on_card_hint, manage_payment_month_year;
    private EditText manage_payment_edit_city, manage_payment_edit_state, manage_payment_edit_address, card_number, name_on_card, manage_payment_zipcode;
    private RelativeLayout managePaymentForm;
    private String nameOnCard, cardNumber, date, zipCode, address, city, state, phoneNumber; /* , cVvNumber; */
    private static AlertDialog.Builder alertDialogBuilder;
    private boolean accessFromAccountSettingPage = false;
    private TextView cardTypeText;

    private String[] items;
    private String cardTypeString = "";

    Integer dobYear;
    Integer dobMonth;
    Integer dobDate;


    Calendar myCalendar = Calendar.getInstance();

    private Handler uiHandler;

    private static final int UPDATE_UI = 10;


    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.manage_payment, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {


        InputFilter spaceFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isSpaceChar(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

        items = new String[6];
        items[0] = "Visa";
        items[1] = "Master Card";
        items[2] = "Amex";
        items[3] = "Diners Club";
        items[4] = "Discover";
        items[5] = "JCB";


        uiHandler = new Handler(Looper.getMainLooper(), this);

        name_on_card = view.findViewById(R.id.manage_payment_edit_nameoncard);
        card_number = view.findViewById(R.id.card_number);
        manage_payment_edit_address = view.findViewById(R.id.manage_payment_edit_address);
        name_on_card_hint = view.findViewById(R.id.manage_payment_edit_nameoncard_hint);
        card_number_hint = view.findViewById(R.id.card_number_hint);
        manage_payment_edit_address_hint = view.findViewById(R.id.manage_payment_edit_address_hint);
        manage_payment_edit_card_type_hint = view.findViewById(R.id.manage_payment_edit_card_type_hint);
        manage_payment_month_year = view.findViewById(R.id.manage_payment_month_year);
        manage_payment_edit_city_hint = view.findViewById(R.id.manage_payment_edit_city_hint);
        manage_payment_edit_city = view.findViewById(R.id.manage_payment_city_address_);
        manage_payment_edit_state_hint = view.findViewById(R.id.manage_payment_edit_state_hint);
        manage_payment_edit_state = view.findViewById(R.id.manage_payment_edit_state);
        manage_payment_zipcode = view.findViewById(R.id.manage_payment_zipcode);
        manage_payment_zipcode_hint = view.findViewById(R.id.manage_payment_zipcode_hint);
        save_btn = view.findViewById(R.id.save_btn);
        managePaymentForm = view.findViewById(R.id.managePaymentForm);
        cardTypeText = view.findViewById(R.id.manage_payment_edit_card_type);

        /*
        setup the font and the color of the views
         */

        Fonts.fontRobotoCodensedBoldTextView(name_on_card, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(manage_payment_edit_address, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(cardTypeText, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(card_number, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(manage_payment_month_year, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(manage_payment_zipcode, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(name_on_card_hint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(card_number_hint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(manage_payment_edit_address_hint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(manage_payment_edit_card_type_hint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(manage_payment_zipcode_hint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(manage_payment_edit_city_hint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(manage_payment_edit_city, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(manage_payment_edit_state_hint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(manage_payment_edit_state, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(save_btn, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        dobYear = 0;
        dobMonth = 0;
        dobDate = 0;

        /*
        set text watchers for the edit texts
         */

        SetTextWatcherForAmountEditView(name_on_card);
        SetTextWatcherForAmountEditView(card_number);
        SetTextWatcherForAmountEditView(manage_payment_edit_address);
        SetTextWatcherForAmountEditView(manage_payment_zipcode);
        SetTextWatcherForAmountEditView(manage_payment_edit_city);
        SetTextWatcherForAmountEditView(manage_payment_edit_state);
        save_btn.setEnabled(false);




        /*
        set click listiners
         */
        cardTypeText.setOnClickListener(this);
        manage_payment_month_year.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        card_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean flag = true;
                String eachBlock[] = card_number.getText().toString().split("-");
                for (int i = 0; i < eachBlock.length; i++) {
                    if (eachBlock[i].length() > 4) {
                        flag = false;
                    }
                }
                if (flag) {

                    card_number.setOnKeyListener(new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {

                            if (keyCode == KeyEvent.KEYCODE_DEL)
                                keyDel = 1;
                            return false;
                        }
                    });

                    if (keyDel == 0) {

                        if (((card_number.getText().length() + 1) % 5) == 0) {

                            if (card_number.getText().toString().split("-").length <= 3) {
                                card_number.setText(card_number.getText() + "-");
                                card_number.setSelection(card_number.getText().length());
                            }
                        }
                        a = card_number.getText().toString();
                    } else {
                        a = card_number.getText().toString();
                        keyDel = 0;
                    }

                } else {
                    card_number.setText(a);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getClientToken();

        managePaymentForm.setVisibility(View.GONE);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.manage_payment_edit_card_type:

                openDialog();


                break;

            case R.id.manage_payment_month_year:
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateLabel();
                    }

                };

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

                try {
                    ((ViewGroup) dialog.getDatePicker())
                            .findViewById(Resources.getSystem().getIdentifier("day", "id", "android"))
                            .setVisibility(View.GONE);

                } catch (Exception ex) {
                }

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
                cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
                cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));

                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                dialog.show();


                break;

            case R.id.card_number:

                break;


            case R.id.save_btn:

                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(card_number.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }


                cardNumber = card_number.getText().toString();
                nameOnCard = name_on_card.getText().toString();
                zipCode = manage_payment_zipcode.getText().toString();
                address = manage_payment_edit_address.getText().toString();
                city = manage_payment_edit_city.getText().toString();
                state = manage_payment_edit_state.getText().toString();
                String cardholderName = "";
//                try {
//                    cardholderName = URLEncoder.encode(nameOnCard, "utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

                String expMonth = dobMonth.toString();
                String expYear = dobYear.toString();

                address = address.replace(" ", "%20");
                city = city.replace(" ", "%20");
                cardholderName = cardholderName.replace(" ", "%20");
                state = state.replace(" ", "%20");
                nameOnCard = nameOnCard.replace(" ", "%20");

                cardNumber = cardNumber.replace("-", "");


                String url = getResources().getString(R.string.BASE_URL_MOBILEPAY) + "/user/method/?" +
                        "cardNumber=" + cardNumber + "&cardholderName=" + nameOnCard + "&expMonth=" + expMonth + "&expYear=" + expYear +
                        "&cvv=" + "&streetAddress=" + address + "&city=" + city + "&state=" + state + "&postalCode=" + zipCode;

                Log.d("URLPAYMENT", url);


                postPaymentDetail(url);

                break;


        }

    }

    private void getClientToken() {

        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");
        RotiHomeActivity.getProgressDialog().show();
        String url = getResources().getString(R.string.BASE_URL_MOBILEPAY) + "/user/method";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ClientToken", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        GetClientTokenResponse getClientTokenResponse = Gson.getGson().fromJson(response.toString(), GetClientTokenResponse.class);

                        int code = getClientTokenResponse.getCode();
                        if (getClientTokenResponse.getStatus()) {
                            String cardNumber = getClientTokenResponse.getPaymentMethodId();
                            // String lastDigits = resObject.getString("lastDigits");
                            // String type = resObject.getString("type");
                            String expiryMonth = getClientTokenResponse.getExpiryMonth();
                            String expiryYear = getClientTokenResponse.getExpiryYear();

                            if (expiryMonth.length() == 1)
                                expiryMonth = "0" + expiryMonth;

                            String expiredDate = expiryMonth + "/" + expiryYear;

                            clearBackStack();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.home_linear_container, new ShowCardSummaryPageFragment(cardNumber, expiredDate), AppConstants.TAG_CARD_SUMMARY);
                            transaction.addToBackStack(AppConstants.TAG_CARD_SUMMARY);
                            transaction.commit();

                        }
                        managePaymentForm.setVisibility(View.VISIBLE);
                        if (code == 4) {
                            AppConstants.showMsgDialog("", AppConstants.BLANKMESSAGEREPLACEMENT, getActivity());

                            if (Engine.getInstance().getSetNextPage().length() > 0) {
                                ((OpenFragmetListener) getActivity()).startFragment(Engine.getInstance().getSetNextPage());
                                Engine.getInstance().setSetNextPage("");
                            } else {

                            }
                        } else {
                            if (code == 57)
                                managePaymentForm.setVisibility(View.VISIBLE);

                            else
                                createNewMobilePaymentAccount();
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

    /*
    volley call to create new mobile payment account
     */
    public void createNewMobilePaymentAccount() {
        RotiHomeActivity.getProgressDialog().show();
        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");

        String url = getResources().getString(R.string.BASE_URL_MOBILEPAY) + "/user";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("newMobilePaymentAccount", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        GetClientTokenResponse getClientTokenResponse = Gson.getGson().fromJson(response.toString(), GetClientTokenResponse.class);

                        try {
                            if (getClientTokenResponse.getStatus()) {
                                int code = getClientTokenResponse.getCode();
                                String message = getClientTokenResponse.getMessage();

                                managePaymentForm.setVisibility(View.VISIBLE);

                            } else {
                                String message = getClientTokenResponse.getMessage();
                                if (!message.equals(""))
                                    AppConstants.showMsgDialog("", message, getActivity());
                                else
                                    AppConstants.showMsgDialog("", AppConstants.BLANKMESSAGEREPLACEMENT, getActivity());

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            AppConstants.showMsgDialog("", AppConstants.BLANKMESSAGEREPLACEMENT, getActivity());
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

    /*
    volley call to pass the payment method
     */
    private void postPaymentDetail(String url) {

        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");
        RotiHomeActivity.getProgressDialog().show();
        JsonObjectRequest request_json = null;
        request_json = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        RotiHomeActivity.getProgressDialog().dismiss();

                        Log.d("PUTCREDITCARD", response.toString());
                        GetClientTokenResponse getClientTokenResponse = Gson.getGson().fromJson(response.toString(), GetClientTokenResponse.class);

                        if (getClientTokenResponse.getCode() == 501 || getClientTokenResponse.getCode() == 502 || !getClientTokenResponse.getStatus()) {

                            Dialogs.showMsgDialog("", getClientTokenResponse.getMessage(), (getContext()));

                        }


                        if (getClientTokenResponse.getStatus()) {

                            try {
                                AlertDialog.Builder alertDialogBuilder;
                                alertDialogBuilder = new AlertDialog.Builder(getContext());
                                alertDialogBuilder.setMessage(getClientTokenResponse.getMessage()).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();


                                        clearBackStack();
                                        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                                        fm.popBackStack();
//                                        if (Engine.getInstance().getSetNextPage().length() > 0) {
//                                            ((OpenFragmetListener) getActivity()).startFragment(Engine.getInstance().getSetNextPage());
//                                            Engine.getInstance().setSetNextPage("");
//                                        }

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



        /*
         add the request object to the queue to be executed
         */
        VolleyControllerAndTracker.getInstance().addToRequestQueue(request_json);


    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    private void openDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list_dialog, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("CARD TYPE");
        ListView lv = (ListView) convertView.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterGroup, View view, int position, long id) {
                // TODO Auto-generated method stub
                cardTypeString = items[position];
                if (filterLongEnoughCardName(name_on_card) && filterLongEnough(card_number)
                        // && filterLongEnoughPhone(manage_payment_edit_phone)
                        && filterLongEnough(manage_payment_edit_state) && filterLongEnough(manage_payment_edit_city)
                        && filterLongEnough(manage_payment_edit_address) && (dobYear > 0)
                        /* && filterLongEnough(manage_payment_month_year) */
                        /* && filterLongEnoughCvv(manage_payment_cw) */
                        && filterLongEnoughZipCode(manage_payment_zipcode))
                // && !cardTypeString.equals(""))
                {
                    save_btn.setEnabled(true);
                }
                cardTypeText.setText(cardTypeString);
                manage_payment_edit_card_type_hint.setVisibility(View.GONE);

                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void SetTextWatcherForAmountEditView(final EditText tmpEditText) {
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                name_on_card.setSelection(name_on_card.getText().toString().length());
                uiHandler.sendEmptyMessageDelayed(UPDATE_UI, 100);
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

                if (filterLongEnoughCardName(name_on_card) && filterLongEnough(card_number)

                        && filterLongEnough(manage_payment_edit_city) && filterLongEnough(manage_payment_edit_state)
                        && filterLongEnough(manage_payment_edit_address) && (dobYear > 0)
                        // && filterLongEnough(manage_payment_month_year)
                        // && filterLongEnoughCvv(manage_payment_cw)
                        // && filterLongEnoughPhone(manage_payment_edit_phone)
                        // && !cardTypeString.equals(""))

                        && filterLongEnoughZipCode(manage_payment_zipcode)) {

                    try {
                        // save_btn.setImageResource(R.drawable.save);
                        save_btn.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else {

                    // save_btn.setImageResource(R.drawable.save_idle);
                    save_btn.setEnabled(false);
                }
            }

        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }

    private boolean filterLongEnoughPhone(EditText amountEditText) {
        return amountEditText.getText().toString().trim().length() >= 12;
    }

    private boolean filterLongEnoughCardName(EditText tmpEditText) {
        return tmpEditText.getText().toString().trim().length() > 1;
    }

    private boolean filterLongEnoughZipCode(EditText tmpEditText) {
        return tmpEditText.getText().toString().trim().length() > 4;
    }

    private boolean filterLongEnoughCvv(EditText tmpEditText) {
        return tmpEditText.getText().toString().trim().length() == 3;
    }

    private boolean filterLongEnough(EditText amountEditText) {
        return amountEditText.getText().toString().trim().length() > 0;
    }


    private void updateDateLabel() {

        dobYear = myCalendar.get(Calendar.YEAR);
        dobMonth = myCalendar.get(Calendar.MONTH) + 1;
        dobDate = myCalendar.get(Calendar.DAY_OF_MONTH);

        Log.d("le6end4", dobYear + "-" + dobMonth + "-" + dobDate);

        String myFormat = "MM/yyyy"; // In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        manage_payment_month_year.setText(sdf.format(myCalendar.getTime()));
        manage_payment_month_year.setGravity(Gravity.CENTER_VERTICAL);
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    public void clearBackStack() {
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            android.support.v4.app.FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_PAYMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        }

    }
}
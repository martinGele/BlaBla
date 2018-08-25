package com.fragments.RewardFragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.fragments.LoginOptionFragment;
import com.fragments.PostSignUpEmailFragment;
import com.google.android.gms.analytics.Tracker;
import com.postmodels.PromoCodeModel;
import com.responsemodels.PromoCodeResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.util.Utility;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import static com.util.Dialogs.trimMessage;

public class PromoPageFragment extends Fragment implements View.OnClickListener {

    Button submitBtn;
    View view;
    ImageView logoutBtn, imgBack;
    EditText promocodeEdit;
    TextView promocodeEditHint, headerTxt, pageTitleText, contactLinkText;

    private Tracker mTracker;
    private String SCREEN_NAME = "Promo Page Fragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.info_promocode, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {

        setTracker();
        /*
         initiate all the views
         */
        headerTxt = view.findViewById(R.id.text_header);
        promocodeEdit = view.findViewById(R.id.promocode_edit_promo);
        promocodeEditHint = view.findViewById(R.id.promocode_edit_promo_hint);
        submitBtn = view.findViewById(R.id.promocode_image_submit);
        imgBack = view.findViewById(R.id.imgBack);
        pageTitleText = view.findViewById(R.id.promocode_page_title);
        contactLinkText = view.findViewById(R.id.promocode_edit_contactus_link);

        contactLinkText.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        SetTextWatcherForAmountEditView(promocodeEdit);
        submitBtn.setEnabled(false);

        Fonts.fontFinkHeavyRegularTextView(headerTxt,
                (int) (getActivity().getResources().getDimension(R.dimen.header_font_size) /
                        getActivity().getResources().getDisplayMetrics().density),
                AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(promocodeEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(promocodeEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(submitBtn, 18, AppConstants.COLORWHITE, getActivity().getAssets());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.promocode_edit_contactus_link:

                onContactUsCreate();
                break;
            case R.id.promocode_image_submit:
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                imm1.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            promocodeEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }
                String promocode = promocodeEdit.getText().toString();
                if (Engine.isNetworkAvailable(getActivity())) {
                    if (RotiHomeActivity.checkIfLogin()) {
                        submitPromoCodeToServer(promocode);
                    } else {
                        if (RotiHomeActivity.checkIfemailId()) {

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            String email = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINID, "");

                            String password = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINPAS, "");

                            Engine.getInstance().setSetNextPage(AppConstants.showPromoPageFragment);getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                            transaction.addToBackStack(AppConstants.TAG_SIGN);
                            transaction.commit();
                        } else {

                            clearBackStack();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                            Engine.getInstance().setSetNextPage(AppConstants.showPromoPageFragment);
                            transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                            transaction.addToBackStack(AppConstants.TAG_SIGN);
                            transaction.commit();
                        }



                    }
                } else {
                    AppConstants.showMsgDialog("",
                            AppConstants.ERRORNETWORKCONNECTION, getActivity());

                }
                break;
        }

    }

    /*
    make post method to submit the promo code
     */

    private void submitPromoCodeToServer(String promocode) {
        RotiHomeActivity.getProgressDialog().show();

        String authToken = RotiHomeActivity.getPreference().getString(
                AppConstants.PREFAUTH_TOKEN, "");

        String url = getResources().getString(R.string.BASE_URL) + "/promocode";
        Log.d("URL", url);

        PromoCodeModel promoCodeModel = new PromoCodeModel(getResources().getString(R.string.APPKEY), authToken, promocode, "true");
        String json = Gson.getGson().toJson(promoCodeModel);

        Log.d("JSON", json);
        JsonObjectRequest request_json = null;
        try {
            request_json = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            RotiHomeActivity.getProgressDialog().dismiss();
                            Log.d("PROMO", response.toString());
                            PromoCodeResponse promoCodeResponse = Gson.getGson().fromJson(response.toString(), PromoCodeResponse.class);

                            if (!promoCodeResponse.getStatus()) {

                                Dialogs.showMsgDialog("", promoCodeResponse.getNotice(), ((Activity) getContext()));

                            }
                            if (promoCodeResponse.getStatus()) {
                                Dialogs.showMsgDialog("", promoCodeResponse.getNotice(), ((Activity) getContext()));

                                promocodeEdit.setText("");

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
//						submitBtn
//								.setBackgroundResource(R.drawable.submit_button);
                        submitBtn.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//					submitBtn
//							.setBackgroundResource(R.drawable.submit_button_disable);
                    submitBtn.setEnabled(false);
                }
            }

            private boolean filterLongEnough(EditText tmpEditText) {
                return tmpEditText.getText().toString().trim().length() > 0;
            }

        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }

    protected void onContactUsCreate() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:"
                + AppConstants.EMAILFAQ_CONTACT_US + "?subject="
                + AppConstants.EMAILSUBJECTFAQ + "&body="
                + getDeviceName());
        intent.setData(data);
        getActivity().startActivity(intent);

    }

    public String getDeviceName() {
        String texts = "\n\n";

        try {
            PackageInfo pInfo;
            pInfo = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            String androidOS = Build.VERSION.RELEASE;
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            if (model.startsWith(manufacturer)) {
                texts = texts + capitalize(model);
            } else {
                texts = texts + capitalize(manufacturer) + " " + model;
            }
            texts = texts + " " + androidOS + " \nApp Version " + version;
            String email = RotiHomeActivity.getPreference().getString(
                    AppConstants.PREFLOGINID, "");
            if (!email.equals(""))
                texts = texts + "  \nEmail used " + email;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return texts;
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
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
}

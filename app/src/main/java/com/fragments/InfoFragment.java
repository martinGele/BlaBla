package com.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
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
import com.fragments.InfoFragments.ActivateAccountFragment;
import com.fragments.InfoFragments.ChangePasswordFragment;
import com.fragments.InfoFragments.FAQFragment;
import com.fragments.InfoFragments.ReferFriendFragment;
import com.fragments.InfoFragments.TutorialPageFragment;
import com.fragments.InfoFragments.ViewActivityFragment;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.NavigationItemChangedListener;
import com.interfaces.RefreshListner;
import com.payment.ManagePaymentFragment;
import com.responsemodels.ForgotPasswordResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import static com.util.Dialogs.trimMessage;

public class InfoFragment extends Fragment implements View.OnClickListener, RefreshListner {


    View view;
    TextView aboutrotiText, howtogetrewardsText, promocodeText, contactusText, faqText, referfriendText, socializeText, transferCardText, infomainpageText,
            fullwebsiteText, managePaymentMenu, provideFeedbackMenu, activityHistoryMenu, socialShareMenu, updateAccountMenu, logoutMenu, surveyMenu, tutorialMenu, infomainpage_text_activate_account;

    RelativeLayout referFriendBtn, aboutusBtn, tutorialBtn, promocodeBtn, howtogetrewardsBtn, contactusBtn, faqBtn, socializeBtn, transferYakinikuCardBtn, menuBtn, fullWebsiteBtn,
            managePaymentBtn, provideFeedbackBtn, activityHistoryBtn, socialShareBtn, updateAccountBtn, logoutBtn, surveyBtn, activateAccountButton;

    private Tracker mTracker;
    private String SCREEN_NAME = "Info Fragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.infomainpage, container, false);
        inintView(view);
        return view;
    }

    private void inintView(View view) {
        ((NavigationItemChangedListener) getActivity()).onItemChanged(AppConstants.ITEM_MORE);

        setTracker();
        aboutrotiText = view.findViewById(R.id.infomainpage_text_aboutroti);
        howtogetrewardsText = view.findViewById(R.id.infomainpage_text_howtogetrewards);
        promocodeText = view.findViewById(R.id.infomainpage_text_promocode);
        contactusText = view.findViewById(R.id.infomainpage_text_contactus);
        faqText = view.findViewById(R.id.infomainpage_text_faq);
        referfriendText = view.findViewById(R.id.infomainpage_text_referfriend);
        socializeText = view.findViewById(R.id.infomainpage_text_socialize);
        transferCardText = view.findViewById(R.id.settings_text_transfer_yaki_card);
        infomainpageText = view.findViewById(R.id.infomainpage_text_menu);
        fullwebsiteText = view.findViewById(R.id.infomainpage_text_fullwebsite);
        managePaymentMenu = view.findViewById(R.id.infomainpage_text_managepayment);
        provideFeedbackMenu = view.findViewById(R.id.infomainpage_text_provide_feedback);
        activityHistoryMenu = view.findViewById(R.id.infomainpage_text_activity_history);
        socialShareMenu = view.findViewById(R.id.infomainpage_text_social_share);
        updateAccountMenu = view.findViewById(R.id.infomainpage_text_updateaccount);
        logoutMenu = view.findViewById(R.id.infomainpage_text_logout);
        surveyMenu = view.findViewById(R.id.infomainpage_text_survey);
        tutorialMenu = view.findViewById(R.id.infomainpage_text_tutorial);
        infomainpage_text_activate_account = view.findViewById(R.id.infomainpage_text_activate_account);

        referFriendBtn = view.findViewById(R.id.referFriendBtn);
        aboutusBtn = view.findViewById(R.id.aboutusBtn);
        tutorialBtn = view.findViewById(R.id.tutorialBtn);
        promocodeBtn = view.findViewById(R.id.promocodeBtn);
        howtogetrewardsBtn = view.findViewById(R.id.howtogetrewardsBtn);
        contactusBtn = view.findViewById(R.id.contactusBtn);
        faqBtn = view.findViewById(R.id.faqBtn);
        socializeBtn = view.findViewById(R.id.socializeBtn);
        transferYakinikuCardBtn = view.findViewById(R.id.transferYakinikuCardBtn);
        menuBtn = view.findViewById(R.id.menuBtn);
        fullWebsiteBtn = view.findViewById(R.id.fullWebsiteBtn);
        managePaymentBtn = view.findViewById(R.id.managePaymentBtn);
        provideFeedbackBtn = view.findViewById(R.id.provideFeedbackBtn);
        activityHistoryBtn = view.findViewById(R.id.activityHistoryBtn);
        socialShareBtn = view.findViewById(R.id.socialShareBtn);
        updateAccountBtn = view.findViewById(R.id.updateAccountBtn);
        logoutBtn = view.findViewById(R.id.logoutBtn);
        surveyBtn = view.findViewById(R.id.surveyBtn);
        activateAccountButton = view.findViewById(R.id.activateAccountButton);

        /*
        if the user is logged in then the button will not be usable
         */
        if (RotiHomeActivity.checkIfLogin()) {
            activateAccountButton.setVisibility(View.GONE);
        }

        activateAccountButton.setOnClickListener(this);
        referFriendBtn.setOnClickListener(this);
        aboutusBtn.setOnClickListener(this);
        tutorialBtn.setOnClickListener(this);
        promocodeBtn.setOnClickListener(this);
        howtogetrewardsBtn.setOnClickListener(this);
        contactusBtn.setOnClickListener(this);
        faqBtn.setOnClickListener(this);
        socializeBtn.setOnClickListener(this);
        transferYakinikuCardBtn.setOnClickListener(this);
        menuBtn.setOnClickListener(this);
        fullWebsiteBtn.setOnClickListener(this);
        managePaymentBtn.setOnClickListener(this);
        provideFeedbackBtn.setOnClickListener(this);
        activityHistoryBtn.setOnClickListener(this);
        socialShareBtn.setOnClickListener(this);
        updateAccountBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        surveyBtn.setOnClickListener(this);
        provideFeedbackMenu.setOnClickListener(this);
        transferCardText.setOnClickListener(this);


        /*
         * AppConstants.fontLSTKClabolTextView(InfomainpageRelativeHeaderText,
         * 20, AppConstants.COLORWHITERGB, mHomePage.getAssets());
         */
        Fonts.fontRobotoCodensedBoldTextView(fullwebsiteText, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(infomainpageText, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(transferCardText, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(aboutrotiText, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(howtogetrewardsText, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(promocodeText, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(contactusText, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(faqText, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(referfriendText, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(socializeText, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(managePaymentMenu, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(provideFeedbackMenu, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(activityHistoryMenu, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(socialShareMenu, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(updateAccountMenu, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(logoutMenu, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(surveyMenu, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(tutorialMenu, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(infomainpage_text_activate_account, 18, AppConstants.COLORWHITE, getActivity().getAssets());


        refreshView();

    }

    private void refreshView() {

        closeKeyboard();

        if (RotiHomeActivity.checkIfLogin()) {

            logoutMenu.setText(getActivity().getString(R.string.LOGOUT));
        } else {
            logoutMenu.setText(getActivity().getString(R.string.LOGIN));


        }

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (v.getId()) {

            case R.id.tutorialBtn:
                transaction.replace(R.id.home_linear_container, new TutorialPageFragment(), AppConstants.TAG_MORE);
                transaction.addToBackStack(AppConstants.TAG_MORE);
                transaction.commit();

                break;

            case R.id.managePaymentBtn:
                if (RotiHomeActivity.checkIfLogin()) {
                    Engine.getInstance().setSetNextPage(AppConstants.showInfoFragment);

                    transaction.replace(R.id.home_linear_container, new ManagePaymentFragment(), AppConstants.TAG_PAYMENT);
                    transaction.addToBackStack(AppConstants.TAG_PAYMENT);
                    transaction.commit();
                } else {
                    if (RotiHomeActivity.checkIfemailId()) {

                        String email = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINID, "");

                        String password = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINPAS, "");

                        Engine.getInstance().setSetNextPage(AppConstants.showInfoFragment);
                        getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    } else {


                        clearBackStack();
                        Engine.getInstance().setSetNextPage(AppConstants.showInfoFragment);
                        transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    }

                }
                break;

            case R.id.updateAccountBtn:

                if (RotiHomeActivity.checkIfLogin()) {
                    Engine.getInstance().setSetNextPage(AppConstants.showInfoFragment);

                    transaction.replace(R.id.home_linear_container, new ChangePasswordFragment(), AppConstants.TAG_MORE);
                    transaction.addToBackStack(AppConstants.TAG_MORE);
                    transaction.commit();
                } else {

                    if (RotiHomeActivity.checkIfemailId()) {

                        String email = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINID, "");

                        String password = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINPAS, "");

                        Engine.getInstance().setSetNextPage(AppConstants.showChangePasswordFragment);
                        getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    } else {

                        clearBackStack();
                        Engine.getInstance().setSetNextPage(AppConstants.showChangePasswordFragment);
                        transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    }


                }
                break;

            case R.id.infomainpage_text_provide_feedback:

                if (RotiHomeActivity.checkIfLogin()) {

                    HomeFragment homeFragment = new HomeFragment();

                    homeFragment.pullSurveyAsyncTask();

                }

                break;

            case R.id.settings_text_transfer_yaki_card:

                break;
            case R.id.referFriendBtn:

                if (RotiHomeActivity.checkIfLogin()) {
                    Engine.getInstance().setSetNextPage(AppConstants.showInfoFragment);
                    transaction.replace(R.id.home_linear_container, new ReferFriendFragment(), AppConstants.TAG_MORE);
                    transaction.addToBackStack(AppConstants.TAG_MORE);
                    transaction.commit();
                } else {

                    if (RotiHomeActivity.checkIfemailId()) {

                        String email = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINID, "");

                        String password = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINPAS, "");

                        Engine.getInstance().setSetNextPage(AppConstants.showReferFriendFragment);
                        getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    } else {

                        clearBackStack();
                        Engine.getInstance().setSetNextPage(AppConstants.showReferFriendFragment);
                        transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    }


                }


                break;

            case R.id.aboutusBtn:

                break;
            case R.id.faqBtn:

                transaction.replace(R.id.home_linear_container, new FAQFragment(), AppConstants.TAG_MORE);
                transaction.addToBackStack(AppConstants.TAG_MORE);
                transaction.commit();


                break;

            case R.id.activityHistoryBtn:
                if (RotiHomeActivity.checkIfLogin()) {
                    transaction.replace(R.id.home_linear_container, new ViewActivityFragment(), AppConstants.TAG_MORE);
                    transaction.addToBackStack(AppConstants.TAG_MORE);
                    transaction.commit();
                } else {

                    if (RotiHomeActivity.checkIfemailId()) {

                        String email = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINID, "");

                        String password = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINPAS, "");

                        Engine.getInstance().setSetNextPage(AppConstants.showViewActivityFragment);
                        getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    } else {

                        clearBackStack();
                        Engine.getInstance().setSetNextPage(AppConstants.showViewActivityFragment);
                        transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    }
                }

                break;

            case R.id.fullWebsiteBtn:

                break;

            case R.id.howtogetrewardsBtn:

                break;
            case R.id.promocodeBtn:


                break;

            case R.id.contactusBtn:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:" + AppConstants.EMAILFAQ_CONTACT_US
                        + "?subject=" + AppConstants.EMAILSUBJECTFAQ + "&body="
                        + getDeviceName());
                intent.setData(data);
                getActivity().startActivity(intent);


                break;

            case R.id.socialShareBtn:

                break;

            case R.id.socializeBtn:

                break;

            case R.id.logoutBtn:

                if (RotiHomeActivity.checkIfLogin()) {
                    logoutAccount();

                } else {
                    if (RotiHomeActivity.checkIfemailId()) {

                        String email = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINID, "");

                        String password = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINPAS, "");

                        Engine.getInstance().setSetNextPage(AppConstants.showInfoFragment);
                        getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    } else {

                        clearBackStack();
                        Engine.getInstance().setSetNextPage(AppConstants.showInfoFragment);
                        transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    }


                }

                break;

            case R.id.activateAccountButton:

                Engine.getInstance().setSetNextPage(AppConstants.showInfoFragment);
                transaction.replace(R.id.home_linear_container, new ActivateAccountFragment(), AppConstants.TAG_MORE);
                transaction.addToBackStack(AppConstants.TAG_MORE);
                transaction.commit();
                break;

        }

    }

    private void closeKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    @Override
    public void notifyRefresh(String className) {
        refreshView();
    }


    public void logoutAccount() {
        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");


        String url = getResources().getString(R.string.BASE_URL) + "/user/logout?" + "auth_token=" + authToken + "&appkey=" + getResources().getString(R.string.APPKEY);
        RotiHomeActivity.getProgressDialog().show();
        final JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        RotiHomeActivity.getProgressDialog().dismiss();
                        Log.d("Logout response", response.toString());

                        ForgotPasswordResponse forgotPasswordResponse = Gson.getGson().fromJson(response.toString(), ForgotPasswordResponse.class);

                        if (forgotPasswordResponse.getStatus()) {
                            /*
                            set the button to be visible after logut
                             */
                            activateAccountButton.setVisibility(View.VISIBLE);

                            SharedPreferences.Editor prefsEditor = RotiHomeActivity.getPreferenceEditor();
                            prefsEditor.putString(AppConstants.PREFLOGINID, "");
                            prefsEditor.putBoolean(AppConstants.PREFLOGOUTBUTTONTAG, true);
                            prefsEditor.putString(AppConstants.PREFAUTH_TOKEN, "");
                            prefsEditor.putString(AppConstants.PREFLOGINPAS, "");
                            prefsEditor.commit();

                            Dialogs.showMsgDialog("", forgotPasswordResponse.getNotice(), (getContext()));

                            refreshView();


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

                            Dialogs.showMsgDialog("", String.valueOf(AppConstants.BLANKMESSAGEREPLACEMENT), getContext());


                    }
                }

            }
        });

        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request_json.setRetryPolicy(policy);


        /*
         add the request object to the queue to be executed
         */
        VolleyControllerAndTracker.getInstance().addToRequestQueue(request_json);


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

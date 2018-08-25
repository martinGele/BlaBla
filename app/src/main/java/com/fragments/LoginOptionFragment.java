package com.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.google.android.gms.analytics.Tracker;
import com.util.AppConstants;
import com.util.Fonts;
import com.volleyandtracker.VolleyControllerAndTracker;

public class LoginOptionFragment extends Fragment implements View.OnClickListener {

    Button signupFB, signupEmail, loginBtn;
    TextView weclomeText, privacyText, termsText, returningusersText, howToTransferPointText, termofuse, and, privacy, signuptermstextText;

    private Tracker mTracker;
    private String SCREEN_NAME = "Login Option Fragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.info_loginoption, container, false);

        initView(view);
        return view;
    }

    /*
    get all the views in the fragment and cast them
     */
    private void initView(View view) {
        setTracker();
        /*
        find views by id
         */
        signupFB = view.findViewById(R.id.loginoption_image_facebook);
        signupEmail = view.findViewById(R.id.loginoption_image_signup);
        loginBtn = view.findViewById(R.id.loginoption_image_login);
        weclomeText = view.findViewById(R.id.loginoption_text_welcome);
        privacyText = view.findViewById(R.id.loginoption_text_privacy);


        termsText = view.findViewById(R.id.loginoption_text_terms);

        returningusersText = view.findViewById(R.id.loginoption_text_returningusers);
        howToTransferPointText = view.findViewById(R.id.howToTransferPointText);
        termofuse = view.findViewById(R.id.termTxt);
        termofuse.setPaintFlags(termofuse.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        and = view.findViewById(R.id.andTxt);
        privacy = view.findViewById(R.id.privacyTxt);
        privacy.setPaintFlags(privacy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        signuptermstextText = view.findViewById(R.id.loginoption_text_signuptermstext);
        /*
          get the click listiners
         */
        signupFB.setOnClickListener(this);
        signupEmail.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        weclomeText.setOnClickListener(this);
        privacyText.setOnClickListener(this);
        termsText.setOnClickListener(this);
        termofuse.setOnClickListener(this);
        privacy.setOnClickListener(this);

        /*
        setup views text size font and color
         */
        Fonts.fontLSTKClabolTextView(weclomeText, 26, AppConstants.COLORWHITEFH, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(signuptermstextText, 14, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(returningusersText, 18, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontBaileySanITCStdBookRegularTextView(howToTransferPointText, 14, AppConstants.COLORWHITEFH, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(termofuse, 14, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(and, 14, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(privacy, 14, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(signupFB, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(signupEmail, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(loginBtn, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        String TIPS_HTML_TEXT = "By tapping on \"Sign up with Facebook\", \"Sign up with email\", or \"Sign in\" above you are indicating that you have read and agree to the<br />"
                + "<font size=\"2\" color=\"fffff\" </font>";
        signuptermstextText.setText(Html.fromHtml(TIPS_HTML_TEXT));
        signuptermstextText.setLinkTextColor(Color.parseColor("#fec10d"));
        signuptermstextText.setMovementMethod(LinkMovementMethod.getInstance());
        privacyText.setVisibility(View.GONE);
        termsText.setVisibility(View.GONE);


    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (v.getId()) {

            case R.id.loginoption_image_facebook:

                clearBackStack();
                transaction.replace(R.id.home_linear_container, new FbSignUpFragment(), AppConstants.TAG_SIGN);
                transaction.addToBackStack(AppConstants.TAG_SIGN);
                transaction.commit();

                break;

            case R.id.loginoption_image_signup:

                clearBackStack();
                transaction.replace(R.id.home_linear_container, new SignUpFragment(), AppConstants.TAG_SIGN);
                transaction.addToBackStack(AppConstants.TAG_SIGN);
                transaction.commit();

                break;
            case R.id.loginoption_image_login:
                clearBackStack();
                transaction.replace(R.id.home_linear_container, new SignInFragment(), AppConstants.TAG_SIGN);
                transaction.addToBackStack(AppConstants.TAG_SIGN);
                transaction.commit();

                break;


            case R.id.termTxt:

                clearBackStack();
                transaction.replace(R.id.home_linear_container, new TermsOfUseFragment(), AppConstants.TAG_SIGN);
                transaction.addToBackStack(AppConstants.TAG_SIGN);
                transaction.commit();


                break;


            case R.id.privacyTxt:

                clearBackStack();
                transaction.replace(R.id.home_linear_container, new PrivacyPolicyFragment(), AppConstants.TAG_SIGN);
                transaction.addToBackStack(AppConstants.TAG_SIGN);
                transaction.commit();

                break;

        }
    }


    public void clearBackStack() {
        android.support.v4.app.FragmentManager fragmentManag = getFragmentManager();
        if (fragmentManag.getBackStackEntryCount() > 0) {

            fragmentManag.popBackStack(AppConstants.TAG_SIGN, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }

    }

    public void clearBackStackSign() {
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
        }
    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }
}

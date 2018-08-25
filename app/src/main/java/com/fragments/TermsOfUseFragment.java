package com.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.RefreshListner;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.volleyandtracker.VolleyControllerAndTracker;

public class TermsOfUseFragment extends Fragment implements RefreshListner {

    public String classNames = this.getClass().getSimpleName();


    String URL;
    private WebView webView;
    TextView headerText;

    private Tracker mTracker;
    private String SCREEN_NAME = "Terms of use Fragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.info_termsofusage, container, false);

        URL = getResources().getString(R.string.URL_TERMS_OF_USE);
        initView(view);
        return view;
    }

    private void initView(View view) {
        setTracker();

        webView = view.findViewById(R.id.termsofusage_browser_webview);
        headerText = view.findViewById(R.id.text_header);
        /*
        setup custom size of the text view with getting the dimensions from the main activity
         */
        Fonts.fontFinkHeavyRegularTextView(headerText, (int) (getActivity().getResources().getDimension(R.dimen.header_font_size) / getActivity().getResources().getDisplayMetrics().density), AppConstants.COLORWHITE,
                getActivity().getAssets());


         /*
         setup the header text on the webview
          */
        if (URL.contains("terms_of_use"))
            headerText.setText("TERMS OF USE");
        else
            headerText.setText("PRIVACY POLICY");

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        if (getActivity() != null
                && RotiHomeActivity.getProgressDialog() != null
                && !RotiHomeActivity.getProgressDialog()
                .isShowing())
            RotiHomeActivity.getProgressDialog().show();

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("", "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i("", "Finished loading URL: " + url);
                if (getActivity() != null && RotiHomeActivity.getProgressDialog() !=
                        null
                        && RotiHomeActivity.getProgressDialog().isShowing()) {
                    RotiHomeActivity.getProgressDialog().dismiss();
                }
                if (getActivity() != null
                        && RotiHomeActivity.getProgressDialog() != null
                        && RotiHomeActivity.getProgressDialog()
                        .isShowing())
                    RotiHomeActivity.getProgressDialog().dismiss();

            }


            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Log.i("", "onReceivedError loading URL: " + "");
                if (getActivity() != null && RotiHomeActivity.getProgressDialog() !=
                        null
                        && RotiHomeActivity.getProgressDialog().isShowing()) {
                    RotiHomeActivity.getProgressDialog().dismiss();
                }
                if (getActivity() != null
                        && RotiHomeActivity.getProgressDialog() != null
                        && RotiHomeActivity.getProgressDialog()
                        .isShowing())
                    RotiHomeActivity.getProgressDialog().dismiss();
            }
        });
        refreshView();
    }

    /*
    this method with the website is called every time there is a refresh on the fragment called
     */
    private void refreshView() {
        if (Engine.isNetworkAvailable(getActivity())) {
            /*
            this URL contains terms of use from relevant
             */
            webView.loadUrl(URL);
        } else {
            Dialogs.showMsgDialog("",
                    AppConstants.ERRORNETWORKCONNECTION, getActivity());
        }
    }

    @Override
    public void notifyRefresh(String className) {

        if (className.equalsIgnoreCase(classNames))
            refreshView();
    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }

}
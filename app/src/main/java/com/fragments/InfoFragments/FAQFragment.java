package com.fragments.InfoFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.google.android.gms.analytics.Tracker;
import com.util.AppConstants;
import com.util.Fonts;
import com.volleyandtracker.VolleyControllerAndTracker;
import com.widgets.CustomProgressDialog;

import java.lang.reflect.Method;

public class FAQFragment extends Fragment implements View.OnClickListener {

    View view;
    private CustomProgressDialog progressDialog;
    TextView faqEmail;
    RelativeLayout emailBar;

    private WebView webView;
    private Tracker mTracker;
    private String SCREEN_NAME = "FAQ Fragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_faq, container, false);
        inintView(view);
        return view;
    }

    private void inintView(View view) {

        setTracker();
        faqEmail = view.findViewById(R.id.faqEmail);
        emailBar = view.findViewById(R.id.emailBar);
        emailBar.setOnClickListener(this);


        Fonts.fontRobotoCodensedBoldTextView(faqEmail, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");

        webView = view.findViewById(R.id.faq_webview);
//		 webView.setBackgroundColor(Color.TRANSPARENT);
//		 webView.setVisibility(View.GONE);
//		 loadWebPages(webView, AppConstants.URL_FAQ);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        try {
            Method m = WebSettings.class.getMethod("setDomStorageEnabled",
                    new Class[]{boolean.class});
            m.invoke(webView.getSettings(), true);
        } catch (Exception e) {
        }
        settings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");

        //settings.setRenderPriority(RenderPriority.HIGH);
        //settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            public void onPageFinished(WebView view, String url) {
                Log.i("", "Finished loading URL: " + url);
                if (RotiHomeActivity.getProgressDialog() != null && RotiHomeActivity.getProgressDialog().isShowing()) {
                    webView.setVisibility(View.VISIBLE);

                    RotiHomeActivity.getProgressDialog().dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Log.i("", "onReceivedError loading URL: " + "");
                if (RotiHomeActivity.getProgressDialog() != null && RotiHomeActivity.getProgressDialog().isShowing()) {
                    RotiHomeActivity.getProgressDialog().dismiss();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                if (RotiHomeActivity.getProgressDialog() != null
                        && !RotiHomeActivity.getProgressDialog().isShowing()) {
                    RotiHomeActivity.getProgressDialog().show();
                }
            }
        });
        refreshView();


    }

    private void refreshView() {

        webView.loadUrl(getResources().getString(R.string.URL_FAQ));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.emailBar:

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:"
                        + AppConstants.EMAILFAQ_CONTACT_US + "?subject="
                        + AppConstants.EMAILSUBJECTFAQ + "&body="
                        + getDeviceName());
                intent.setData(data);
                getActivity().startActivity(intent);
                break;

        }

    }


    public class GetWebChromClient extends WebChromeClient {

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            // Always grant permission since the app itself requires location
            // permission and the user has therefore already granted it
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }

    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(AppConstants.CONSTANTTITLEMESSAGE)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    result.confirm();
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    result.cancel();
                                }
                            }).create().show();

            return true;
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin,
                                                       final GeolocationPermissions.Callback callback) {
            // TODO Auto-generated method stub
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            Log.i("TAG", "onGeolocationPermissionsShowPrompt()");

            final boolean remember = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Locations");
            builder.setMessage("Would like to use your Current Location ")
                    .setCancelable(true)
                    .setPositiveButton("Allow",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // origin, allow, remember
                                    callback.invoke(origin, true, remember);
                                }
                            })
                    .setNegativeButton("Don't Allow",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // origin, allow, remember
                                    callback.invoke(origin, false, remember);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

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

}

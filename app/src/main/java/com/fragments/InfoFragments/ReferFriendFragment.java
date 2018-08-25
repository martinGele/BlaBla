package com.fragments.InfoFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.responsemodels.ReferalProgramResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Fonts;
import com.util.Gson;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import static com.util.Dialogs.trimMessage;

public class ReferFriendFragment extends Fragment implements View.OnClickListener {

    View view;
    private TextView referFriendText, connectWithFriends, shareYourCode, codeRefer, codeCopied;
    private ImageView shareMailImage;
    private LinearLayout copyBackgound;

    private Tracker mTracker;
    private String SCREEN_NAME = "Refer a Friend Fragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.refer_friend, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {

        setTracker();
        fetchReferralRequestServer();
        /*
        get all the views
         */
        referFriendText = view.findViewById(R.id.referFriendText);
        connectWithFriends = view.findViewById(R.id.connect_with_friends);
        shareYourCode = view.findViewById(R.id.share_your_code);
        codeRefer = view.findViewById(R.id.code);
        codeCopied = view.findViewById(R.id.code_copied_to_clipboard);
        copyBackgound = view.findViewById(R.id.copy_background);
        shareMailImage = view.findViewById(R.id.shareMailLayout);

        /*
        setup the font's and colors
         */

        Fonts.fontFinkHeavyRegularTextView(codeRefer, 30, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(codeCopied, 17, AppConstants.COLORSOMEWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(referFriendText, 30, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(connectWithFriends, 20, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedRegularTextView(shareYourCode, 15, AppConstants.COLORWHITE, getActivity().getAssets())
        ;

        boolean isTutorialShown = RotiHomeActivity.getPreference()
                .getBoolean(AppConstants.PREF_REFER_FRIEND_ISNOTOPENSTARTPAGE, false);

        if (!isTutorialShown) {
            SharedPreferences.Editor edit = RotiHomeActivity.getPreferenceEditor();
            edit.putBoolean(AppConstants.PREF_REFER_FRIEND_ISNOTOPENSTARTPAGE, true);
            edit.commit();
        }
        /*
        setup click listiners
         */
        copyBackgound.setOnClickListener(this);
        shareMailImage.setOnClickListener(this);


    }

    protected void onContactUsCreate() {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getDeviceName());
        getActivity().startActivity(Intent.createChooser(emailIntent, "Email"));
    }

    public String getDeviceName() {
        String texts = "\n\n";

        try {
            PackageInfo pInfo;
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
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
            String email = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINID, "");
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

    static AlertDialog.Builder alertDialogBuilder;

    public void showShareMsgDialog(String title, final String message, Context context) {
        try {
            if (alertDialogBuilder == null) // {
                alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            alertDialogBuilder = null;
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            if (title.equals("")) {
                alert.setTitle(AppConstants.CONSTANTTITLEMESSAGE);
                alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
            } else {
                alert.setTitle(title);
                alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
            }
            alert.show();
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    volley call to get the referral code
     */
    private void fetchReferralRequestServer() {
        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");

        String url = getResources().getString(R.string.BASE_URL) + "/referral/email?" + "auth_token=" + authToken + "&appkey=" + getResources().getString(R.string.APPKEY);
        RotiHomeActivity.getProgressDialog().show();
        final JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        RotiHomeActivity.getProgressDialog().dismiss();
                        Log.d("Referral code", response.toString());

                        final ReferalProgramResponse referalProgramResponse = Gson.getGson().fromJson(response.toString(), ReferalProgramResponse.class);


                        if (referalProgramResponse.getStatus()) {
                            try {
                                String referal = referalProgramResponse.getReferralCode();
                                codeRefer.setText(referal);


                                shareMailImage.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        Uri data;
                                        data = Uri.parse("mailto:?subject=" + referalProgramResponse.getEmailTitle()
                                                + "&body=" + referalProgramResponse.getEmailBody());
                                        intent.setData(data);
                                        getActivity().startActivity(intent);

                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Error: ", error.getMessage());

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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.copy_background:
                codeCopied.setText(R.string.CODE_COPIED_TO_CLIPBOARD);
                String stringYouExtracted = codeRefer.getText().toString();

                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity()
                        .getSystemService(getActivity().CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text",
                        stringYouExtracted);

                clipboard.setPrimaryClip(clip);


                break;

            case R.id.shareMailLayout:

                break;
        }

    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }
}

package com.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ak.app.wetzel.activity.BaseActivity;
import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pushnotifications.PushNotificationDialog;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import java.util.UUID;

import static com.util.AppConstants.PREFAUTH_TOKEN;
import static com.util.AppConstants.PREFLOGINID;
import static com.util.AppConstants.PREFLOGOUTBUTTONTAG;

public class Engine {

    public AlertDialog.Builder alertDialogBuilder;
    private String messageShow;
    private static Engine instance = null;

    public static Engine getInstance() {
        if (instance == null) {
            instance = new Engine();
        }
        return instance;
    }





    public static boolean CheckEnableGPS(Context context) {
        boolean isGPSEnabled = false;
        try {
            String provider = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.equals("")) {
                isGPSEnabled = true;// GPS Enabled
            } else {
                isGPSEnabled = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isGPSEnabled;
    }

    public static Context context;
    private String openPage;
    private String setNextPage = "";

    public String androidOS = Build.VERSION.RELEASE;
    public String model = Build.MODEL;
    public String manufacturer = Build.MANUFACTURER;

    public static Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }


    /*
    with this method i'am checking if network is available
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean isNetworkEnable = false;
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
                    || connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState() == NetworkInfo.State.CONNECTING) {
                isNetworkEnable = true;
            } else if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                    || connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .getState() == NetworkInfo.State.CONNECTING) {
                isNetworkEnable = true;
            } else
                isNetworkEnable = false;
        } catch (Exception e) {
            isNetworkEnable = false;
            e.printStackTrace();
        }
        return isNetworkEnable;
    }


    public static boolean isApplicationSentToBackground(final Context context) {
        return BaseActivity.isInBackGround();
    }


    public static void showDialog(final Activity activity, final PushNotificationDialog dialog) {
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (activity instanceof BaseActivity) {
                    ((BaseActivity) activity).showDialog(dialog);
                }
            }
        });
    }

    public void showMsgDialog(String title, final String message, final Activity context) {

        try {

            if (alertDialogBuilder == null) {
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alertDialogBuilder = null;
                                dialog.cancel();
                                setMessageShow("");
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                if (!title.equals("")) {
                    alert.setTitle(title);
                }
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMessageShow() {
        return messageShow;
    }

    public void setMessageShow(String messageShow) {
        this.messageShow = messageShow;
    }


    /*
         with this method i am checking the device
     */

    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getDeviceID(Context context) {
        String deviceId = "";
        try {
            final TelephonyManager tm = (TelephonyManager) context
                    ./* getBaseContext(). */getSystemService(Context.TELEPHONY_SERVICE);

            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            deviceId = deviceUuid.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceId;
    }

    public String getSetNextPage() {
        return setNextPage;
    }

    public void setSetNextPage(String setNextPage) {
        this.setNextPage = setNextPage;
    }

    public void setOpenPage(String openPage) {
        this.openPage = openPage;
    }


    /*
    logut the user from anywhere from this method
     */
    public static void logoutUser(String url, final String setNextPage) {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("getgiftcard", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        SharedPreferences.Editor prefsEditor = RotiHomeActivity.getPreferenceEditor();
                        prefsEditor.putString(PREFLOGINID, "");
                        prefsEditor.putBoolean(PREFLOGOUTBUTTONTAG, true);
                        prefsEditor.putString(PREFAUTH_TOKEN, "");
                        // prefsEditor.putString(CUSTOMER_ID, "");
                        prefsEditor.commit();


                        //TODO make the fragment transaction here

                    }
                }, new Response.ErrorListener() {

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
                                AppConstants.showMsgDialog("", json, ((Activity) getContext()));
                            break;
                        default:
                            AppConstants.showMsgDialog("", getContext().getString(R.string.BLANKMESSAGEREPLACEMENT), (Activity) getContext());
                            break;
                    }
                }

            }
        });
        VolleyControllerAndTracker.getInstance().addToRequestQueue(jsonObjReq, "");


    }

}

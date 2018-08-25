package com.pushnotifications;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import com.util.AppConstants;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Relevant-20 on 8/3/2017.
 */

public class PushNotificationHelper {
    /**
     * instance of Activity.
     */
    private final Activity activity;
    /**
     * 'name',returning a SharedPreferences through which can be retrieve and modify values.
     */
    private static final String SHARED_REGISTRATION = "SHARED_REGISTRATION";
    /**
     * key, the name of the preference to modify.
     */
    public static final String PROPERTY_REG_ID = "registrationId";
    /**
     * key, the name of the preference to modify.
     */
    private static final String PROPERTY_APP_VERSION = "appVersion";
    /**
     * key, the name of the preference to modify.
     */
    private static final String DEVICE_HAS_BEEN_REGISTERED = "registeredDeviceToGmc";
    /**
     * key, the name of the preference to modify.
     */
    private static final String DEVICE_HAS_BEEN_ACTIVATED = "activateDeviceToserver";
    /**
     * time while checking isGooglePlayServicesAvailable.
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    private final String SENDER_ID = AppConstants.PUSH_NOTIFICATION_KEY;

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";
    /**
     * instance of GoogleCloudMessaging.
     */
    private GoogleCloudMessaging gcm;
    /**
     * instance of AtomicInteger.
     */
    private final AtomicInteger msgId = new AtomicInteger();
    /**
     * push notification registration id.
     */
    private String regid;

    /**
     * @param thisActivity instance of current activity.
     */
    public PushNotificationHelper(final Activity thisActivity) {
        this.activity = thisActivity;
    }

    /**
     * register if push notification id is null.
     */
    @SuppressLint("NewApi")
    public final void registration() {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(activity);
            regid = getRegistrationId(activity);

            // if (regid.isEmpty()) {
            registerInBackground();
            // }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }
    /**
     * @return true if Google Play Service is available.
     */
    private boolean checkPlayServices() {
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
//                    }
//                });

            } else {
                Log.i(TAG, "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }
    /**
     * @param context instance of Context.
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(final Context context) {
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (final PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, String, String>() {
            @Override
            protected String doInBackground(final Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(activity);
                    }
                    regid = InstanceID.getInstance(activity)
                            .getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                   // regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over xmpp,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    postData();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(activity, regid);
                } catch (final IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(final String msg) {

            }
        }.execute(null, null, null);

    }
    /**
     * @param context instance of app. Context.
     * @return registration id.
     */
    public final String registrationId(final Context context) {
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            regid = InstanceID.getInstance(activity)
                    .getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
           // regid = gcm.register(SENDER_ID);

            // For this demo: we don't need to send it because the device
            // will send upstream messages to a server that echo back the
            // message using the 'from' address in the message.

            // Persist the regID - no need to register again.
            storeRegistrationId(context, regid);
        } catch (final IOException ex) {
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
        }
        return regid;
    }
    /**
     * @param pushNotificationID    already registered push notification id.
     * @param newPushNotificationID new registration push notification id.
     */
    public final void registerDevice(final String pushNotificationID, final String newPushNotificationID) {
//        final Connection connection = new Connection(activity);
//        connection.execute(RequestType.REGISTER_PUSH_NOTIFICATION.name(), pushNotificationID, newPushNotificationID);
    }
    /**
     * send request to the server for push notification registration by xmpp.
     */
    public final void postData() {
//        final Connection connection = new Connection(activity);
//        connection.execute(RequestType.REGISTER_PUSH_NOTIFICATION.name(), null, regid);
    }
    /**
     * Stores the registration ID and app versionCode in the application's {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    public final void storeRegistrationId(final Context context, final String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        final int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    /**
     * @param thisContext instance of app context.
     *                    method for saving push notification registration.
     */
    public static final void storeFlagHasBeenRegistered(final Context thisContext) {
        final SharedPreferences prefs = getGCMPreferences(thisContext);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(DEVICE_HAS_BEEN_REGISTERED, true);
        editor.commit();
    }
    /**
     * @param thisContext instance of app context.
     *                    method for saving push notification activation.
     */
    public static final void storeFlagHasBeenActivated(final Context thisContext) {
        final SharedPreferences prefs = getGCMPreferences(thisContext);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(DEVICE_HAS_BEEN_ACTIVATED, true);
        editor.commit();
    }
    /**
     * @return push notification registration id.
     */
    public final String getRegID() {
        final SharedPreferences prefs = getGCMPreferences(activity);
        return prefs.getString(PROPERTY_REG_ID, regid);
    }
    /**
     * @return true if push notification is already registered to the server.
     */
    public final boolean isPushNotificationRegistered() {
        final SharedPreferences prefs = getGCMPreferences(activity);
        return prefs.getBoolean(DEVICE_HAS_BEEN_REGISTERED, false);
    }
    /**
     * @return true if push notification is already activated to the server.
     */
    public final boolean isPushNotificationActivated() {
        final SharedPreferences prefs = getGCMPreferences(activity);
        return prefs.getBoolean(DEVICE_HAS_BEEN_ACTIVATED, false);
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @param context instance of app context.
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    @SuppressLint("NewApi")
    public final String getRegistrationId(final Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        final String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        final int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        final int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @param context instance of app context.
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getGCMPreferences(final Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(SHARED_REGISTRATION, Context.MODE_PRIVATE);
    }
}

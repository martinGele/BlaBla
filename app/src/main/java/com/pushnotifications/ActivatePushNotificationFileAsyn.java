package com.pushnotifications;

import android.app.Activity;
import android.os.AsyncTask;

/**
 * Created by Relevant-20 on 8/3/2017.
 */

public class ActivatePushNotificationFileAsyn extends AsyncTask<String, String, String> {
    /**
     * instance of Activity.
     */
    private final Activity activity;

    /**
     * @param thisActivity instance of current activity.
     */
    public ActivatePushNotificationFileAsyn(final Activity thisActivity) {
        this.activity = thisActivity;
    }

    @Override
    protected final String doInBackground(final String... params) {
        final PushNotificationHelper helper = new PushNotificationHelper(activity);
      //  if (helper.isPushNotificationRegistered()) {
            final String pushNotificationID = helper.getRegID();
            if ((pushNotificationID != null) && (pushNotificationID.length() > 0)) {
                final String newPushNotificationID = helper.registrationId(activity);
                if ((newPushNotificationID != null) && !pushNotificationID.equals(newPushNotificationID)) {
                    helper.storeRegistrationId(activity, newPushNotificationID);
                   // helper.registerDevice(pushNotificationID, newPushNotificationID);
                }
//                else if (!helper.isPushNotificationActivated()) {
//                   // helper.registerDevice(pushNotificationID, null);
//                }
            } else {
                helper.registration();
//                final String newPushNotificationID = helper.registrationId(activity);
//                if ((newPushNotificationID != null) && (newPushNotificationID.length() > 0)) {
//                    helper.registerDevice(newPushNotificationID, null);
//                }
            }
//        } else {
//            helper.registration();
//        }
        return null;
    }

}

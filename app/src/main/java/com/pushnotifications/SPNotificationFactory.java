package com.pushnotifications;

import android.content.Context;

import com.interfaces.PushNotificationListener;
import com.util.AppConstants;


/**
 * Created by Relevant-20 on 9/18/2017.
 */

public class SPNotificationFactory {
    public PushNotificationListener getNotification(Context context, String message) {
        PushNotificationListener pushNotificationListener;

        pushNotificationListener = new SPNotificationMessage(context, message, AppConstants.PUSH_NOTIFICATION_TAG);
        return pushNotificationListener;
    }
}

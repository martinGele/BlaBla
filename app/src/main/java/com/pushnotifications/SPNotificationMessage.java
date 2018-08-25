package com.pushnotifications;

import android.content.Context;

import com.interfaces.PushNotificationListener;
import com.util.Engine;


/**
 * Created by Relevant-20 on 9/18/2017.
 */

public class SPNotificationMessage extends SPNotificationModel implements PushNotificationListener {
    public SPNotificationMessage(Context context, String bodyMessage, String title) {
        super(context, bodyMessage, title);
    }

    @Override
    public void showDialog(String message) {
        PushNotificationDialog dialog;
        dialog = PushNotificationDialog.getInstance(getTitle(), getBodyMessage());
        Engine.getInstance().showDialog(getActivity(), dialog);
    }

    @Override
    public void onPushNotification(String message) {

    }
}

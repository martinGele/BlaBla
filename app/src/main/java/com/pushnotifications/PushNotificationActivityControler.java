package com.pushnotifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ak.app.wetzel.activity.BaseActivity;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.util.AppConstants;


/**
 * Created by Relevant-20 on 9/16/2017.
 */

public class PushNotificationActivityControler extends Activity {

    /**
     * Instance of application Context
     */
    public static Context context;

    /**
     * push notification detail body message.
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String messageContent = getIntent().getStringExtra(AppConstants.PUSH_NOTIFICATION_MESSAGE);
        String mFromClassName = getIntent().getStringExtra(AppConstants.PUSH_NOTIFICATION_CLASS);
        int pushNotificationId = getIntent().getIntExtra(String.valueOf(AppConstants.PUSH_NOTIFICATION_ID), 0);
        try {
            if (context != null) {
                Intent intent = new Intent(this, context.getClass());
                intent = ((BaseActivity) context).getIntent();
                intent.putExtra(AppConstants.PUSH_NOTIFICATION_MESSAGE, messageContent);
                intent.putExtra(AppConstants.PUSH_NOTIFICATION_CLASS, mFromClassName);
                intent.putExtra(String.valueOf(AppConstants.PUSH_NOTIFICATION_ID), pushNotificationId);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), RotiHomeActivity.class);
                intent.putExtra(AppConstants.PUSH_NOTIFICATION_MESSAGE, messageContent);
                intent.putExtra(AppConstants.PUSH_NOTIFICATION_CLASS, mFromClassName);
                intent.putExtra(String.valueOf(AppConstants.PUSH_NOTIFICATION_ID), pushNotificationId);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }
}

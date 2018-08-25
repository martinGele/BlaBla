package com.pushnotifications;


/**
 * Created by Relevant-16 on 9/16/2017.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ak.app.wetzel.activity.BaseActivity;
import com.ak.app.wetzel.activity.R;
import com.util.AppConstants;
import com.util.Engine;

import java.security.SecureRandom;


/**
 * Created by Relevant-20 on 9/16/2017.
 */

public class GcmIntentService extends JobIntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    private String messageContent = "";
    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1021;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, GcmIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i("SimpleJobIntentService", "Executing work: " + intent);
        messageContent = intent.getStringExtra("alert");
        if (messageContent != null && !messageContent.equals("")) {
            if (Engine.isApplicationSentToBackground(getApplicationContext())) {
                sendNotification(getApplicationContext(), messageContent);
            } else {
                BaseActivity.setMessage(getApplicationContext(), messageContent, 0);
            }
        }
    }

    private void sendNotification(Context applicationContext, String message) {
        final int id = new SecureRandom().nextInt(Integer.MAX_VALUE);
        Intent pushIntent = null;
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
            nm.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }
        pushIntent = startMyMessageScreen(message, id);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                pushIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        int icon;
        icon = R.drawable.icon25;
        builder = builder
                .setSmallIcon(icon)
                .setContentTitle(AppConstants.PUSH_NOTIFICATION_TAG)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);
        nm.notify(0, builder.build());
    }

    private Intent startMyMessageScreen(String message, int id) {
        Intent pushIntent = null;
        pushIntent = new Intent(this, PushNotificationActivityControler.class);
        pushIntent.putExtra(AppConstants.PUSH_NOTIFICATION_MESSAGE, message);
        pushIntent.putExtra(AppConstants.PUSH_NOTIFICATION_CLASS, "C2DMRECEIVER");
        pushIntent.putExtra(String.valueOf(AppConstants.PUSH_NOTIFICATION_ID), id);
        pushIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return pushIntent;
    }
}
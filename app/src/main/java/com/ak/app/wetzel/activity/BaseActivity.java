package com.ak.app.wetzel.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.interfaces.PushNotificationListener;
import com.pushnotifications.ActivatePushNotificationFileAsyn;
import com.pushnotifications.PushNotificationActivityControler;
import com.pushnotifications.PushNotificationDialog;
import com.pushnotifications.SPNotificationFactory;
import com.util.AppConstants;
import com.util.Engine;

import io.fabric.sdk.android.Fabric;

public class BaseActivity extends AppCompatActivity {

    // keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    // keep track of cropping intent
    final int PIC_CROP = 2;


    // Uri selectedImageUri;
    public boolean isFromCamera = false;
    private static final int SELECT_PICTURE = 90;


    // camera instances
    private static byte[] bytes;
    private static byte[] bytesSecond;

    public static NotificationManager nm;
    boolean isPhotoBoothOpened = false;
    public static boolean isPhotoBoothPreviewOpened = false;

    private String messageContent;
    private String mFromClassName;
    private int pushNotificationId;


    public static BaseActivity activity;

    public static BaseActivity getInstance() {
        return activity;
    }

    private SharedPreferences.Editor prefsEditor;


    public void setIsFromCamera(boolean b) {
        isFromCamera = b;
    }

    public boolean isFromCamera() {
        return isFromCamera;
    }


    public void openPhotoBooth() {
        isPhotoBoothOpened = true;
    }

    public void closePhotoBooth() {
        isPhotoBoothOpened = false;
    }

    public static void openPhotoBoothPreview() {
        isPhotoBoothPreviewOpened = true;
    }

    public void closePhotoBoothPreview() {
        isPhotoBoothPreviewOpened = false;
    }

    public static byte[] getBytesSecond() {
        return bytesSecond;
    }

    public static void setBytesSecond(byte[] bytesSecond) {
        BaseActivity.bytesSecond = bytesSecond;
    }

    public static byte[] getBytes() {
        return bytes;
    }

    public static void setBytes(byte[] bytes) {
        BaseActivity.bytes = bytes;
    }


    public static void setIsInBackGround(boolean isInBackGround) {
        BaseActivity.isInBackGround = isInBackGround;
    }

    public static boolean isInBackGround() {
        return isInBackGround;
    }

    public static boolean isInBackGround = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());


        activity = this;

        Engine.getInstance().setContext(activity);
        new ActivatePushNotificationFileAsyn(activity).execute();
        handlePushNotification();

    }

    private void handlePushNotification() {
        messageContent = getIntent().getStringExtra(AppConstants.PUSH_NOTIFICATION_MESSAGE);
        mFromClassName = getIntent().getStringExtra(AppConstants.PUSH_NOTIFICATION_CLASS);
        pushNotificationId = getIntent().getIntExtra(String.valueOf(AppConstants.PUSH_NOTIFICATION_ID), 0);
        if (mFromClassName != null && mFromClassName.equalsIgnoreCase("C2DMRECEIVER") && messageContent != null
                && !messageContent.equalsIgnoreCase("")) {
            setMessage(getApplicationContext(), messageContent, pushNotificationId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        PushNotificationActivityControler.context = this;
        isInBackGround = false;
        Engine.getInstance().setContext(activity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isInBackGround = true;
        PushNotificationActivityControler.context = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PushNotificationActivityControler.context = null;
    }


    public static void setMessage(final Context context, String message, int pushNotificationId) {
        if (message != null && !message.equals("")) {
            clearNotificationStatus(pushNotificationId);
            SPNotificationFactory notificationFactory = new SPNotificationFactory();
            PushNotificationListener notification = notificationFactory.getNotification(activity, message);
            notification.showDialog(message);
        }
    }


    public static void clearNotificationStatus(int pushNotificationId) {
        try {

            nm.cancel(AppConstants.PUSH_NOTIFICATION_TAG, pushNotificationId);
            nm.cancelAll();
//            prefsEditor.putString(AppConstants.PUSH_NOTIFICATION_MESSAGE, "");
//            prefsEditor.putString(AppConstants.PUSH_NOTIFICATION_CLASS, "");
//            prefsEditor.commit();
        } catch (Exception e) {
        }
    }

    public void showDialog(PushNotificationDialog dialog) {
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();


    }


}

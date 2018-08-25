package com.pushnotifications;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;


import com.util.AppConstants;


public class PushNotificationDialog extends DialogFragment {
    public static final String BUNDLE_MESSAGE = "BUNDLE_MESSAGE";
    public static final String BUNDLE_TITLE = "BUNDLE_TITLE";
    private View layout;
    protected final int gravity = Gravity.BOTTOM | Gravity.FILL_HORIZONTAL;

    public PushNotificationDialog() {

    }

    public static PushNotificationDialog getInstance(final String title, final String message) {
        final PushNotificationDialog info = new PushNotificationDialog();
        final Bundle infoBundle = new Bundle();
        infoBundle.putString(BUNDLE_TITLE, title);
        infoBundle.putString(BUNDLE_MESSAGE, message);
        info.setArguments(infoBundle);
        return info;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String message = getArguments().getString(BUNDLE_MESSAGE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(AppConstants.PUSH_NOTIFICATION_TAG);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        return alertDialogBuilder.create();
    }
}

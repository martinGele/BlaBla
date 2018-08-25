package com.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.ak.app.wetzel.activity.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Dialogs {

    static AlertDialog.Builder alertDialogBuilder;

    public static void showMsgDialog(String title, String message, Context context) {

        try {
            if (alertDialogBuilder == null) {

                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialogBuilder = null;
                        dialog.cancel();


                    }
                });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();

                if (title.equals("")) {

                    alert.setTitle(R.string.app_name);

                        alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
                } else {
                    alert.setTitle(title);
                    alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
                }

                alert.show();

            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public static void showMsgDialogAppConst(String title, final String message, Context context) {
        try {
            if (alertDialogBuilder == null) {
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
                    alert.setIcon(android.app.AlertDialog.BUTTON_NEGATIVE);
                } else {
                    alert.setTitle(title);
                    alert.setIcon(android.app.AlertDialog.BUTTON_NEUTRAL);
                }
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showMessageDialogWithNewIntent(String message, Context context) {
        if (alertDialogBuilder == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            alertDialogBuilder = null;
                            dialog.cancel();
                            // HomePage.getInstance().showLoginOptionPage(
                            // false);//TODO
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }


    private static boolean isConfirm = false;

    public static boolean showConfirmMsgDialog(String title, final String message, final String billid,
                                               Context context) {
        isConfirm = false;
        try {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
            alt_bld.setMessage(message).setCancelable(false)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            isConfirm = false;
                            dialog.cancel();
                        }
                    }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    isConfirm = true;
                    dialog.cancel();
                    // new
                    // deleteBillFromServer().execute(billid);
                }
            });
            AlertDialog alert = alt_bld.create();
            // alert.setTitle(title);
            alert.setTitle(AppConstants.CONSTANTTITLEMESSAGE);
            alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isConfirm;
    }


    public static String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

}

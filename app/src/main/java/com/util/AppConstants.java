package com.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Base64;
import android.util.Log;
import android.view.Window;

import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AppConstants {

    public RotiHomeActivity rotiHomeActivity;

    public static AlertDialog.Builder alertDialogBuilder;


    public static final String ITEM_HOME = "itemHome";
    public static final String ITEM_GIFT = "itemGift";
    public static final String ITEM_REWARDS = "itemRewards";
    public static final String ITEM_FIND = "itemFind";
    public static final String ITEM_MORE = "itemMore";
    public static final String TAG_LOGIN_SIGNUP = "tagLoginSignup";
    public static final String TAG_PAYMENT = "tagManagePayment";
    public static final String TAG_ACCOUNT_VERIFIED = "tagAccountVerified";
    public static final String TAG_FAVORITE_STORE = "tagFavoriteStore";
    public static final String TAG_ACCOUNT = "tagAccount";
    public static final String TAG_CAMERA = "tagCamera";
    public static final String TAG_POST_SIGN_IN = "tagPostSignIn";
    public static final String TAG_CARD_SUMMARY = "tagCardSummary";
    public static final String TAG_PHOTO = "tagPhoto";


    /*
    open fragments from main activity
     */
    public static final String showAddPhotoPage = "showAddPhotoPage";
    public static final String showLoginOptionPage = "showLoginOptionPage";
    public static final String showHomeFragment = "showHomeFragment";
    public static final String showAddCurrencyFragment = "showAddCurrencyFragment";
    public static final String showSignInFragment = "showSignInFragment";
    public static final String showRewardsFragment = "showRewardsFragment";
    public static final String showGiftCardStartFragment = "showGiftCardStartFragment";
    public static final String showSnapSpecifyFragment = "showSnapSpecifyFragment";
    public static final String showInfoFragment = "showInfoFragment";
    public static final String showManagePaymentFragment = "showManagePaymentFragment";
    public static final String showChangePasswordFragment = "showChangePasswordFragment";
    public static final String showReferFriendFragment = "showReferFriendFragment";
    public static final String giftCardSendDetail = "giftCardSendDetail";
    public static final String showRewardPageFragment = "showRewardPageFragment";
    public static final String showPromoPageFragment = "showPromoPageFragment";
    public static final String showViewActivityFragment = "showViewActivityFragment";


    /*
     color constants
     */

    public static final String COLORKATHTHI = "5f3032";
    public static final String COLORLOCATIONKATHTHI = "5a2a2b";
    public static final String COLORABOUTRED = "902e26";
    public static final String COLORABOUTKATHTHI = "2d0a0a";
    public static final String COLORGREEN = "87c748";
    public static final String COLORGREY = "7d7d7d";
    public static final String COLORGREYGKK = "bababa";
    /* public static final String COLORORANGE = "f58022"; */
    public static final String COLORDARKGREEB = "458a2a";
    public static final String COLORDARKGREY = " 464646t";
    public static final String COLORORANGEFH = "EEA717";
    public static final String COLORWHITEFH = "F5F5F5";
    public static final String COLORGREYFH = "A6A6A6";
    public static final String COLORCREAM = "FFEFD1";
    public static final String COLORREDMAROONFH = "8C0204";
    public static final String COLORDARKCREAM = "DFBB77";
    public static final String COLORYELLOW = "FFBD14";
    public static final String COLORORANGEGKK = "E98C27";
    public static final String COLORGREYB1 = "B1B1B1";
    public static final String COLORGREY99 = "999999";

    public static final String COLORWHITE = "FFFFFF";
    public static final String COLORBLUE = "004694";
    public static final String COLORORANGE = "e86724";
    public static final String COLORBG = "55c2e8";
    public static final String COLORSOMEWHITE = "019fe0";


    public static final String COLORORANGETEXTRGB = "DF821D";
    public static final String COLORBROWNTEXTRGB = "3F2513";
    public static final String COLORDARKGRAYRGB = "666666";
    public static final String COLORWHITERGB = "FFFFFF";
    public static final String COLORBLACKRGB = "000000";
    public static final String COLORGRAY = "636363";
    public static final String COLORRED = "EC1C24";
    public static final String COLORBLACKFH = "202020";
    public static final String COLORBLURBLACKFH = "303030";
    public static final String COLORCREAMGKK = "FCFCF5";
    // INTENT_EXTRA CONSTATNS

    public static final String INTENT_EXTRA_HOME_TABNUMBER = "homepage.tab.number";
    public static final String INTENT_EXTRA_HOME_ISFROMTAB = "homepage.tab.isfromTab";

    // INTENT_EXTRA CONSTATNS


    public static String TAG_SIGN = "tagSign";
    public static String TAG_GIFT = "tagGift";
    public static String TAG_REWARD = "tagReward";
    public static String TAG_FIND = "tagFind";
    public static String TAG_MORE = "tagMore";


    // PREF CONSTATNS

    public static final String PREF_HOME_ISOPENHOMEPAGE = "PREFHOMEPAGE_ISOPEN";
    public static final String PREF_SNAP_ISNOTOPENSTARTPAGE = "PREFSNAPSTART_ISOPEN";
    public static final String IS_ORDER_ONLINE_POPUPOPEN = "IS_ORDER_ONLINE_POPUPOPEN";
    public static final String IS_LOCATION_PAGE_OPEN = "IS_LOCATION_PAGE_OPEN";
    public static final String PREF_REFER_FRIEND_ISNOTOPENSTARTPAGE = "PREFREFERFRIENDSTART_ISOPEN";
    public static final String IS_OFFERING_TUTORIAL_POP_UP_HAS_SHOWN = "IS_OFFERING_TUTORIAL_POP_UP_HAS_SHOWN";
    public static final String IS_IT_GIFT_FIRST_TIME = "IS_IT_GIFT_FIRST_TIME";
    public static final String IS_IT_ORDER_FIRST_TIME = "IS_IT_ORDER_FIRST_TIME";

    // PREF CONSTATNS

    // DIALOG CONSTATNS
    public static final String TAG_APP = "RotiTAG ** ";
    public static final int DIALOG_ALERT = 101;
    public static final int DIALOG_PROGRESS = 105;
    public static String PROGRESS_MSG = null;
    public static String DIALOG_MSG = "";
    public static String CONNECTION_FAILURE = "A connection failure occurred";
    public static String ClaimRewardPageHeader = "Are you done?";
    public static String ClaimRewardPageMessage = "Do NOT exit this page until the cashier has seen your 3-digit code. \n\n"
            + "Tapping \"Continue\" will return you to My Goddies. You will not be able to access this code again.";
    public static String TIME_OUT = "The request timed out";
    public static final String DEVICE_TYPE = "android";
    public static final String REGISTERTYPE = "1";
    public static final String REGISTERTYPEFB = "2";

    public static final String EMAILCONTACT_US = "wetzels@rlvt.net";
    public static final String EMAILFAQ_CONTACT_US = "wetzels@rlvt.net";
    public static final String EMAILSUBJECT = "Wetzel\'s Pretzels";
    public static final String EMAILSUBJECTFAQ = "Wetzel\'s Pretzels";
    public static final String EMAILSUBJECTCONTACTUS = "Wetzel\'s customer query";
    public static String EMAILSUBJECT1 = "Wetzel\'s";
    public static String EMAILBODY = "Wetzel\'s";
    public static String EMAILSUBJECTFB = "Wetzel\'s";
    public static String EMAILBODYFB = "Wetzel\'s";
    public static String EMAILSUBJECTTWT = "Wetzel\'s";
    public static String EMAILBODYTWT = "Wetzel\'s";


    public static String CONSTANTTITLEMESSAGE =
            String.valueOf(R.string.app_name);
    public static final String ERRORLOCATIONSERVICES = "Please Turn On Location Services (in Settings) to allow "
            + CONSTANTTITLEMESSAGE + " to reward you!";


    public static final String ERROR401SERVICES = "Please login with different id";
    public static String ERROR401 = "";

    public static final String ERRORNETWORKCONNECTION = "Could not connect to server, please check your network connection";
    public static final String ERRORGOALCOMPLETE = "Mark this goal as complete?";
    // public static final String BLANKMESSAGEREPLACEMENT =
    // "We are sorry. Something went wrong. Please try again. If problem persists,
    // contact customer support from the info section of the app for further
    // assistance.";
    public static final String BLANKMESSAGEREPLACEMENT = "We are sorry! Something went wrong. Please contact customer support from the info section of the app for further assistance.";

    // PUSH PARAMS

    public static final String PUSH_NOTIFICATION_KEY = "389484218706";
    public static final String PUSH_NOTIFICATION_TAG = CONSTANTTITLEMESSAGE + " Message";
    public static final int PUSH_NOTIFICATION_ID = 1234;
    public static final String PUSH_NOTIFICATION_MESSAGE = "packageName.push.message";
    public static final String PUSH_NOTIFICATION_CLASS = "packageName.push.classname";

    // FACEBOOK PARAMS

    public static final String FACEBOOK_APPID = "500373520130644";
    public static final String[] FACEBOOK_PERMISSIONARR = new String[]{
            /* "read_stream", */"email" /*
										 * , "user_photos", "publish_checkins", "publish_stream", "offline_access",
										 * "photo_upload"
										 */};
    public static String FB_APPID = "500373520130644"; // "353154718074522";
    public static final String POSTID = "postid";


    public static String URL_INSTAGRAM_PAGE = "http://trelevant.herokuapp.com/url/36/instagram";

    public static String URL_MENU = "http://trelevant.herokuapp.com/url/36/menu";
    public static String URL_WEBSITE = "http://trelevant.herokuapp.com/url/36/full-website";

    public static String CALL_US_NOW_CELL = "CALL_US_NOW_CELL_IDENTIFIER";
    public static String OFFER_LIST_CELL = "OFFER_LIST_CELL_IDENTIFIER";
    public static String CALL_US_NOW_CUSTOM_LABEL_NAME = "1";
    public static String CALL_US_NOW_CUSTOM_LABEL_DESCRIPTION = "2";
    public static String CALL_US_NOW_CUSTOM_LABEL_DISTANCE = "3";
    public static String CALL_US_NOW_CUSTOM_BUTTON_MOBILE = "4";

    public static boolean IS_IT_LAUNCH_FIRST_TIME = true;

    // PREFERENCE VARIABLE

    public static String PREFAUTH_TOKEN = "authtoken";
    public static String PREFPUSHREGISTRATIONID = "registrationId";
    public static String PREFLOGOUTBUTTONTAG = "loginbuttonTag";
    public static String FROMFBSIGNUP = "fbLoggingSignUp";
    public static String PREFCATEGORYSELECTED = "categoryselected";
    public static String PREFLOGINID = "prefloginid";
    public static String PREFLOGINPAS = "preflogindpas";
    public static String PHONE_NUMB = "phone_number";

    public static String USER_RELEVANT_ID = "user_relevant_id";
    public static String PREFGIFT_CARD = "giftcard_code";

    // METHODS


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            // getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            // Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }


    public static Boolean check = false;

    public static void parseInput(String result, Activity mHomePage, String s) {
        if (result != null && !result.equals("")) {
            boolean hasNotice = false;
            boolean hasMessage = false;
            boolean hasError = false;
            try {

                if (result.equals(""))
                    return;
                JSONObject resObject = new JSONObject(result);
                String sucess = "";// resObject.getString("status");
                String notice = "";
                if (resObject.has("status"))
                    sucess = resObject.getString("status");
                if (resObject.has("notice")) {
                    hasNotice = true;
                    notice = resObject.getString("notice");
                    if (!notice.equals("") && notice.equals("Unauthorized API request.")) {
                        // mHomePage.showLoginOptionPage(false);//TODO
                        return;
                    }
                }
                if (resObject.has("message")) {
                    hasMessage = true;
                    notice = resObject.getString("message");
                }

                String errors = "";
                String auth_token = "";
                try {
                    if (resObject.has("auth_token")) {
                        auth_token = resObject.getString("auth_token");
                        SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(mHomePage);
                        Editor prefsEditor = mPreference.edit();
                        prefsEditor.putString(AppConstants.PREFAUTH_TOKEN, auth_token);
                        Log.d("auth_token", auth_token);
                        prefsEditor.commit();
                    }
                } catch (Exception e) {
                }
                try {
                    if (resObject.has("errors")) {
                        hasError = true;
                        errors = resObject.getString("errors");
                    }
                } catch (Exception e) {
                }
                if (sucess != null && !sucess.equals("") && !notice.equals("")) {// false
                    if (sucess.equals("true")) {
                        check = true;
                    } else {
                        check = false;
                    }
                    Dialogs.showMsgDialogAppConst(CONSTANTTITLEMESSAGE, notice, mHomePage);

                } else {
                    if (errors != null && (!errors.equals(""))) {
                        Dialogs.showMsgDialogAppConst(CONSTANTTITLEMESSAGE, errors + notice, mHomePage);
                    } else if (hasError || hasMessage || hasNotice)
                        Dialogs.showMsgDialogAppConst(CONSTANTTITLEMESSAGE, BLANKMESSAGEREPLACEMENT, mHomePage);
                }

                // RotiHomeActivity.getInstance().oPenTabView(4);
            } catch (Exception e) {
                e.printStackTrace();

                Dialogs.showMsgDialogAppConst(CONSTANTTITLEMESSAGE, BLANKMESSAGEREPLACEMENT, mHomePage);
            }
        } else {
            Dialogs.showMsgDialogAppConst(CONSTANTTITLEMESSAGE, BLANKMESSAGEREPLACEMENT, mHomePage);
        }
    }


    // Fast Implementation
    public static StringBuilder inputStreamToString(InputStream is) throws IOException {
        String line = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        while ((line = rd.readLine()) != null) {
            total.append(line);
        }
        rd.close();
        return total;
    }

    public static Uri selectedImageUri;
    public static String selectedImagePath;

    public static Bitmap getBitmap(String url, /* String imageView, */
                                   String type) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(new FlushedInputStream(is));
            if (type.equals("0")/* == 0 */)
                saveImageToSdCard(url);
            bis.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return bm;
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            try {
                while (totalBytesSkipped < n) {
                    long bytesSkipped = in.skip(n - totalBytesSkipped);
                    if (bytesSkipped == 0L) {
                        int b = read();
                        if (b < 0) {
                            break; // we reached EOF
                        } else {
                            bytesSkipped = 1; // we read one byte
                        }
                    }
                    totalBytesSkipped += bytesSkipped;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return totalBytesSkipped;
        }
    }

    private static void saveImageToSdCard(String imageView) {
        InputStream input = null;
        try {
            input = new URL((String) imageView).openStream();
            File storagePath = Environment.getExternalStorageDirectory();
            String path = storagePath.getAbsolutePath() + "/wetzels";
            storagePath = new File(path);
            if (!storagePath.exists())
                storagePath.mkdir();
            File imageFile = new File(storagePath.getAbsolutePath() + "/daily.png");
            if (imageFile.exists())
                imageFile.delete();
            OutputStream output = new FileOutputStream(new File(storagePath, "daily.png"));
            try {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
            } finally {
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Date makeDate(String dateString) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(dateString);
    }

    public static int DateDiff(Date date1, Date date2) {
        int diffDay = diff(date1, date2);
        System.out.println("Different Day : " + diffDay);
        return diffDay--;
    }

    private static int diff(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(date1);
        c2.setTime(date2);
        int diffDay = 0;

        if (c1.before(c2)) {
            diffDay = countDiffDay(c1, c2);
        } else {
            diffDay = countDiffDay(c2, c1);
        }

        return diffDay;
    }

    private static int countDiffDay(Calendar c1, Calendar c2) {
        int returnInt = 0;
        while (!c1.after(c2)) {
            c1.add(Calendar.DAY_OF_MONTH, 1);
            returnInt++;
        }
        if (returnInt > 0) {
            returnInt = returnInt - 1;
        }
        return (returnInt);
    }

    private static int DEFAULT_BRIGHTNESS_VALUE = -1;
    // Content resolver used as a handle to the system's settings
    private static ContentResolver cResolver;
    // Window object, that will store a reference to the current window
    private static Window window;

    public static void changeScreenBrightness(Activity object) {
        // Get the content resolver
        cResolver = object.getContentResolver();

        // Get the current window
        window = object.getWindow();

        try {
            // Get the current system brightness
            DEFAULT_BRIGHTNESS_VALUE = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {
            // Throw an error case it couldn't be retrieved
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

        if (DEFAULT_BRIGHTNESS_VALUE < 200) {
            // Set the system brightness using the brightness variable value
            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 200);
        }
    }

    public static void reduceScreenBrightness(Activity object) {
        if (DEFAULT_BRIGHTNESS_VALUE > -1) {
            // Get the content resolver
            cResolver = object.getContentResolver();

            // Get the current window
            window = object.getWindow();

            // Set the system brightness using the brightness variable value
            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, DEFAULT_BRIGHTNESS_VALUE);
        }
    }


    public static void showMsgDialog(String title, final String message, Context context) {
        try {
            if (alertDialogBuilder == null) {
                alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alertDialogBuilder = null;
                                dialog.cancel();
                                /*
                                 * if (check == true) { if (PromoPage.getInstance().isPromoPage == true) {
                                 * Info.getInstance() .handleBackButton(); PromoPage.getInstance().isPromoPage =
                                 * false; } }
                                 */
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                if (title.equals("")) {
                    alert.setTitle(CONSTANTTITLEMESSAGE);
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


    // ANALYTICS
    // //
    // PARAMS
	/*
	set this to volley callback
	 */

//
//	public static class logoutAccount extends AsyncTask<String, Void, String> {
//		private String nextView = "";
//
//		@Override
//		protected String doInBackground(String... params) {
//			nextView = params[0];
//			String authToken = Tabbars.getInstance().getPreference().getString(PREFAUTH_TOKEN, "");
//			String result = WebHTTPMethodClass.httpGetService("/user/logout",
//					"auth_token=" + authToken + "&appkey=" + R.string.APPKEY);
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//
//			if (result != null && !result.equals("")) {
//				try {
//					JSONObject resObject = new JSONObject(result);
//					String sucess = resObject.getString("status");
//					if (sucess != null && !sucess.equals("") && sucess.equals("true")) {
//						Editor prefsEditor = Tabbars.getInstance().getPreferenceEditor();
//						prefsEditor.putString(PREFLOGINID, "");
//						prefsEditor.putBoolean(PREFLOGOUTBUTTONTAG, true);
//						prefsEditor.putString(PREFAUTH_TOKEN, "");
//						// prefsEditor.putString(CUSTOMER_ID, "");
//						prefsEditor.commit();
//						if (nextView.equals("showRewardPage")) {
//							Rewards.getInstance().clearViewStack();
//							Rewards.getInstance().setNextViewName(nextView);
//							Rewards.getInstance().showLoginOptionPage(false, "REWARDS");
//						}
//						if (nextView.equals("showHomePage")) {
//							RotiHomeActivity.getInstance().setNextViewName(nextView);
//							RotiHomeActivity.getInstance().showLoginOptionPage(false, "ROTIHOMEACTIVITY");
//						}
//						showMsgDialog("", ERROR401SERVICES, Tabbars.getInstance());
//						ERROR401 = "";
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}


}


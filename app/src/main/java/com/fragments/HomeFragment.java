package com.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.Tracker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.interfaces.NavigationItemChangedListener;
import com.interfaces.RefreshListner;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.responsemodels.FetchUserCodeResponse;
import com.responsemodels.FetchUserGiftCardCodeResponse;
import com.responsemodels.FetchUserResponse;
import com.responsemodels.ForgotPasswordResponse;
import com.responsemodels.GetGiftCardResponse;
import com.responsemodels.SurveyResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.util.Dialogs.trimMessage;

public class HomeFragment extends Fragment implements RefreshListner, Animation.AnimationListener {


    public static HomeFragment home;

    public static HomeFragment getInstance() {

        return home;
    }

    private Animation animation1;
    private Animation animation2;
    private Animation animation3;
    private Animation animation4;
    private boolean isBackOfMeterShowing = true;
    private boolean isBackOfCardShowing = true;

    private Tracker mTracker;
    private String SCREEN_NAME = "Home page";

    LinearLayout cardImgBack, meterImgBack, homeCardBtnRefresh;

    TextView greetingText, profileName, meterTitle, userCode, homeCardBtnRefreshText, meterTogo;

    private int default_milestonePoints = 6;
    private int points = 0;
    private float balance = 0;
    private String giftCard = "";

    Button btnBalance;
    ProgressBar qrPb;

    RelativeLayout barcodeLayout;

    ImageView btn_profile_photo, backedImg, cardImg, meterImg, homeCardBtnRefreshImg, qrCode;
    ImageLoader imageLoader;
    ImageLoaderConfiguration imageLoaderConfig;


    View bgMainPB;

    int I = 2; //seconds
    int TimeCounter = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.splash, container, false);
        initView(view);
        return view;
    }


    /*cardImgBack
    method that contains the whole view of the fragment
     */

    private void initView(final View view) {

        ((NavigationItemChangedListener) getActivity()).onItemChanged(AppConstants.ITEM_HOME);

        setTracker();
        setTracker();
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        imageLoaderConfig = new ImageLoaderConfiguration.Builder(getActivity())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        /*
        get the views by id
         */
        //  bgMainPB = view.findViewById(R.id.bgMainPB);
        greetingText = view.findViewById(R.id.home_greeting);
        profileName = view.findViewById(R.id.home_profile_name);
        backedImg = view.findViewById(R.id.backed_img);
        btn_profile_photo = view.findViewById(R.id.home_profile_photo);

        cardImg = view.findViewById(R.id.cardImg);
        cardImgBack = view.findViewById(R.id.cardImgBack);

        meterTitle = view.findViewById(R.id.home_meter_title);
        meterTogo = view.findViewById(R.id.home_meter_togo);
        userCode = view.findViewById(R.id.userCode);
        qrPb = view.findViewById(R.id.QRpb);
        qrCode = view.findViewById(R.id.QRCode);
        barcodeLayout = view.findViewById(R.id.barcodeLayout);
        homeCardBtnRefresh = view.findViewById(R.id.home_card_refresh);
        homeCardBtnRefreshText = view.findViewById(R.id.home_card_btn_refresh);
        homeCardBtnRefreshImg = view.findViewById(R.id.home_card_refresh_img);
        btnBalance = view.findViewById(R.id.btnBalance);


        meterImg = view.findViewById(R.id.meterImg);
        meterImgBack = view.findViewById(R.id.meterImgBack);
        meterImgBack.setVisibility(View.GONE);
        /*
        animations for the buttons
         */


        final AnimationDrawable animFire;
        final ImageView imgFire = view.findViewById(R.id.simple_anim);

        animFire = (AnimationDrawable) imgFire.getDrawable();

        cardImgBack = (LinearLayout) view.findViewById(R.id.cardImgBack);
        cardImgBack.setVisibility(View.GONE);

        animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.to_middle);
        animation1.setAnimationListener((Animation.AnimationListener) this);

        animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.from_middle);
        animation2.setAnimationListener((Animation.AnimationListener) this);

        animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.to_middle);
        animation3.setAnimationListener((Animation.AnimationListener) this);

        animation4 = AnimationUtils.loadAnimation(getActivity(), R.anim.from_middle);
        animation4.setAnimationListener((Animation.AnimationListener) this);


        /*
        set the font and the color of the textviews
         */

        Fonts.fontFinkHeavyRegularTextView(meterTitle, 70, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(meterTogo, 30, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(btnBalance, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedRegularTextView(homeCardBtnRefreshText, 18, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedRegularTextView(userCode, 16, AppConstants.COLORBLACKRGB, getActivity().getAssets());
        Fonts.fontRobotoCodensedRegularTextView(greetingText, 18, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(profileName, 18, AppConstants.COLORBLUE, getActivity().getAssets());


        meterTitle.setText("" + default_milestonePoints);

        btnBalance.setText("Balance $" + String.format("%.2f", balance));


        backedImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imgFire.setVisibility(View.VISIBLE);
                final Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        Log.d("le6end4", "play anim");
                        animFire.start();
                    }
                };

                imgFire.post(run);

                TimeCounter = 0;

                final Timer t = new Timer();
                t.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Log.d("le6end4", "TimeCounter:" + TimeCounter);

                                if (TimeCounter == I) {
                                    t.cancel();

                                    imgFire.removeCallbacks(run);
                                    imgFire.setVisibility(View.GONE);
                                    return;
                                }

                                TimeCounter++;
                            }
                        });
                    }
                }, 0, 1000);
            }
        });

        btnBalance.setOnClickListener(new View.OnClickListener() {

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            @Override
            public void onClick(View v) {
                if (Engine.isNetworkAvailable(getActivity())) {
                    if (RotiHomeActivity.checkIfLogin()) {
                        clearBackStack();
                        transaction.replace(R.id.home_linear_container, new AddCurrencyFragment(), AppConstants.ITEM_HOME);
                        transaction.addToBackStack(AppConstants.ITEM_HOME);
                        transaction.commit();
                    } else {

                        if (RotiHomeActivity.checkIfemailId()) {

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            String email = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINID, "");

                            String password = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINPAS, "");

                            Engine.getInstance().setSetNextPage(AppConstants.showAddCurrencyFragment);
                            getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                            transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                            transaction.addToBackStack(AppConstants.TAG_SIGN);
                            transaction.commit();
                        } else {
                            clearBackStack();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                            Engine.getInstance().setSetNextPage(AppConstants.showAddCurrencyFragment);
                            transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                            transaction.addToBackStack(AppConstants.TAG_SIGN);
                            transaction.commit();
                        }


                    }
                }
            }

        });


        btn_profile_photo.setOnClickListener(new View.OnClickListener() {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            @Override
            public void onClick(View v) {
                if (RotiHomeActivity.checkIfLogin()) {
                    clearBackStack();
                    transaction.replace(R.id.home_linear_container, new AddPhotoFragment(), AppConstants.TAG_PHOTO);
                    transaction.addToBackStack(AppConstants.TAG_PHOTO);
                    transaction.commit();
                } else {
                    if (RotiHomeActivity.checkIfemailId()) {

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        String email = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINID, "");

                        String password = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINPAS, "");

                        Engine.getInstance().setSetNextPage(AppConstants.showAddPhotoPage);
                        getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    } else {
                        clearBackStack();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        Engine.getInstance().setSetNextPage(AppConstants.showAddPhotoPage);
                        transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    }


                }

            }
        });
        meterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                meterImgBack.setEnabled(false);

                meterImg.clearAnimation();
                meterImg.setAnimation(animation1);
                meterImg.startAnimation(animation1);
            }
        });

        meterImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                meterImg.setEnabled(false);

                meterImgBack.clearAnimation();
                meterImgBack.setAnimation(animation1);
                meterImgBack.startAnimation(animation1);
            }
        });

        cardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RotiHomeActivity.checkIfLogin()) {
                    v.setEnabled(false);
                    cardImgBack.setEnabled(false);

                    cardImg.clearAnimation();
                    cardImg.setAnimation(animation3);
                    cardImg.startAnimation(animation3);
                } else {

                    if (RotiHomeActivity.checkIfemailId()) {

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        String email = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINID, "");
                        String password = RotiHomeActivity.getPreference().getString(AppConstants.PREFLOGINPAS, "");
                        Engine.getInstance().setSetNextPage(AppConstants.showHomeFragment);
                        getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    } else {
                        ((NavigationItemChangedListener) getActivity()).onItemChanged(AppConstants.ITEM_HOME);
                        clearBackStack();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        Engine.getInstance().setSetNextPage(AppConstants.showHomeFragment);
                        transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    }


                }
            }
        });

        cardImgBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                cardImg.setEnabled(false);

                cardImgBack.clearAnimation();
                cardImgBack.setAnimation(animation3);
                cardImgBack.startAnimation(animation3);
            }
        });


        homeCardBtnRefresh.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (Engine.isNetworkAvailable(getActivity())) {

                    qrCode.setVisibility(View.GONE);
                    qrPb.setVisibility(View.VISIBLE);
                    userCode.setText("");

                    if (balance > 0)
                        fetchUserGiftCardCode();

                    else
                        fetchUserCode();
                } else {
                    Dialogs.showMsgDialog("", AppConstants.CONNECTION_FAILURE, getActivity());
                }

            }

        });

        refreshView();

    }


    private void refreshView() {


        closeSoftKeyBoard();
//        bgMainPB.setVisibility(View.VISIBLE);
        setActionStatus(false);
        /*
         * 0:00 to 11:59 : Good Morning! 12:00 to 17:00 : Good Afternoon! 17:01 to 23:59
         * Good Evening!
         */
        balance = 0;
        btnBalance.setText("Balance $" + String.format("%.2f", balance));
        // btnBalance.setEnabled(false);

        isBackOfMeterShowing = true;
        isBackOfCardShowing = true;

        Calendar mCalCurrent = Calendar.getInstance();
        Calendar mCalMorningStart = Calendar.getInstance();
        Calendar mCalMorningEnd = Calendar.getInstance();
        Calendar mCalAfternoonStart = Calendar.getInstance();
        Calendar mCalAfternoonEnd = Calendar.getInstance();
        Calendar mCalEveningStart = Calendar.getInstance();
        Calendar mCalEveningEnd = Calendar.getInstance();

        // set morning range
        mCalMorningStart.set(Calendar.HOUR_OF_DAY, 0);
        mCalMorningStart.set(Calendar.MINUTE, 0);
        mCalMorningStart.set(Calendar.SECOND, 0);
        mCalMorningEnd.set(Calendar.HOUR_OF_DAY, 11);
        mCalMorningEnd.set(Calendar.MINUTE, 59);
        mCalMorningEnd.set(Calendar.SECOND, 0);
        // set afternoon range
        mCalAfternoonStart.set(Calendar.HOUR_OF_DAY, 12);
        mCalAfternoonStart.set(Calendar.MINUTE, 0);
        mCalAfternoonStart.set(Calendar.SECOND, 0);
        mCalAfternoonEnd.set(Calendar.HOUR_OF_DAY, 17);
        mCalAfternoonEnd.set(Calendar.MINUTE, 0);
        mCalAfternoonEnd.set(Calendar.SECOND, 0);
        // set evening range
        mCalEveningStart.set(Calendar.HOUR_OF_DAY, 17);
        mCalEveningStart.set(Calendar.MINUTE, 0);
        mCalEveningStart.set(Calendar.SECOND, 1);
        mCalEveningEnd.set(Calendar.HOUR_OF_DAY, 23);
        mCalEveningEnd.set(Calendar.MINUTE, 59);
        mCalEveningEnd.set(Calendar.SECOND, 0);

        String greetingStr = "";
        if (mCalCurrent.after(mCalMorningStart) && mCalCurrent.before(mCalMorningEnd))
            greetingStr = "Good Morning";
        else if (mCalCurrent.after(mCalAfternoonStart) && mCalCurrent.before(mCalAfternoonEnd))
            greetingStr = "Good Afternoon";
        else if (mCalCurrent.after(mCalEveningStart) && mCalCurrent.before(mCalEveningEnd))
            greetingStr = "Good Evening";

        greetingText.setText(greetingStr);
        profileName.setText(""); // Hello!

        if (Engine.isNetworkAvailable(getActivity())) {
            if (RotiHomeActivity.checkIfLogin()) {
                Log.d("le6end4", "IS_IT_LAUNCH_FIRST_TIME:" + AppConstants.IS_IT_LAUNCH_FIRST_TIME);
                if (AppConstants.IS_IT_LAUNCH_FIRST_TIME) {
                    AppConstants.IS_IT_LAUNCH_FIRST_TIME = false;
                    pullSurveyAsyncTask();
                } else
                    fetchUserProfile();

            } else {
//                bgMainPB.setVisibility(View.VISIBLE);
                setActionStatus(true);


                // btnBalance.setEnabled(true);
            }
        } else {
//            bgMainPB.setVisibility(View.GONE);
            setActionStatus(true);

            Dialogs.showMsgDialog("", AppConstants.ERRORNETWORKCONNECTION, getActivity());


        }

    }


    private void setActionStatus(boolean status) {
        btnBalance.setEnabled(status);
        cardImg.setEnabled(status);
        meterImg.setEnabled(status);
        backedImg.setEnabled(status);
    }

    private void closeSoftKeyBoard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(btnBalance.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    private void setHomeCardBtnRefresh(boolean state) {
        homeCardBtnRefresh.setEnabled(state);

        if (state) {
            homeCardBtnRefresh.setBackgroundResource(R.drawable.btn_rounded_orange);

            homeCardBtnRefreshImg.setImageResource(R.drawable.refresh_img);

            Fonts.fontRobotoCodensedRegularTextView(homeCardBtnRefreshText, 18, AppConstants.COLORORANGE,
                    getActivity().getAssets());
        } else {
            homeCardBtnRefresh.setBackgroundResource(R.drawable.btn_rounded_grey);

            homeCardBtnRefreshImg.setImageResource(R.drawable.refresh_img_disabled);

            Fonts.fontRobotoCodensedRegularTextView(homeCardBtnRefreshText, 18, AppConstants.COLORGRAY,
                    getActivity().getAssets());
        }
    }


    public void animEnd(Animation animation) {
        if (animation == animation1) {

            if (isBackOfMeterShowing) {
                meterImg.setVisibility(View.GONE);
                meterImgBack.setVisibility(View.VISIBLE);

                meterImgBack.clearAnimation();
                meterImgBack.setAnimation(animation2);
                meterImgBack.startAnimation(animation2);

                isBackOfMeterShowing = !isBackOfMeterShowing;
            } else {
                meterImgBack.setVisibility(View.GONE);
                meterImg.setVisibility(View.VISIBLE);

                meterImg.clearAnimation();
                meterImg.setAnimation(animation2);
                meterImg.startAnimation(animation2);

                isBackOfMeterShowing = !isBackOfMeterShowing;
            }
        } else if (animation == animation2) {
            if (isBackOfMeterShowing)
                meterImg.setEnabled(true);
            else
                meterImgBack.setEnabled(true);
        } else if (animation == animation3) {

            if (isBackOfCardShowing) {
                cardImg.setVisibility(View.GONE);
                cardImgBack.setVisibility(View.VISIBLE);

                userCode.setText("");
                qrCode.setVisibility(View.GONE);
                qrPb.setVisibility(View.VISIBLE);

                cardImgBack.clearAnimation();
                cardImgBack.setAnimation(animation4);
                cardImgBack.startAnimation(animation4);

                isBackOfCardShowing = !isBackOfCardShowing;

                Log.d("le6end4", "balance:" + balance);
                if (balance > 0)
                    fetchUserGiftCardCode();
                else
                    fetchUserCode();
            } else {
                cardImgBack.setVisibility(View.GONE);
                cardImg.setVisibility(View.VISIBLE);

                cardImg.clearAnimation();
                cardImg.setAnimation(animation4);
                cardImg.startAnimation(animation4);

                isBackOfCardShowing = !isBackOfCardShowing;
            }
        } else if (animation == animation4) {
            if (isBackOfCardShowing)
                cardImg.setEnabled(true);
            else
                cardImgBack.setEnabled(true);
        }
    }


    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    private void setDisplayBarcode(String barcodeNumber) {
        try {
            // int width = barcodeLayout.getWidth();
            // int height = Integer.valueOf(width / 2);
            int width = (int) (barcodeLayout.getWidth() * 0.95f);
            int height = (int) (width * 0.2f);
            Bitmap bitmap = encodeAsBitmap(barcodeNumber, BarcodeFormat.CODE_128, width, height);
            qrCode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        qrPb.setVisibility(View.GONE);
        qrCode.setVisibility(View.VISIBLE);
        userCode.setText(barcodeNumber);
    }


    /*
   volley call to fetch user profile
    */
    private void fetchUserProfile() {
//        RotiHomeActivity.getProgressDialog().show();
        String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");


        String url = getResources().getString(R.string.BASE_URL) + "/user/profile?" + "appkey=" + getResources().getString(R.string.APPKEY) + "&auth_token=" + authToken;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("fetchUser", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

//                        bgMainPB.setVisibility(View.GONE);
                        setActionStatus(true);

                        FetchUserResponse user = Gson.getGson().fromJson(response.toString(), FetchUserResponse.class);

                        if (user.getStatus()) {
                            FetchUserResponse.User userObject = user.getUser();
                            String firstname = userObject.getFirstName();
                            String profile_pic_url = String.valueOf(userObject.getProfilePic());

                            if (!profile_pic_url.equalsIgnoreCase("<null>")
                                    && !profile_pic_url.equalsIgnoreCase("null")) {
                                if (imageLoader.isInited())
                                    imageLoader.destroy();

                                imageLoader.init(imageLoaderConfig);
                                imageLoader.displayImage(profile_pic_url, btn_profile_photo);


                            }

                            int milestone_points = userObject.getMilestonePoints();
                            points = userObject.getPoints();

                            if (milestone_points < 0 || milestone_points > 6)
                                milestone_points = 0;

                            if (points < 0 || points > 5)
                                points = 0;

                            int milestonePoints = default_milestonePoints - milestone_points;
                            meterTitle.setText("" + milestonePoints);

                            try {


                                if (firstname.equals("")) {
                                    firstname = ""; // Hello!
                                    profileName.setText(firstname);
                                } else
                                    profileName.setText(firstname + "!");
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }

                            if (milestone_points == 1)
                                meterImg.setImageResource(R.drawable.meter1);
                            else if (milestone_points == 2)
                                meterImg.setImageResource(R.drawable.meter2);
                            else if (milestone_points == 3)
                                meterImg.setImageResource(R.drawable.meter3);
                            else if (milestone_points == 4)
                                meterImg.setImageResource(R.drawable.meter4);
                            else if (milestone_points == 5)
                                meterImg.setImageResource(R.drawable.meter5);
                            else if (milestone_points == 6)
                                meterImg.setImageResource(R.drawable.meter6);
                            else {
                                meterImg.setImageResource(R.drawable.meter0);
                            }

                            if (points == 1)
                                backedImg.setImageResource(R.drawable.main_backing02);
                            else if (points == 2)
                                backedImg.setImageResource(R.drawable.main_backing03);
                            else if (points == 3)
                                backedImg.setImageResource(R.drawable.main_backing04);
                            else if (points == 4)
                                backedImg.setImageResource(R.drawable.main_backing05);
                            else if (points == 5)
                                backedImg.setImageResource(R.drawable.main_backing06);
                            else
                                backedImg.setImageResource(R.drawable.main_backing01);

                            /*
                            pass the url to the volley
                             */
                            try {
                                getGiftCardDetail();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            /*
                             * if (progressDialog != null && progressDialog.isShowing())
                             * progressDialog.dismiss();
                             */
                            setActionStatus(true);
                        }
                    }
                }, new Response.ErrorListener()

        {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("le6end4", "Error: " + error.getMessage());
                Log.d("le6end4", "fetchOrderSiteFromServer Error: " + error.getMessage());
                String json = null;

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            json = Dialogs.trimMessage(json, "message");
                            if (json != null)
                                Dialogs.showMsgDialog("", json, ((Activity) getContext()));
                            break;
                        case 401:


                            logoutAccount();
                            json = new String(response.data);
                            json = Dialogs.trimMessage(json, "message");
                            if (json != null)
                                Dialogs.showMsgDialog("", json, ((Activity) getContext()));
                            break;
                        default:
                            Dialogs.showMsgDialog("", json, ((Activity) getContext()));

                            break;
                    }
                }

            }
        });

        // Adding request to request queue
        VolleyControllerAndTracker.getInstance().addToRequestQueue(jsonObjReq, "");


    }

    /*
    Volley call to get the gift card detail
     */
    private void getGiftCardDetail() throws Exception {
        String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");


        String url = getActivity().getResources().getString(R.string.BASE_URL_GIFTCARD) + "/card/?" + "appkey=" + getResources().getString(R.string.APPKEY) + "&auth_token=" + authToken;


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("getgiftcard", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();


                        GetGiftCardResponse giftCardResponse = Gson.getGson().fromJson(response.toString(), GetGiftCardResponse.class);
                        try {
                            if (giftCardResponse.getStatus()) {
                                giftCard = giftCardResponse.getCardNumber();
                                try {
                                    balance = Float.valueOf(giftCardResponse.getBalance());
                                } catch (Exception e) {
                                    balance = 0;
                                }
                                btnBalance.setText("Balance $" + String.format("%.2f", balance));

                                Log.d("balance", String.valueOf(balance));
                            } else {
                                createDefaultGiftCard();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener()

        {

            @Override
            public void onErrorResponse(VolleyError error) {
                RotiHomeActivity.getProgressDialog().dismiss();
                VolleyLog.d("le6end4", "Error: " + error.getMessage());
                Log.d("le6end4", "fetchOrderSiteFromServer Error: " + error.getMessage());
                String json = null;
                RotiHomeActivity.getProgressDialog().dismiss();


                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            RotiHomeActivity.getProgressDialog().dismiss();

                            json = new String(response.data);
                            json = Dialogs.trimMessage(json, "message");
                            if (json != null)
                                AppConstants.showMsgDialog("", json, ((Activity) getContext()));
                            break;
                        default:
                            RotiHomeActivity.getProgressDialog().dismiss();

                            AppConstants.showMsgDialog("", getContext().getString(R.string.BLANKMESSAGEREPLACEMENT), (Activity) getContext());
                            break;
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appkey", getResources().getString(R.string.APPKEY));
                params.put("auth_token", authToken);

                return params;
            }
        };

        VolleyControllerAndTracker.getInstance().addToRequestQueue(jsonObjReq, "");
    }


    /*
    volley call to pull survey for the user details
    */
    Boolean isGetApiSurvey = true;
    Boolean isPullApiSurvey = true;

    public void pullSurveyAsyncTask() {
        String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");

        String url = getResources().getString(R.string.BASE_URL) + "/survey/pull" + "?appkey=" + getResources().getString(R.string.APPKEY) + "&auth_token=" + authToken;

        RotiHomeActivity.getProgressDialog().show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("PULL SURVEY", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        SurveyResponse surveyResponse = Gson.getGson().fromJson(response.toString(), SurveyResponse.class);
                        try {

                            try {
                                if (surveyResponse.getStatus()) {
                                    Log.i("elang", "elang->1");

                                    //TODO fix this later there should be a fragment here
//                                    RotiHomeActivity.getInstance().showReceiptServeyPage(responseJson.getString("receipt_id"),
//                                            responseJson.getString("survey_id"), responseJson.getString("restaurant_name"),
//                                            responseJson.getString("receipt_date"));
                                } else {
                                    isPullApiSurvey = false;
                                }
                            } catch (Exception e) {
                                Log.i("elang", "elang->2" + e);
                                isPullApiSurvey = false;
                                isGetApiSurvey = false;
                            }

                            if (isGetApiSurvey == false) {
                                isGetApiSurvey = true;
                            }
                            if (!isPullApiSurvey) {
                                isPullApiSurvey = true;
                                fetchUserProfile();
                            }

                        } catch (Exception e) {
                            isGetApiSurvey = true;
                            isPullApiSurvey = true;
                            fetchUserProfile();
                        }

                    }


                }, new Response.ErrorListener()

        {

            @Override
            public void onErrorResponse(VolleyError error) {
                RotiHomeActivity.getProgressDialog().dismiss();
                VolleyLog.d("le6end4", "Error: " + error.getMessage());
                Log.d("le6end4", "fetchOrderSiteFromServer Error: " + error.getMessage());
                String json = null;

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            json = Dialogs.trimMessage(json, "message");
                            if (json != null)
                                AppConstants.showMsgDialog("", json, ((Activity) getContext()));
                            break;

                        case 401:
                            logoutAccount();
                            if (json != null)
                                AppConstants.showMsgDialog("", json, ((Activity) getContext()));
                            break;
                        default:
                            AppConstants.showMsgDialog("", getContext().getString(R.string.BLANKMESSAGEREPLACEMENT), (Activity) getContext());
                            break;
                    }
                }

            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        RotiHomeActivity.getProgressDialog().dismiss();

        VolleyControllerAndTracker.getInstance().addToRequestQueue(jsonObjReq, "");


    }

    /*
     volley call to create a default gift card
     */
    private void createDefaultGiftCard() {
        String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");


        String url = getResources().getString(R.string.BASE_URL_GIFTCARD) + "/card/?" + "auth_token=" + authToken + "&appkey=" + getResources().getString(R.string.APPKEY) + "&amount=0";

        Log.d("URL12", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("getgiftcarddefult", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        GetGiftCardResponse giftCardResponse = Gson.getGson().fromJson(response.toString(), GetGiftCardResponse.class);


                        if (giftCardResponse.getStatus()) {

                            SharedPreferences.Editor prefsEditor = null;
                            prefsEditor = RotiHomeActivity.getPreferenceEditor();
                            String giftCard = "";
                            try {
                                if (giftCardResponse.getCardNumber() != null) {
                                    giftCard = giftCardResponse.getCardNumber();
                                    prefsEditor.putString(AppConstants.PREFGIFT_CARD, giftCard);
                                    Log.d("giftCard", giftCard);
                                }
                            } catch (Exception e) {
                            }
                            prefsEditor.commit();

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                RotiHomeActivity.getProgressDialog().dismiss();
                VolleyLog.d("le6end4", "Error: " + error.getMessage());
                Log.d("le6end4", "fetchOrderSiteFromServer Error: " + error.getMessage());
                String json = null;

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            RotiHomeActivity.getProgressDialog().dismiss();

                            json = new String(response.data);
                            json = Dialogs.trimMessage(json, "message");
                            if (json != null)
                                AppConstants.showMsgDialog("", json, ((Activity) getContext()));
                            break;
                        case 401:

                            logoutAccount();
                            break;
                        default:
                            RotiHomeActivity.getProgressDialog().dismiss();

                            AppConstants.showMsgDialog("", getContext().getString(R.string.BLANKMESSAGEREPLACEMENT), (Activity) getContext());
                            break;
                    }
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appkey", getResources().getString(R.string.APPKEY));
                params.put("auth_token", authToken);

                return params;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RotiHomeActivity.getProgressDialog().dismiss();


        // Adding request to request queue
        VolleyControllerAndTracker.getInstance().addToRequestQueue(jsonObjReq, "");


    }


    /*
    volley call for user gift card code
     */
    private void fetchUserGiftCardCode() {
        setHomeCardBtnRefresh(false);
        String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");

        String url = getResources().getString(R.string.BASE_URL) + "/user/giftcard_code?" + "appkey="
                + getResources().getString(R.string.APPKEY) + "&auth_token=" + authToken + "&latitude="
                + RotiHomeActivity.getGetLatLongObj().getLatitude() + "&longitude="
                + RotiHomeActivity.getGetLatLongObj().getLongitude();


        Log.d("Fetch User Gift URL", url);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Fetch User Gift", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();
                        setHomeCardBtnRefresh(true);

                        FetchUserGiftCardCodeResponse fetchUserGiftCardCodeResponse = Gson.getGson().fromJson(response.toString(), FetchUserGiftCardCodeResponse.class);
                        try {
                            if (fetchUserGiftCardCodeResponse.getStatus()) {
                                String giftCardCode = fetchUserGiftCardCodeResponse.getGiftcardCode();

                                setDisplayBarcode(giftCardCode);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                RotiHomeActivity.getProgressDialog().dismiss();
                VolleyLog.d("le6end4", "Error: " + error.getMessage());
                Log.d("le6end4", "fetchOrderSiteFromServer Error: " + error.getMessage());
                String json = null;

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            json = Dialogs.trimMessage(json, "message");
                            if (json != null)
                                AppConstants.showMsgDialog("", json, ((Activity) getContext()));
                            break;
                        case 401:
                            logoutAccount();
                            break;
                        default:
                            AppConstants.showMsgDialog("", getContext().getString(R.string.BLANKMESSAGEREPLACEMENT), (Activity) getContext());
                            break;
                    }
                }

            }
        });

        // Adding request to request queue
        VolleyControllerAndTracker.getInstance().addToRequestQueue(jsonObjReq, "");


    }


    /*
    volley call to fetch the user code
     */
    private void fetchUserCode() {
        String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");

        String url = getResources().getString(R.string.BASE_URL) + "/user/code?" + "appkey="
                + getResources().getString(R.string.APPKEY) + "&auth_token=" + authToken;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("FetchUserCode", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();
                        setHomeCardBtnRefresh(true);

                        FetchUserCodeResponse fetchUserResponse = Gson.getGson().fromJson(response.toString(), FetchUserCodeResponse.class);
                        if (fetchUserResponse.getStatus()) {
                            try {
                                String userCode = fetchUserResponse.getUsercode();
                                if (!userCode.equals(""))
                                    setDisplayBarcode(userCode);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                RotiHomeActivity.getProgressDialog().dismiss();
                VolleyLog.d("le6end4", "Error: " + error.getMessage());
                Log.d("le6end4", "fetchOrderSiteFromServer Error: " + error.getMessage());
                String json = null;

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            json = Dialogs.trimMessage(json, "message");
                            if (json != null)
                                AppConstants.showMsgDialog("", json, ((Activity) getContext()));
                            break;
                        case 401:
                            logoutAccount();
                            break;
                        default:
                            AppConstants.showMsgDialog("", getContext().getString(R.string.BLANKMESSAGEREPLACEMENT), (Activity) getContext());
                            break;
                    }
                }

            }
        });

        // Adding request to request queue
        VolleyControllerAndTracker.getInstance().addToRequestQueue(jsonObjReq, "");
    }


    public void logoutAccount() {
        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");


        String url = getResources().getString(R.string.BASE_URL) + "/user/logout?" + "auth_token=" + authToken + "&appkey=" + getResources().getString(R.string.APPKEY);
        RotiHomeActivity.getProgressDialog().show();
        final JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        RotiHomeActivity.getProgressDialog().dismiss();
                        Log.d("Logout response", response.toString());

                        ForgotPasswordResponse forgotPasswordResponse = Gson.getGson().fromJson(response.toString(), ForgotPasswordResponse.class);

                        if (forgotPasswordResponse.getStatus()) {

                            SharedPreferences.Editor prefsEditor = RotiHomeActivity.getPreferenceEditor();
                            prefsEditor.putString(AppConstants.PREFLOGINID, "");
                            prefsEditor.putBoolean(AppConstants.PREFLOGOUTBUTTONTAG, true);
                            prefsEditor.putString(AppConstants.PREFAUTH_TOKEN, "");
                            prefsEditor.putString(AppConstants.PREFLOGINPAS, "");

                            prefsEditor.commit();

                            Dialogs.showMsgDialog("", forgotPasswordResponse.getNotice(), (getContext()));

                            Engine.getInstance().setSetNextPage(AppConstants.showHomeFragment);
                            clearBackStack();

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                            transaction.addToBackStack(AppConstants.TAG_SIGN);
                            transaction.commit();

                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Error: ", error.getMessage());
                if (RotiHomeActivity.getProgressDialog() != null && RotiHomeActivity.getProgressDialog().isShowing())
                    RotiHomeActivity.getProgressDialog().dismiss();


                VolleyLog.d("le6end4", "Error: " + error.getMessage());
                Log.d("le6end4", "fetchOrderSiteFromServer Error: " + error.getMessage());
                String json = null;

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 401:
                            json = new String(response.data);
                            json = trimMessage(json, "message");
                            if (json != null)
                                Dialogs.showMsgDialog("", json, (getContext()));

                            logoutAccount();
                            break;

                        case 501:
                            json = new String(response.data);
                            json = trimMessage(json, "message");
                            if (json != null)
                                Dialogs.showMsgDialog("", json, (getContext()));
                            break;
                        case 500:
                            json = new String(response.data);
                            json = trimMessage(json, "message");
                            if (json != null)
                                Dialogs.showMsgDialog("", json, (getContext()));
                            break;
                        default:

                            Dialogs.showMsgDialog("", String.valueOf(AppConstants.BLANKMESSAGEREPLACEMENT), getContext());


                    }
                }

            }
        });

        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request_json.setRetryPolicy(policy);


        /*
         add the request object to the queue to be executed
         */
        VolleyControllerAndTracker.getInstance().addToRequestQueue(request_json);


    }

    @Override
    public void notifyRefresh(String className) {
        if (className.equalsIgnoreCase(AppConstants.ITEM_HOME))
            refreshView();
    }

    public void clearBackStack() {
        android.support.v4.app.FragmentManager fragmentManag = getFragmentManager();
        if (fragmentManag.getBackStackEntryCount() > 0) {
            fragmentManag.popBackStack(AppConstants.TAG_PHOTO, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);


        }

    }

    public void clearBackStackSign() {
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack(AppConstants.TAG_SIGN, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
    }


    @Override
    public void onAnimationStart(Animation animation) {


    }

    @Override
    public void onAnimationEnd(Animation animation) {
        animEnd(animation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }


}

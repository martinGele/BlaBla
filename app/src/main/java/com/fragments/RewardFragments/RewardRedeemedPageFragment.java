package com.fragments.RewardFragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fragments.RewardsFragment;
import com.google.android.gms.analytics.Tracker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.responsemodels.Balance;
import com.responsemodels.FetchUserCodeResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.volleyandtracker.VolleyControllerAndTracker;
import com.widgets.AutoResizeTextView;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

@SuppressLint("ValidFragment")
public class RewardRedeemedPageFragment extends Fragment implements View.OnClickListener {


    private EditText enterBarcodeEdit;
    private ImageView enterBtn;
    public static Boolean firstTimeTwitterLoggedin = false;
    private ProgressBar pageProgressBar;
    private TextView userCode, bottomDesc, rewardsDesc, titleTV;
    private float startDegree;
    private float endDegree;
    private LinearLayout rewardTitleLayout;
    private ImageView userRotationImage;
    public Context context;


    private Tracker mTracker;
    private String SCREEN_NAME = "Reward Redeemed Fragment";

    View view;
    Balance.MyGoodieRewardsResponse.Reward reward;
    String rewardCode;
    Button doneBtn;

    @SuppressLint("ValidFragment")
    public RewardRedeemedPageFragment(Balance.MyGoodieRewardsResponse.Reward reward, String rewardCode) {
        this.reward = reward;
        this.rewardCode = rewardCode;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.reward_redeemed, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {

        setTracker();
        final AutoResizeTextView fontFitTextView = new AutoResizeTextView(getActivity());
        fontFitTextView.setText(reward.getName());


        titleTV = view.findViewById(R.id.rewardTitle);
        rewardTitleLayout = view.findViewById(R.id.rewardTitleLayout);

        rewardTitleLayout.setVisibility(View.INVISIBLE);

        rewardContent = view.findViewById(R.id.rewardContent);
        rewardContent.setVisibility(View.INVISIBLE);

        rewardsDesc = view.findViewById(R.id.redeemDesc);
        doneBtn = view.findViewById(R.id.doneBtn);
        bottomDesc = view.findViewById(R.id.reward_claim_text_redeemtitle);

        userCode = view.findViewById(R.id.userCode);
        pageProgressBar = view.findViewById(R.id.greenProgressBar);

        userRotationImage = view.findViewById(R.id.userRotationImage);
        titleTV.setText(reward.getName());
        bottomDesc.setText(reward.getFineprint());

        Log.i("elang", "elang size: " + reward.getFineprint().length());
        if (reward.getFineprint().length() > 20) {
            Fonts.fontRobotoCodensedRegularTextView(bottomDesc, 10, AppConstants.COLORBLUE,
                    getActivity().getAssets());
        } else {
            Fonts.fontRobotoCodensedRegularTextView(bottomDesc, 15, AppConstants.COLORBLUE,
                    getActivity().getAssets());
        }

        // AppConstants.fontEANBarcodeTextView(barcodeResult, 150,
        // AppConstants.COLORBLACKRGB, mHomePage.getAssets());
        Fonts.fontFinkHeavyRegularTextView(titleTV, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(rewardsDesc, 18, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(userCode, 15, AppConstants.COLORBLACKRGB, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(doneBtn, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        doneBtn.setOnClickListener(this);

        getUserDetail();
    }

    /*
    get user details and fulfil the UI
     */
    private void getUserDetail() {

        RotiHomeActivity.getProgressDialog().show();
        String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");

        String url = getResources().getString(R.string.BASE_URL) + "/user/code?" + "appkey="
                + getResources().getString(R.string.APPKEY) + "&auth_token=" + authToken;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("UserDetail", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();
                        FetchUserCodeResponse fetchUserResponse = Gson.getGson().fromJson(response.toString(), FetchUserCodeResponse.class);

                        if (fetchUserResponse.getStatus()) {

                            // barcode data
                            String barcode_data = rewardCode;

                            // barcode image
                            Bitmap bitmap = null;
                            ImageView QRCode = view.findViewById(R.id.QRCode);
                            LinearLayout barcodeBackground = view.findViewById(R.id.qrcode_background);

                            barcodeBackground.setBackgroundColor(WHITE);

                            try {
                                int width = QRCode.getWidth();
                                int height = QRCode.getHeight();
                                bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, width, height);
                                QRCode.setImageBitmap(bitmap);

                            } catch (WriterException e) {
                                e.printStackTrace();
                            }

                            userCode.setText(rewardCode);

                            rewardTitleLayout.setVisibility(View.VISIBLE);
                            rewardContent.setVisibility(View.VISIBLE);

                            startRotatingImage();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.doneBtn:
                if (Engine.isNetworkAvailable(getActivity())) {
                    clearBackStack();

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.home_linear_container, new RewardsFragment(), AppConstants.TAG_REWARD);
                    transaction.addToBackStack(AppConstants.TAG_REWARD);
                    transaction.commit();
                } else {
                    AppConstants.showMsgDialog("", AppConstants.ERRORNETWORKCONNECTION, getActivity());

                }

                break;
        }

    }

    private void SetTextWatcherForAmountEditView(final EditText amountEditText) {
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (filterLongEnough(amountEditText)) {
                    try {
                        // enterBtn.setBackgroundResource(R.drawable.purple_enter_btn);
                        enterBtn.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // enterBtn.setBackgroundResource(R.drawable.purple_enter_btn_idle);
                    enterBtn.setEnabled(false);
                }
            }

            private boolean filterLongEnough(EditText amountEditText) {
                return amountEditText.getText().toString().trim().length() > 0;
            }

        };
        amountEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }

    static String intToString(int num, int digits) {
        if (digits == 0)
            return String.valueOf(num);
        else {
            // create variable length array of zeros
            char[] zeros = new char[digits];
            Arrays.fill(zeros, '0');
            // format number as String
            DecimalFormat df = new DecimalFormat(String.valueOf(zeros));

            return df.format(num);
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

    View rewardContent;

    public void clearBackStack() {
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            android.support.v4.app.FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);

            getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_REWARD, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        }

    }

    public void startRotatingImage() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                userRotationImage.animate().rotationBy(360).withEndAction(this).setDuration(3000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };

        userRotationImage.animate().rotationBy(360).withEndAction(runnable).setDuration(3000)
                .setInterpolator(new LinearInterpolator()).start();
    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }
}

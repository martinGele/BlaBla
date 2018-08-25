package com.fragments.SendGiftCardFragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adapters.GiftCardAdapter;
import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fragments.HomeFragment;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.NavigationItemChangedListener;
import com.interfaces.RefreshListner;
import com.responsemodels.GiftCardImageResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.util.Utility;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import java.util.ArrayList;

public class GiftCardStartFragment extends Fragment implements View.OnClickListener, RefreshListner {

    private static GiftCardStartFragment mGift;

    private GiftCardAdapter adapter;
    private ViewPager viewPager;
    private ImageView btnLeft, btnRight;
    private GiftCardImageResponse.Image giftCardImageImage;
    private ArrayList<String> imageList;
    private ArrayList<String> imageEmailHeader;

    private LinearLayout layoutIndicator;
    private ImageView[] giftcardIndicator;

    private ArrayList<Bitmap> tampImage;

    EditText editHideFocus;
    private EditText eOtherAmount;
    private TextView eOtherAmountHint, addBalanceText, giftcardDesignText, giftcardLabel;
    Button btnNext;

    RadioButton rValue1;
    RadioButton rValue2;
    RadioButton rValue3;
    RadioButton rValue4;

    private int VALUE1 = 15;
    private int VALUE2 = 25;
    private int VALUE3 = 35;
    private int VALUE4 = 45;

    int defaultValue = VALUE2;

    View view;
    private Tracker mTracker;
    private String SCREEN_NAME = "Gift Card Fragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.snap_start, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {
        ((NavigationItemChangedListener) getActivity()).onItemChanged(AppConstants.ITEM_GIFT);

        setTracker();
        imageList = new ArrayList<String>();
        imageEmailHeader = new ArrayList<String>();
        giftCardImageImage = new GiftCardImageResponse.Image();

        tampImage = new ArrayList<Bitmap>();

        btnNext = view.findViewById(R.id.btn_addnew_creditcard);
        eOtherAmount = view.findViewById(R.id.addbalance_edit_eOtherAmount);
        eOtherAmountHint = view.findViewById(R.id.addbalance_edit_eOtherAmount_hint);
        addBalanceText = view.findViewById(R.id.add_balance_text);
        giftcardDesignText = view.findViewById(R.id.giftcard_design_text);
        giftcardLabel = view.findViewById(R.id.giftcard_label);
        editHideFocus = view.findViewById(R.id.editHideFocus);
        btnLeft = view.findViewById(R.id.btn_left);
        btnRight = view.findViewById(R.id.btn_right);

        layoutIndicator = view.findViewById(R.id.layout_indicator);

        rValue1 = view.findViewById(R.id.radio0);
        rValue2 = view.findViewById(R.id.radio1);
        rValue3 = view.findViewById(R.id.radio2);
        rValue4 = view.findViewById(R.id.radio3);

        Fonts.fontRobotoCodensedBoldTextView(eOtherAmount, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(eOtherAmountHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(btnNext, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(addBalanceText, 18, AppConstants.COLORBLUE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(giftcardDesignText, 16, AppConstants.COLORBLUE,
                getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(giftcardLabel, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        Fonts.fontRobotoCodensedBoldTextView(rValue1, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(rValue2, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(rValue3, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(rValue4, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x - ((size.x / 3));

        LinearLayout.LayoutParams relativeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                width);

        RelativeLayout rel = view.findViewById(R.id.pager_layout);
        rel.setLayoutParams(relativeParams);

        viewPager = view.findViewById(R.id.pager);
        float valueDensity = (float) view.getResources().getDisplayMetrics().density;
        int valuePaddingInPixels = (int) (getActivity().getResources().getDimension(R.dimen.viewpager_padding)
                / valueDensity);
        int valueMarginInPixels = (int) (getActivity().getResources().getDimension(R.dimen.viewpager_margin)
                / valueDensity);

        Log.d("le6end4-view", "density:" + valueDensity);
        Log.d("le6end4-view", "padding:" + valuePaddingInPixels);
        Log.d("le6end4-view", "margin:" + valueMarginInPixels);

        // Disable clip to padding
        viewPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev &
        // next page
        viewPager.setPadding(valuePaddingInPixels, 0, valuePaddingInPixels, 0); // (120, 0, 120, 0)
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        viewPager.setPageMargin(valueMarginInPixels); // (30)
        btnLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);

            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                
                // TODO Auto-generated method stub

                giftCardImageImage.setAppImageUrl(imageList.get(position));

                giftCardImageImage.setEmailHeaderImageUrl(imageEmailHeader.get(position));

                Log.d("ImagePosition", imageList.get(position));


                if (imageList.size() > 1) {
                    layoutIndicator.setVisibility(View.VISIBLE);
                    setIndicatorOn(position);

                    if (position == 0) {
                        btnLeft.setVisibility(View.GONE);
                        btnRight.setVisibility(View.VISIBLE);
                    } else if (position == imageList.size() - 1) {
                        btnLeft.setVisibility(View.VISIBLE);
                        btnRight.setVisibility(View.GONE);
                    } else {
                        btnLeft.setVisibility(View.VISIBLE);
                        btnRight.setVisibility(View.VISIBLE);
                    }
                } else {
                    btnLeft.setVisibility(View.GONE);
                    btnRight.setVisibility(View.GONE);
                    layoutIndicator.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        rValue1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!eOtherAmount.getText().toString().equals(""))
                        eOtherAmount.setText("");

                    defaultValue = VALUE1;
                    rValue1.setTextColor(Color.parseColor("#e86724"));

                    rValue2.setChecked(false);
                    rValue3.setChecked(false);
                    rValue4.setChecked(false);

                    editHideFocus.requestFocus();
                    btnNext.setEnabled(true);
                    closeSoftKeyBoard();
                } else {
                    rValue1.setTextColor(Color.WHITE);
                }
            }
        });

        rValue2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!eOtherAmount.getText().toString().equals(""))
                        eOtherAmount.setText("");

                    defaultValue = VALUE2;
                    rValue2.setTextColor(Color.parseColor("#e86724"));

                    rValue1.setChecked(false);
                    rValue3.setChecked(false);
                    rValue4.setChecked(false);

                    editHideFocus.requestFocus();
                    btnNext.setEnabled(true);
                    closeSoftKeyBoard();
                } else {
                    rValue2.setTextColor(Color.WHITE);
                }
            }
        });

        rValue3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!eOtherAmount.getText().toString().equals(""))
                        eOtherAmount.setText("");

                    defaultValue = VALUE3;
                    rValue3.setTextColor(Color.parseColor("#e86724"));

                    rValue1.setChecked(false);
                    rValue2.setChecked(false);
                    rValue4.setChecked(false);

                    editHideFocus.requestFocus();
                    btnNext.setEnabled(true);
                    closeSoftKeyBoard();
                } else {
                    rValue3.setTextColor(Color.WHITE);
                }
            }
        });

        rValue4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!eOtherAmount.getText().toString().equals(""))
                        eOtherAmount.setText("");

                    eOtherAmount.clearFocus();
                    defaultValue = VALUE4;
                    rValue4.setTextColor(Color.parseColor("#e86724"));

                    rValue1.setChecked(false);
                    rValue2.setChecked(false);
                    rValue3.setChecked(false);

                    editHideFocus.requestFocus();
                    btnNext.setEnabled(true);
                    closeSoftKeyBoard();
                } else {
                    rValue4.setTextColor(Color.WHITE);
                }
            }
        });

        eOtherAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        eOtherAmount.setMaxLines(2);
        eOtherAmount.setEllipsize(TextUtils.TruncateAt.END);
        eOtherAmount.setHorizontallyScrolling(false);


        SetTextWatcherForAmountEditView(eOtherAmount);
        // btnNext.setEnabled(false);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSoftKeyBoard();

                Log.d("wetzel", "eOtherAmount:" + eOtherAmount.getText());

                if (Engine.isNetworkAvailable(getActivity())) {
                    boolean isPass = false;
                    int tmpDefault = 0;
                    if (!eOtherAmount.getText().toString().equals("")) {
                        tmpDefault = Integer.valueOf(eOtherAmount.getText().toString());
                        if (tmpDefault >= 2 && tmpDefault <= 100) {
                            isPass = true;
                            defaultValue = tmpDefault;


                        } else if (tmpDefault <= 1 || tmpDefault >= 100) {

                            Dialogs.showMsgDialog("", getResources().getString(R.string.enter_amount), getActivity());

                        }
                    } else
                        isPass = true;

                    if (isPass) {
                        if (RotiHomeActivity.checkIfLogin()) {


                            Engine.getInstance().setSetNextPage(AppConstants.showGiftCardStartFragment);

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.home_linear_container, new SnapSpecifyDetailFragment(defaultValue, giftCardImageImage), AppConstants.TAG_GIFT);
                            transaction.addToBackStack(AppConstants.TAG_GIFT);
                            transaction.commit();

                        } else {


                        }
                    } else {
                        // eOtherAmount.setText("");

                        AppConstants.showMsgDialog("", "Please enter a dollar amount that is between $2 and $100.",
                                getActivity());
                    }
                } else {
                    AppConstants.showMsgDialog("", AppConstants.CONNECTION_FAILURE, getActivity());
                }
            }
        });

        refreshView();

    }

    private void refreshView() {

        closeSoftKeyBoard();

        if (eOtherAmount.getText().toString().equals("")) {
            rValue1.setChecked(false);
            rValue2.setChecked(true);
            rValue3.setChecked(false);
            rValue4.setChecked(false);
        } else {
            rValue1.setChecked(false);
            rValue2.setChecked(false);
            rValue3.setChecked(false);
            rValue4.setChecked(false);
        }

        boolean isGetStartedPopUpHasShown = RotiHomeActivity.getPreference().getBoolean(AppConstants.IS_IT_GIFT_FIRST_TIME,
                false);

        if (!isGetStartedPopUpHasShown) {
            SharedPreferences.Editor edit = RotiHomeActivity.getPreferenceEditor();
            edit.putBoolean(AppConstants.IS_IT_GIFT_FIRST_TIME, true);
            edit.commit();

            getStartedDialog();
        }

        defaultValue = VALUE2;

        fetchImageFromServer();
    }

    /*
    get the images from server with volley call
     */
    private void fetchImageFromServer() {

        RotiHomeActivity.getProgressDialog().show();
        final String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");
        String url = getResources().getString(R.string.BASE_URL) + "/user/giftcard_skin?" + "appkey=" + getResources().getString(R.string.APPKEY) + "&auth_token=" + authToken;

        Log.d("Fetch", url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Fetch Image From Server", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();
                        imageList.clear();
                        imageEmailHeader.clear();
                        tampImage.clear();

                        GiftCardImageResponse giftCardImageResponse = Gson.getGson().fromJson(response.toString(), GiftCardImageResponse.class);

                        if (giftCardImageResponse.getStatus()) {
                            /*
freaking nested array
 */

                            for (int i = 0; i < giftCardImageResponse.getImages().size(); i++) {
                                imageList.add(giftCardImageResponse.getImages().get(i).getAppImageUrl());
                                imageEmailHeader.add(giftCardImageResponse.getImages().get(i).getEmailHeaderImageUrl());

                                Log.d("image", giftCardImageResponse.getImages().get(i).getAppImageUrl());

                                try {
                                    DownloadImageTask(imageList.get(i));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

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

                            HomeFragment.getInstance().logoutAccount();
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

    private void DownloadImageTask(String s) throws Exception {

        RotiHomeActivity.getProgressDialog().show();
        ImageRequest request = new ImageRequest(s,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        RotiHomeActivity.getProgressDialog().dismiss();
                        tampImage.add(bitmap);

                        Log.d("Bitmap", bitmap.toString());

                        if (tampImage.size() == imageList.size()) {
                            adapter = new GiftCardAdapter(getActivity(), tampImage);

                            viewPager.setAdapter(adapter);
                            viewPager.setCurrentItem(0);

                            adapter.notifyDataSetChanged();


                            // displaying selected image first

//
                            giftCardImageImage.setAppImageUrl(imageList.get(0));
                            giftCardImageImage.setEmailHeaderImageUrl(imageEmailHeader.get(0));

                            try {


                                if (imageList.size() > 0) {
                                    giftcardIndicator = new ImageView[imageList.size()];

                                    for (int i = 0; i < imageList.size(); i++) {
                                        ImageView img = new ImageView(getActivity());

                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT);
                                        img.setLayoutParams(params);
                                        img.setId(1000 + i);
                                        if (i == 0)
                                            img.setImageResource(R.drawable.indicator_on);
                                        else
                                            img.setImageResource(R.drawable.indicator_off);
                                        giftcardIndicator[i] = img;

                                        layoutIndicator.addView(img);
                                    }

                                    if (imageList.size() < 0) {
                                        btnLeft.setVisibility(View.GONE);
                                        btnRight.setVisibility(View.GONE);
                                        layoutIndicator.setVisibility(View.GONE);
                                    } else {
                                        btnLeft.setVisibility(View.GONE);
                                    }
                                }

                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        RotiHomeActivity.getProgressDialog().dismiss();
                    }
                });
// Access the RequestQueue through your singleton class.
        VolleyControllerAndTracker.getInstance().addToRequestQueue(request, "");
    }

    /*
    download the images from server
     */

    private void setIndicatorOn(int j) {
        for (int i = 0; i < giftcardIndicator.length; i++) {
            if (i == j)
                giftcardIndicator[i].setImageResource(R.drawable.indicator_on);
            else
                giftcardIndicator[i].setImageResource(R.drawable.indicator_off);
        }
    }

    public void getStartedDialog() {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.custom_get_started);

        TextView desc = (TextView) dialog1.findViewById(R.id.alert_desc);
        Button yes = (Button) dialog1.findViewById(R.id.alert_ok);

        Fonts.fontRobotoCodensedRegularTextView(desc, 16, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(yes, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.setCancelable(false);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.show();
    }

    private void SetTextWatcherForAmountEditView(final EditText tmpEditText) {
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (rValue1.isChecked())
                        rValue1.setChecked(false);
                    if (rValue2.isChecked())
                        rValue2.setChecked(false);
                    if (rValue3.isChecked())
                        rValue3.setChecked(false);
                    if (rValue4.isChecked())
                        rValue4.setChecked(false);
                }

                defaultValue = 0;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*
                 * if (s.length() > 0){ // no text entered. Center the hint text.
                 * tmpEditText.setGravity(Gravity.CENTER_VERTICAL); }else{ // position the text
                 * type in the left top corner tmpEditText.setGravity(Gravity.LEFT |
                 * Gravity.TOP); }
                 */

                Utility.editTextHideShowHint(s.length(), tmpEditText, getActivity(), view);

                if (filterLongEnough(eOtherAmount)) {
                    try {
                        btnNext.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    btnNext.setEnabled(false);
                }
            }

            private boolean filterLongEnough(EditText tmpEditText) {
                return tmpEditText.getText().toString().trim().length() > 0;
            }

        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }

    private void closeSoftKeyBoard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(eOtherAmount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void notifyRefresh(String className) {
        refreshView();
    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }


}

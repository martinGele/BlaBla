package com.ak.app.wetzel.activity;


import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fragments.AddCurrencyFragment;
import com.fragments.AddPhotoFragment;
import com.fragments.FindFragment;
import com.fragments.HomeFragment;
import com.fragments.InfoFragment;
import com.fragments.InfoFragments.ChangePasswordFragment;
import com.fragments.InfoFragments.ReferFriendFragment;
import com.fragments.InfoFragments.ViewActivityFragment;
import com.fragments.LoginOptionFragment;
import com.fragments.PostSignUpEmailFragment;
import com.fragments.RewardFragments.ClaimRewardPageFragment;
import com.fragments.RewardFragments.ClaimRewardPageGiftFragment;
import com.fragments.RewardFragments.PromoPageFragment;
import com.fragments.RewardsFragment;
import com.fragments.SendGiftCardFragments.GiftCardSendDetail;
import com.fragments.SendGiftCardFragments.GiftCardStartFragment;
import com.fragments.SendGiftCardFragments.SnapSpecifyDetailFragment;
import com.fragments.SignInFragment;
import com.fragments.SignUpFragment;
import com.interfaces.FavoriteStoreInterface;
import com.interfaces.LocationInterface;
import com.interfaces.NavigationItemChangedListener;
import com.interfaces.OpenFragmetListener;
import com.interfaces.PhoneInterface;
import com.interfaces.RewardInterface;
import com.payment.ManagePaymentFragment;
import com.responsemodels.AvailableOfferResponse;
import com.responsemodels.Balance;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.GetLatLongFromGPS;
import com.volleyandtracker.VolleyControllerAndTracker;
import com.widgets.CustomProgressDialog;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RotiHomeActivity extends BaseActivity implements NavigationItemChangedListener, View.OnClickListener, OpenFragmetListener, RewardInterface, LocationInterface, PhoneInterface, FavoriteStoreInterface {


    public static SharedPreferences mPreference;
    ImageView navoHome, navoGift, navoReward, navoFind, navoMore;


    private static final int SELECT_PICTURE = 90;


    public static ProgressDialog progressDialog;

    public static Editor prefsEditor;

    public static Editor getPreferenceEditor() {
        return prefsEditor;
    }

    public static SharedPreferences getPreference() {
        return mPreference;
    }

    public static GetLatLongFromGPS getLatLongObj;

    public static GetLatLongFromGPS getGetLatLongObj() {
        return getLatLongObj;
    }


    public static void startGPS() {
        if (getLatLongObj != null)
            getLatLongObj.startGPS();
    }

    public static void stopGPS() {
        if (getLatLongObj != null) { // PP
            getLatLongObj.stopLocationListening();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        startGPS();

        getLatLongObj = GetLatLongFromGPS.getinstance(this);


        progressDialog = new CustomProgressDialog(this, R.style.AppTheme);


        mPreference = PreferenceManager.getDefaultSharedPreferences(RotiHomeActivity.this);
        prefsEditor = mPreference.edit();


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ak.app.wetzel.activity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        initView();

    }


    public void initView() {

        navoHome = findViewById(R.id.navHome);
        navoGift = findViewById(R.id.navGift);
        navoReward = findViewById(R.id.navRewards);
        navoFind = findViewById(R.id.navFind);
        navoMore = findViewById(R.id.navInfo);

        navoHome.setOnClickListener(this);
        navoGift.setOnClickListener(this);
        navoReward.setOnClickListener(this);
        navoFind.setOnClickListener(this);
        navoMore.setOnClickListener(this);
        ((NavigationItemChangedListener) activity).onItemChanged(AppConstants.ITEM_HOME);
        if (getVisibleHomeFragment() == null) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new HomeFragment());
            transaction.commit();
        }

        try {
            final FrameLayout contentView = findViewById(R.id.home_linear_container);
            final LinearLayout tabView = findViewById(R.id.tabView);
            contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    Rect r = new Rect();
                    contentView.getWindowVisibleDisplayFrame(r);
                    int screenHeight = contentView.getRootView().getHeight();

                    int keypadHeight = screenHeight - r.bottom;

                    if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                        tabView.setVisibility(View.GONE);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tabView.setVisibility(View.VISIBLE);
                            }
                        }, 100);


                    }
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onItemChanged(String item) {

        if (item.equals(AppConstants.ITEM_HOME)) {
            navoHome.setImageResource(R.drawable.home_hit);
        } else {
            navoHome.setImageResource(R.drawable.home);
        }
        if (item.equals(AppConstants.ITEM_GIFT)) {
            navoGift.setImageResource(R.drawable.snap_hit);
        } else {
            navoGift.setImageResource(R.drawable.snap);
        }
        if (item.equals(AppConstants.ITEM_REWARDS)) {
            navoReward.setImageResource(R.drawable.rewards_hit);
        } else {
            navoReward.setImageResource(R.drawable.rewards);
        }
        if (item.equals(AppConstants.ITEM_FIND)) {
            navoFind.setImageResource(R.drawable.social_hit);
        } else {
            navoFind.setImageResource(R.drawable.social);
        }
        if (item.equals(AppConstants.ITEM_MORE)) {
            navoMore.setImageResource(R.drawable.info_hit);
        } else {
            navoMore.setImageResource(R.drawable.info);
        }
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (v.getId()) {

            case R.id.navHome:
                ((NavigationItemChangedListener) activity).onItemChanged(AppConstants.ITEM_HOME);
                clearBackStack();
                if (getVisibleHomeFragment() == null) {
                    transaction.replace(R.id.home_linear_container, new HomeFragment());
                    transaction.commit();
                }

                break;
            case R.id.navGift:
                if (Engine.isNetworkAvailable(activity)) {

                    if (this.checkIfLogin()) {
                        if (getVisibleGiftFragment() == null) {
                            ((NavigationItemChangedListener) activity).onItemChanged(AppConstants.ITEM_GIFT);
                            clearBackStack();

                            transaction.replace(R.id.home_linear_container, new GiftCardStartFragment(), AppConstants.TAG_GIFT);
                            transaction.addToBackStack(AppConstants.TAG_GIFT);
                            transaction.commit();
                        }
                    } else {

                        if (checkIfemailId()) {
                            ((NavigationItemChangedListener) activity).onItemChanged(AppConstants.ITEM_GIFT);

                            String email = mPreference.getString(AppConstants.PREFLOGINID,
                                    "");
                            String password = mPreference.getString(AppConstants.PREFLOGINPAS,
                                    "");
                            Engine.getInstance().setSetNextPage(AppConstants.showGiftCardStartFragment);

                            transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                            transaction.addToBackStack(AppConstants.TAG_SIGN);
                            transaction.commit();
                        } else {
                            ((NavigationItemChangedListener) activity).onItemChanged(AppConstants.ITEM_GIFT);

                            clearBackStack();
                            Engine.getInstance().setSetNextPage(AppConstants.showGiftCardStartFragment);
                            transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                            transaction.addToBackStack(AppConstants.TAG_SIGN);
                            transaction.commit();
                        }
                    }
                } else {
                    Dialogs.showMsgDialog("Alert",
                            AppConstants.ERRORNETWORKCONNECTION, activity);
                }
                break;

            case R.id.navRewards:

                if (Engine.isNetworkAvailable(activity)) {
                    if (this.checkIfLogin()) {
                        if (getVisibleRewardsFragment() == null) {
                            ((NavigationItemChangedListener) activity).onItemChanged(AppConstants.ITEM_REWARDS);
                            clearBackStack();
                            transaction.replace(R.id.home_linear_container, new RewardsFragment(), AppConstants.TAG_REWARD);
                            transaction.addToBackStack(AppConstants.TAG_REWARD);
                            transaction.commit();
                        }
                    } else {

                        if (checkIfemailId()) {
                            ((NavigationItemChangedListener) activity).onItemChanged(AppConstants.ITEM_REWARDS);

                            String email = mPreference.getString(AppConstants.PREFLOGINID,
                                    "");
                            String password = mPreference.getString(AppConstants.PREFLOGINPAS,
                                    "");
                            Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                            getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            transaction.replace(R.id.home_linear_container, new PostSignUpEmailFragment(email, password), AppConstants.TAG_SIGN);
                            transaction.addToBackStack(AppConstants.TAG_SIGN);
                            transaction.commit();
                        } else {
                            ((NavigationItemChangedListener) activity).onItemChanged(AppConstants.ITEM_REWARDS);

                            clearBackStack();
                            Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                            transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                            transaction.addToBackStack(AppConstants.TAG_SIGN);
                            transaction.commit();
                        }
                    }
                } else {
                    Dialogs.showMsgDialog("Alert",
                            AppConstants.ERRORNETWORKCONNECTION, activity);
                }

                break;

            case R.id.navFind:

                if (getVisibleFindFragment() == null) {
                    ((NavigationItemChangedListener) activity).onItemChanged(AppConstants.ITEM_FIND);
                    clearBackStack();
                    transaction.replace(R.id.home_linear_container, new FindFragment(), AppConstants.TAG_FIND);
                    transaction.addToBackStack(AppConstants.TAG_FIND);
                    transaction.commit();
                }
                break;


            case R.id.navInfo:
                if (getVisibleMoreFragment() == null) {
                    ((NavigationItemChangedListener) activity).onItemChanged(AppConstants.ITEM_MORE);
                    clearBackStack();
                    transaction.replace(R.id.home_linear_container, new InfoFragment(), AppConstants.TAG_MORE);
                    transaction.addToBackStack(AppConstants.TAG_MORE);
                    transaction.commit();
                }
                break;
        }

    }

    @Override
    public void startFragment(String nextFragment) {

        if (nextFragment.equals(AppConstants.showAddPhotoPage)) {
            clearBackStack();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new AddPhotoFragment(), AppConstants.ITEM_HOME);
            transaction.addToBackStack(AppConstants.ITEM_HOME);
            transaction.commit();

        } else if (nextFragment.equals(AppConstants.TAG_LOGIN_SIGNUP)) {
            clearBackStack();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
            transaction.addToBackStack(AppConstants.TAG_SIGN);
            transaction.commit();

        } else if (nextFragment.equals(AppConstants.showHomeFragment)) {

            ((NavigationItemChangedListener) activity).onItemChanged(AppConstants.ITEM_HOME);


            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new HomeFragment(), null);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (nextFragment.equals(AppConstants.showAddCurrencyFragment)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new AddCurrencyFragment(), AppConstants.ITEM_HOME);
            transaction.addToBackStack(AppConstants.ITEM_HOME);
            transaction.commit();
        } else if (nextFragment.equals(AppConstants.showSignInFragment)) {
            clearBackStack();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new SignInFragment(), AppConstants.TAG_SIGN);
            transaction.addToBackStack(AppConstants.TAG_SIGN);
            transaction.commit();

        } else if (nextFragment.equals(AppConstants.showRewardsFragment)) {
            clearBackStack();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new RewardsFragment(), AppConstants.ITEM_HOME);
            transaction.addToBackStack(AppConstants.ITEM_HOME);
            transaction.commit();
        } else if (nextFragment.equals(AppConstants.showGiftCardStartFragment)) {
            clearBackStack();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new GiftCardStartFragment(), AppConstants.ITEM_GIFT);
            transaction.addToBackStack(AppConstants.ITEM_GIFT);
            transaction.commit();


        } else if (nextFragment.equals(AppConstants.showSnapSpecifyFragment)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new SnapSpecifyDetailFragment(), AppConstants.ITEM_GIFT);
            transaction.addToBackStack(AppConstants.ITEM_GIFT);
            transaction.commit();
        } else if (nextFragment.equals(AppConstants.showInfoFragment)) {
            clearBackStack();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new InfoFragment(), AppConstants.ITEM_GIFT);
            transaction.addToBackStack(AppConstants.ITEM_GIFT);
            transaction.commit();
        } else if (nextFragment.equals(AppConstants.showManagePaymentFragment)) {

            clearBackStack();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new ManagePaymentFragment(), AppConstants.TAG_PAYMENT);
            transaction.addToBackStack(AppConstants.TAG_PAYMENT);
            transaction.commit();
        } else if (nextFragment.equals(AppConstants.showChangePasswordFragment)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new ChangePasswordFragment(), AppConstants.TAG_MORE);
            transaction.addToBackStack(AppConstants.TAG_MORE);
            transaction.commit();
        } else if (nextFragment.equals(AppConstants.showReferFriendFragment)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new ReferFriendFragment(), AppConstants.TAG_MORE);
            transaction.addToBackStack(AppConstants.TAG_MORE);
            transaction.commit();


        } else if (nextFragment.equals(AppConstants.giftCardSendDetail)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new GiftCardSendDetail(), AppConstants.TAG_MORE);
            transaction.addToBackStack(AppConstants.TAG_MORE);
            transaction.commit();
        } else if (nextFragment.equals(AppConstants.showRewardPageFragment)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new ClaimRewardPageFragment(), AppConstants.TAG_MORE);
            transaction.addToBackStack(AppConstants.TAG_MORE);
            transaction.commit();
        } else if (nextFragment.equals(AppConstants.showPromoPageFragment)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new PromoPageFragment(), AppConstants.TAG_REWARD);
            transaction.addToBackStack(AppConstants.TAG_REWARD);
            transaction.commit();
        } else if (nextFragment.equals(AppConstants.showViewActivityFragment)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_linear_container, new ViewActivityFragment(), AppConstants.TAG_REWARD);
            transaction.addToBackStack(AppConstants.TAG_REWARD);
            transaction.commit();
        }


    }


    /*
    clear the back-stack from all the fragments, just leave the Home Fragment
     */

    public void clearBackStack() {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            android.support.v4.app.FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);

            getSupportFragmentManager().popBackStack(AppConstants.TAG_PAYMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().popBackStack(AppConstants.TAG_GIFT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().popBackStack(AppConstants.TAG_REWARD, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().popBackStack(AppConstants.TAG_FIND, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().popBackStack(AppConstants.TAG_MORE, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().popBackStack(AppConstants.TAG_PHOTO, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        }

    }

    /*
    get the progress dialog, NOTE this can be made in any class probably should be moved from this activity
     */
    public static ProgressDialog getProgressDialog() {
        return progressDialog;
    }

      /*
    with this method i am checking if the user is logged in

     */

    public static boolean checkIfLogin() {

        String authToken = mPreference.getString(AppConstants.PREFAUTH_TOKEN,
                "");
        if (authToken.equals("")) {


            return false;
        } else
            return true;
    }

    public static boolean checkIfemailId() {

        String email = mPreference.getString(AppConstants.PREFLOGINID,
                "");
        if (email.equals("")) {


            return false;
        } else
            return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null)
                fragment.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Uri selectedImageUri = data.getData();

                AppConstants.selectedImageUri = data.getData();

                // OI FILE Manager
                String filemanagerstring = AppConstants.selectedImageUri
                        .getPath();


                // MEDIA GALLERY
                String selectedImagePath = getPath(AppConstants.selectedImageUri);

                Log.d("le6end4", "filemanagerstring:" + filemanagerstring);
                Log.d("le6end4", "selectedImagePath:" + selectedImagePath);

                // NOW WE HAVE OUR WANTED STRING
                if (selectedImagePath != null)
                    AppConstants.selectedImagePath = selectedImagePath;
                else
                    AppConstants.selectedImagePath = filemanagerstring;

                // showAddPhotoPage(true);
                showCameraPreviewPage();
            }
        }
    }

    private void showCameraPreviewPage() {
        Intent intent = new Intent(RotiHomeActivity.this, ImageCropActivity.class);
        startActivity(intent);

    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStack();
    }

    @Override
    public void onItemClickCall(Balance.MyGoodieRewardsResponse.Reward reward) {


        if (reward.getExpired() && reward.getExpirestate().equalsIgnoreCase("delete")) {
            if (RotiHomeActivity.checkIfLogin()) {


                setDeleteButtonState(reward);
                deleteRewardFromServer(reward.getId());


            } else {
                Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                clearBackStack();
                transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                transaction.addToBackStack(AppConstants.TAG_SIGN);
                transaction.commit();


            }
        } else if (!reward.getExpired() && reward.getExpirestate().equals("reward")) {
            if (Engine.isNetworkAvailable(this)) {
                if (RotiHomeActivity.checkIfLogin()) {
                    // Rewards.getInstance().showClaimRewardPage(R
                    // myGoodieRewardsBean);


                    if (reward.getGifter() != null && reward.getGifter().equals("true")) {

                        Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.home_linear_container, new ClaimRewardPageGiftFragment(reward), AppConstants.TAG_REWARD);
                        transaction.addToBackStack(AppConstants.TAG_REWARD);
                        transaction.commit();


                    } else {
                        Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.home_linear_container, new ClaimRewardPageFragment(reward), AppConstants.TAG_REWARD);
                        transaction.addToBackStack(AppConstants.TAG_REWARD);
                        transaction.commit();
                    }


                } else {
                    Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                    clearBackStack();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                    transaction.addToBackStack(AppConstants.TAG_SIGN);
                    transaction.commit();

                }

            } else
                AppConstants.showMsgDialog("",
                        AppConstants.ERRORNETWORKCONNECTION, this);
        }
    }

    private void deleteRewardFromServer(Integer id) {
        String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");


        String url = getResources().getString(R.string.BASE_URL) + "/rewards/" + id + "?" + "auth_token=" + authToken + "&appkey=" + getResources().getString(R.string.APPKEY) + "&locale="
                + getResources().getString(R.string.LOCALE_HEADER_VALUE);

        Log.d("delete", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("deleteresponse", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();


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
                                AppConstants.showMsgDialog("", json, RotiHomeActivity.this);
                            break;
                        case 401:


                            break;
                        default:
                            RotiHomeActivity.getProgressDialog().dismiss();

                            AppConstants.showMsgDialog("", getResources().getString(R.string.BLANKMESSAGEREPLACEMENT), RotiHomeActivity.this);
                            break;
                    }
                }

            }
        });

        // Adding request to request queue
        VolleyControllerAndTracker.getInstance().addToRequestQueue(jsonObjReq, "");


    }
/*
if the reward is exipred set the b
 */
    public void setDeleteButtonState(Balance.MyGoodieRewardsResponse.Reward myGoodieRewardsBean) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        try {

            if (myGoodieRewardsBean.getExpired().equals("true")
                    && myGoodieRewardsBean.getExpirestate().equals("expire")) {
                myGoodieRewardsBean.setExpirestate("delete");
            } else if (myGoodieRewardsBean.getExpired().equals("true")
                    && myGoodieRewardsBean.getExpirestate().equals("delete")) {
                if (RotiHomeActivity.checkIfLogin()) {
//                    deleteRewardFromServer(myGoodieRewardsBean.getId());
                } else {
                    clearBackStack();
                    Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                    transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                    transaction.addToBackStack(AppConstants.TAG_SIGN);
                    transaction.commit();
                }
            } else if (!myGoodieRewardsBean.getExpired().equals("true")
                    && myGoodieRewardsBean.getExpirestate().equals("reward")) {

                if (Engine.isNetworkAvailable(this)) {
                    if (RotiHomeActivity.checkIfLogin()) {

                        if (myGoodieRewardsBean.getGifter().equals("true")) {
                            Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                            transaction.replace(R.id.home_linear_container, new ClaimRewardPageGiftFragment(myGoodieRewardsBean), AppConstants.TAG_REWARD);
                            transaction.addToBackStack(AppConstants.TAG_REWARD);
                            transaction.commit();

                        } else {
                            Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                            transaction.replace(R.id.home_linear_container, new ClaimRewardPageGiftFragment(myGoodieRewardsBean), AppConstants.TAG_REWARD);
                            transaction.addToBackStack(AppConstants.TAG_REWARD);
                            transaction.commit();
                        }

                    } else {
                        clearBackStack();
                        Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                        transaction.replace(R.id.home_linear_container, new LoginOptionFragment(), AppConstants.TAG_SIGN);
                        transaction.addToBackStack(AppConstants.TAG_SIGN);
                        transaction.commit();
                    }
                } else
                    AppConstants.showMsgDialog("",
                            AppConstants.ERRORNETWORKCONNECTION, this);


            } else if (!myGoodieRewardsBean.getExpired().equals("true")
                    && myGoodieRewardsBean.getExpirestate().equals("reward")) {

                if (myGoodieRewardsBean.getGifter().equals("true")) {
                    Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                    transaction.replace(R.id.home_linear_container, new ClaimRewardPageGiftFragment(myGoodieRewardsBean), AppConstants.TAG_REWARD);
                    transaction.addToBackStack(AppConstants.TAG_REWARD);
                    transaction.commit();

                } else
                    Engine.getInstance().setSetNextPage(AppConstants.showRewardsFragment);
                transaction.replace(R.id.home_linear_container, new ClaimRewardPageGiftFragment(myGoodieRewardsBean), AppConstants.TAG_REWARD);
                transaction.addToBackStack(AppConstants.TAG_REWARD);
                transaction.commit();

            }

        } catch (Exception e) {
        }
    }

    @Override
    public void onItemClickLocation(AvailableOfferResponse.OperatingHour.Restaurant location) {


        Uri gmmIntentUri = Uri.parse("geo:" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + "?q=" + location.getAddress());

        Log.d("MapsAPI", String.valueOf(gmmIntentUri));

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);


    }

    @Override
    public void onItemClickCall(AvailableOfferResponse.OperatingHour.Restaurant call) {

        try {
            Log.d("----------------------", "test");
            String urlPhonenum = (String) call.getPhoneNumber();
            urlPhonenum = "tel:" + urlPhonenum;
            Log.e("hphone-no", urlPhonenum);
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                    .parse(urlPhonenum));
            startActivity(intent);
        } catch (Exception activityException) {
            Log.e("url Phonenum", "" + "Call failed");
        }
    }

    @Override
    public void onItemClickFavoriteStore(AvailableOfferResponse.OperatingHour.Restaurant location) {


        SignUpFragment.setName(location.getName());
        SignUpFragment.setId(String.valueOf(location.getId()));
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();


    }

    /*
    if current fragment is visible don't allow the user to open the fragment again
     */

    public Fragment getVisibleHomeFragment() {
        android.support.v4.app.FragmentManager fragmentManager = RotiHomeActivity.this.getSupportFragmentManager();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {

            for (Fragment fragment : fragments) {
                if (fragment != null && fragment instanceof HomeFragment)
                    return fragment;
            }
        }
        return null;
    }

    /*
    check if gift start fragment is visible
     */
    public Fragment getVisibleGiftFragment() {
        android.support.v4.app.FragmentManager fragmentManager = RotiHomeActivity.this.getSupportFragmentManager();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment instanceof GiftCardStartFragment)
                    return fragment;
            }
        }
        return null;
    }

    /*
    check if Rewards fragment is open or not
     */
    public Fragment getVisibleRewardsFragment() {
        android.support.v4.app.FragmentManager fragmentManager = RotiHomeActivity.this.getSupportFragmentManager();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment instanceof RewardsFragment)
                    return fragment;
            }
        }
        return null;
    }

    public Fragment getVisibleFindFragment() {
        android.support.v4.app.FragmentManager fragmentManager = RotiHomeActivity.this.getSupportFragmentManager();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment instanceof FindFragment)
                    return fragment;
            }
        }
        return null;
    }

    public Fragment getVisibleMoreFragment() {
        android.support.v4.app.FragmentManager fragmentManager = RotiHomeActivity.this.getSupportFragmentManager();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment instanceof InfoFragment)
                    return fragment;
            }
        }
        return null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}




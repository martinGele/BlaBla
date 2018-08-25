package com.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ak.app.wetzel.activity.BaseActivity;
import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.RefreshListner;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.responsemodels.FetchUserResponse;
import com.util.AppConstants;
import com.util.Dialogs;
import com.util.Engine;
import com.util.Fonts;
import com.util.Gson;
import com.util.ServerCommunication;
import com.volleyandtracker.VolleyControllerAndTracker;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

@SuppressLint("ValidFragment")
public class AddPhotoFragment extends Fragment implements View.OnClickListener, RefreshListner {


    int[] result;
    private static final int SELECT_PICTURE = 90;

    ImageView photoAvatar;
    boolean isPhotoBoothOpened = false;
    boolean isPhotoBoothPreviewOpened = false;
    ImageLoader imageLoader;
    ImageLoaderConfiguration imageLoaderConfig;
    Button btnConfirm, btnSelect;
    File filePath;
    View view;
    boolean isFromCamera;
    private Tracker mTracker;
    private String SCREEN_NAME = "Add Photo Fragment";


    public void openPhotoBooth() {
        isPhotoBoothOpened = true;
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshView();


    }


    public AddPhotoFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_addphoto, container, false);
        initView(view);
        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    openPhotoBooth();
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

                } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
                    Dialogs.showMsgDialog("", getResources().getString(R.string.to_enable_storage_access), ((Activity) getContext()));

                } else {

                    Dialogs.showMsgDialog("", getResources().getString(R.string.you_cannot_use_the_add_photo), ((Activity) getContext()));

                }
                return;
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.addphoto_btn_select:

                /*
                on runtime we check the permission for read external storage, if there is no permission granted the user will
                not be redirected to the other activity for picture cropping. If there is no permission for this, the app will crash by default
                 */
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        }, 1);


                break;

            case R.id.addphoto_btn_confirm:
                if (Engine.isNetworkAvailable(getActivity())) {
                    if (RotiHomeActivity.checkIfLogin()) {
                        if (Engine.isNetworkAvailable(getActivity())) {
                            new executeUpdateUserProfile().execute("");
                        }
                    }
                } else
                    AppConstants.showMsgDialog("",
                            AppConstants.ERRORNETWORKCONNECTION, getActivity());


                break;
        }

    }

    private void initView(View view) {

        setTracker();
        /*
        cache the memory from the picture to file
         */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        imageLoaderConfig = new ImageLoaderConfiguration.Builder(getActivity())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        photoAvatar = view.findViewById(R.id.addphoto_avatar);
        btnSelect = view.findViewById(R.id.addphoto_btn_select);
        btnConfirm = view.findViewById(R.id.addphoto_btn_confirm);
        btnConfirm.setEnabled(false);


        /*
        set click listiners to the buttons
         */
        btnConfirm.setOnClickListener(this);
        btnSelect.setOnClickListener(this);

        Fonts.fontRobotoCodensedBoldTextView(btnSelect, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(btnConfirm, 18, AppConstants.COLORWHITE, getActivity().getAssets());


        refreshView();
    }


    private void refreshView() {
        btnConfirm.setEnabled(BaseActivity.getInstance().isFromCamera());

        if (BaseActivity.getInstance().isFromCamera()) {
            CreateFileImageMethod();
        } else {
            if (Engine.isNetworkAvailable(getActivity())) {
                if (RotiHomeActivity.checkIfLogin()) {
                    if (Engine.isNetworkAvailable(getActivity())) {
                        fetchUserProfile();
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
            } else
                AppConstants.showMsgDialog("", AppConstants.ERRORNETWORKCONNECTION, getActivity());


        }
    }


    /*
    fetch the user profile
     */
    private void fetchUserProfile() {
        RotiHomeActivity.getProgressDialog().show();
        String authToken = RotiHomeActivity.getPreference().getString(AppConstants.PREFAUTH_TOKEN, "");


        String url = getResources().getString(R.string.BASE_URL) + "/user/profile?" + "appkey=" + getResources().getString(R.string.APPKEY) + "&auth_token=" + authToken;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response fetch user", response.toString());
                        RotiHomeActivity.getProgressDialog().dismiss();

                        FetchUserResponse user = Gson.getGson().fromJson(response.toString(), FetchUserResponse.class);


                        if (user.getStatus()) {
                            String profile_pic_url = String.valueOf(user.getUser().getProfilePic());

                            Log.d("ProfilePic", String.valueOf(user.getUser().getProfilePic()));

                            if (!profile_pic_url.equalsIgnoreCase("<null>") &&
                                    !profile_pic_url.equalsIgnoreCase("null")) {
                                try {
                                    if (imageLoader.isInited())
                                        imageLoader.destroy();

                                    imageLoader.init(imageLoaderConfig);
                                    imageLoader.displayImage(profile_pic_url, photoAvatar);

                                    //BitmapWorkerTask task = new BitmapWorkerTask(photoAvatar);
                                    //task.execute(profile_pic_url);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("Load Pic", "error:" + e.getMessage());
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

    @Override
    public void notifyRefresh(String className) {
        if (className == "AddPhotoFragment") {

            refreshView();
        }

    }

    public void CreateFileImageMethod() {
        Calendar calendar = Calendar.getInstance();

        String filename = calendar.getTimeInMillis() + ".jpg";
        filePath = new File(getActivity().getFilesDir() + "/" + filename);
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(filePath, true);
            outputStream.write(RotiHomeActivity.getInstance().getBytesSecond());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String fileExist = "false";

        if (RotiHomeActivity.checkIfLogin()) {

            if (filePath.exists()) {
                long Filesize = filePath.length() / 1024;//call function and convert bytes into Kb
                String value;
                if (Filesize >= 1024)
                    value = Filesize / 1024 + " Mb";
                else
                    value = Filesize + " Kb";

                Log.d("le6end4", filePath.getAbsolutePath() + ": " + value);
                Bitmap myBitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath());
                photoAvatar.setImageBitmap(myBitmap);
                fileExist = "true";

            }
            if (!fileExist.equals("true")) {
                btnConfirm.setEnabled(false);
                photoAvatar.setImageDrawable(null);

            }
        } else {
            photoAvatar.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.default_avatar));


        }

    }


    private class executeUpdateUserProfile extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            RotiHomeActivity.getProgressDialog().show();
        }

        @Override
        protected String doInBackground(String... params) {
            String authToken = RotiHomeActivity.getPreference().getString(
                    AppConstants.PREFAUTH_TOKEN, "");


            Log.d("le6end4", "authToken:" + authToken);
            Log.d("le6end4", "appkey:" + getResources().getString(R.string.APPKEY));
            ServerCommunication serverComm = new ServerCommunication(getActivity());
            String result = serverComm.uploadUserPhoto("/user/profile/update",
                    authToken, filePath);

            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(String result) {


            RotiHomeActivity.getProgressDialog().dismiss();

            Log.d("le6end4", "result post:" + result);
            if (result != null && !result.equals("")) {
                try {
                    JSONObject resObject = new JSONObject(result);
                    String sucess = resObject.getString("status");
                    if (sucess != null && !sucess.equals("")
                            && sucess.equals("true")) {


                        clearBackStack();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        transaction.replace(R.id.home_linear_container, new HomeFragment(), AppConstants.ITEM_HOME);
                        transaction.addToBackStack(AppConstants.ITEM_HOME);
                        transaction.commit();

                    } else {
//                        AppConstants.parseInput(result, mHomePage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }

    public void clearBackStack() {
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            android.support.v4.app.FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);


            getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_SIGN, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getActivity().getSupportFragmentManager().popBackStack(AppConstants.TAG_PHOTO, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        }

    }

}

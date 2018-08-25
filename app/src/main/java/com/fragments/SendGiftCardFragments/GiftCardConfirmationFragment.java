package com.fragments.SendGiftCardFragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.ak.app.wetzel.activity.RotiHomeActivity;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.android.gms.analytics.Tracker;
import com.responsemodels.GiftCardImageResponse;
import com.util.AppConstants;
import com.util.Fonts;
import com.volleyandtracker.VolleyControllerAndTracker;

@SuppressLint("ValidFragment")
public class GiftCardConfirmationFragment extends Fragment implements View.OnClickListener {

    View view;

    Button btnNext;

    String mToName;
    String mToEmail;
    String mFromName;
    String mCustomMessage;
    int thisAmount;
    String thisCardNumber;
    String giftCardNumber;
    String imageUrl;
    private ImageView giftcardDesignImage;


    private Tracker mTracker;
    private String SCREEN_NAME = "Gift Card Confirmation Fragment";


    @SuppressLint("ValidFragment")
    public GiftCardConfirmationFragment(String mToName, String mToEmail, String mFromName, String mCustomMessage, int mAmount, GiftCardImageResponse.Image giftCardImageResponse) {
        this.mToName = mToName;
        this.mToEmail = mToEmail;
        this.mFromName = mFromName;
        this.mCustomMessage = mCustomMessage;
        this.thisAmount = mAmount;
        this.imageUrl = giftCardImageResponse.getAppImageUrl();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.snap_confirm_gift_detail, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {
        setTracker();

        Button back_btn = view.findViewById(R.id.back_btn);
        btnNext = view.findViewById(R.id.btn_confirm_next);
        TextView giftcardDesignText = view.findViewById(R.id.giftcard_design_text);
        TextView confirmTitle = view.findViewById(R.id.confirm_title);
        TextView confirmAmountTitle = view.findViewById(R.id.confirm_amount_title);
        TextView confirmAmountValue = view.findViewById(R.id.confirm_amount);
        TextView confirmSendToTitle = view.findViewById(R.id.confirm_sendto_title);
        TextView confirmSendToValue = view.findViewById(R.id.confirm_sendto);
        giftcardDesignImage = (ImageView) view.findViewById(R.id.giftcard_design_image);

        btnNext.setOnClickListener(this);
        back_btn.setOnClickListener(this);

        Fonts.fontFinkHeavyRegularTextView(confirmTitle, 16, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(confirmAmountTitle, 18, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(confirmAmountValue, 30, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(confirmSendToTitle, 18, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontFinkHeavyRegularTextView(confirmSendToValue, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        Fonts.fontFinkHeavyRegularTextView(giftcardDesignText, 16,
                AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(back_btn, 18,
                AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(btnNext, 18,
                AppConstants.COLORWHITE, getActivity().getAssets());

        confirmAmountValue.setText("$"
                + String.format("%.2f", Float.valueOf(thisAmount)));
        confirmSendToValue.setText(mToEmail);

        DownloadImageTask(imageUrl);
    }


    private void DownloadImageTask(final String appImageUrl) {


        RotiHomeActivity.getProgressDialog().show();
        ImageRequest request = new ImageRequest(appImageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        RotiHomeActivity.getProgressDialog().dismiss();
                        giftcardDesignImage.setImageBitmap(bitmap);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_btn:

                getFragmentManager().popBackStackImmediate();
                break;

            case R.id.btn_confirm_next:

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.home_linear_container, new GiftCardSendDetail(mToName, mToEmail, mFromName, mCustomMessage, thisAmount, imageUrl), AppConstants.TAG_GIFT);
                transaction.addToBackStack(AppConstants.TAG_GIFT);
                transaction.commit();

                break;
        }

    }


    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }
}

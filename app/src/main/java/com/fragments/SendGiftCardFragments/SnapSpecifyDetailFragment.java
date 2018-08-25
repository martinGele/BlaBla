package com.fragments.SendGiftCardFragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.OpenFragmetListener;
import com.responsemodels.GiftCardImageResponse;
import com.util.AppConstants;
import com.util.Engine;
import com.util.Fonts;
import com.util.Utility;
import com.volleyandtracker.VolleyControllerAndTracker;

@SuppressLint("ValidFragment")
public class SnapSpecifyDetailFragment extends Fragment implements View.OnClickListener, Handler.Callback {

    private String androidOS = Build.VERSION.RELEASE;
    private String model = Build.MODEL;
    private String manufacturer = Build.MANUFACTURER;
    private ImageView giftcardDesignImage;


    private Tracker mTracker;
    private String SCREEN_NAME = "Snap Specify Detail Fragment";


    private Handler uiHandler;

    private static final int UPDATE_UI = 10;
    EditText toNameEdit, toEmailEdit, fromNameEdit, customMessageEdit;
    TextView toNameEditHint, toEmailEditHint, fromNameEditHint, customMessageEditHint;
    Button nextBtn, back_btn;


    int mAmount;

    View view;

    GiftCardImageResponse.Image giftCardImageResponse;

    public SnapSpecifyDetailFragment() {
    }

    @SuppressLint("ValidFragment")
    public SnapSpecifyDetailFragment(int defaultValue, GiftCardImageResponse.Image giftCardImage) {
        this.mAmount = defaultValue;
        this.giftCardImageResponse = giftCardImage;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.snap_specify_gift_detail, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {

        setTracker();
        uiHandler = new Handler(Looper.getMainLooper(), this);

        Log.d("wetzel", "mAmount: " + mAmount);
        Log.d("wetzel", "id: " + giftCardImageResponse);


        toNameEdit = view.findViewById(R.id.specify_edit_nameto);
        toEmailEdit = view.findViewById(R.id.specify_edit_emailto);
        fromNameEdit = view.findViewById(R.id.specify_edit_namefrom);
        customMessageEdit = view.findViewById(R.id.specify_edit_custommessage);
        toNameEditHint = view.findViewById(R.id.specify_edit_nameto_hint);
        toEmailEditHint = view.findViewById(R.id.specify_edit_emailto_hint);
        fromNameEditHint = view.findViewById(R.id.specify_edit_namefrom_hint);
        customMessageEditHint = view.findViewById(R.id.specify_edit_custommessage_hint);
        back_btn = view.findViewById(R.id.back_btn);
        nextBtn = view.findViewById(R.id.specify_gift_next);

        back_btn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        Fonts.fontRobotoCodensedBoldTextView(toNameEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(toEmailEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(fromNameEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(customMessageEdit, 16, AppConstants.COLORORANGE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(toNameEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(toEmailEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(fromNameEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(customMessageEditHint, 16, AppConstants.COLORBG, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(back_btn, 18, AppConstants.COLORWHITE, getActivity().getAssets());
        Fonts.fontRobotoCodensedBoldTextView(nextBtn, 18, AppConstants.COLORWHITE, getActivity().getAssets());

        SetTextWatcherForAmountEditView(toNameEdit);
        SetTextWatcherForAmountEditView(toEmailEdit);
        SetTextWatcherForAmountEditView(fromNameEdit);
        SetTextWatcherForAmountEditView(customMessageEdit);

        nextBtn.setEnabled(false);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_btn:

                if (Engine.getInstance().getSetNextPage().length() > 0) {
                    ((OpenFragmetListener) getActivity()).startFragment(Engine.getInstance().getSetNextPage());
                    Engine.getInstance().setSetNextPage("");
                }
                break;

            case R.id.specify_gift_next:


                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            toNameEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }

                if (!isValidEmail(toEmailEdit)) {
                    AppConstants.showMsgDialog("",
                            "Please enter a valid email address.", getActivity());
                } else {

                    String mToName = toNameEdit.getText().toString();
                    String mToEmail = toEmailEdit.getText().toString();
                    String mFromName = fromNameEdit.getText().toString();
                    String mCustomMessage = customMessageEdit.getText()
                            .toString();


                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.home_linear_container, new GiftCardConfirmationFragment(mToName, mToEmail, mFromName, mCustomMessage, mAmount, giftCardImageResponse), AppConstants.TAG_GIFT);
                    transaction.addToBackStack(AppConstants.TAG_GIFT);
                    transaction.commit();


                    Log.i("elang", "elang snapSpecifyDetail: "
                            + giftCardImageResponse.getAppImageUrl());
                }

                break;
        }

    }

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }


    public void showconfirmDialog() {
        try {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
            alt_bld.setMessage("Please enter a valid 10-digit number")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();

                                }
                            });
            AlertDialog alert = alt_bld.create();
            alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetTextWatcherForAmountEditView(final EditText tmpEditText) {
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
//				if (tmpEditText.getId() == R.id.specify_edit_emailto) {
//					String result = s.toString().replaceAll(" ", "");
//					if (!s.toString().equals(result)) {
//						tmpEditText.setText(result);
//						tmpEditText.setSelection(result.length());
//						// alert the user
//					}
//				}

                toNameEdit.setSelection(toNameEdit.getText().toString().length());
                uiHandler.sendEmptyMessageDelayed(UPDATE_UI, 100);
                if (tmpEditText.getId() == R.id.specify_edit_emailto) {
                    String result = s.toString().replaceAll(" ", "");
                    if (!s.toString().equals(result)) {
                        tmpEditText.setText(result);
                        tmpEditText.setSelection(result.length());
                        // alert the user
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                /*
                 * if (s.length() > 0){ // no text entered. Center the hint
                 * text. tmpEditText.setGravity(Gravity.CENTER_VERTICAL); }else{
                 * // position the text type in the left top corner
                 * tmpEditText.setGravity(Gravity.LEFT | Gravity.TOP); }
                 */

                Utility.editTextHideShowHint(s.length(), tmpEditText,
                        getActivity(), view);

                if (filterLongEnough(toNameEdit)
                        && filterLongEnough(toEmailEdit)
                        && isValidEmail(toEmailEdit)) {
                    try {
                        nextBtn.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    nextBtn.setEnabled(false);
                }
            }

            private boolean filterLongEnough(EditText tmpEditText) {
                return tmpEditText.getText().toString().trim().length() > 1;
            }

        };
        tmpEditText.addTextChangedListener(fieldValidatorTextWatcher);
    }

    public boolean isValidEmail(EditText emailEditText) {
        return !TextUtils.isEmpty(emailEditText.getText())
                && android.util.Patterns.EMAIL_ADDRESS.matcher(
                emailEditText.getText()).matches();
    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }
}

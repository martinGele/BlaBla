package com.fragments;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.google.android.gms.analytics.Tracker;
import com.interfaces.OpenFragmetListener;
import com.util.AppConstants;
import com.util.Engine;
import com.util.Fonts;
import com.volleyandtracker.VolleyControllerAndTracker;

import java.util.Timer;
import java.util.TimerTask;

public class AccountSuccessfullyVerified extends Fragment {
    private Timer my_timer;


    TextView text_header, verified;


    private Tracker mTracker;
    private String SCREEN_NAME = "Account Verified Fragment";
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();

        my_timer = new Timer();
        my_timer.schedule(new TimerTask() {
            public void run() {

                try {

                    clearBackStack();

                    if (Engine.getInstance().getSetNextPage().length() > 0) {
                        ((OpenFragmetListener) getActivity()).startFragment(Engine.getInstance().getSetNextPage());
                        Engine.getInstance().setSetNextPage("");

                    }
                } catch (Exception e) {
                    AppConstants.parseInput("Cannot Go to Next Page", getActivity(), "");

                }
            }
        }, 2500);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (my_timer != null) {
            my_timer.cancel();
            my_timer = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.account_successfully_verified, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        setTracker();
        text_header = view.findViewById(R.id.text_header);

        Fonts.fontFinkHeavyRegularTextView(text_header,
                (int) (getActivity().getResources().getDimension(R.dimen.header_font_size) /
                        getActivity().getResources().getDisplayMetrics().density),
                AppConstants.COLORWHITE, getActivity().getAssets());

        verified = view.findViewById(R.id.verified);


        Fonts.fontRobotoCodensedBoldTextView(verified, 26, AppConstants.COLORWHITE, getActivity().getAssets());


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


        }

    }


}

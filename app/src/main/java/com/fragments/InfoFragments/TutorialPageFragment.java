package com.fragments.InfoFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ak.app.wetzel.activity.R;
import com.google.android.gms.analytics.Tracker;
import com.util.TutorialAdapter;
import com.volleyandtracker.VolleyControllerAndTracker;

public class TutorialPageFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private TutorialAdapter adapter;
    private ViewPager viewPager;

    private ImageView btnGetStarted, imgIndicator;
    View view;


    private Tracker mTracker;
    private String SCREEN_NAME = "Tutorial Page Fragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_tutorial, container, false);
        inintView(view);
        return view;
    }

    private void inintView(View view) {

        setTracker();
        /*
        get the views
         */
        viewPager = view.findViewById(R.id.pager);
        btnGetStarted = view.findViewById(R.id.btn_get_started);
        imgIndicator = view.findViewById(R.id.img_indicator);

        /*
        set the click listeners to the buttons
         */
        btnGetStarted.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);

        adapter = new TutorialAdapter(getActivity());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_get_started:
                getFragmentManager().popBackStack();
                break;

        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateTutorialStepBar(position);
        if (position == 4) {
            btnGetStarted.setVisibility(View.VISIBLE);
            imgIndicator.setVisibility(View.GONE);
        } else {
            imgIndicator.setVisibility(View.VISIBLE);
            btnGetStarted.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void updateTutorialStepBar(int position) {
        if (position == 0) {
            imgIndicator.setImageResource(R.drawable.tutorial01);
        } else if (position == 1) {
            imgIndicator.setImageResource(R.drawable.tutorial02);
        } else if (position == 2) {
            imgIndicator.setImageResource(R.drawable.tutorial03);
        } else if (position == 3) {
            imgIndicator.setImageResource(R.drawable.tutorial04);
        } else if (position == 4) {
        }
    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }
}

package com.fragments.RewardFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.google.android.gms.analytics.Tracker;
import com.responsemodels.Balance;
import com.volleyandtracker.VolleyControllerAndTracker;

@SuppressLint("ValidFragment")
public class ClaimRewardPageGiftFragment extends Fragment implements View.OnClickListener {
    private LinearLayout mClaimLinearParnt;
    private LinearLayout mRedeemLinearParnt;

    private TextView storeaddressTV;
    private Balance.MyGoodieRewardsResponse.Reward reward;
    private TextView mRedeemtitleTV;

    View view;
    private Tracker mTracker;
    private String SCREEN_NAME = "Claim Rewards Page  Fragment";


    @SuppressLint("ValidFragment")
    public ClaimRewardPageGiftFragment(Balance.MyGoodieRewardsResponse.Reward reward) {
        this.reward = reward;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.reward_claim_gift_true, container, false);

        Log.d("REWARDS",String.valueOf(reward.getId()));
        initView(view);
        return view;
    }

    private void initView(View view) {

        setTracker();
    }

    @Override
    public void onClick(View v) {

    }

    private void setTracker() {
        VolleyControllerAndTracker application = (VolleyControllerAndTracker) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(SCREEN_NAME);
    }
}

package com.interfaces;

import android.view.View;

import com.adapters.RewardsAdapter;
import com.responsemodels.Balance;

public interface RewardInterface {
    void onItemClickCall(Balance.MyGoodieRewardsResponse.Reward reward);
}

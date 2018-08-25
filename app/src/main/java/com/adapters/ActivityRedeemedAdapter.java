package com.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.responsemodels.RewardClaimResponse;
import com.util.AppConstants;
import com.util.Fonts;

import java.util.List;

public class ActivityRedeemedAdapter extends RecyclerView.Adapter<ActivityRedeemedAdapter.MyViewHolder> {


    TextView titleTextView, dateTextView, addressTextView, statusTextView;
    private List<RewardClaimResponse.Activity.Reward> rewards;
    private List<RewardClaimResponse.Activity> activities;
    View line1;
    Context context;

    public ActivityRedeemedAdapter(List<RewardClaimResponse.Activity.Reward> rewards, List<RewardClaimResponse.Activity> activities) {
        this.rewards = rewards;
        this.activities = activities;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewactivity_list, parent, false);


        return new ActivityRedeemedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RewardClaimResponse.Activity.Reward reward = rewards.get(position);
        RewardClaimResponse.Activity activity = activities.get(position);

        holder.init(reward, activity);


    }

    @Override
    public int getItemCount() {
        return rewards.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            titleTextView = itemView.findViewById(R.id.viewactivity_list_text_date);
            dateTextView = itemView.findViewById(R.id.viewactivity_list_text_points);
            addressTextView = itemView.findViewById(R.id.viewactivity_list_text_date_description);
            statusTextView = itemView.findViewById(R.id.viewactivity_list_text_description);
            line1 = itemView.findViewById(R.id.line1);

            titleTextView.setText("");
            dateTextView.setText("");
            addressTextView.setText("");
            statusTextView.setText("");

            Fonts.fontRobotoCodensedBoldTextView(titleTextView, 16, AppConstants.COLORORANGE, context.getAssets());


            Fonts.fontRobotoCodensedBoldTextView(dateTextView, 16, AppConstants.COLORORANGE, context.getAssets());


            addressTextView.setVisibility(View.GONE);
            statusTextView.setVisibility(View.GONE);


        }

        public void init(RewardClaimResponse.Activity.Reward reward, RewardClaimResponse.Activity activity) {


            titleTextView.setText(activity.getClaimDate().toUpperCase());
            dateTextView.setText(reward.getName().toUpperCase());


            for (int i = 0; i < rewards.size(); i++) {

                Log.d("REWARDSIZE", String.valueOf(rewards.size()));

                if (i < rewards.size() - 1) {

                    line1.setVisibility(View.GONE);
                }else if (i <rewards.size()){
                    line1.setVisibility(View.VISIBLE);

                }
            }

        }
    }
}



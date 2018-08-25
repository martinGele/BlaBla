package com.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.interfaces.RewardInterface;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.responsemodels.Balance;
import com.util.AppConstants;
import com.util.Fonts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.MyViewHolder> {

    public double mTotalPoint = 0;

    private List<Balance.MyGoodieRewardsResponse.Reward> rewards = new ArrayList<Balance.MyGoodieRewardsResponse.Reward>();
    Context context;
    RewardInterface rewardInterface;


    ImageLoader imageLoader;
    ImageLoaderConfiguration imageLoaderConfig;
    ImageView thumbnail;

    public RewardsAdapter(List<Balance.MyGoodieRewardsResponse.Reward> rewards, RewardInterface listener) {
        this.rewards = rewards;
        this.rewardInterface = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.enjoy_mygodie_reward_list, parent, false);


        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Balance.MyGoodieRewardsResponse.Reward balanceRewards = rewards.get(position);

        holder.init(balanceRewards);
    }


    @Override
    public int getItemCount() {
        return rewards.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, dateTextView, dateText, ExpireText;


        public MyViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            imageLoader = ImageLoader.getInstance();

            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();

            imageLoaderConfig = new ImageLoaderConfiguration.Builder(context)
                    .defaultDisplayImageOptions(defaultOptions)
                    .build();

            titleTextView = itemView.findViewById(R.id.enjoy_mygodie_reward_list_text_title);
            dateTextView = itemView.findViewById(R.id.enjoy_mygodie_reward_list_date);

            ExpireText = itemView.findViewById(R.id.expire_text);
            thumbnail = itemView.findViewById(R.id.enjoy_mygodie_reward_list_thumbnail);


            Fonts.fontRobotoCodensedBoldTextView(titleTextView, 18, AppConstants.COLORWHITE, context.getAssets());
            Fonts.fontFinkHeavyRegularTextView(dateTextView, 14, AppConstants.COLORWHITE, context.getAssets());


        }


        public void init(Balance.MyGoodieRewardsResponse.Reward balanceRewards) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDeleteButtonState(balanceRewards, ExpireText);
                    rewardInterface.onItemClickCall(balanceRewards);


                }
            });
/*
if reward is expired then set the button to be delete
 */
            if (balanceRewards.getExpired()
                    && balanceRewards.getExpirestate().equals("expire")) {
                dateTextView.setText("DELETE");
                Fonts.fontChronicleTextG1RomanProTextView(dateTextView,
                        14, AppConstants.COLORGREY99, context.getAssets());
                balanceRewards.setExpirestate("delete");
            }


            String points = String.valueOf(balanceRewards.getPoints());

            if (points.contains("."))
                points = points.substring(0, points.indexOf("."));
            titleTextView.setText("" + balanceRewards.getName());
            {
                double point = 0;
                try {
                    point = Double.parseDouble(points);
                } catch (Exception e) {
                }

                if (!balanceRewards.getExpired().equals("true")
                        && point <= mTotalPoint) {
                    dateTextView.setVisibility(View.VISIBLE);
                    balanceRewards.setExpirestate("reward");
                    try {

                        if (points.equals("0")) {
                            dateTextView.setText("FREE");

                            try {
                                String deadline = balanceRewards.getExpiryDate();

                                java.util.Date d1 = AppConstants.makeDate(deadline);

                                SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yyyy");// "MM-dd-yy"

                                String currentTime = curFormater.format(d1);
                                dateText.setText("Exp " + currentTime); // el
                                dateText.setVisibility(View.VISIBLE);
                            } catch (Exception e) {

                            }
                        } else if (!points.equals("0"))
                            dateTextView.setText(points + " FRUITS");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (balanceRewards.getExpired().equals("false")) {
                    balanceRewards.setExpirestate("expired");
                    dateTextView.setText(points + " FRUITS");
                    dateTextView.setVisibility(View.VISIBLE);
                    Log.d("pointe", points);
                }

//
//
//                else if (balanceRewards.getGifter().equals("true")) {
//
//                } else if (balanceRewards.getGifter().equals("false")) {

                else if (!points.equals("0")) {
//                    cellParent
//                            .setBackgroundResource(R.drawable.rewards_button_leg_used);
                    balanceRewards.setExpirestate("expire");
                    ExpireText.setText("EXPIRED");
                    dateTextView.setVisibility(View.VISIBLE);
                    dateTextView.setText(points + " FRUITS");


                }

            }


        }
    }

    public void setDeleteButtonState(Balance.MyGoodieRewardsResponse.Reward reward, TextView dateTextView) {


        try {
            if (reward.getExpired().equals("true")
                    && reward.getExpirestate().equals("expire")) {
                dateTextView.setText("DELETE");
                Fonts.fontChronicleTextG1RomanProTextView(dateTextView,
                        14, AppConstants.COLORGREY99, context.getAssets());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


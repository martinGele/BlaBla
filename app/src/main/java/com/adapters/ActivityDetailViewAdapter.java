package com.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.responsemodels.LastTransactionResponse;
import com.util.AppConstants;
import com.util.Fonts;

import java.util.ArrayList;
import java.util.List;


public class ActivityDetailViewAdapter extends RecyclerView.Adapter<ActivityDetailViewAdapter.MyViewHolder> {


    TextView titleTextView;
    TextView pointsTextView;
    TextView addressTextView;
    TextView statusTextView;
    LinearLayout mParntReward;
    View line1;
    Context context;

    private List<LastTransactionResponse.Receipt> transaction = new ArrayList<LastTransactionResponse.Receipt>();
    private List<LastTransactionResponse> transactionResponses = new ArrayList<LastTransactionResponse>();


    public ActivityDetailViewAdapter(List<LastTransactionResponse.Receipt> transactions, List<LastTransactionResponse> transactions1) {

        this.transaction = transactions;
        this.transactionResponses = transactions1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewactivity_list, parent, false);


        return new ActivityDetailViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        LastTransactionResponse.Receipt locations = transaction.get(position);
        LastTransactionResponse transactionRespons = transactionResponses.get(position);

        holder.init(locations, transactionRespons);

    }

    @Override
    public int getItemCount() {
        return transactionResponses.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

//            mParntReward = itemView.findViewById(R.id.viewactivity_linear_list_parent);
            titleTextView = itemView.findViewById(R.id.viewactivity_list_text_date);
            pointsTextView = itemView.findViewById(R.id.viewactivity_list_text_points);
            addressTextView = itemView.findViewById(R.id.viewactivity_list_text_date_description);
            statusTextView = itemView.findViewById(R.id.viewactivity_list_text_description);
            line1 = itemView.findViewById(R.id.line1);


            titleTextView.setText("");
            pointsTextView.setText("");
            addressTextView.setText("");
            statusTextView.setText("");

            Fonts.fontRobotoCodensedBoldTextView(
                    titleTextView, 16, AppConstants.COLORORANGE,
                    context.getAssets());
            Fonts.fontRobotoCodensedBoldTextView(
                    pointsTextView, 16, AppConstants.COLORORANGE,
                    context.getAssets());

            Fonts.fontRobotoCodensedBoldTextView(
                    addressTextView, 14, AppConstants.COLORWHITEFH,
                    context.getAssets());
            Fonts.fontRobotoCodensedBoldTextView(
                    statusTextView, 14, AppConstants.COLORWHITEFH,
                    context.getAssets());

            addressTextView.setVisibility(View.GONE);
            statusTextView.setVisibility(View.GONE);

        }

        @SuppressLint("SetTextI18n")
        public void init(LastTransactionResponse.Receipt transactions, LastTransactionResponse lastTransactionResponse) {

            if (lastTransactionResponse.getStatus() != 3) {
                mParntReward.setVisibility(View.GONE);
            }

            if (lastTransactionResponse.getStatus() == 3) {
                titleTextView.setText("" + transactions.getCreatedAt().toUpperCase());

                if (Integer.valueOf(lastTransactionResponse.getTotalPointsEarned()) > 1)
                    pointsTextView.setText("EARNED " + lastTransactionResponse.getTotalPointsEarned() + " POINTS");

                else
                    pointsTextView.setText("EARNED " + lastTransactionResponse.getTotalPointsEarned() + " POINT");


            }


            Log.d("Points", lastTransactionResponse.getTotalPointsEarned());
        }
    }
}

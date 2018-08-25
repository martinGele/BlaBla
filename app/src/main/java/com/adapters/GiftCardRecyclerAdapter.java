package com.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ak.app.wetzel.activity.R;

import java.util.ArrayList;


/*
MAYBE I WILL USE THIS IN FUTURE, DON'T DELETE IT
 */

public class GiftCardRecyclerAdapter extends RecyclerView.Adapter<GiftCardRecyclerAdapter.MyViewHolder> {

    private Activity activity;
    ImageView imgDisplay;

    private ArrayList<Bitmap> tampImage;

    public GiftCardRecyclerAdapter(Activity activity, ArrayList<Bitmap> bitmaps) {

        this.activity = activity;
        this.tampImage = bitmaps;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_slide_giftcard, parent, false);
        return new GiftCardRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Bitmap tampImage1= tampImage.get(position);





        holder.init(tampImage1);


    }

    @Override
    public int getItemCount() {
        return tampImage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View itemView) {
            super(itemView);




        }

        public void init(Bitmap tampImage1) {
            imgDisplay = (ImageView) itemView.findViewById(R.id.imgDisplay);
            imgDisplay.setImageBitmap(tampImage1);

        }
    }
}

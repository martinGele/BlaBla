package com.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.interfaces.LocationInterface;
import com.interfaces.PhoneInterface;
import com.responsemodels.AvailableOfferResponse;
import com.util.AppConstants;
import com.util.Fonts;

import java.util.ArrayList;
import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.MyViewHolder> {


    private List<AvailableOfferResponse.OperatingHour.Restaurant> locations1 = new ArrayList<AvailableOfferResponse.OperatingHour.Restaurant>();
    Context context;
    TextView nameTextView, addressTextView;
    LinearLayout phoneLinear, mapLinear;
    LocationInterface locationInterface;
    PhoneInterface phoneInterface;

    public LocationsAdapter(List<AvailableOfferResponse.OperatingHour.Restaurant> locations, LocationInterface locationInterface, PhoneInterface phoneInterface) {
        this.locations1 = locations;
        this.locationInterface = locationInterface;
        this.phoneInterface = phoneInterface;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.foode_findzoes_list, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AvailableOfferResponse.OperatingHour.Restaurant locations = locations1.get(position);

        holder.init(locations);
    }

    @Override
    public int getItemCount() {
        return locations1.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View itemView) {
            super(itemView);


            Context context = itemView.getContext();

            nameTextView = itemView.findViewById(R.id.findzoes_list_text_name);
            addressTextView = itemView.findViewById(R.id.findzoes_list_text_address);


            nameTextView.setText("");
            addressTextView.setText("");
            Fonts.fontRobotoCodensedBoldTextView(nameTextView, 16,
                    AppConstants.COLORBG, context.getAssets());
            Fonts.fontRobotoCodensedRegularTextView(addressTextView, 14,
                    AppConstants.COLORBLUE, context.getAssets());


        }


        public void init(AvailableOfferResponse.OperatingHour.Restaurant locations) {


            phoneLinear = itemView.findViewById(R.id.phone_image);
            phoneLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneInterface.onItemClickCall(locations);

                }
            });
            mapLinear = itemView.findViewById(R.id.map_image);
            mapLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationInterface.onItemClickLocation(locations);

                }
            });


            nameTextView.setText(locations.getName());
            addressTextView.setText(locations.getAddress());


        }
    }


}

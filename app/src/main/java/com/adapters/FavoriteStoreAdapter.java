package com.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ak.app.wetzel.activity.R;
import com.interfaces.FavoriteStoreInterface;
import com.responsemodels.AvailableOfferResponse;
import com.util.AppConstants;
import com.util.Fonts;

import java.util.ArrayList;
import java.util.List;

public class FavoriteStoreAdapter extends RecyclerView.Adapter<FavoriteStoreAdapter.MyViewHolder> {

    TextView nameTextView, addressTextView, cityStateLabel;
    ImageView favorite_restaurant;
    LinearLayout favorite_spot_to_eat;
    FavoriteStoreInterface favoriteStoreInterface;
    private List<AvailableOfferResponse.OperatingHour.Restaurant> locations1 = new ArrayList<AvailableOfferResponse.OperatingHour.Restaurant>();


    public FavoriteStoreAdapter(List<AvailableOfferResponse.OperatingHour.Restaurant> restaurantArrayList, FavoriteStoreInterface favoriteStoreInterface) {

        this.locations1 = restaurantArrayList;
        this.favoriteStoreInterface = favoriteStoreInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_store_listv_item, parent, false);


        return new FavoriteStoreAdapter.MyViewHolder(itemView);
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
            favorite_restaurant = itemView.findViewById(R.id.favorite_restaurant);
            favorite_spot_to_eat = itemView.findViewById(R.id.favorite_spot_to_eat);
            cityStateLabel = itemView.findViewById(R.id.city_state_zipcode);

            nameTextView.setText("");
            addressTextView.setText("");
            Fonts.fontRobotoCodensedBoldTextView(nameTextView, 16,
                    AppConstants.COLORBG, context.getAssets());
            Fonts.fontRobotoCodensedRegularTextView(addressTextView, 14,
                    AppConstants.COLORBLUE, context.getAssets());
            Fonts.fontRobotoCodensedRegularTextView(cityStateLabel, 14,
                    AppConstants.COLORBLUE, context.getAssets());
        }

        public void init(AvailableOfferResponse.OperatingHour.Restaurant locations) {

            nameTextView.setText(locations.getAppDisplayText());
            addressTextView.setText(locations.getAddress());
            String citystatezipcode = locations.getCityLabel()+", " + locations.getStateLabel()+" " + locations.getZipcode();
            cityStateLabel.setText(citystatezipcode);

            favorite_spot_to_eat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    favoriteStoreInterface.onItemClickFavoriteStore(locations);


                }
            });
        }
    }
}

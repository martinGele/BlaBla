package com.interfaces;

import com.fragments.SignUpFragment;
import com.responsemodels.AvailableOfferResponse;

public interface FavoriteStoreInterface {
    void onItemClickFavoriteStore(AvailableOfferResponse.OperatingHour.Restaurant location);

}

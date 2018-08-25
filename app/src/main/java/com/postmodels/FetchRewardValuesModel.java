package com.postmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchRewardValuesModel {

    @SerializedName("appkey")
    @Expose
    String appkey;
    @SerializedName("auth_token")
    @Expose
    String auth_token;
    @SerializedName("reward_id")
    @Expose
    String reward_id;
    @SerializedName("lat")
    @Expose
    String lat;
    @SerializedName("lng")
    @Expose
    String lng;
    @SerializedName("location")
    @Expose
    String location;
    @SerializedName("warn")
    @Expose
    String warn;

    public FetchRewardValuesModel(String appkey, String auth_token, String reward_id, String lat, String lng, String location) {
        this.appkey = appkey;
        this.auth_token = auth_token;
        this.reward_id = reward_id;
        this.lat = lat;
        this.lng = lng;
        this.location = location;
    }


    public FetchRewardValuesModel(String appkey, String auth_token, String reward_id, String lat, String lng, String location, String warn) {
        this.appkey = appkey;
        this.auth_token = auth_token;
        this.reward_id = reward_id;
        this.lat = lat;
        this.lng = lng;
        this.location = location;
        this.warn = warn;
    }


}

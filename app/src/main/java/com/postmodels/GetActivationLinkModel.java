package com.postmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetActivationLinkModel {

    @SerializedName("appkey")
    @Expose
    String appkey;
    @SerializedName("email")
    @Expose
    String email;

    public GetActivationLinkModel(String appkey, String email) {
        this.appkey = appkey;
        this.email = email;
    }
}
